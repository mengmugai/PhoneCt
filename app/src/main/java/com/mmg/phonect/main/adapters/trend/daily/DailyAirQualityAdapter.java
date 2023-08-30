package com.mmg.phonect.main.adapters.trend.daily;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

import com.mmg.phonect.R;
import com.mmg.phonect.common.basic.GeoActivity;
import com.mmg.phonect.common.basic.models.Location;
import com.mmg.phonect.common.basic.models.weather.AirQuality;
import com.mmg.phonect.common.basic.models.weather.Daily;
import com.mmg.phonect.common.basic.models.weather.Weather;
import com.mmg.phonect.common.ui.widgets.trend.TrendRecyclerView;
import com.mmg.phonect.common.ui.widgets.trend.chart.PolylineAndHistogramView;
import com.mmg.phonect.main.utils.MainThemeColorProvider;
import com.mmg.phonect.theme.ThemeManager;
import com.mmg.phonect.theme.weatherView.WeatherViewController;

/**
 * Daily air quality adapter.
 * */

public class DailyAirQualityAdapter extends AbsDailyTrendAdapter<DailyAirQualityAdapter.ViewHolder> {

    private int mHighestIndex;
    private int mSize;

    class ViewHolder extends AbsDailyTrendAdapter.ViewHolder {

        private final PolylineAndHistogramView mPolylineAndHistogramView;

        ViewHolder(View itemView) {
            super(itemView);
            mPolylineAndHistogramView = new PolylineAndHistogramView(itemView.getContext());
            dailyItem.setChartItemView(mPolylineAndHistogramView);
        }

        @SuppressLint("DefaultLocale")
        void onBindView(GeoActivity activity,
                        Location location,
                        int position) {
            StringBuilder talkBackBuilder = new StringBuilder(activity.getString(R.string.tag_aqi));

            super.onBindView(activity, location, talkBackBuilder, position);

            assert location.getWeather() != null;
            Daily daily = location.getWeather().getDailyForecast().get(position);
            Integer index = daily.getAirQuality().getAqiIndex();
            talkBackBuilder.append(", ").append(index).append(", ").append(daily.getAirQuality().getAqiText());
            mPolylineAndHistogramView.setData(
                    null, null,
                    null, null,
                    null, null,
                    (float) (index == null ? 0 : index),
                    String.format("%d", index == null ? 0 : index),
                    (float) mHighestIndex,
                    0f
            );
            mPolylineAndHistogramView.setLineColors(
                    daily.getAirQuality().getAqiColor(activity),
                    daily.getAirQuality().getAqiColor(activity),
                    MainThemeColorProvider.getColor(location, R.attr.colorOutline)
            );
            int[] themeColors = ThemeManager
                    .getInstance(itemView.getContext())
                    .getPhoneCtThemeDelegate()
                    .getThemeColors(
                            itemView.getContext(),
                            WeatherViewController.getWeatherKind(location.getWeather()),
                            location.isDaylight()
                    );
            boolean lightTheme = MainThemeColorProvider.isLightTheme(itemView.getContext(), location);
            mPolylineAndHistogramView.setShadowColors(themeColors[1], themeColors[2], lightTheme);
            mPolylineAndHistogramView.setTextColors(
                    MainThemeColorProvider.getColor(location, R.attr.colorTitleText),
                    MainThemeColorProvider.getColor(location, R.attr.colorBodyText),
                    MainThemeColorProvider.getColor(location, R.attr.colorTitleText)
            );
            mPolylineAndHistogramView.setHistogramAlpha(lightTheme ? 1f : 0.5f);

            dailyItem.setContentDescription(talkBackBuilder.toString());
        }
    }

    @SuppressLint("SimpleDateFormat")
    public DailyAirQualityAdapter(GeoActivity activity, TrendRecyclerView parent, Location location) {
        super(activity, location);

        Weather weather = location.getWeather();
        assert weather != null;

        mHighestIndex = Integer.MIN_VALUE;
        boolean valid = false;
        for (int i = weather.getDailyForecast().size() - 1; i >= 0; i --) {
            Integer index = weather.getDailyForecast().get(i).getAirQuality().getAqiIndex();
            if (index != null && index > mHighestIndex) {
                mHighestIndex = index;
            }
            if ((index != null && index != 0) || valid) {
                valid = true;
                mSize++;
            }
        }
        if (mHighestIndex == 0) {
            mHighestIndex = AirQuality.AQI_INDEX_5;
        }

        List<TrendRecyclerView.KeyLine> keyLineList = new ArrayList<>();
        keyLineList.add(
                new TrendRecyclerView.KeyLine(
                        AirQuality.AQI_INDEX_1,
                        String.valueOf(AirQuality.AQI_INDEX_1),
                        activity.getString(R.string.aqi_1),
                        TrendRecyclerView.KeyLine.ContentPosition.ABOVE_LINE
                )
        );
        keyLineList.add(
                new TrendRecyclerView.KeyLine(
                        AirQuality.AQI_INDEX_3,
                        String.valueOf(AirQuality.AQI_INDEX_3),
                        activity.getString(R.string.aqi_3),
                        TrendRecyclerView.KeyLine.ContentPosition.ABOVE_LINE
                )
        );
        keyLineList.add(
                new TrendRecyclerView.KeyLine(
                        AirQuality.AQI_INDEX_5,
                        String.valueOf(AirQuality.AQI_INDEX_5),
                        activity.getString(R.string.aqi_5),
                        TrendRecyclerView.KeyLine.ContentPosition.ABOVE_LINE
                )
        );
        parent.setData(keyLineList, mHighestIndex, 0);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_trend_daily, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.onBindView(getActivity(), getLocation(), position);
    }

    @Override
    public int getItemCount() {
        return mSize;
    }
}