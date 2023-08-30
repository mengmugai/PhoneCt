package com.mmg.phonect.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.github.yuweiguocn.library.greendao.MigrationHelper;

import org.greenrobot.greendao.database.Database;

import com.mmg.phonect.db.entities.AlertEntityDao;
import com.mmg.phonect.db.entities.ChineseCityEntityDao;
import com.mmg.phonect.db.entities.DailyEntityDao;
import com.mmg.phonect.db.entities.DaoMaster;
import com.mmg.phonect.db.entities.HistoryEntityDao;
import com.mmg.phonect.db.entities.HourlyEntityDao;
import com.mmg.phonect.db.entities.LocationEntityDao;
import com.mmg.phonect.db.entities.MinutelyEntityDao;
import com.mmg.phonect.db.entities.WeatherEntityDao;

class DatabaseOpenHelper extends DaoMaster.OpenHelper {

    DatabaseOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory) {
        super(context, name, factory);
    }

    @Override
    public void onUpgrade(Database db, int oldVersion, int newVersion) {
        if (oldVersion >= 53) {
            MigrationHelper.migrate(
                    db,
                    new MigrationHelper.ReCreateAllTableListener() {
                        @Override
                        public void onCreateAllTables(Database db, boolean ifNotExists) {
                            DaoMaster.createAllTables(db, ifNotExists);
                        }

                        @Override
                        public void onDropAllTables(Database db, boolean ifExists) {
                            DaoMaster.dropAllTables(db, ifExists);
                        }
                    },
                    AlertEntityDao.class,
                    ChineseCityEntityDao.class,
                    DailyEntityDao.class,
                    HistoryEntityDao.class,
                    HourlyEntityDao.class,
                    LocationEntityDao.class,
                    MinutelyEntityDao.class,
                    WeatherEntityDao.class
            );
        } else {
            DaoMaster.dropAllTables(db, true);
            onCreate(db);
        }
    }
}
