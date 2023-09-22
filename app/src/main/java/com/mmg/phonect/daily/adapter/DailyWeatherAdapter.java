//package com.mmg.phonect.daily.adapter;
//
//import android.content.Context;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//
//import androidx.annotation.NonNull;
//import androidx.recyclerview.widget.GridLayoutManager;
//import androidx.recyclerview.widget.RecyclerView;
//
//import java.util.ArrayList;
//import java.util.List;
//import java.util.TimeZone;
//
//import com.mmg.phonect.R;
//import com.mmg.phonect.common.basic.models.options.unit.DurationUnit;
//import com.mmg.phonect.common.basic.models.options.unit.PrecipitationUnit;
//import com.mmg.phonect.common.basic.models.options.unit.ProbabilityUnit;
//import com.mmg.phonect.common.basic.models.options.unit.TemperatureUnit;
//import com.mmg.phonect.common.basic.models.weather.Daily;
//import com.mmg.phonect.common.basic.models.weather.HalfDay;
//import com.mmg.phonect.common.basic.models.weather.Precipitation;
//import com.mmg.phonect.common.basic.models.weather.PrecipitationDuration;
//import com.mmg.phonect.common.basic.models.weather.PrecipitationProbability;
//import com.mmg.phonect.common.basic.models.weather.Temperature;
//import com.mmg.phonect.daily.adapter.holder.AirQualityHolder;
//import com.mmg.phonect.daily.adapter.holder.AstroHolder;
//import com.mmg.phonect.daily.adapter.holder.LargeTitleHolder;
//import com.mmg.phonect.daily.adapter.holder.LineHolder;
//import com.mmg.phonect.daily.adapter.holder.MarginHolder;
//import com.mmg.phonect.daily.adapter.holder.OverviewHolder;
//import com.mmg.phonect.daily.adapter.holder.PollenHolder;
//import com.mmg.phonect.daily.adapter.holder.TitleHolder;
//import com.mmg.phonect.daily.adapter.holder.UVHolder;
//import com.mmg.phonect.daily.adapter.holder.ValueHolder;
//import com.mmg.phonect.daily.adapter.holder.WindHolder;
//import com.mmg.phonect.daily.adapter.model.DailyAirQuality;
//import com.mmg.phonect.daily.adapter.model.DailyAstro;
//import com.mmg.phonect.daily.adapter.model.DailyPollen;
//import com.mmg.phonect.daily.adapter.model.DailyUV;
//import com.mmg.phonect.daily.adapter.model.DailyWind;
//import com.mmg.phonect.daily.adapter.model.LargeTitle;
//import com.mmg.phonect.daily.adapter.model.Line;
//import com.mmg.phonect.daily.adapter.model.Margin;
//import com.mmg.phonect.daily.adapter.model.Overview;
//import com.mmg.phonect.daily.adapter.model.Title;
//import com.mmg.phonect.daily.adapter.model.Value;
//import com.mmg.phonect.databinding.ItemWeatherDailyPollenBinding;
//import com.mmg.phonect.settings.SettingsManager;
//
//public class DailyWeatherAdapter extends RecyclerView.Adapter<DailyWeatherAdapter.ViewHolder> {
//
//    private final List<ViewModel> mModelList;
//    private final int mSpanCount;
//
//    public interface ViewModel {
//        int getCode();
//    }
//
//    public static abstract class ViewHolder extends RecyclerView.ViewHolder {
//
//        public ViewHolder(@NonNull View itemView) {
//            super(itemView);
//        }
//
//        public abstract void onBindView(ViewModel model, int position);
//    }
//
//    public GridLayoutManager.SpanSizeLookup spanSizeLookup = new GridLayoutManager.SpanSizeLookup() {
//        @Override
//        public int getSpanSize(int position) {
//            int viewType = getItemViewType(position);
//            if (Value.isCode(viewType)) {
//                return 1;
//            } else {
//                return mSpanCount;
//            }
//        }
//    };
//
//    public DailyWeatherAdapter(Context context, TimeZone timeZone, Daily daily, int spanCount) {
//        // model list.
//        mModelList = new ArrayList<>();
//
//        mModelList.add(new LargeTitle(context.getString(R.string.daytime)));
//        mModelList.add(new Overview(daily.day(), true));
//        mModelList.add(new DailyWind(daily.day().getWind()));
//        mModelList.addAll(getHalfDayOptionalModelList(context, daily.day()));
//
//        mModelList.add(new Line());
//        mModelList.add(new LargeTitle(context.getString(R.string.nighttime)));
//        mModelList.add(new Overview(daily.night(), false));
//        mModelList.add(new DailyWind(daily.night().getWind()));
//        mModelList.addAll(getHalfDayOptionalModelList(context, daily.night()));
//
//        mModelList.add(new Line());
//        mModelList.add(new LargeTitle(context.getString(R.string.life_details)));
//        mModelList.add(new DailyAstro(timeZone, daily.sun(), daily.moon(), daily.getMoonPhase()));
//        if (daily.getAirQuality().isValid()) {
//            mModelList.add(new Title(R.drawable.weather_haze_mini_xml, context.getString(R.string.air_quality)));
//            mModelList.add(new DailyAirQuality(daily.getAirQuality()));
//        }
//        if (daily.getPollen().isValid()) {
//            mModelList.add(new Title(R.drawable.ic_flower, context.getString(R.string.allergen)));
//            mModelList.add(new DailyPollen(daily.getPollen()));
//        }
//        if (daily.getUV().isValid()) {
//            mModelList.add(new Title(R.drawable.ic_uv, context.getString(R.string.uv_index)));
//            mModelList.add(new DailyUV(daily.getUV()));
//        }
//        mModelList.add(new Line());
//        mModelList.add(new Value(
//                context.getString(R.string.hours_of_sun),
//                DurationUnit.H.getValueText(context, daily.getHoursOfSun())
//        ));
//        mModelList.add(new Margin());
//
//        // span count.
//        mSpanCount = spanCount;
//    }
//
//    @NonNull
//    @Override
//    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        if (LargeTitle.isCode(viewType)) {
//            return new LargeTitleHolder(parent);
//        } else if (Overview.isCode(viewType)) {
//            return new OverviewHolder(parent);
//        } else if (Line.isCode(viewType)) {
//            return new LineHolder(parent);
//        } else if (Margin.isCode(viewType)) {
//            return new MarginHolder(parent);
//        } else if (Value.isCode(viewType)) {
//            return new ValueHolder(parent);
//        } else if (Title.isCode(viewType)) {
//            return new TitleHolder(parent);
//        } else if (DailyAirQuality.isCode(viewType)) {
//            return new AirQualityHolder(parent);
//        } else if (DailyAstro.isCode(viewType)) {
//            return new AstroHolder(parent);
//        } else if (DailyPollen.isCode(viewType)) {
//            return new PollenHolder(
//                    ItemWeatherDailyPollenBinding.inflate(
//                            LayoutInflater.from(parent.getContext())
//                    )
//            );
//        } else if (DailyUV.isCode(viewType)) {
//            return new UVHolder(parent);
//        } else if (DailyWind.isCode(viewType)) {
//            return new WindHolder(parent);
//        }
//        throw new RuntimeException("Invalid viewType.");
//    }
//
//    @Override
//    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
//        holder.onBindView(mModelList.get(position), position);
//    }
//
//    @Override
//    public int getItemViewType(int position) {
//        return mModelList.get(position).getCode();
//    }
//
//    @Override
//    public int getItemCount() {
//        return mModelList.size();
//    }
//
//    private List<ViewModel> getHalfDayOptionalModelList(Context context, HalfDay halfDay) {
//        List<ViewModel> list = new ArrayList<>();
//        // temperature.
//        Temperature temperature = halfDay.getTemperature();
//        TemperatureUnit temperatureUnit = SettingsManager.getInstance(context).getTemperatureUnit();
//        if (temperature.isValid()) {
//            TemperatureUnit unit = SettingsManager.getInstance(context).getTemperatureUnit();
//            int resId;
//            if (unit == TemperatureUnit.C) {
//                resId = R.drawable.ic_temperature_celsius;
//            } else if (unit == TemperatureUnit.F) {
//                resId = R.drawable.ic_temperature_fahrenheit;
//            } else {
//                resId = R.drawable.ic_temperature_kelvin;
//            }
//            list.add(new Title(resId, context.getString(R.string.temperature)));
//            if (temperature.getRealFeelTemperature() != null) {
//                list.add(new Value(
//                        context.getString(R.string.real_feel_temperature),
//                        temperatureUnit.getValueText(context, temperature.getRealFeelTemperature())
//                ));
//            }
//            if (temperature.getRealFeelShaderTemperature() != null) {
//                list.add(new Value(
//                        context.getString(R.string.real_feel_shade_temperature),
//                        temperatureUnit.getValueText(context, temperature.getRealFeelShaderTemperature())
//                ));
//            }
//            if (temperature.getApparentTemperature() != null) {
//                list.add(new Value(
//                        context.getString(R.string.apparent_temperature),
//                        temperatureUnit.getValueText(context, temperature.getApparentTemperature())
//                ));
//            }
//            if (temperature.getWindChillTemperature() != null) {
//                list.add(new Value(
//                        context.getString(R.string.wind_chill_temperature),
//                        temperatureUnit.getValueText(context, temperature.getWindChillTemperature())
//                ));
//            }
//            if (temperature.getWetBulbTemperature() != null) {
//                list.add(new Value(
//                        context.getString(R.string.wet_bulb_temperature),
//                        temperatureUnit.getValueText(context, temperature.getWetBulbTemperature())
//                ));
//            }
//            if (temperature.getDegreeDayTemperature() != null) {
//                list.add(new Value(
//                        context.getString(R.string.degree_day_temperature),
//                        temperatureUnit.getValueText(context, temperature.getDegreeDayTemperature())
//                ));
//            }
//            list.add(new Margin());
//        }
//
//        // precipitation.
//        Precipitation precipitation = halfDay.getPrecipitation();
//        PrecipitationUnit precipitationUnit = SettingsManager.getInstance(context).getPrecipitationUnit();
//        if (precipitation.getTotal() != null && precipitation.getTotal() > 0) {
//            list.add(new Title(R.drawable.ic_water, context.getString(R.string.precipitation)));
//            list.add(new Value(
//                    context.getString(R.string.total),
//                    precipitationUnit.getValueText(context, precipitation.getTotal())
//            ));
//            if (precipitation.getRain() != null && precipitation.getRain() > 0) {
//                list.add(new Value(
//                        context.getString(R.string.rain),
//                        precipitationUnit.getValueText(context, precipitation.getRain())
//                ));
//            }
//            if (precipitation.getSnow() != null && precipitation.getSnow() > 0) {
//                list.add(new Value(
//                        context.getString(R.string.snow),
//                        precipitationUnit.getValueText(context, precipitation.getSnow())
//                ));
//            }
//            if (precipitation.getIce() != null && precipitation.getIce() > 0) {
//                list.add(new Value(
//                        context.getString(R.string.ice),
//                        precipitationUnit.getValueText(context, precipitation.getIce())
//                ));
//            }
//            if (precipitation.getThunderstorm() != null && precipitation.getThunderstorm() > 0) {
//                list.add(new Value(
//                        context.getString(R.string.thunderstorm),
//                        precipitationUnit.getValueText(context, precipitation.getThunderstorm())
//                ));
//            }
//            list.add(new Margin());
//        }
//
//        // precipitation probability.
//        PrecipitationProbability probability = halfDay.getPrecipitationProbability();
//        if (probability.getTotal() != null && probability.getTotal() > 0) {
//            list.add(new Title(R.drawable.ic_water_percent, context.getString(R.string.precipitation_probability)));
//            list.add(new Value(
//                    context.getString(R.string.total),
//                    ProbabilityUnit.PERCENT.getValueText(context, (int) (float) probability.getTotal())
//            ));
//            if (probability.getRain() != null && probability.getRain() > 0) {
//                list.add(new Value(
//                        context.getString(R.string.rain),
//                        ProbabilityUnit.PERCENT.getValueText(context, (int) (float) probability.getRain())
//                ));
//            }
//            if (probability.getSnow() != null && probability.getSnow() > 0) {
//                list.add(new Value(
//                        context.getString(R.string.snow),
//                        ProbabilityUnit.PERCENT.getValueText(context, (int) (float) probability.getSnow())
//                ));
//            }
//            if (probability.getIce() != null && probability.getIce() > 0) {
//                list.add(new Value(
//                        context.getString(R.string.ice),
//                        ProbabilityUnit.PERCENT.getValueText(context, (int) (float) probability.getIce())
//                ));
//            }
//            if (probability.getThunderstorm() != null && probability.getThunderstorm() > 0) {
//                list.add(new Value(
//                        context.getString(R.string.thunderstorm),
//                        ProbabilityUnit.PERCENT.getValueText(context, (int) (float) probability.getThunderstorm())
//                ));
//            }
//            list.add(new Margin());
//        }
//
//        // precipitation duration.
//        PrecipitationDuration duration = halfDay.getPrecipitationDuration();
//        if (duration.getTotal() != null && duration.getTotal() > 0) {
//            list.add(new Title(R.drawable.ic_time, context.getString(R.string.precipitation_duration)));
//            list.add(new Value(
//                    context.getString(R.string.total),
//                    DurationUnit.H.getValueText(context, duration.getTotal())
//            ));
//            if (duration.getRain() != null && duration.getRain() > 0) {
//                list.add(new Value(
//                        context.getString(R.string.rain),
//                        DurationUnit.H.getValueText(context, duration.getRain())
//                ));
//            }
//            if (duration.getSnow() != null && duration.getSnow() > 0) {
//                list.add(new Value(
//                        context.getString(R.string.snow),
//                        DurationUnit.H.getValueText(context, duration.getSnow())
//                ));
//            }
//            if (duration.getIce() != null && duration.getIce() > 0) {
//                list.add(new Value(
//                        context.getString(R.string.ice),
//                        DurationUnit.H.getValueText(context, duration.getIce())
//                ));
//            }
//            if (duration.getThunderstorm() != null && duration.getThunderstorm() > 0) {
//                list.add(new Value(
//                        context.getString(R.string.thunderstorm),
//                        DurationUnit.H.getValueText(context, duration.getThunderstorm())
//                ));
//            }
//            list.add(new Margin());
//        }
//        return list;
//    }
//}
