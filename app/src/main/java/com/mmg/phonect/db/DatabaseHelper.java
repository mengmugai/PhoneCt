package com.mmg.phonect.db;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

import com.mmg.phonect.common.basic.models.ChineseCity;
import com.mmg.phonect.common.basic.models.Location;
import com.mmg.phonect.common.basic.models.weather.History;
import com.mmg.phonect.common.basic.models.weather.Weather;
import com.mmg.phonect.common.utils.FileUtils;
import com.mmg.phonect.db.controllers.AlertEntityController;
import com.mmg.phonect.db.controllers.ChineseCityEntityController;
import com.mmg.phonect.db.controllers.DailyEntityController;
import com.mmg.phonect.db.controllers.HistoryEntityController;
import com.mmg.phonect.db.controllers.HourlyEntityController;
import com.mmg.phonect.db.controllers.LocationEntityController;
import com.mmg.phonect.db.controllers.MinutelyEntityController;
import com.mmg.phonect.db.controllers.WeatherEntityController;
import com.mmg.phonect.db.entities.ChineseCityEntity;
import com.mmg.phonect.db.entities.DaoMaster;
import com.mmg.phonect.db.entities.DaoSession;
import com.mmg.phonect.db.entities.HistoryEntity;
import com.mmg.phonect.db.entities.LocationEntity;
import com.mmg.phonect.db.entities.WeatherEntity;
import com.mmg.phonect.db.generators.AlertEntityGenerator;
import com.mmg.phonect.db.generators.ChineseCityEntityGenerator;
import com.mmg.phonect.db.generators.DailyEntityGenerator;
import com.mmg.phonect.db.generators.HistoryEntityGenerator;
import com.mmg.phonect.db.generators.HourlyEntityGenerator;
import com.mmg.phonect.db.generators.LocationEntityGenerator;
import com.mmg.phonect.db.generators.MinutelyEntityGenerator;
import com.mmg.phonect.db.generators.WeatherEntityGenerator;

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

    private final static String DATABASE_NAME = "Geometric_Weather_db";

    private DatabaseHelper(Context c) {
        mSession = new DaoMaster(
                new DatabaseOpenHelper(c, DATABASE_NAME, null).getWritableDatabase()
        ).newSession();
        mWritingLock = new Object();
    }

    // location.

    public void writeLocation(@NonNull Location location) {
        LocationEntity entity = LocationEntityGenerator.generate(location);

        mSession.callInTxNoException(() -> {
            if (LocationEntityController.selectLocationEntity(mSession, location.getFormattedId()) == null) {
                LocationEntityController.insertLocationEntity(mSession, entity);
            } else {
                LocationEntityController.updateLocationEntity(mSession, entity);
            }
            return true;
        });
    }

    public void writeLocationList(@NonNull List<Location> list) {
        mSession.callInTxNoException(() -> {
            LocationEntityController.deleteLocationEntityList(mSession);
            LocationEntityController.insertLocationEntityList(
                    mSession,
                    LocationEntityGenerator.generateEntityList(list)
            );
            return true;
        });
    }

    public void deleteLocation(@NonNull Location location) {
        LocationEntityController.deleteLocationEntity(
                mSession, LocationEntityGenerator.generate(location));
    }

    @Nullable
    public Location readLocation(@NonNull Location location) {
        return readLocation(location.getFormattedId());
    }

    @Nullable
    public Location readLocation(@NonNull String formattedId) {
        LocationEntity entity = LocationEntityController.selectLocationEntity(mSession, formattedId);
        if (entity != null) {
            return LocationEntityGenerator.generate(entity);
        } else {
            return null;
        }
    }

    @NonNull
    public List<Location> readLocationList() {
        List<LocationEntity> entityList = LocationEntityController.selectLocationEntityList(mSession);

        if (entityList.size() == 0) {
            synchronized (mWritingLock) {
                if (countLocation() == 0) {
                    LocationEntity entity = LocationEntityGenerator.generate(
                            Location.buildLocal());
                    entityList.add(entity);

                    LocationEntityController.insertLocationEntityList(mSession, entityList);

                    return LocationEntityGenerator.generateModuleList(entityList);
                }
            }
        }

        return LocationEntityGenerator.generateModuleList(entityList);
    }

    public int countLocation() {
        return LocationEntityController.countLocationEntity(mSession);
    }

    // weather.

    public void writeWeather(@NonNull Location location, @NonNull Weather weather) {
        mSession.callInTxNoException(() -> {
            deleteWeather(location);

            WeatherEntityController.insertWeatherEntity(
                    mSession,
                    WeatherEntityGenerator.generate(location, weather)
            );
            DailyEntityController.insertDailyList(
                    mSession,
                    DailyEntityGenerator.generate(
                            location.getCityId(),
                            location.getWeatherSource(),
                            weather.getDailyForecast()
                    )
            );
            HourlyEntityController.insertHourlyList(
                    mSession,
                    HourlyEntityGenerator.generateEntityList(
                            location.getCityId(),
                            location.getWeatherSource(),
                            weather.getHourlyForecast()
                    )
            );
            MinutelyEntityController.insertMinutelyList(
                    mSession,
                    MinutelyEntityGenerator.generate(
                            location.getCityId(),
                            location.getWeatherSource(),
                            weather.getMinutelyForecast()
                    )
            );
            AlertEntityController.insertAlertList(
                    mSession,
                    AlertEntityGenerator.generate(
                            location.getCityId(),
                            location.getWeatherSource(),
                            weather.getAlertList()
                    )
            );
            HistoryEntityController.insertHistoryEntity(
                    mSession,
                    HistoryEntityGenerator.generate(
                            location.getCityId(), location.getWeatherSource(), weather
                    )
            );
            if (weather.getYesterday() != null) {
                HistoryEntityController.insertHistoryEntity(
                        mSession,
                        HistoryEntityGenerator.generate(
                                location.getCityId(), location.getWeatherSource(), weather.getYesterday()
                        )
                );
            }
            return true;
        });
    }

    @Nullable
    public Weather readWeather(@NonNull Location location) {
        WeatherEntity weatherEntity = WeatherEntityController.selectWeatherEntity(
                mSession,location.getCityId(), location.getWeatherSource());
        if (weatherEntity == null) {
            return null;
        }

        HistoryEntity historyEntity = HistoryEntityController.selectYesterdayHistoryEntity(
                mSession,location.getCityId(), location.getWeatherSource(),weatherEntity.publishDate);

        return WeatherEntityGenerator.generate(weatherEntity, historyEntity);
    }

    public void deleteWeather(@NonNull Location location) {
        mSession.callInTxNoException(() -> {
            WeatherEntityController.deleteWeather(
                    mSession,
                    WeatherEntityController.selectWeatherEntityList(
                            mSession,
                            location.getCityId(),
                            location.getWeatherSource()
                    )
            );
            HistoryEntityController.deleteLocationHistoryEntity(
                    mSession,
                    HistoryEntityController.selectHistoryEntityList(
                            mSession,
                            location.getCityId(),
                            location.getWeatherSource()
                    )
            );
            DailyEntityController.deleteDailyEntityList(
                    mSession,
                    DailyEntityController.selectDailyEntityList(
                            mSession,
                            location.getCityId(),
                            location.getWeatherSource()
                    )
            );
            HourlyEntityController.deleteHourlyEntityList(
                    mSession,
                    HourlyEntityController.selectHourlyEntityList(
                            mSession,
                            location.getCityId(),
                            location.getWeatherSource()
                    )
            );
            MinutelyEntityController.deleteMinutelyEntityList(
                    mSession,
                    MinutelyEntityController.selectMinutelyEntityList(
                            mSession,
                            location.getCityId(),
                            location.getWeatherSource()
                    )
            );
            AlertEntityController.deleteAlertList(
                    mSession,
                    AlertEntityController.selectLocationAlertEntity(
                            mSession,
                            location.getCityId(),
                            location.getWeatherSource()
                    )
            );
            return true;
        });
    }

    // history.

    public History readHistory(@NonNull Location location, @NonNull Weather weather) {
        return HistoryEntityGenerator.generate(
                HistoryEntityController.selectYesterdayHistoryEntity(
                        mSession,
                        location.getCityId(),
                        location.getWeatherSource(),
                        weather.getBase().getPublishDate()
                )
        );
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

