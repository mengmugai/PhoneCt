package com.mmg.phonect.device.services;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.WorkerThread;

import java.util.List;

import com.mmg.phonect.common.basic.models.Phone;
import com.mmg.phonect.common.basic.models.weather.Device;
import com.mmg.phonect.common.utils.LanguageUtils;

/**
 * Weather service.
 * */

public abstract class DeviceService {

    public static class WeatherResultWrapper {
        final Device result;

        public WeatherResultWrapper(@Nullable Device device) {
            result = device;
        }
    }

    public interface RequestWeatherCallback {
        void requestWeatherSuccess(@NonNull Phone requestLocation);
//        void requestWeatherFailed(@NonNull Phone requestLocation);
    }

    public interface RequestLocationCallback {
        void requestLocationSuccess(String query, List<Phone> locationList);
        void requestLocationFailed(String query);
    }

    public abstract void requestDevice(Context context, Phone phone,
                                       @NonNull RequestWeatherCallback callback);

//    @WorkerThread
//    @NonNull
//    public abstract List<Phone> requestLocation(Context context, String query);
//
//    public abstract void requestLocation(Context context, Phone phone,
//                                         @NonNull RequestLocationCallback callback);

    public abstract void cancel();


    protected static String convertChinese(String text) {
        try {
            return LanguageUtils.traditionalToSimplified(text);
        } catch (Exception e) {
            return text;
        }
    }
}
