package com.mmg.phonect.db.controllers;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

import com.mmg.phonect.common.basic.models.options.provider.WeatherSource;
import com.mmg.phonect.db.converters.WeatherSourceConverter;
import com.mmg.phonect.db.entities.DaoSession;
import com.mmg.phonect.db.entities.WeatherEntity;
import com.mmg.phonect.db.entities.WeatherEntityDao;

public class WeatherEntityController extends AbsEntityController {

    // insert.

    public static void insertWeatherEntity(@NonNull DaoSession session,
                                           @NonNull WeatherEntity entity) {
        session.getWeatherEntityDao().insert(entity);
    }

    // delete.

    public static void deleteWeather(@NonNull DaoSession session,
                                     @NonNull List<WeatherEntity> entityList) {
        session.getWeatherEntityDao().deleteInTx(entityList);
    }

    // select.

    @Nullable
    public static WeatherEntity selectWeatherEntity(@NonNull DaoSession session,
                                                    @NonNull String cityId,
                                                    @NonNull WeatherSource source) {
        List<WeatherEntity> entityList = selectWeatherEntityList(session, cityId, source);
        if (entityList.size() <= 0) {
            return null;
        } else {
            return entityList.get(0);
        }
    }

    @NonNull
    public static List<WeatherEntity> selectWeatherEntityList(@NonNull DaoSession session,
                                                              @NonNull String cityId,
                                                              @NonNull WeatherSource source) {
        return getNonNullList(
                session.getWeatherEntityDao().queryBuilder()
                        .where(
                                WeatherEntityDao.Properties.CityId.eq(cityId),
                                WeatherEntityDao.Properties.WeatherSource.eq(
                                        new WeatherSourceConverter().convertToDatabaseValue(source)
                                )
                        ).list()
        );
    }
}
