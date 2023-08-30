package com.mmg.phonect.main.adapters.trend.hourly;

import android.content.Context;
import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

import java.util.TimeZone;

import com.mmg.phonect.R;
import com.mmg.phonect.common.basic.GeoActivity;
import com.mmg.phonect.common.basic.models.Location;
import com.mmg.phonect.common.basic.models.weather.Hourly;
import com.mmg.phonect.common.basic.models.weather.Weather;
import com.mmg.phonect.common.ui.widgets.trend.TrendRecyclerViewAdapter;
import com.mmg.phonect.common.ui.widgets.trend.item.HourlyTrendItemView;
import com.mmg.phonect.main.dialogs.HourlyWeatherDialog;
import com.mmg.phonect.main.utils.MainThemeColorProvider;

public abstract class AbsHourlyTrendAdapter<VH extends RecyclerView.ViewHolder>
        extends TrendRecyclerViewAdapter<VH>  {

    private final GeoActivity mActivity;

    protected static class ViewHolder extends RecyclerView.ViewHolder {

        public final HourlyTrendItemView hourlyItem;

        ViewHolder(View itemView) {
            super(itemView);
            hourlyItem = itemView.findViewById(R.id.item_trend_hourly);
        }

        void onBindView(GeoActivity activity, Location location,
                        StringBuilder talkBackBuilder, int position) {
            Context context = itemView.getContext();
            Weather weather = location.getWeather();
            TimeZone timeZone = location.getTimeZone();

            assert weather != null;
            Hourly hourly = weather.getHourlyForecast().get(position);

            talkBackBuilder.append(", ").append(hourly.getLongDate(context));
            hourlyItem.setDayText(hourly.getShortDate(context));

            talkBackBuilder
                    .append(", ").append(hourly.getLongDate(activity))
                    .append(", ").append(hourly.getHour(activity));
            hourlyItem.setHourText(hourly.getHour(context));

            boolean useAccentColorForDate = position == 0 || hourly.getHourIn24Format() == 0;
            hourlyItem.setTextColor(
                    MainThemeColorProvider.getColor(location, R.attr.colorTitleText),
                    MainThemeColorProvider.getColor(
                            location,
                            useAccentColorForDate ? R.attr.colorBodyText : R.attr.colorCaptionText
                    )
            );

            hourlyItem.setOnClickListener(v -> onItemClicked(
                    activity, location, getAdapterPosition()
            ));
        }
    }

    public AbsHourlyTrendAdapter(GeoActivity activity, Location location) {
        super(location);
        mActivity = activity;
    }

    protected static void onItemClicked(GeoActivity activity,
                                        Location location,
                                        int adapterPosition) {
        if (activity.isActivityResumed()) {
            HourlyWeatherDialog.show(
                    activity,
                    location.getWeather().getHourlyForecast().get(adapterPosition)
            );
        }
    }

    public GeoActivity getActivity() {
        return mActivity;
    }
}