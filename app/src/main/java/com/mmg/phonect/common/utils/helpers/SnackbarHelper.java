package com.mmg.phonect.common.utils.helpers;

import android.view.View;

import androidx.annotation.Nullable;

import com.mmg.phonect.PhoneCt;
import com.mmg.phonect.common.basic.GeoActivity;
import com.mmg.phonect.common.snackbar.Snackbar;
import com.mmg.phonect.common.snackbar.SnackbarContainer;

public class SnackbarHelper {

    public static void showSnackbar(String content) {
        showSnackbar(content, null, null);
    }

    public static void showSnackbar(GeoActivity activity, String content) {
        showSnackbar(activity, content, null, null);
    }

    public static void showSnackbar(String content,
                                    @Nullable String action, @Nullable View.OnClickListener l) {
        showSnackbar(content, action, l, null);
    }

    public static void showSnackbar(GeoActivity activity, String content,
                                    @Nullable String action, @Nullable View.OnClickListener l) {
        showSnackbar(activity, content, action, l, null);
    }

    public static void showSnackbar(String content,
                                    @Nullable String action, @Nullable View.OnClickListener l,
                                    @Nullable Snackbar.Callback callback) {

        GeoActivity activity = PhoneCt.getInstance().getTopActivity();
        if (activity != null) {
            showSnackbar(activity, content, action, l, callback);
        }
    }

    public static void showSnackbar(GeoActivity activity, String content,
                                    @Nullable String action, @Nullable View.OnClickListener l,
                                    @Nullable Snackbar.Callback callback) {
        if (action != null && l == null) {
            throw new RuntimeException("Must send a non null listener as parameter.");
        }

        if (callback == null) {
            callback = new Snackbar.Callback();
        }

        SnackbarContainer container = activity.provideSnackbarContainer();

        Snackbar.make(container.container, content, Snackbar.LENGTH_LONG, container.cardStyle)
                .setAction(action, l)
                .setCallback(callback)
                .show();
    }
}
