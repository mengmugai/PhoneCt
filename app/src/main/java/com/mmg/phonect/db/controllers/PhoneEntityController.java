package com.mmg.phonect.db.controllers;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

import com.mmg.phonect.db.entities.DaoSession;
import com.mmg.phonect.db.entities.PhoneEntity;
import com.mmg.phonect.db.entities.PhoneEntityDao;

public class PhoneEntityController extends AbsEntityController {

    // insert.

    public static void insertLocationEntity(@NonNull DaoSession session,
                                            @NonNull PhoneEntity entity) {
        session.getPhoneEntityDao().insert(entity);
    }

    public static void insertLocationEntityList(@NonNull DaoSession session,
                                                @NonNull List<PhoneEntity> entityList) {
        if (entityList.size() != 0) {
            session.getPhoneEntityDao().insertInTx(entityList);
        }
    }

    // delete.

    public static void deleteLocationEntity(@NonNull DaoSession session,
                                            @NonNull PhoneEntity entity) {
        session.getPhoneEntityDao().deleteByKey(entity.formattedId);
    }

    public static void deleteLocationEntityList(@NonNull DaoSession session) {
        session.getPhoneEntityDao().deleteAll();
    }

    // update.

    public static void updateLocationEntity(@NonNull DaoSession session,
                                            @NonNull PhoneEntity entity) {
        session.getPhoneEntityDao().update(entity);
    }

    // select.

    @Nullable
    public static PhoneEntity selectLocationEntity(@NonNull DaoSession session) {
        List<PhoneEntity> entityList = session.getPhoneEntityDao()
                .queryBuilder()
                .list();
        if (entityList == null || entityList.size() <= 0) {
            return null;
        } else {
            return entityList.get(0);
        }
    }

    @NonNull
    public static List<PhoneEntity> selectLocationEntityList(@NonNull DaoSession session) {
        return getNonNullList(
                session.getPhoneEntityDao()
                        .queryBuilder()
                        .list()
        );
    }

    public static int countLocationEntity(@NonNull DaoSession session) {
        return (int) session.getPhoneEntityDao()
                .queryBuilder()
                .count();
    }
}
