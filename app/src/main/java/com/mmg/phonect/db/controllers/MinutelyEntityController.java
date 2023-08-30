package com.mmg.phonect.db.controllers;

import androidx.annotation.NonNull;

import java.util.List;

import com.mmg.phonect.common.basic.models.options.provider.WeatherSource;
import com.mmg.phonect.db.converters.WeatherSourceConverter;
import com.mmg.phonect.db.entities.DaoSession;
import com.mmg.phonect.db.entities.MinutelyEntity;
import com.mmg.phonect.db.entities.MinutelyEntityDao;

public class MinutelyEntityController extends AbsEntityController {

    // insert.

    public static void insertMinutelyList(@NonNull DaoSession session,
                                          @NonNull List<MinutelyEntity> entityList) {
        session.getMinutelyEntityDao().insertInTx(entityList);
    }

    // delete.

    public static void deleteMinutelyEntityList(@NonNull DaoSession session,
                                                @NonNull List<MinutelyEntity> entityList) {
        session.getMinutelyEntityDao().deleteInTx(entityList);
    }

    // select.

    public static List<MinutelyEntity> selectMinutelyEntityList(@NonNull DaoSession session,
                                                                @NonNull String cityId, @NonNull WeatherSource source) {
        return getNonNullList(
                session.getMinutelyEntityDao()
                        .queryBuilder()
                        .where(
                                MinutelyEntityDao.Properties.CityId.eq(cityId),
                                MinutelyEntityDao.Properties.WeatherSource.eq(
                                        new WeatherSourceConverter().convertToDatabaseValue(source)
                                )
                        ).list()
        );
    }
}
