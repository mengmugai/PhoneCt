package com.mmg.phonect.remoteviews.config;

import android.view.View;
import android.widget.RemoteViews;

import dagger.hilt.android.AndroidEntryPoint;
import com.mmg.phonect.R;
import com.mmg.phonect.remoteviews.presenters.HourlyTrendWidgetIMP;

/**
 * Hourly trend widget config activity.
 * */

@AndroidEntryPoint
public class HourlyTrendWidgetConfigActivity extends AbstractWidgetConfigActivity {

    @Override
    public void initData() {
        super.initData();

        String[] cardStyles = getResources().getStringArray(R.array.widget_card_styles);
        String[] cardStyleValues = getResources().getStringArray(R.array.widget_card_style_values);

        this.cardStyleValueNow = "light";
        this.cardStyles = new String[] {cardStyles[2], cardStyles[3], cardStyles[1]};
        this.cardStyleValues = new String[] {cardStyleValues[2], cardStyleValues[3], cardStyleValues[1]};
    }

    @Override
    public void initView() {
        super.initView();
        mCardStyleContainer.setVisibility(View.VISIBLE);
        mCardAlphaContainer.setVisibility(View.VISIBLE);
    }

    @Override
    public RemoteViews getRemoteViews() {
        return HourlyTrendWidgetIMP.getRemoteViews(
                this, locationNow,
                getResources().getDisplayMetrics().widthPixels,
                cardStyleValueNow, cardAlpha
        );
    }

    @Override
    public String getConfigStoreName() {
        return getString(R.string.sp_widget_hourly_trend_setting);
    }
}