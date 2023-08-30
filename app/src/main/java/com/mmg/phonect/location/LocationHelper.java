package com.mmg.phonect.location;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;

import java.util.List;
import java.util.TimeZone;

import javax.inject.Inject;

import dagger.hilt.android.qualifiers.ApplicationContext;
import com.mmg.phonect.common.basic.models.Location;
import com.mmg.phonect.common.basic.models.options.provider.LocationProvider;
import com.mmg.phonect.common.basic.models.options.provider.WeatherSource;
import com.mmg.phonect.common.utils.NetworkUtils;
import com.mmg.phonect.db.DatabaseHelper;
import com.mmg.phonect.location.services.LocationService;
import com.mmg.phonect.location.services.ip.BaiduIPLocationService;
import com.mmg.phonect.settings.SettingsManager;
import com.mmg.phonect.device.DeviceServiceSet;
import com.mmg.phonect.device.services.DeviceService;

/**
 * Location helper.
 * */

public class LocationHelper {

    private final LocationService[] mLocationServices;
    private final DeviceServiceSet mDeviceServiceSet;

    public interface OnRequestLocationListener {
        void requestLocationSuccess(Location requestLocation);
        void requestLocationFailed(Location requestLocation);
    }

    @Inject
    public LocationHelper(@ApplicationContext Context context,
                          BaiduIPLocationService baiduIPService,
                          DeviceServiceSet deviceServiceSet) {
        mLocationServices = new LocationService[] {
//                new AndroidLocationService(),
//                new BaiduLocationService(context),
                baiduIPService,
//                new AMapLocationService(context)
        };

        mDeviceServiceSet = deviceServiceSet;
    }

    private LocationService getLocationService(LocationProvider provider) {
        switch (provider) {
            case BAIDU:
                return mLocationServices[1];

            case BAIDU_IP:
                return mLocationServices[2];

            case AMAP:
                return mLocationServices[3];

            default: // NATIVE
                return mLocationServices[0];
        }
    }

    public void requestLocation(Context context, Location location, boolean background,
                                @NonNull OnRequestLocationListener l) {
        final OnRequestLocationListener usableCheckListener = new OnRequestLocationListener() {
            @Override
            public void requestLocationSuccess(Location requestLocation) {
                l.requestLocationSuccess(requestLocation);
            }

            @Override
            public void requestLocationFailed(Location requestLocation) {
                if (requestLocation.isUsable()) {
                    l.requestLocationFailed(requestLocation);
                } else {
                    Location finalLocation = Location.copy(
                            Location.buildDefaultLocation(
                                    SettingsManager.getInstance(context).getWeatherSource()
                            ),
                            true,
                            false
                    );
                    DatabaseHelper.getInstance(context).writeLocation(finalLocation);
                    l.requestLocationFailed(finalLocation);
                }
            }
        };

        final LocationProvider provider = SettingsManager.getInstance(context).getLocationProvider();
        final LocationService service = getLocationService(provider);
        if (service.getPermissions().length != 0) {
            // if needs any location permission.
            if (!NetworkUtils.isAvailable(context) || (ActivityCompat.checkSelfPermission(
                    context,
                    Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                    context,
                    Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED)) {
                usableCheckListener.requestLocationFailed(location);
                return;
            }
            if (background) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q && ActivityCompat.checkSelfPermission(
                        context,
                        Manifest.permission.ACCESS_BACKGROUND_LOCATION
                ) != PackageManager.PERMISSION_GRANTED) {
                    usableCheckListener.requestLocationFailed(location);
                    return;
                }
            }
        }

        // 1. get location by location service.
        // 2. get available location by weather service.

        service.requestLocation(
                context,
                result -> {
                    if (result == null) {
                        usableCheckListener.requestLocationFailed(location);
                        return;
                    }

                    requestAvailableWeatherLocation(
                            context,
                            Location.copy(
                                    location,
                                    result.getLatitude(),
                                    result.getLongitude(),
                                    TimeZone.getDefault()
                            ),
                            usableCheckListener
                    );
                }
        );
    }

    private void requestAvailableWeatherLocation(Context context,
                                                 @NonNull Location location,
                                                 @NonNull OnRequestLocationListener l) {
        WeatherSource source = SettingsManager.getInstance(context).getWeatherSource();

        final DeviceService service = mDeviceServiceSet.get(source);
        service.requestLocation(context, location, new DeviceService.RequestLocationCallback() {
            @Override
            public void requestLocationSuccess(String query, List<Location> locationList) {
                if (locationList.size() > 0) {
                    Location src = locationList.get(0);
                    Location result = Location.copy(src, true, src.isResidentPosition());
                    DatabaseHelper.getInstance(context).writeLocation(result);
                    l.requestLocationSuccess(result);
                } else {
                    requestLocationFailed(query);
                }
            }

            @Override
            public void requestLocationFailed(String query) {
                l.requestLocationFailed(location);
            }
        });
    }

    public void cancel() {
        for (LocationService s : mLocationServices) {
            s.cancel();
        }
        for (DeviceService s : mDeviceServiceSet.getAll()) {
            s.cancel();
        }
    }

    public String[] getPermissions(Context context) {
        // if IP:    none.
        // else:
        //      R:   foreground location. (set background location enabled manually)
        //      Q:   foreground location + background location.
        //      K-P: foreground location.

        final LocationProvider provider = SettingsManager.getInstance(context).getLocationProvider();
        final LocationService service = getLocationService(provider);

        String[] permissions = service.getPermissions();
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q || permissions.length == 0) {
            // device has no background location permission or locate by IP.
            return permissions;
        }

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.R) {
            String[] qPermissions = new String[permissions.length + 1];
            System.arraycopy(permissions, 0, qPermissions, 0, permissions.length);
            qPermissions[qPermissions.length - 1] = Manifest.permission.ACCESS_BACKGROUND_LOCATION;
            return qPermissions;
        }

        return permissions;
    }
}