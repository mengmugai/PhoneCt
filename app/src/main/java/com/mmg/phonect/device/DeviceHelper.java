package com.mmg.phonect.device;

import android.Manifest;
import android.content.Context;
import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.disposables.CompositeDisposable;
import com.mmg.phonect.common.basic.models.Location;
import com.mmg.phonect.common.basic.models.options.provider.LocationProvider;
import com.mmg.phonect.common.basic.models.options.provider.WeatherSource;
import com.mmg.phonect.common.basic.models.weather.Weather;
import com.mmg.phonect.common.rxjava.BaseObserver;
import com.mmg.phonect.common.rxjava.ObserverContainer;
import com.mmg.phonect.common.rxjava.SchedulerTransformer;
import com.mmg.phonect.common.utils.NetworkUtils;
import com.mmg.phonect.common.utils.helpers.AsyncHelper;
import com.mmg.phonect.db.DatabaseHelper;
import com.mmg.phonect.device.services.DeviceService;
import com.mmg.phonect.location.services.LocationService;
import com.mmg.phonect.settings.SettingsManager;

public class DeviceHelper {

    private final DeviceServiceSet mServiceSet;
    private final CompositeDisposable mCompositeDisposable;

    public interface OnRequestWeatherListener {
        void requestWeatherSuccess(@NonNull Location requestLocation);
        void requestWeatherFailed(@NonNull Location requestLocation);
    }

    public interface OnRequestLocationListener {
        void requestLocationSuccess(String query, List<Location> locationList);
        void requestLocationFailed(String query);
    }

    @Inject
    public DeviceHelper(DeviceServiceSet deviceServiceSet,
                        CompositeDisposable compositeDisposable) {
        mServiceSet = deviceServiceSet;
        mCompositeDisposable = compositeDisposable;
    }

    public void requestWeather(Context c, Location location, @NonNull final OnRequestWeatherListener l) {
        final DeviceService service = mServiceSet.get(location.getWeatherSource());
        if (!NetworkUtils.isAvailable(c)) {
            l.requestWeatherFailed(location);
            return;
        }

        service.requestWeather(c, location.copy(), new DeviceService.RequestWeatherCallback() {

            @Override
            public void requestWeatherSuccess(@NonNull Location requestLocation) {
                Weather weather = requestLocation.getWeather();
                if (weather != null) {
                    DatabaseHelper.getInstance(c).writeWeather(requestLocation, weather);
                    if (weather.getYesterday() == null) {
                        weather.setYesterday(
                                DatabaseHelper.getInstance(c).readHistory(requestLocation, weather)
                        );
                    }
                    l.requestWeatherSuccess(requestLocation);
                } else {
                    requestWeatherFailed(requestLocation);
                }
            }

            @Override
            public void requestWeatherFailed(@NonNull Location requestLocation) {
                l.requestWeatherFailed(
                        Location.copy(
                                requestLocation,
                                DatabaseHelper.getInstance(c).readWeather(requestLocation)
                        )
                );
            }
        });
    }

    public void requestLocation(Context context, String query, List<WeatherSource> enabledSources,
                                @NonNull final OnRequestLocationListener l) {
        if (enabledSources == null || enabledSources.isEmpty()) {
            AsyncHelper.delayRunOnUI(() -> l.requestLocationFailed(query), 0);
        }

        // generate weather services.
        final DeviceService[] services = new DeviceService[enabledSources.size()];
        for (int i = 0; i < services.length; i ++) {
            services[i] = mServiceSet.get(enabledSources.get(i));
        }

        // generate observable list.
        List<Observable<List<Location>>> observableList = new ArrayList<>();
        for (int i = 0; i < services.length; i ++) {
            int finalI = i;
            observableList.add(
                    Observable.create(emitter ->
                            emitter.onNext(services[finalI].requestLocation(context, query)))
            );
        }

        Observable.zip(observableList, objects -> {
            List<Location> locationList = new ArrayList<>();
            for (Object o : objects) {
                locationList.addAll((List<Location>) o);
            }
            return locationList;
        }).compose(SchedulerTransformer.create())
                .subscribe(new ObserverContainer<>(mCompositeDisposable, new BaseObserver<List<Location>>() {
                    @Override
                    public void onSucceed(List<Location> locationList) {
                        if (locationList != null && locationList.size() != 0) {
                            l.requestLocationSuccess(query, locationList);
                        } else {
                            onFailed();
                        }
                    }

                    @Override
                    public void onFailed() {
                        l.requestLocationFailed(query);
                    }
                }));
    }

    public String[] getPermissions() {

        return new String[]{Manifest.permission.READ_PHONE_STATE};
    }

    public void cancel() {
        for (DeviceService s : mServiceSet.getAll()) {
            s.cancel();
        }
        mCompositeDisposable.clear();
    }
}
