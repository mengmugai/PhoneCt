package com.mmg.phonect.background.polling;

import android.content.Context;
import android.os.Build;

import java.util.List;

import com.mmg.phonect.background.polling.services.permanent.PermanentServiceHelper;
import com.mmg.phonect.background.polling.work.WorkerHelper;
import com.mmg.phonect.common.basic.models.Location;
import com.mmg.phonect.common.utils.helpers.AsyncHelper;
import com.mmg.phonect.common.utils.helpers.IntentHelper;
import com.mmg.phonect.db.DatabaseHelper;
import com.mmg.phonect.remoteviews.NotificationHelper;
import com.mmg.phonect.remoteviews.WidgetHelper;
import com.mmg.phonect.settings.SettingsManager;

public class PollingManager {

    public static void resetAllBackgroundTask(Context context, boolean forceRefresh) {
        SettingsManager settings = SettingsManager.getInstance(context);

        if (forceRefresh) {
            forceRefresh(context);
            return;
        }

        if (settings.isBackgroundFree()) {
            PermanentServiceHelper.stopPollingService(context);

            WorkerHelper.setNormalPollingWork(
                    context,
                    SettingsManager.getInstance(context).getUpdateInterval().getIntervalInHour());

            if (settings.isTodayForecastEnabled()) {
                WorkerHelper.setTodayForecastUpdateWork(context, settings.getTodayForecastTime(), false);
            } else {
                WorkerHelper.cancelTodayForecastUpdateWork(context);
            }

            if (settings.isTomorrowForecastEnabled()) {
                WorkerHelper.setTomorrowForecastUpdateWork(context, settings.getTomorrowForecastTime(), false);
            } else {
                WorkerHelper.cancelTomorrowForecastUpdateWork(context);
            }
        } else {
            WorkerHelper.cancelNormalPollingWork(context);
            WorkerHelper.cancelTodayForecastUpdateWork(context);
            WorkerHelper.cancelTomorrowForecastUpdateWork(context);

            PermanentServiceHelper.startPollingService(context);
        }
    }

    public static void resetNormalBackgroundTask(Context context, boolean forceRefresh) {
        SettingsManager settings = SettingsManager.getInstance(context);

        if (forceRefresh) {
            forceRefresh(context);
            return;
        }

        if (settings.isBackgroundFree()) {
            PermanentServiceHelper.stopPollingService(context);

            WorkerHelper.setNormalPollingWork(
                    context,
                    SettingsManager.getInstance(context).getUpdateInterval().getIntervalInHour());
        } else {
            WorkerHelper.cancelNormalPollingWork(context);
            WorkerHelper.cancelTodayForecastUpdateWork(context);
            WorkerHelper.cancelTomorrowForecastUpdateWork(context);

            PermanentServiceHelper.startPollingService(context);
        }
    }

    public static void resetTodayForecastBackgroundTask(Context context, boolean forceRefresh,
                                                        boolean nextDay) {
        SettingsManager settings = SettingsManager.getInstance(context);

        if (forceRefresh) {
            forceRefresh(context);
            return;
        }

        if (settings.isBackgroundFree()) {
            PermanentServiceHelper.stopPollingService(context);

            if (settings.isTodayForecastEnabled()) {
                WorkerHelper.setTodayForecastUpdateWork(context, settings.getTodayForecastTime(), nextDay);
            } else {
                WorkerHelper.cancelTodayForecastUpdateWork(context);
            }
        } else {
            WorkerHelper.cancelNormalPollingWork(context);
            WorkerHelper.cancelTodayForecastUpdateWork(context);
            WorkerHelper.cancelTomorrowForecastUpdateWork(context);

            PermanentServiceHelper.startPollingService(context);
        }
    }

    public static void resetTomorrowForecastBackgroundTask(Context context, boolean forceRefresh,
                                                           boolean nextDay) {
        SettingsManager settings = SettingsManager.getInstance(context);

        if (forceRefresh) {
            forceRefresh(context);
            return;
        }

        if (settings.isBackgroundFree()) {
            PermanentServiceHelper.stopPollingService(context);

            if (settings.isTomorrowForecastEnabled()) {
                WorkerHelper.setTomorrowForecastUpdateWork(context, settings.getTomorrowForecastTime(), nextDay);
            } else {
                WorkerHelper.cancelTomorrowForecastUpdateWork(context);
            }
        } else {
            WorkerHelper.cancelNormalPollingWork(context);
            WorkerHelper.cancelTodayForecastUpdateWork(context);
            WorkerHelper.cancelTomorrowForecastUpdateWork(context);

            PermanentServiceHelper.startPollingService(context);
        }
    }

    private static void forceRefresh(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            AsyncHelper.runOnIO(() -> {
                List<Location> locationList = DatabaseHelper.getInstance(context).readLocationList();
                for (int i = 0; i < locationList.size(); i ++) {
                    locationList.set(
                            i, Location.copy(
                                    locationList.get(i),
                                    DatabaseHelper.getInstance(context).readWeather(locationList.get(i))
                            )
                    );
                }

//                WidgetHelper.updateWidgetIfNecessary(context, locationList.get(0));
//                WidgetHelper.updateWidgetIfNecessary(context, locationList);
//                NotificationHelper.updateNotificationIfNecessary(context, locationList);
            });

            WorkerHelper.setExpeditedPollingWork(context);
        } else {
            IntentHelper.startAwakeForegroundUpdateService(context);
        }
    }
}
