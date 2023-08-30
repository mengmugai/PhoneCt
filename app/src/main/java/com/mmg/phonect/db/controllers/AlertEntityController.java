package com.mmg.phonect.db.controllers;

import androidx.annotation.NonNull;

import java.util.List;

import com.mmg.phonect.common.basic.models.options.provider.WeatherSource;
import com.mmg.phonect.db.converters.WeatherSourceConverter;
import com.mmg.phonect.db.entities.AlertEntity;
import com.mmg.phonect.db.entities.AlertEntityDao;
import com.mmg.phonect.db.entities.DaoSession;

public class AlertEntityController extends AbsEntityController {

    // insert.

    public static void insertAlertList(@NonNull DaoSession session,
                                @NonNull List<AlertEntity> entityList) {
        session.getAlertEntityDao().insertInTx(entityList);
    }

    // delete.

    public static void deleteAlertList(@NonNull DaoSession session,
                                @NonNull List<AlertEntity> entityList) {
        session.getAlertEntityDao().deleteInTx(entityList);
    }

    // search.

    public static List<AlertEntity> selectLocationAlertEntity(@NonNull DaoSession session,
                                                              @NonNull String cityId,
                                                              @NonNull WeatherSource source) {
        return getNonNullList(
                session.getAlertEntityDao()
                        .queryBuilder()
                        .where(
                                AlertEntityDao.Properties.CityId.eq(cityId),
                                AlertEntityDao.Properties.WeatherSource.eq(
                                        new WeatherSourceConverter().convertToDatabaseValue(source)
                                )
                        ).list()
        );
    }
}
