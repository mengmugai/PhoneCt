package com.mmg.phonect.background.polling.services.basic;

import android.content.Context;

import java.util.List;

import dagger.hilt.android.AndroidEntryPoint;
import com.mmg.phonect.PhoneCt;
import com.mmg.phonect.background.polling.PollingManager;
import com.mmg.phonect.common.basic.models.Location;
import com.mmg.phonect.remoteviews.NotificationHelper;
import com.mmg.phonect.remoteviews.WidgetHelper;

/**
 * Awake foreground update service.
 * */

@AndroidEntryPoint
public class AwakeForegroundUpdateService extends ForegroundUpdateService {

    @Override
    public void updateView(Context context, Location location) {
        WidgetHelper.updateWidgetIfNecessary(context, location);
    }

    @Override
    public void updateView(Context context, List<Location> locationList) {
        WidgetHelper.updateWidgetIfNecessary(context, locationList);
        NotificationHelper.updateNotificationIfNecessary(context, locationList);
    }

    @Override
    public void handlePollingResult(boolean failed) {
        PollingManager.resetAllBackgroundTask(this, false);
    }

    @Override
    public int getForegroundNotificationId() {
        return PhoneCt.NOTIFICATION_ID_UPDATING_AWAKE;
    }
}
