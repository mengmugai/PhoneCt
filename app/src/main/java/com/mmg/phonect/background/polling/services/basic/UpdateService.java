package com.mmg.phonect.background.polling.services.basic;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

import javax.inject.Inject;

import com.mmg.phonect.background.polling.PollingUpdateHelper;
import com.mmg.phonect.common.basic.models.Location;
import com.mmg.phonect.common.basic.models.weather.Weather;
import com.mmg.phonect.common.utils.helpers.AsyncHelper;
import com.mmg.phonect.common.utils.helpers.ShortcutsHelper;
import com.mmg.phonect.location.LocationHelper;
import com.mmg.phonect.device.DeviceHelper;

public abstract class UpdateService extends Service
        implements PollingUpdateHelper.OnPollingUpdateListener {

    private PollingUpdateHelper mPollingHelper;
    @Inject LocationHelper mLocationHelper;
    @Inject
    DeviceHelper mDeviceHelper;
    private AsyncHelper.Controller mDelayController;
    private boolean mFailed;

    @Override
    public void onCreate() {
        super.onCreate();

        mFailed = false;

        mPollingHelper = new PollingUpdateHelper(this, mLocationHelper, mDeviceHelper);
        mPollingHelper.setOnPollingUpdateListener(this);
        mPollingHelper.pollingUpdate();

        mDelayController = AsyncHelper.delayRunOnIO(() -> stopService(true), 30 * 1000);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mDelayController != null) {
            mDelayController.cancel();
            mDelayController = null;
        }
        if (mPollingHelper != null) {
            mPollingHelper.setOnPollingUpdateListener(null);
            mPollingHelper.cancel();
            mPollingHelper = null;
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    // control.

    public abstract void updateView(Context context, Location location);

    public abstract void updateView(Context context, List<Location> locationList);

    public abstract void handlePollingResult(boolean updateSucceed);

    public void stopService(boolean updateFailed) {
        handlePollingResult(updateFailed);
        stopSelf();
    }

    // interface.

    // on polling update listener.

    @Override
    public void onUpdateCompleted(@NonNull Location location, @Nullable Weather old,
                                  boolean succeed, int index, int total) {
        if (index == 0) {
            updateView(this, location);
            if (succeed) {

            } else {
                mFailed = true;
            }
        }
    }

    @Override
    public void onPollingCompleted(List<Location> locationList) {
        updateView(this, locationList);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N_MR1) {
            ShortcutsHelper.refreshShortcutsInNewThread(this, locationList);
        }
        stopService(mFailed);
    }
}
