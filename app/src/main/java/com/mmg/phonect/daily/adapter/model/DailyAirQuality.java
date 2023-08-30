package com.mmg.phonect.daily.adapter.model;

import com.mmg.phonect.common.basic.models.weather.AirQuality;
import com.mmg.phonect.daily.adapter.DailyWeatherAdapter;

public class DailyAirQuality implements DailyWeatherAdapter.ViewModel {

    private AirQuality airQuality;

    public DailyAirQuality(AirQuality airQuality) {
        this.airQuality = airQuality;
    }

    public AirQuality getAirQuality() {
        return airQuality;
    }

    public void setAirQuality(AirQuality airQuality) {
        this.airQuality = airQuality;
    }

    public static boolean isCode(int code) {
        return code == 5;
    }

    @Override
    public int getCode() {
        return 5;
    }
}
