package com.mmg.phonect.device;

import android.Manifest;
import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.disposables.CompositeDisposable;
import com.mmg.phonect.common.basic.models.Phone;
import com.mmg.phonect.common.basic.models.weather.Device;
import com.mmg.phonect.common.utils.NetworkUtils;
import com.mmg.phonect.device.services.DeviceService;

public class DeviceHelper {

    private final DeviceServiceSet mServiceSet;
    private final CompositeDisposable mCompositeDisposable;

//    public interface OnRequestWeatherListener {
//        void requestWeatherSuccess(@NonNull Phone requestPhone);
//        void requestWeatherFailed(@NonNull Phone requestPhone);
//    }

    public interface OnRequestDeviceListener {
        void requestDeviceSuccess(@NonNull Phone requestPhone);
        void requestDeviceFailed(@NonNull Phone requestPhone);
    }


    @Inject
    public DeviceHelper(DeviceServiceSet deviceServiceSet,
                        CompositeDisposable compositeDisposable) {
        mServiceSet = deviceServiceSet;
        mCompositeDisposable = compositeDisposable;
    }

    public void requestWeather(Context c, Phone phone, @NonNull final OnRequestDeviceListener l) {
        final DeviceService service = mServiceSet.get();
        if (!NetworkUtils.isAvailable(c)) {
            l.requestDeviceFailed(phone);
            return;
        }

        service.requestDevice(c, phone.copy(), new DeviceService.RequestWeatherCallback() {

            @Override
            public void requestWeatherSuccess(@NonNull Phone requestPhone) {
                Device device = requestPhone.getDevice();
                if (device != null) {
//                    DatabaseHelper.getInstance(c).writeWeather(requestPhone, device);

                    l.requestDeviceSuccess(requestPhone);
                } else {
                    Log.d("tag","requestWeatherSuccess 这里获取device失败了");
//                    requestWeatherFailed(requestPhone);
                }
            }

//            @Override
//            public void requestWeatherFailed(@NonNull Phone requestPhone) {
//                l.requestWeatherFailed(
//                        requestPhone
//                );
//            }
        });
    }



    public String[] getPermissions() {

        return new String[]{
                Manifest.permission.READ_PHONE_STATE,
                Manifest.permission.READ_SMS,
//                Manifest.permission.BLUETOOTH_CONNECT
                Manifest.permission.ACCESS_FINE_LOCATION
        };
    }

    public void cancel() {
        for (DeviceService s : mServiceSet.getAll()) {
            s.cancel();
        }
        mCompositeDisposable.clear();
    }
}
