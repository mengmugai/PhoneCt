package com.mmg.phonect.background.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;

import com.mmg.phonect.background.polling.PollingManager;

/**
 * Main receiver.
 * */

public class MainReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if (TextUtils.isEmpty(action)) {
            return;
        }
        switch (action) {
            case Intent.ACTION_BOOT_COMPLETED:
            case Intent.ACTION_WALLPAPER_CHANGED:
                PollingManager.resetAllBackgroundTask(context, true);
                break;
        }
    }
}