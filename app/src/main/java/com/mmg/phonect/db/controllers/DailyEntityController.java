package com.mmg.phonect.db.controllers;

import androidx.annotation.NonNull;

import java.util.List;

import com.mmg.phonect.common.basic.models.options.provider.WeatherSource;
import com.mmg.phonect.db.converters.WeatherSourceConverter;
import com.mmg.phonect.db.entities.DailyEntity;
import com.mmg.phonect.db.entities.DailyEntityDao;
import com.mmg.phonect.db.entities.DaoSession;

public class DailyEntityController extends AbsEntityController {

    // insert.

    public static void insertDailyList(@NonNull DaoSession session,
                                       @NonNull List<DailyEntity> entityList) {
        session.getDailyEntityDao().insertInTx(entityList);
    }

    // delete.

    public static void deleteDailyEntityList(@NonNull DaoSession session,
                                             @NonNull List<DailyEntity> entityList) {
        session.getDailyEntityDao().deleteInTx(entityList);
    }

    // select.

    @NonNull
    public static List<DailyEntity> selectDailyEntityList(@NonNull DaoSession session,
                                                          @NonNull String cityId,
                                                          @NonNull WeatherSource source) {
        return getNonNullList(
                session.getDailyEntityDao()
                        .queryBuilder()
                        .where(
                                DailyEntityDao.Properties.CityId.eq(cityId),
                                DailyEntityDao.Properties.WeatherSource.eq(
                                        new WeatherSourceConverter().convertToDatabaseValue(source)
                                )
                        ).list()
        );
    }
}
