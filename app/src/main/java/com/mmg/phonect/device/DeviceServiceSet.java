package com.mmg.phonect.device;

import androidx.annotation.NonNull;

import javax.inject.Inject;

import com.mmg.phonect.common.basic.models.options.provider.WeatherSource;
import com.mmg.phonect.device.services.CaiYunDeviceService;
import com.mmg.phonect.device.services.DeviceService;
import com.mmg.phonect.device.services.OwmWeatherService;

public class DeviceServiceSet {

    private final DeviceService[] mWeatherServices;

    @Inject
    public DeviceServiceSet(CaiYunDeviceService caiYunWeatherService, OwmWeatherService owmWeatherService) {
        mWeatherServices = new DeviceService[] {
//                accuWeatherService,
                caiYunWeatherService,
//                mfWeatherService,
                owmWeatherService
        };
    }

    @NonNull
    public DeviceService get(WeatherSource source) {
        switch (source) {
            case OWM:
                return mWeatherServices[1];

            case MF:
                return mWeatherServices[1];

            case CAIYUN:
                return mWeatherServices[1];

            default: // ACCU.
                return mWeatherServices[0];
        }

    }

    @NonNull
    public DeviceService[] getAll() {
        return mWeatherServices;
    }
}
