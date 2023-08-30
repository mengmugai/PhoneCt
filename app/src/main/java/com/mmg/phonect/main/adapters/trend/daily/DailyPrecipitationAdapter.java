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
import com.mmg.phonect.common.basic.models.options.unit.PrecipitationUnit;
import com.mmg.phonect.common.basic.models.weather.Daily;
import com.mmg.phonect.common.basic.models.weather.Precipitation;
import com.mmg.phonect.common.basic.models.weather.Weather;
import com.mmg.phonect.common.ui.widgets.trend.TrendRecyclerView;
import com.mmg.phonect.common.ui.widgets.trend.chart.DoubleHistogramView;
import com.mmg.phonect.main.utils.MainThemeColorProvider;
import com.mmg.phonect.theme.resource.ResourceHelper;
import com.mmg.phonect.theme.resource.providers.ResourceProvider;

/**
 * Daily precipitation adapter.
 * */
public class DailyPrecipitationAdapter extends AbsDailyTrendAdapter<DailyPrecipitationAdapter.ViewHolder> {

    private final ResourceProvider mResourceProvider;
    private final PrecipitationUnit mPrecipitationUnit;

    private float mHighestPrecipitation;

    class ViewHolder extends AbsDailyTrendAdapter.ViewHolder {

        private final DoubleHistogramView mDoubleHistogramView;

        ViewHolder(View itemView) {
            super(itemView);
            mDoubleHistogramView = new DoubleHistogramView(itemView.getContext());
            dailyItem.setChartItemView(mDoubleHistogramView);
        }

        @SuppressLint("SetTextI18n, InflateParams")
        void onBindView(GeoActivity activity, Location location, int position) {
            StringBuilder talkBackBuilder = new StringBuilder(activity.getString(R.string.tag_precipitation));

            super.onBindView(activity, location, talkBackBuilder, position);

            Weather weather = location.getWeather();
            assert weather != null;
            Daily daily = weather.getDailyForecast().get(position);

            Float daytimePrecipitation = weather.getDailyForecast().get(position).day().getPrecipitation().getTotal();
            Float nighttimePrecipitation = weather.getDailyForecast().get(position).night().getPrecipitation().getTotal();

            daytimePrecipitation = daytimePrecipitation == null ? 0 : daytimePrecipitation;
            nighttimePrecipitation = nighttimePrecipitation == null ? 0 : nighttimePrecipitation;

            if (daytimePrecipitation != 0 || nighttimePrecipitation != 0) {
                talkBackBuilder.append(", ")
                        .append(activity.getString(R.string.daytime))
                        .append(" : ")
                        .append(mPrecipitationUnit.getValueVoice(activity, daytimePrecipitation));
                talkBackBuilder.append(", ")
                        .append(activity.getString(R.string.nighttime))
                        .append(" : ")
                        .append(mPrecipitationUnit.getValueVoice(activity, nighttimePrecipitation));
            } else {
                talkBackBuilder.append(", ")
                        .append(activity.getString(R.string.content_des_no_precipitation));
            }

            dailyItem.setDayIconDrawable(
                    ResourceHelper.getWeatherIcon(mResourceProvider, daily.day().getWeatherCode(), true));

            mDoubleHistogramView.setData(
                    weather.getDailyForecast().get(position).day().getPrecipitation().getTotal(),
                    weather.getDailyForecast().get(position).night().getPrecipitation().getTotal(),
                    mPrecipitationUnit.getValueTextWithoutUnit(daytimePrecipitation),
                    mPrecipitationUnit.getValueTextWithoutUnit(nighttimePrecipitation),
                    mHighestPrecipitation
            );
            mDoubleHistogramView.setLineColors(
                    daily.day().getPrecipitation().getPrecipitationColor(activity),
                    daily.night().getPrecipitation().getPrecipitationColor(activity),
                    MainThemeColorProvider.getColor(location, R.attr.colorOutline)
            );
            mDoubleHistogramView.setTextColors(
                    MainThemeColorProvider.getColor(location, R.attr.colorBodyText)
            );
            mDoubleHistogramView.setHistogramAlphas(1f, 0.5f);

            dailyItem.setNightIconDrawable(
                    ResourceHelper.getWeatherIcon(mResourceProvider, daily.night().getWeatherCode(), false));

            dailyItem.setContentDescription(talkBackBuilder.toString());
        }
    }

    @SuppressLint("SimpleDateFormat")
    public DailyPrecipitationAdapter(GeoActivity activity,
                                     TrendRecyclerView parent,
                                     Location location,
                                     ResourceProvider provider,
                                     PrecipitationUnit unit) {
        super(activity, location);

        Weather weather = location.getWeather();
        assert weather != null;
        mResourceProvider = provider;
        mPrecipitationUnit = unit;

        mHighestPrecipitation = Integer.MIN_VALUE;
        Float daytimePrecipitation;
        Float nighttimePrecipitation;
        for (int i = weather.getDailyForecast().size() - 1; i >= 0; i --) {
            daytimePrecipitation = weather.getDailyForecast().get(i).day().getPrecipitation().getTotal();
            nighttimePrecipitation = weather.getDailyForecast().get(i).night().getPrecipitation().getTotal();
            if (daytimePrecipitation != null && daytimePrecipitation > mHighestPrecipitation) {
                mHighestPrecipitation = daytimePrecipitation;
            }
            if (nighttimePrecipitation != null && nighttimePrecipitation > mHighestPrecipitation) {
                mHighestPrecipitation = nighttimePrecipitation;
            }
        }
        if (mHighestPrecipitation == 0) {
            mHighestPrecipitation = Precipitation.PRECIPITATION_HEAVY;
        }

        List<TrendRecyclerView.KeyLine> keyLineList = new ArrayList<>();
        keyLineList.add(
                new TrendRecyclerView.KeyLine(
                        Precipitation.PRECIPITATION_LIGHT,
                        unit.getValueTextWithoutUnit(Precipitation.PRECIPITATION_LIGHT),
                        activity.getString(R.string.precipitation_light),
                        TrendRecyclerView.KeyLine.ContentPosition.ABOVE_LINE
                )
        );
        keyLineList.add(
                new TrendRecyclerView.KeyLine(
                        Precipitation.PRECIPITATION_HEAVY,
                        unit.getValueTextWithoutUnit(Precipitation.PRECIPITATION_HEAVY),
                        activity.getString(R.string.precipitation_heavy),
                        TrendRecyclerView.KeyLine.ContentPosition.ABOVE_LINE
                )
        );
        keyLineList.add(
                new TrendRecyclerView.KeyLine(
                        -Precipitation.PRECIPITATION_LIGHT,
                        unit.getValueTextWithoutUnit(Precipitation.PRECIPITATION_LIGHT),
                        activity.getString(R.string.precipitation_light),
                        TrendRecyclerView.KeyLine.ContentPosition.BELOW_LINE
                )
        );
        keyLineList.add(
                new TrendRecyclerView.KeyLine(
                        -Precipitation.PRECIPITATION_HEAVY,
                        unit.getValueTextWithoutUnit(Precipitation.PRECIPITATION_HEAVY),
                        activity.getString(R.string.precipitation_heavy),
                        TrendRecyclerView.KeyLine.ContentPosition.BELOW_LINE
                )
        );
        parent.setData(keyLineList, mHighestPrecipitation, -mHighestPrecipitation);
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
        assert getLocation().getWeather() != null;
        return getLocation().getWeather().getDailyForecast().size();
    }
}