package com.mmg.phonect.background.polling.services.permanent.update;

import android.content.Context;

import java.util.List;

import dagger.hilt.android.AndroidEntryPoint;
import com.mmg.phonect.PhoneCt;
import com.mmg.phonect.background.polling.services.basic.ForegroundUpdateService;
import com.mmg.phonect.background.polling.services.permanent.PermanentServiceHelper;
import com.mmg.phonect.common.basic.models.Location;
import com.mmg.phonect.remoteviews.NotificationHelper;
import com.mmg.phonect.remoteviews.WidgetHelper;

/**
 * Foreground normal update service.
 * */

@AndroidEntryPoint
public class ForegroundNormalUpdateService extends ForegroundUpdateService {

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
        PermanentServiceHelper.updatePollingService(this, failed);
    }

    @Override
    public int getForegroundNotificationId() {
        return PhoneCt.NOTIFICATION_ID_UPDATING_NORMALLY;
    }
}
