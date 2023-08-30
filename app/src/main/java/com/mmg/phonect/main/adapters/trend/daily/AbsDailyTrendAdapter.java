package com.mmg.phonect.main.adapters.trend.daily;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

import java.util.TimeZone;

import com.mmg.phonect.R;
import com.mmg.phonect.common.basic.GeoActivity;
import com.mmg.phonect.common.basic.models.Location;
import com.mmg.phonect.common.basic.models.weather.Daily;
import com.mmg.phonect.common.basic.models.weather.Weather;
import com.mmg.phonect.common.ui.widgets.trend.TrendRecyclerViewAdapter;
import com.mmg.phonect.common.ui.widgets.trend.item.DailyTrendItemView;
import com.mmg.phonect.common.utils.helpers.IntentHelper;
import com.mmg.phonect.main.utils.MainThemeColorProvider;

public abstract class AbsDailyTrendAdapter<VH extends RecyclerView.ViewHolder> extends TrendRecyclerViewAdapter<VH>  {

    private final GeoActivity mActivity;

    protected static class ViewHolder extends RecyclerView.ViewHolder {

        public final DailyTrendItemView dailyItem;

        ViewHolder(View itemView) {
            super(itemView);
            dailyItem = itemView.findViewById(R.id.item_trend_daily);
        }

        @SuppressLint({"SetTextI18n, InflateParams", "DefaultLocale"})
        void onBindView(GeoActivity activity, Location location,
                        StringBuilder talkBackBuilder, int position) {
            Context context = itemView.getContext();
            Weather weather = location.getWeather();
            TimeZone timeZone = location.getTimeZone();

            assert weather != null;
            Daily daily = weather.getDailyForecast().get(position);

            if (daily.isToday(timeZone)) {
                talkBackBuilder.append(", ").append(context.getString(R.string.today));
                dailyItem.setWeekText(context.getString(R.string.today));
            } else {
                talkBackBuilder.append(", ").append(daily.getWeek(context));
                dailyItem.setWeekText(daily.getWeek(context));
            }

            talkBackBuilder.append(", ").append(daily.getLongDate(context));
            dailyItem.setDateText(daily.getShortDate(context));

            dailyItem.setTextColor(
                    MainThemeColorProvider.getColor(location, R.attr.colorTitleText),
                    MainThemeColorProvider.getColor(location, R.attr.colorBodyText)
            );

            dailyItem.setOnClickListener(v -> onItemClicked(activity, location, getAdapterPosition()));
        }
    }

    public AbsDailyTrendAdapter(GeoActivity activity, Location location) {
        super(location);
        mActivity = activity;
    }

    protected static void onItemClicked(GeoActivity activity, Location location, int adapterPosition) {
        if (activity.isActivityResumed()) {
            IntentHelper.startDailyWeatherActivity(activity, location.getFormattedId(), adapterPosition);
        }
    }

    public GeoActivity getActivity() {
        return mActivity;
    }
}
