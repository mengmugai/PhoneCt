package com.mmg.phonect.main.adapters.trend.daily;

import android.annotation.SuppressLint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

import java.util.ArrayList;
import java.util.List;

import com.mmg.phonect.R;
import com.mmg.phonect.common.basic.GeoActivity;
import com.mmg.phonect.common.basic.models.Location;
import com.mmg.phonect.common.basic.models.options.unit.SpeedUnit;
import com.mmg.phonect.common.basic.models.weather.Daily;
import com.mmg.phonect.common.basic.models.weather.Weather;
import com.mmg.phonect.common.basic.models.weather.Wind;
import com.mmg.phonect.common.ui.images.RotateDrawable;
import com.mmg.phonect.common.ui.widgets.trend.TrendRecyclerView;
import com.mmg.phonect.common.ui.widgets.trend.chart.DoubleHistogramView;
import com.mmg.phonect.main.utils.MainThemeColorProvider;

/**
 * Daily wind adapter.
 * */
public class DailyWindAdapter extends AbsDailyTrendAdapter<DailyWindAdapter.ViewHolder> {

    private final SpeedUnit mSpeedUnit;

    private float mHighestWindSpeed;
    private int mSize;

    class ViewHolder extends AbsDailyTrendAdapter.ViewHolder {

        private final DoubleHistogramView mDoubleHistogramView;

        ViewHolder(View itemView) {
            super(itemView);

            mDoubleHistogramView = new DoubleHistogramView(itemView.getContext());
            dailyItem.setChartItemView(mDoubleHistogramView);
        }

        @SuppressLint("SetTextI18n, InflateParams")
        void onBindView(GeoActivity activity, Location location, int position) {
            StringBuilder talkBackBuilder = new StringBuilder(activity.getString(R.string.tag_wind));

            super.onBindView(activity, location, talkBackBuilder, position);

            Weather weather = location.getWeather();
            assert weather != null;
            Daily daily = weather.getDailyForecast().get(position);

            talkBackBuilder
                    .append(", ").append(activity.getString(R.string.daytime))
                    .append(" : ").append(daily.day().getWind().getWindDescription(activity, mSpeedUnit))
                    .append(", ").append(activity.getString(R.string.nighttime))
                    .append(" : ").append(daily.night().getWind().getWindDescription(activity, mSpeedUnit));

            int daytimeWindColor = daily.day().getWind().getWindColor(activity);
            int nighttimeWindColor = daily.night().getWind().getWindColor(activity);

            RotateDrawable dayIcon = daily.day().getWind().isValidSpeed()
                    ? new RotateDrawable(ContextCompat.getDrawable(activity, R.drawable.ic_navigation))
                    : new RotateDrawable(ContextCompat.getDrawable(activity, R.drawable.ic_circle_medium));
            dayIcon.rotate(daily.day().getWind().getDegree().getDegree() + 180);
            dayIcon.setColorFilter(new PorterDuffColorFilter(daytimeWindColor, PorterDuff.Mode.SRC_ATOP));
            dailyItem.setDayIconDrawable(dayIcon);

            Float daytimeWindSpeed = weather.getDailyForecast().get(position).day().getWind().getSpeed();
            Float nighttimeWindSpeed = weather.getDailyForecast().get(position).night().getWind().getSpeed();
            mDoubleHistogramView.setData(
                    weather.getDailyForecast().get(position).day().getWind().getSpeed(),
                    weather.getDailyForecast().get(position).night().getWind().getSpeed(),
                    mSpeedUnit.getValueTextWithoutUnit(daytimeWindSpeed == null ? 0 : daytimeWindSpeed),
                    mSpeedUnit.getValueTextWithoutUnit(nighttimeWindSpeed == null ? 0 : nighttimeWindSpeed),
                    mHighestWindSpeed
            );
            mDoubleHistogramView.setLineColors(
                    daytimeWindColor,
                    nighttimeWindColor,
                    MainThemeColorProvider.getColor(location, R.attr.colorOutline)
            );
            mDoubleHistogramView.setTextColors(
                    MainThemeColorProvider.getColor(location, R.attr.colorBodyText)
            );
            mDoubleHistogramView.setHistogramAlphas(1f, 0.5f);

            RotateDrawable nightIcon = daily.night().getWind().isValidSpeed()
                    ? new RotateDrawable(ContextCompat.getDrawable(activity, R.drawable.ic_navigation))
                    : new RotateDrawable(ContextCompat.getDrawable(activity, R.drawable.ic_circle_medium));
            nightIcon.rotate(daily.night().getWind().getDegree().getDegree() + 180);
            nightIcon.setColorFilter(new PorterDuffColorFilter(nighttimeWindColor, PorterDuff.Mode.SRC_ATOP));
            dailyItem.setNightIconDrawable(nightIcon);

            dailyItem.setContentDescription(talkBackBuilder.toString());
        }
    }

    @SuppressLint("SimpleDateFormat")
    public DailyWindAdapter(GeoActivity activity, TrendRecyclerView parent,
                            Location location, SpeedUnit unit) {
        super(activity, location);

        Weather weather = location.getWeather();
        assert weather != null;
        mSpeedUnit = unit;

        mHighestWindSpeed = Integer.MIN_VALUE;
        Float daytimeWindSpeed;
        Float nighttimeWindSpeed;
        boolean valid = false;
        for (int i = weather.getDailyForecast().size() - 1; i >= 0; i --) {
            daytimeWindSpeed = weather.getDailyForecast().get(i).day().getWind().getSpeed();
            nighttimeWindSpeed = weather.getDailyForecast().get(i).night().getWind().getSpeed();
            if (daytimeWindSpeed != null && daytimeWindSpeed > mHighestWindSpeed) {
                mHighestWindSpeed = daytimeWindSpeed;
            }
            if (nighttimeWindSpeed != null && nighttimeWindSpeed > mHighestWindSpeed) {
                mHighestWindSpeed = nighttimeWindSpeed;
            }
            if ((daytimeWindSpeed != null && daytimeWindSpeed != 0)
                    || (nighttimeWindSpeed != null && nighttimeWindSpeed != 0)
                    || valid) {
                valid = true;
                mSize++;
            }
        }
        if (mHighestWindSpeed == 0) {
            mHighestWindSpeed = Wind.WIND_SPEED_11;
        }

        List<TrendRecyclerView.KeyLine> keyLineList = new ArrayList<>();
        keyLineList.add(
                new TrendRecyclerView.KeyLine(
                        Wind.WIND_SPEED_3,
                        unit.getValueTextWithoutUnit(Wind.WIND_SPEED_3),
                        activity.getString(R.string.wind_3),
                        TrendRecyclerView.KeyLine.ContentPosition.ABOVE_LINE
                )
        );
        keyLineList.add(
                new TrendRecyclerView.KeyLine(
                        Wind.WIND_SPEED_7,
                        unit.getValueTextWithoutUnit(Wind.WIND_SPEED_7),
                        activity.getString(R.string.wind_7),
                        TrendRecyclerView.KeyLine.ContentPosition.ABOVE_LINE
                )
        );
        keyLineList.add(
                new TrendRecyclerView.KeyLine(
                        -Wind.WIND_SPEED_3,
                        unit.getValueTextWithoutUnit(Wind.WIND_SPEED_3),
                        activity.getString(R.string.wind_3),
                        TrendRecyclerView.KeyLine.ContentPosition.BELOW_LINE
                )
        );
        keyLineList.add(
                new TrendRecyclerView.KeyLine(
                        -Wind.WIND_SPEED_7,
                        unit.getValueTextWithoutUnit(Wind.WIND_SPEED_7),
                        activity.getString(R.string.wind_7),
                        TrendRecyclerView.KeyLine.ContentPosition.BELOW_LINE
                )
        );
        parent.setData(keyLineList, mHighestWindSpeed, -mHighestWindSpeed);
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