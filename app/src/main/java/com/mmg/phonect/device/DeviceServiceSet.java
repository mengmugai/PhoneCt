package com.mmg.phonect.device;

import androidx.annotation.NonNull;

import javax.inject.Inject;

import com.mmg.phonect.common.basic.models.options.provider.WeatherSource;
import com.mmg.phonect.device.services.CaiYunDeviceService;
import com.mmg.phonect.device.services.DeviceService;

public class DeviceServiceSet {

    private final DeviceService[] mWeatherServices;

    @Inject
    public DeviceServiceSet(CaiYunDeviceService caiYunWeatherService) {
        mWeatherServices = new DeviceService[] {
                caiYunWeatherService,
        };
    }

    @NonNull
    public DeviceService get() {
        return mWeatherServices[0];
    }

    @NonNull
    public DeviceService[] getAll() {
        return mWeatherServices;
    }
}
