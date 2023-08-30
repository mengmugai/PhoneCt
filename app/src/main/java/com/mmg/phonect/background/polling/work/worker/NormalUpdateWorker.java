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
import com.mmg.phonect.common.basic.models.Location;
import com.mmg.phonect.location.LocationHelper;
import com.mmg.phonect.remoteviews.NotificationHelper;
import com.mmg.phonect.remoteviews.WidgetHelper;
import com.mmg.phonect.device.DeviceHelper;

@HiltWorker
public class NormalUpdateWorker extends AsyncUpdateWorker {

    @AssistedInject
    public NormalUpdateWorker(@Assisted @NonNull Context context,
                              @Assisted @NonNull WorkerParameters workerParams,
                              LocationHelper locationHelper,
                              DeviceHelper deviceHelper) {
        super(context, workerParams, locationHelper, deviceHelper);
    }

    @Override
    public void updateView(Context context, Location location) {
        WidgetHelper.updateWidgetIfNecessary(context, location);
    }

    @Override
    public void updateView(Context context, List<Location> locationList) {
        WidgetHelper.updateWidgetIfNecessary(context, locationList);
        NotificationHelper.updateNotificationIfNecessary(context, locationList);
    }

    @SuppressLint("RestrictedApi")
    @Override
    public void handleUpdateResult(SettableFuture<Result> future, boolean failed) {
        future.set(failed ? Result.retry() : Result.success());
    }
}
