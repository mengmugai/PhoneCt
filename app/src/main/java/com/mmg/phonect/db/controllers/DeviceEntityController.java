package com.mmg.phonect.db.controllers;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.mmg.phonect.common.basic.models.options.provider.WeatherSource;
import com.mmg.phonect.db.converters.WeatherSourceConverter;
import com.mmg.phonect.db.entities.DaoSession;
import com.mmg.phonect.db.entities.DeviceEntity;
import com.mmg.phonect.db.entities.DeviceEntityDao;

import java.util.List;

public class DeviceEntityController extends AbsEntityController {

    // insert.

    public static void insertDeviceEntity(@NonNull DaoSession session,
                                           @NonNull DeviceEntity entity) {
        session.getDeviceEntityDao().insert(entity);
    }

    // delete.

    public static void deleteWeather(@NonNull DaoSession session,
                                     @NonNull List<DeviceEntity> entityList) {
        session.getDeviceEntityDao().deleteInTx(entityList);
    }

    // select.

    @Nullable
    public static DeviceEntity selectDeviceEntity(@NonNull DaoSession session) {
        List<DeviceEntity> entityList = selectDeviceEntityList(session);
        if (entityList.size() <= 0) {
            return null;
        } else {
            return entityList.get(0);
        }
    }

    @NonNull
    public static List<DeviceEntity> selectDeviceEntityList(@NonNull DaoSession session) {
        return getNonNullList(
                session.getDeviceEntityDao().queryBuilder().list()
        );
    }
}
