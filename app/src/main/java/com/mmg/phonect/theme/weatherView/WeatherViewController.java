package com.mmg.phonect.theme.weatherView;

import android.annotation.SuppressLint;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.mmg.phonect.common.basic.models.weather.Device;
import com.mmg.phonect.common.basic.models.weather.Weather;
import com.mmg.phonect.common.basic.models.weather.WeatherCode;
import com.mmg.phonect.theme.resource.providers.ResourceProvider;

import java.util.Random;

public class WeatherViewController {
    //todo: 这里不该在这里获取code
    public static void setWeatherCode(
            @NonNull WeatherView view,
            @Nullable Device device,
            boolean dayTime,
            @NonNull ResourceProvider provider
    ) {
        view.setWeather(getWeatherKind(device), dayTime, provider);
    }

    @SuppressLint("SwitchIntDef")
    public static WeatherCode getWeatherCode(
            @WeatherView.WeatherKindRule int weatherKind
    ) {
        switch (weatherKind) {
            case WeatherView.WEATHER_KIND_CLOUDY:
                return WeatherCode.CLOUDY;

            case WeatherView.WEATHER_KIND_CLOUD:
                return WeatherCode.PARTLY_CLOUDY;

            case WeatherView.WEATHER_KIND_FOG:
                return WeatherCode.FOG;

            case WeatherView.WEATHER_KIND_HAIL:
                return WeatherCode.HAIL;

            case WeatherView.WEATHER_KIND_HAZE:
                return WeatherCode.HAZE;

            case WeatherView.WEATHER_KIND_RAINY:
                return WeatherCode.RAIN;

            case WeatherView.WEATHER_KIND_SLEET:
                return WeatherCode.SLEET;

            case WeatherView.WEATHER_KIND_SNOW:
                return WeatherCode.SNOW;

            case WeatherView.WEATHER_KIND_THUNDERSTORM:
                return WeatherCode.THUNDERSTORM;

            case WeatherView.WEATHER_KIND_THUNDER:
                return WeatherCode.THUNDER;

            case WeatherView.WEATHER_KIND_WIND:
                return WeatherCode.WIND;

            default:
                return WeatherCode.CLEAR;
        }
    }

    @WeatherView.WeatherKindRule
    public static int getWeatherKind(@Nullable Device device) {
        if (device == null) {
            return WeatherView.WEATHER_KIND_CLEAR;
        }else {
            if (device.getScore() < 0){
                return WeatherView.WEATHER_KIND_THUNDERSTORM;
            }
//            else if (device.getScore()>90) {
//                return 2;
//            } else if (device.getScore()>80) {
//                return 3;
//            }
            Log.d("TAG", "getWeatherKind: "+(11 - (device.getScore() / 10)));
            return 11 - (device.getScore() / 10);
        }
//        Random random = new Random();
//
//        // 随机生成一个 0 到 11（包括 11）之间的整数  具体含义见getWeatherKind方法
//        return random.nextInt(11) + 1;

    }
    // 获取状态
    @WeatherView.WeatherKindRule
    public static int getWeatherKind(WeatherCode weatherCode) {
        switch (weatherCode) {
            case CLEAR:
                return WeatherView.WEATHER_KIND_CLEAR;

            case PARTLY_CLOUDY:
                return WeatherView.WEATHER_KIND_CLOUD;

            case CLOUDY:
                return WeatherView.WEATHER_KIND_CLOUDY;

            case RAIN:
                return WeatherView.WEATHER_KIND_RAINY;

            case SNOW:
                return WeatherView.WEATHER_KIND_SNOW;

            case WIND:
                return WeatherView.WEATHER_KIND_WIND;

            case FOG:
                return WeatherView.WEATHER_KIND_FOG;

            case HAZE:
                return WeatherView.WEATHER_KIND_HAZE;

            case SLEET:
                return WeatherView.WEATHER_KIND_SLEET;

            case HAIL:
                return WeatherView.WEATHER_KIND_HAIL;

            case THUNDER:
                return WeatherView.WEATHER_KIND_THUNDER;

            case THUNDERSTORM:
                return WeatherView.WEATHER_KIND_THUNDERSTORM;
        }
        return WeatherView.WEATHER_KIND_CLEAR;
    }
}
