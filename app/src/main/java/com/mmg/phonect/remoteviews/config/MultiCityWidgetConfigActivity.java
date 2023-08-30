package com.mmg.phonect.remoteviews.config;

import android.view.View;
import android.widget.RemoteViews;

import java.util.List;

import dagger.hilt.android.AndroidEntryPoint;
import com.mmg.phonect.R;
import com.mmg.phonect.common.basic.models.Location;
import com.mmg.phonect.db.DatabaseHelper;
import com.mmg.phonect.remoteviews.presenters.MultiCityWidgetIMP;

/**
 * Multi city widget config activity.
 * */

@AndroidEntryPoint
public class MultiCityWidgetConfigActivity extends AbstractWidgetConfigActivity {

    private List<Location> locationList;

    @Override
    public void initData() {
        super.initData();

        locationList = DatabaseHelper.getInstance(this).readLocationList();
        for (int i = 0; i < locationList.size(); i ++) {
            locationList.set(
                    i, Location.copy(
                            locationList.get(i),
                            DatabaseHelper.getInstance(this).readWeather(locationList.get(i))
                    )
            );
        }
    }

    @Override
    public void initView() {
        super.initView();
        mCardStyleContainer.setVisibility(View.VISIBLE);
        mCardAlphaContainer.setVisibility(View.VISIBLE);
        mTextColorContainer.setVisibility(View.VISIBLE);
        mTextSizeContainer.setVisibility(View.VISIBLE);
    }

    @Override
    public RemoteViews getRemoteViews() {
        return MultiCityWidgetIMP.getRemoteViews(
                this,
                locationList,
                cardStyleValueNow, cardAlpha,
                textColorValueNow, textSize
        );
    }

    @Override
    public String getConfigStoreName() {
        return getString(R.string.sp_widget_multi_city);
    }
}
