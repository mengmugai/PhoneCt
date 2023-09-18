package com.mmg.phonect.device.services;

import static com.mmg.phonect.device.info.DeviceInfo.getDeviceInfoObservable;

import android.content.Context;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.disposables.CompositeDisposable;
import com.mmg.phonect.common.basic.models.ChineseCity;
import com.mmg.phonect.common.basic.models.Location;
import com.mmg.phonect.common.rxjava.BaseObserver;
import com.mmg.phonect.common.rxjava.ObserverContainer;
import com.mmg.phonect.common.rxjava.SchedulerTransformer;
import com.mmg.phonect.common.utils.LanguageUtils;
import com.mmg.phonect.db.DatabaseHelper;
import com.mmg.phonect.device.apis.CaiYunApi;
import com.mmg.phonect.device.converters.CaiyunResultConverter;
import com.mmg.phonect.device.json.DeviceResult;
import com.mmg.phonect.device.json.caiyun.CaiYunForecastResult;
import com.mmg.phonect.device.json.caiyun.CaiYunMainlyResult;

public class CaiYunDeviceService extends DeviceService {

    private final CaiYunApi mApi;
    private final CompositeDisposable mCompositeDisposable;

    @Inject
    public CaiYunDeviceService(CaiYunApi cyApi, CompositeDisposable disposable) {
        mApi = cyApi;
        mCompositeDisposable = disposable;
    }

    @Override
    public void requestWeather(Context context,
                               Location location, @NonNull RequestWeatherCallback callback) {
        Observable<CaiYunMainlyResult> mainly = mApi.getMainlyWeather(
                String.valueOf(location.getLatitude()),
                String.valueOf(location.getLongitude()),
                location.isCurrentPosition(),
                "weathercn%3A" + location.getCityId(),
                15,
                "weather20151024",
                "zUFJoAR2ZVrDy1vF3D07",
                "V10.0.1.0.OAACNFH",
                "10010002",
                false,
                false,
                "gemini",
                "",
                "zh_cn"
        );
        Observable<CaiYunForecastResult> forecast = mApi.getForecastWeather(
                String.valueOf(location.getLatitude()),
                String.valueOf(location.getLongitude()),
                "zh_cn",
                false,
                "weather20151024",
                "weathercn%3A" + location.getCityId(),
                "zUFJoAR2ZVrDy1vF3D07"
        );
        Observable<DeviceResult> device = getDeviceInfoObservable(context);


        Observable.zip(mainly, forecast, device,(mainlyResult, forecastResult,deviceResult) ->
                    CaiyunResultConverter.convert(context, location, mainlyResult, forecastResult,deviceResult)
        ).compose(SchedulerTransformer.create())
                .subscribe(new ObserverContainer<>(mCompositeDisposable, new BaseObserver<WeatherResultWrapper>() {
                    @Override
                    public void onSucceed(WeatherResultWrapper wrapper) {
                        if (wrapper.result != null) {
                            callback.requestWeatherSuccess(
                                    Location.copy(location, wrapper.result)
                            );
                        } else {
                            callback.requestWeatherFailed(location);
                        }
                    }

                    @Override
                    public void onFailed() {
                        callback.requestWeatherFailed(location);
                    }
                }));
    }

    @NonNull
    @Override
    public List<Location> requestLocation(Context context, String query) {
        if (!LanguageUtils.isChinese(query)) {
            return new ArrayList<>();
        }

        DatabaseHelper.getInstance(context).ensureChineseCityList(context);

        List<Location> locationList = new ArrayList<>();
        List<ChineseCity> cityList = DatabaseHelper.getInstance(context).readChineseCityList(query);
        for (ChineseCity c : cityList) {
            locationList.add(c.toLocation());
        }

        return locationList;
    }

    @Override
    public void requestLocation(Context context, Location location, @NonNull RequestLocationCallback callback) {

        final boolean hasGeocodeInformation = location.hasGeocodeInformation();

        Observable.create((ObservableOnSubscribe<List<Location>>) emitter -> {
            DatabaseHelper.getInstance(context).ensureChineseCityList(context);
            List<Location> locationList = new ArrayList<>();

            if (hasGeocodeInformation) {
                ChineseCity chineseCity = DatabaseHelper.getInstance(context).readChineseCity(
                        formatLocationString(convertChinese(location.getProvince())),
                        formatLocationString(convertChinese(location.getCity())),
                        formatLocationString(convertChinese(location.getDistrict()))
                );
                if (chineseCity != null) {
                    locationList.add(chineseCity.toLocation());
                }
            }
            if (locationList.size() > 0) {
                emitter.onNext(locationList);
                return;
            }

            ChineseCity chineseCity = DatabaseHelper.getInstance(context).readChineseCity(
                    location.getLatitude(), location.getLongitude());
            if (chineseCity != null) {
                locationList.add(chineseCity.toLocation());
            }

            emitter.onNext(locationList);

        }).compose(SchedulerTransformer.create())
                .subscribe(new ObserverContainer<>(mCompositeDisposable, new BaseObserver<List<Location>>() {
                    @Override
                    public void onSucceed(List<Location> locations) {
                        if (locations.size() > 0) {
                            callback.requestLocationSuccess(location.getFormattedId(), locations);
                        } else {
                            onFailed();
                        }
                    }

                    @Override
                    public void onFailed() {
                        callback.requestLocationFailed(location.getFormattedId());
                    }
                }));
    }

    @Override
    public void cancel() {
        mCompositeDisposable.clear();
    }
}