package com.mmg.phonect.remoteviews.config;

import android.view.View;
import android.widget.RemoteViews;

import dagger.hilt.android.AndroidEntryPoint;
import com.mmg.phonect.R;
import com.mmg.phonect.remoteviews.presenters.TextWidgetIMP;

/**
 * Text widget config activity.
 * */

@AndroidEntryPoint
public class TextWidgetConfigActivity extends AbstractWidgetConfigActivity {

    @Override
    public void initView() {
        super.initView();
        mTextColorContainer.setVisibility(View.VISIBLE);
        mTextSizeContainer.setVisibility(View.VISIBLE);
        mAlignEndContainer.setVisibility(View.VISIBLE);
    }

    @Override
    public RemoteViews getRemoteViews() {
        return TextWidgetIMP.getRemoteViews(this, getLocationNow(), textColorValueNow, textSize, alignEnd);
    }

    @Override
    public String getConfigStoreName() {
        return getString(R.string.sp_widget_text_setting);
    }
}