package com.mmg.phonect.db;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

import com.mmg.phonect.common.basic.models.ChineseCity;
import com.mmg.phonect.common.basic.models.Location;
import com.mmg.phonect.common.basic.models.Phone;
import com.mmg.phonect.common.basic.models.weather.Device;
import com.mmg.phonect.common.basic.models.weather.History;
import com.mmg.phonect.common.basic.models.weather.Weather;
import com.mmg.phonect.common.utils.FileUtils;
import com.mmg.phonect.db.controllers.AlertEntityController;
import com.mmg.phonect.db.controllers.ChineseCityEntityController;
import com.mmg.phonect.db.controllers.DailyEntityController;
//import com.mmg.phonect.db.controllers.MinutelyEntityController;
import com.mmg.phonect.db.controllers.PhoneEntityController;
import com.mmg.phonect.db.controllers.DeviceEntityController;
import com.mmg.phonect.db.entities.ChineseCityEntity;
import com.mmg.phonect.db.entities.DaoMaster;
import com.mmg.phonect.db.entities.DaoSession;
import com.mmg.phonect.db.entities.DeviceEntity;
import com.mmg.phonect.db.entities.PhoneEntity;
import com.mmg.phonect.db.entities.DeviceEntity;
import com.mmg.phonect.db.generators.AlertEntityGenerator;
import com.mmg.phonect.db.generators.ChineseCityEntityGenerator;
import com.mmg.phonect.db.generators.DailyEntityGenerator;
import com.mmg.phonect.db.generators.DeviceEntityGenerator;
import com.mmg.phonect.db.generators.LocationEntityGenerator;
//import com.mmg.phonect.db.generators.MinutelyEntityGenerator;
import com.mmg.phonect.db.generators.PhoneEntityGenerator;
import com.mmg.phonect.db.generators.DeviceEntityGenerator;

/**
 * Database helper
 * */

public class DatabaseHelper {

    private static volatile DatabaseHelper sInstance;
    public static DatabaseHelper getInstance(Context c) {
        if (sInstance == null) {
            synchronized (DatabaseHelper.class) {
                sInstance = new DatabaseHelper(c);
            }
        }
        return sInstance;
    }

    private final DaoSession mSession;
    private final Object mWritingLock;

    private final static String DATABASE_NAME = "PhoneCt_db";

    private DatabaseHelper(Context c) {
        mSession = new DaoMaster(
                new DatabaseOpenHelper(c, DATABASE_NAME, null).getWritableDatabase()
        ).newSession();
        mWritingLock = new Object();
    }

    // location.

    public void writeLocation(@NonNull Phone phone) {
//        PhoneEntity entity = LocationEntityGenerator.generate(phone);
        PhoneEntity entity = PhoneEntityGenerator.generate(phone);

        mSession.callInTxNoException(() -> {
            if (PhoneEntityController.selectLocationEntity(mSession) == null) {
                PhoneEntityController.insertLocationEntity(mSession, entity);
            } else {
                PhoneEntityController.updateLocationEntity(mSession, entity);
            }
            return true;
        });
    }

    public void writeLocationList(@NonNull List<Phone> list) {
        mSession.callInTxNoException(() -> {
            PhoneEntityController.deleteLocationEntityList(mSession);
            PhoneEntityController.insertLocationEntityList(
                    mSession,
                    LocationEntityGenerator.generateEntityList(list)
            );
            return true;
        });
    }

    public void deleteLocation(@NonNull Phone phone) {
        PhoneEntityController.deleteLocationEntity(
                mSession, PhoneEntityGenerator.generate(phone));
    }

//    @Nullable
//    public Phone readPhone(@NonNull Phone phone) {
//        return readPhone();
//    }

    @Nullable
    public Phone readPhone() {
        PhoneEntity entity = PhoneEntityController.selectLocationEntity(mSession);
        if (entity != null) {
            return PhoneEntityGenerator.generate(entity);
        } else {

            synchronized (mWritingLock) {

//                entity = PhoneEntityGenerator.generate(
//                        Phone.buildPhone());
//
//                PhoneEntityController.insertLocationEntity(mSession, entity);

//                return PhoneEntityGenerator.generate(entity);
                return Phone.buildPhone();

            }


        }

    }

//    @NonNull
//    public List<Phone> readLocationList() {
//        List<PhoneEntity> entityList = PhoneEntityController.selectLocationEntityList(mSession);
//
//        if (entityList.size() == 0) {
//            synchronized (mWritingLock) {
//                if (countLocation() == 0) {
//                    PhoneEntity entity = PhoneEntityGenerator.generate(
//                            Phone.buildPhone());
//                    entityList.add(entity);
//
//                    PhoneEntityController.insertLocationEntityList(mSession, entityList);
//
//                    return LocationEntityGenerator.generateModuleList(entityList);
//                }
//            }
//        }
//
//        return LocationEntityGenerator.generateModuleList(entityList);
//    }

    public int countLocation() {
        return PhoneEntityController.countLocationEntity(mSession);
    }

    // weather.

//    public void writeWeather(@NonNull Phone phone, @NonNull Device device) {
//        mSession.callInTxNoException(() -> {
////            deleteWeather(phone);
//
//            DeviceEntityController.insertDeviceEntity(
//                    mSession,
//                    DeviceEntityGenerator.generate(phone, device)
//            );
//            DailyEntityController.insertDailyList(
//                    mSession,
//                    DailyEntityGenerator.generate(
//                            phone.getBrand(),
//                            phone.getWeatherSource(),
//                            weather.getDailyForecast()
//                    )
//            );

//            MinutelyEntityController.insertMinutelyList(
//                    mSession,
//                    MinutelyEntityGenerator.generate(
//                            phone.getBrand(),
//                            phone.getWeatherSource(),
//                            weather.getMinutelyForecast()
//                    )
//            );
//            AlertEntityController.insertAlertList(
//                    mSession,
//                    AlertEntityGenerator.generate(
//                            phone.getBrand(),
//                            phone.getWeatherSource(),
//                            weather.getAlertList()
//                    )
//            );


//            return true;
//        });
//    }

//    @Nullable
//    public Device readDevice(@NonNull Phone phone) {
//        DeviceEntity deviceEntity = DeviceEntityController.selectDeviceEntity(mSession);
//
//        if (deviceEntity == null) {
//            return null;
//        }
//
//
//        return DeviceEntityGenerator.generate(deviceEntity);
//    }

    public void deleteWeather(@NonNull Phone phone) {
        mSession.callInTxNoException(() -> {
            DeviceEntityController.deleteWeather(
                    mSession,
                    DeviceEntityController.selectDeviceEntityList(
                            mSession
                    )
            );

//            MinutelyEntityController.deleteMinutelyEntityList(
//                    mSession,
//                    MinutelyEntityController.selectMinutelyEntityList(
//                            mSession,
//                            phone.getCity(),
//                            phone.getWeatherSource()
//                    )
//            );
//            AlertEntityController.deleteAlertList(
//                    mSession,
//                    AlertEntityController.selectLocationAlertEntity(
//                            mSession,
//                            phone.getCity(),
//                            phone.getWeatherSource()
//                    )
//            );
            return true;
        });
    }

    // history.

    public History readHistory(@NonNull Location location, @NonNull Weather weather) {
        return null;
    }

    // chinese city.

    public void ensureChineseCityList(Context context) {
        if (countChineseCity() < 3216) {
            synchronized (mWritingLock) {
                if (countChineseCity() < 3216) {
                    List<ChineseCity> list = FileUtils.readCityList(context);

                    ChineseCityEntityController.deleteChineseCityEntityList(mSession);
                    ChineseCityEntityController.insertChineseCityEntityList(
                            mSession, ChineseCityEntityGenerator.generateEntityList(list));
                }
            }
        }
    }

    @Nullable
    public ChineseCity readChineseCity(@NonNull String name) {
        ChineseCityEntity entity = ChineseCityEntityController.selectChineseCityEntity(mSession, name);
        if (entity != null) {
            return ChineseCityEntityGenerator.generate(entity);
        } else {
            return null;
        }
    }

    @Nullable
    public ChineseCity readChineseCity(@NonNull String province,
                                       @NonNull String city,
                                       @NonNull String district) {
        ChineseCityEntity entity = ChineseCityEntityController.selectChineseCityEntity(
                mSession, province, city, district);
        if (entity != null) {
            return ChineseCityEntityGenerator.generate(entity);
        } else {
            return null;
        }
    }

    @Nullable
    public ChineseCity readChineseCity(float latitude, float longitude) {
        ChineseCityEntity entity = ChineseCityEntityController.selectChineseCityEntity(
                mSession, latitude, longitude);
        if (entity != null) {
            return ChineseCityEntityGenerator.generate(entity);
        } else {
            return null;
        }
    }

    @NonNull
    public List<ChineseCity> readChineseCityList(@NonNull String name) {
        return ChineseCityEntityGenerator.generateModuleList(
                ChineseCityEntityController.selectChineseCityEntityList(mSession, name));
    }

    public int countChineseCity() {
        return ChineseCityEntityController.countChineseCityEntity(mSession);
    }
}

