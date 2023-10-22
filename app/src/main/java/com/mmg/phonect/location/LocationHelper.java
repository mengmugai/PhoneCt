package com.mmg.phonect.location;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;

import java.util.Arrays;
import java.util.List;
import java.util.TimeZone;

import javax.inject.Inject;

import dagger.hilt.android.qualifiers.ApplicationContext;
import com.mmg.phonect.common.basic.models.Location;
import com.mmg.phonect.common.basic.models.Phone;
import com.mmg.phonect.common.basic.models.options.provider.LocationProvider;
import com.mmg.phonect.common.basic.models.options.provider.WeatherSource;
import com.mmg.phonect.common.utils.NetworkUtils;
import com.mmg.phonect.db.DatabaseHelper;
import com.mmg.phonect.location.services.LocationService;
import com.mmg.phonect.location.services.AndroidLocationService;
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

    public interface OnRequestPhoneListener {
        void requestPhoneSuccess(Phone requestPhone);
    }

    @Inject
    public LocationHelper(@ApplicationContext Context context,
                          BaiduIPLocationService baiduIPService,
                          DeviceServiceSet deviceServiceSet) {
        mLocationServices = new LocationService[] {
                new AndroidLocationService(),
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

    public void requestLocation(Context context, Phone phone, boolean background,
                                @NonNull OnRequestPhoneListener l) {
        // 获取phone 数据
        //timeZone: TimeZone? = null,
        //        brand: String? = null,
        //        model: String? = null,
        //        country: String? = null,
        //        province: String? = null,
        //        city: String? = null,
        //        district: String? = null,
        //        device: Device? = null,
        //        weatherSource: WeatherSource? = null,
        phone.copy(TimeZone.getTimeZone("Asia/Shanghai"),
                Build.BRAND,
                Build.MODEL
                );

        // 1. get location by location service.
        // 2. get available location by weather service.


//        DatabaseHelper.getInstance(context).writeLocation(phone);
        l.requestPhoneSuccess(phone);

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
        Log.d("mmg", "getPermissions: "+ Arrays.toString(permissions));
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q || permissions.length == 0) {
            // 设备是否有后台定位权限或通过IP进行定位。
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