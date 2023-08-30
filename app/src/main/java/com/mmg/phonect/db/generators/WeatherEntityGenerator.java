package com.mmg.phonect.db.generators;

import androidx.annotation.Nullable;

import com.mmg.phonect.common.basic.models.Location;
import com.mmg.phonect.common.basic.models.weather.AirQuality;
import com.mmg.phonect.common.basic.models.weather.Base;
import com.mmg.phonect.common.basic.models.weather.Current;
import com.mmg.phonect.common.basic.models.weather.Precipitation;
import com.mmg.phonect.common.basic.models.weather.PrecipitationProbability;
import com.mmg.phonect.common.basic.models.weather.Temperature;
import com.mmg.phonect.common.basic.models.weather.UV;
import com.mmg.phonect.common.basic.models.weather.Weather;
import com.mmg.phonect.common.basic.models.weather.Wind;
import com.mmg.phonect.db.converters.WeatherSourceConverter;
import com.mmg.phonect.db.entities.HistoryEntity;
import com.mmg.phonect.db.entities.WeatherEntity;

public class WeatherEntityGenerator {

    public static WeatherEntity generate(Location location, Weather weather) {
        WeatherEntity entity = new WeatherEntity();

        // base.
        entity.cityId = weather.getBase().getCityId();
        entity.weatherSource = new WeatherSourceConverter().convertToDatabaseValue(location.getWeatherSource());
        entity.timeStamp = weather.getBase().getTimeStamp();
        entity.publishDate = weather.getBase().getPublishDate();
        entity.publishTime = weather.getBase().getPublishTime();
        entity.updateDate = weather.getBase().getUpdateDate();
        entity.updateTime = weather.getBase().getUpdateTime();

        // current.
        entity.weatherText = weather.getCurrent().getWeatherText();
        entity.weatherCode = weather.getCurrent().getWeatherCode();

        entity.temperature = weather.getCurrent().getTemperature().getTemperature();
        entity.realFeelTemperature = weather.getCurrent().getTemperature().getRealFeelTemperature();
        entity.realFeelShaderTemperature = weather.getCurrent().getTemperature().getRealFeelShaderTemperature();
        entity.apparentTemperature = weather.getCurrent().getTemperature().getApparentTemperature();
        entity.windChillTemperature = weather.getCurrent().getTemperature().getWindChillTemperature();
        entity.wetBulbTemperature = weather.getCurrent().getTemperature().getWetBulbTemperature();
        entity.degreeDayTemperature = weather.getCurrent().getTemperature().getDegreeDayTemperature();

        entity.totalPrecipitation = weather.getCurrent().getPrecipitation().getTotal();
        entity.thunderstormPrecipitation = weather.getCurrent().getPrecipitation().getThunderstorm();
        entity.rainPrecipitation = weather.getCurrent().getPrecipitation().getRain();
        entity.snowPrecipitation = weather.getCurrent().getPrecipitation().getSnow();
        entity.icePrecipitation = weather.getCurrent().getPrecipitation().getIce();

        entity.totalPrecipitationProbability = weather.getCurrent().getPrecipitationProbability().getTotal();
        entity.thunderstormPrecipitationProbability = weather.getCurrent().getPrecipitationProbability().getThunderstorm();
        entity.rainPrecipitationProbability = weather.getCurrent().getPrecipitationProbability().getRain();
        entity.snowPrecipitationProbability = weather.getCurrent().getPrecipitationProbability().getSnow();
        entity.icePrecipitationProbability = weather.getCurrent().getPrecipitationProbability().getIce();

        entity.windDirection = weather.getCurrent().getWind().getDirection();
        entity.windDegree = weather.getCurrent().getWind().getDegree();
        entity.windSpeed = weather.getCurrent().getWind().getSpeed();
        entity.windLevel = weather.getCurrent().getWind().getLevel();

        entity.uvIndex = weather.getCurrent().getUV().getIndex();
        entity.uvLevel = weather.getCurrent().getUV().getLevel();
        entity.uvDescription = weather.getCurrent().getUV().getDescription();

        entity.aqiText = weather.getCurrent().getAirQuality().getAqiText();
        entity.aqiIndex = weather.getCurrent().getAirQuality().getAqiIndex();
        entity.pm25 = weather.getCurrent().getAirQuality().getPM25();
        entity.pm10 = weather.getCurrent().getAirQuality().getPM10();
        entity.so2 = weather.getCurrent().getAirQuality().getSO2();
        entity.no2 = weather.getCurrent().getAirQuality().getNO2();
        entity.o3 = weather.getCurrent().getAirQuality().getO3();
        entity.co = weather.getCurrent().getAirQuality().getCO();

        entity.relativeHumidity = weather.getCurrent().getRelativeHumidity();
        entity.pressure = weather.getCurrent().getPressure();
        entity.visibility = weather.getCurrent().getVisibility();
        entity.dewPoint = weather.getCurrent().getDewPoint();
        entity.cloudCover = weather.getCurrent().getCloudCover();
        entity.ceiling = weather.getCurrent().getCeiling();

        entity.dailyForecast = weather.getCurrent().getDailyForecast();
        entity.hourlyForecast = weather.getCurrent().getHourlyForecast();

        return entity;
    }

    public static Weather generate(@Nullable WeatherEntity weatherEntity,
                                   @Nullable HistoryEntity historyEntity) {
        if (weatherEntity == null) {
            return null;
        }

        return new Weather(
                new Base(
                        weatherEntity.cityId, weatherEntity.timeStamp,
                        weatherEntity.publishDate, weatherEntity.publishTime,
                        weatherEntity.updateDate, weatherEntity.updateTime
                ),
                new Current(
                        weatherEntity.weatherText, weatherEntity.weatherCode,
                        new Temperature(
                                weatherEntity.temperature,
                                weatherEntity.realFeelTemperature, weatherEntity.realFeelShaderTemperature,
                                weatherEntity.apparentTemperature,
                                weatherEntity.windChillTemperature,
                                weatherEntity.wetBulbTemperature,
                                weatherEntity.degreeDayTemperature
                        ),
                        new Precipitation(
                                weatherEntity.totalPrecipitation,
                                weatherEntity.thunderstormPrecipitation,
                                weatherEntity.rainPrecipitation,
                                weatherEntity.snowPrecipitation,
                                weatherEntity.icePrecipitation
                        ),
                        new PrecipitationProbability(
                                weatherEntity.totalPrecipitationProbability,
                                weatherEntity.thunderstormPrecipitationProbability,
                                weatherEntity.rainPrecipitationProbability,
                                weatherEntity.snowPrecipitationProbability,
                                weatherEntity.icePrecipitationProbability
                        ),
                        new Wind(
                                weatherEntity.windDirection,
                                weatherEntity.windDegree,
                                weatherEntity.windSpeed,
                                weatherEntity.windLevel
                        ),
                        new UV(
                                weatherEntity.uvIndex,
                                weatherEntity.uvLevel,
                                weatherEntity.uvDescription
                        ),
                        new AirQuality(
                                weatherEntity.aqiText,
                                weatherEntity.aqiIndex,
                                weatherEntity.pm25,
                                weatherEntity.pm10,
                                weatherEntity.so2,
                                weatherEntity.no2,
                                weatherEntity.o3,
                                weatherEntity.co
                        ),
                        weatherEntity.relativeHumidity,
                        weatherEntity.pressure,
                        weatherEntity.visibility,
                        weatherEntity.dewPoint,
                        weatherEntity.cloudCover,
                        weatherEntity.ceiling,
                        weatherEntity.dailyForecast,
                        weatherEntity.hourlyForecast
                ),
                HistoryEntityGenerator.generate(historyEntity),
                DailyEntityGenerator.generate(weatherEntity.getDailyEntityList()),
                HourlyEntityGenerator.generateModuleList(weatherEntity.getHourlyEntityList()),
                MinutelyEntityGenerator.generate(weatherEntity.getMinutelyEntityList()),
                AlertEntityGenerator.generate(weatherEntity.getAlertEntityList())
        );
    }
}
