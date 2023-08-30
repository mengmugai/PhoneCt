package com.mmg.phonect.db.controllers;

import androidx.annotation.NonNull;

import java.util.List;

import com.mmg.phonect.common.basic.models.options.provider.WeatherSource;
import com.mmg.phonect.db.converters.WeatherSourceConverter;
import com.mmg.phonect.db.entities.DaoSession;
import com.mmg.phonect.db.entities.HourlyEntity;
import com.mmg.phonect.db.entities.HourlyEntityDao;

public class HourlyEntityController extends AbsEntityController {

    // insert.

    public static void insertHourlyList(@NonNull DaoSession session,
                                        @NonNull List<HourlyEntity> entityList) {
        session.getHourlyEntityDao().insertInTx(entityList);
    }

    // delete.

    public static void deleteHourlyEntityList(@NonNull DaoSession session,
                                              @NonNull List<HourlyEntity> entityList) {
        session.getHourlyEntityDao().deleteInTx(entityList);
    }

    // select.

    public static List<HourlyEntity> selectHourlyEntityList(@NonNull DaoSession session,
                                                            @NonNull String cityId,
                                                            @NonNull WeatherSource source) {
        return getNonNullList(
                session.getHourlyEntityDao()
                        .queryBuilder()
                        .where(
                                HourlyEntityDao.Properties.CityId.eq(cityId),
                                HourlyEntityDao.Properties.WeatherSource.eq(
                                        new WeatherSourceConverter().convertToDatabaseValue(source)
                                )
                        ).list()
        );
    }
}
