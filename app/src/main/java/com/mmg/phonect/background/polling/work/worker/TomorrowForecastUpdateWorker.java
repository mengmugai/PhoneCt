package com.mmg.phonect.background.polling.work.worker;

import android.annotation.SuppressLint;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.hilt.work.HiltWorker;
import androidx.work.WorkerParameters;
import androidx.work.impl.utils.futures.SettableFuture;

import java.util.List;

import dagger.assisted.Assisted;
import dagger.assisted.AssistedInject;
import com.mmg.phonect.background.polling.PollingManager;
import com.mmg.phonect.common.basic.models.Location;
import com.mmg.phonect.location.LocationHelper;
import com.mmg.phonect.remoteviews.presenters.notification.ForecastNotificationIMP;
import com.mmg.phonect.device.DeviceHelper;

@HiltWorker
public class TomorrowForecastUpdateWorker extends AsyncUpdateWorker {

    @AssistedInject
    public TomorrowForecastUpdateWorker(@Assisted @NonNull Context context,
                                        @Assisted @NonNull WorkerParameters workerParams,
                                        LocationHelper locationHelper,
                                        DeviceHelper deviceHelper) {
        super(context, workerParams, locationHelper, deviceHelper);
    }

    @Override
    public void updateView(Context context, Location location) {
        if (ForecastNotificationIMP.isEnable(context, false)) {
            ForecastNotificationIMP.buildForecastAndSendIt(context, location, false);
        }
    }

    @Override
    public void updateView(Context context, List<Location> locationList) {
    }

    @SuppressLint("RestrictedApi")
    @Override
    public void handleUpdateResult(SettableFuture<Result> future, boolean failed) {
        future.set(failed ? Result.failure() : Result.success());
        PollingManager.resetTomorrowForecastBackgroundTask(
                getApplicationContext(), false, true);
    }
}
