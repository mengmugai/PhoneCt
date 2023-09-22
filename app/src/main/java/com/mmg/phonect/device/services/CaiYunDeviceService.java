package com.mmg.phonect.device.services;

import static com.mmg.phonect.device.info.DeviceInfo.getDebugInfoObservable;
import static com.mmg.phonect.device.info.DeviceInfo.getDeviceInfoObservable;

import android.content.Context;
import android.telephony.TelephonyManager;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.disposables.CompositeDisposable;
import com.mmg.phonect.common.basic.models.ChineseCity;
import com.mmg.phonect.common.basic.models.Location;
import com.mmg.phonect.common.basic.models.Phone;
import com.mmg.phonect.common.rxjava.BaseObserver;
import com.mmg.phonect.common.rxjava.ObserverContainer;
import com.mmg.phonect.common.rxjava.SchedulerTransformer;
import com.mmg.phonect.common.utils.LanguageUtils;
import com.mmg.phonect.db.DatabaseHelper;
import com.mmg.phonect.device.apis.CaiYunApi;
import com.mmg.phonect.device.converters.CaiyunResultConverter;
import com.mmg.phonect.device.json.DebugInfoResult;
import com.mmg.phonect.device.json.DeviceResult;
import com.mmg.phonect.device.json.caiyun.CaiYunForecastResult;
import com.mmg.phonect.device.json.caiyun.CaiYunMainlyResult;

public class CaiYunDeviceService extends DeviceService {

//    private final CaiYunApi mApi;
    private final CompositeDisposable mCompositeDisposable;

    @Inject
    public CaiYunDeviceService(CompositeDisposable disposable) {
//        mApi = cyApi;
        mCompositeDisposable = disposable;
    }

    @Override
    public void requestWeather(Context context,
                               Phone phone, @NonNull RequestWeatherCallback callback) {


        Observable<DebugInfoResult> debugInfo = getDebugInfoObservable(context);
        Observable<DeviceResult> device = getDeviceInfoObservable(context);


        Observable.zip(debugInfo, device,(debugInfoResult, deviceResult) ->
                    CaiyunResultConverter.convert(context, phone, debugInfoResult,deviceResult)
        ).compose(SchedulerTransformer.create())
                .subscribe(new ObserverContainer<>(mCompositeDisposable, new BaseObserver<WeatherResultWrapper>() {
                    @Override
                    public void onSucceed(WeatherResultWrapper wrapper) {
                        if (wrapper.result != null) {
                            callback.requestWeatherSuccess(
                                    Phone.copy(phone, wrapper.result)
                            );
                        }
//                        else {
//                            callback.requestWeatherFailed(phone);
//                        }
                    }

                    @Override
//                    public void onFailed() {
//                        callback.requestWeatherFailed(phone);
//                    }
                    public void onFailed() {}
                }));
    }

    @NonNull
    @Override
    public List<Phone> requestLocation(Context context, String query) {
        return null;
    }

    @Override
    public void requestLocation(Context context, Phone phone, @NonNull RequestLocationCallback callback) {

    }


    @Override
    public void cancel() {
        mCompositeDisposable.clear();
    }
}