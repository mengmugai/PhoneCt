package com.mmg.phonect.remoteviews.config;

import android.os.Build;
import android.view.View;
import android.widget.RemoteViews;

import dagger.hilt.android.AndroidEntryPoint;
import com.mmg.phonect.R;
import com.mmg.phonect.remoteviews.presenters.DayWidgetIMP;

/**
 * Day widget config activity.
 * */

@AndroidEntryPoint
public class DayWidgetConfigActivity extends AbstractWidgetConfigActivity {

    @Override
    public void initData() {
        super.initData();
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
            String[] widgetStyles = getResources().getStringArray(R.array.widget_styles);
            String[] widgetStyleValues = getResources().getStringArray(R.array.widget_style_values);

            this.viewTypeValueNow = "rectangle";
            this.viewTypes = new String[] {
                    widgetStyles[0],
                    widgetStyles[1],
                    widgetStyles[2],
                    widgetStyles[3],
                    widgetStyles[4],
                    widgetStyles[5],
                    widgetStyles[6],
                    widgetStyles[7],
                    widgetStyles[9]
            };
            this.viewTypeValues = new String[] {
                    widgetStyleValues[0],
                    widgetStyleValues[1],
                    widgetStyleValues[2],
                    widgetStyleValues[3],
                    widgetStyleValues[4],
                    widgetStyleValues[5],
                    widgetStyleValues[6],
                    widgetStyleValues[7],
                    widgetStyleValues[9]
            };
        }
    }

    @Override
    public void initView() {
        super.initView();
        mViewTypeContainer.setVisibility(View.VISIBLE);
        mCardStyleContainer.setVisibility(View.VISIBLE);
        mCardAlphaContainer.setVisibility(View.VISIBLE);
        mHideSubtitleContainer.setVisibility(View.VISIBLE);
        mSubtitleDataContainer.setVisibility(View.VISIBLE);
        mTextColorContainer.setVisibility(View.VISIBLE);
        mTextSizeContainer.setVisibility(View.VISIBLE);
    }

    @Override
    public RemoteViews getRemoteViews() {
        return DayWidgetIMP.getRemoteViews(
                this, getLocationNow(),
                viewTypeValueNow, cardStyleValueNow, cardAlpha, textColorValueNow, textSize,
                hideSubtitle, subtitleDataValueNow
        );
    }

    @Override
    public String getConfigStoreName() {
        return getString(R.string.sp_widget_day_setting);
    }
}