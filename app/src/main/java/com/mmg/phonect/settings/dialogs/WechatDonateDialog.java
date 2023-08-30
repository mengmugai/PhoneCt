package com.mmg.phonect.settings.dialogs;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;

import androidx.appcompat.widget.AppCompatImageView;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import com.mmg.phonect.R;
import com.mmg.phonect.common.utils.helpers.ImageHelper;

public class WechatDonateDialog {

    public static void show(Context context) {
        View view = LayoutInflater
                .from(context)
                .inflate(R.layout.dialog_donate_wechat, null, false);

        AppCompatImageView image = view.findViewById(R.id.dialog_donate_wechat_img);
        ImageHelper.load(context, image, R.drawable.donate_wechat);

        new MaterialAlertDialogBuilder(context)
                .setView(view)
                .show();
    }
}
