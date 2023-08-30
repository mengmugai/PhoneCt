package com.mmg.phonect.background.polling.services.permanent.update;

import android.content.Context;

import androidx.core.app.NotificationCompat;

import java.util.List;

import dagger.hilt.android.AndroidEntryPoint;
import com.mmg.phonect.PhoneCt;
import com.mmg.phonect.R;
import com.mmg.phonect.background.polling.services.basic.ForegroundUpdateService;
import com.mmg.phonect.common.basic.models.Location;
import com.mmg.phonect.remoteviews.presenters.notification.ForecastNotificationIMP;

/**
 * Foreground Today forecast update service.
 * */

@AndroidEntryPoint
public class ForegroundTomorrowForecastUpdateService extends ForegroundUpdateService {

    @Override
    public void updateView(Context context, Location location) {
        if (ForecastNotificationIMP.isEnable(this, false)) {
            ForecastNotificationIMP.buildForecastAndSendIt(context, location, false);
        }
    }

    @Override
    public void updateView(Context context, List<Location> locationList) {
    }

    @Override
    public void handlePollingResult(boolean failed) {
        // do nothing.
    }

    @Override
    public NotificationCompat.Builder getForegroundNotification(int total) {
        return super.getForegroundNotification(total).setContentTitle(
                getString(R.string.geometric_weather) + " " + getString(R.string.forecast)
        );
    }

    @Override
    public int getForegroundNotificationId() {
        return PhoneCt.NOTIFICATION_ID_UPDATING_TOMORROW_FORECAST;
    }
}