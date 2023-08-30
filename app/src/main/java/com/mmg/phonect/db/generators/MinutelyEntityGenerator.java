package com.mmg.phonect.db.generators;

import java.util.ArrayList;
import java.util.List;

import com.mmg.phonect.common.basic.models.options.provider.WeatherSource;
import com.mmg.phonect.common.basic.models.weather.Minutely;
import com.mmg.phonect.db.converters.WeatherSourceConverter;
import com.mmg.phonect.db.entities.MinutelyEntity;

public class MinutelyEntityGenerator {

    public static MinutelyEntity generate(String cityId, WeatherSource source, Minutely minutely) {
        MinutelyEntity entity = new MinutelyEntity();

        entity.cityId = cityId;
        entity.weatherSource = new WeatherSourceConverter().convertToDatabaseValue(source);
        
        entity.date = minutely.getDate();
        entity.time = minutely.getTime();
        entity.daylight = minutely.isDaylight();

        entity.weatherCode = minutely.getWeatherCode();
        entity.weatherText = minutely.getWeatherText();

        entity.minuteInterval = minutely.getMinuteInterval();
        entity.dbz = minutely.getDbz();
        entity.cloudCover = minutely.getCloudCover();

        return entity;
    }

    public static List<MinutelyEntity> generate(String cityId, WeatherSource source,
                                                List<Minutely> minutelyList) {
        List<MinutelyEntity> entityList = new ArrayList<>(minutelyList.size());
        for (Minutely minutely : minutelyList) {
            entityList.add(generate(cityId, source, minutely));
        }
        return entityList;
    }

    public static Minutely generate(MinutelyEntity entity) {
        return new Minutely(
                entity.date, entity.time, entity.daylight,
                entity.weatherText, entity.weatherCode,
                entity.minuteInterval, entity.dbz, entity.cloudCover
        );
    }

    public static List<Minutely> generate(List<MinutelyEntity> entityList) {
        List<Minutely> dailyList = new ArrayList<>(entityList.size());
        for (MinutelyEntity entity : entityList) {
            dailyList.add(generate(entity));
        }
        return dailyList;
    }
}
