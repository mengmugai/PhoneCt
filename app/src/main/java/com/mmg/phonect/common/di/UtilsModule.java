package com.mmg.phonect.common.di;

import android.content.Context;

import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.android.qualifiers.ApplicationContext;
import dagger.hilt.components.SingletonComponent;
import com.mmg.phonect.db.DatabaseHelper;
import com.mmg.phonect.settings.SettingsManager;

@InstallIn(SingletonComponent.class)
@Module
public class UtilsModule {

    @Provides
    public DatabaseHelper provideDatabaseHelper(@ApplicationContext Context context) {
        return DatabaseHelper.getInstance(context);
    }

    @Provides
    public SettingsManager provideSettingsOptionManager(@ApplicationContext Context context) {
        return SettingsManager.getInstance(context);
    }
}
