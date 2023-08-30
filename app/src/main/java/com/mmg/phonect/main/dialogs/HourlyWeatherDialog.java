package com.mmg.phonect.main.dialogs;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.text.SimpleDateFormat;

import com.mmg.phonect.R;
import com.mmg.phonect.common.basic.models.options.unit.PrecipitationUnit;
import com.mmg.phonect.common.basic.models.options.unit.ProbabilityUnit;
import com.mmg.phonect.common.basic.models.options.unit.TemperatureUnit;
import com.mmg.phonect.common.basic.models.weather.Hourly;
import com.mmg.phonect.common.basic.models.weather.WeatherCode;
import com.mmg.phonect.common.ui.widgets.AnimatableIconView;
import com.mmg.phonect.settings.SettingsManager;
import com.mmg.phonect.theme.resource.ResourceHelper;
import com.mmg.phonect.theme.resource.ResourcesProviderFactory;
import com.mmg.phonect.theme.resource.providers.ResourceProvider;

public class HourlyWeatherDialog {

    @SuppressLint("SimpleDateFormat")
    public static void show(Activity activity, Hourly hourly) {
        View view = LayoutInflater
                .from(activity)
                .inflate(R.layout.dialog_weather_hourly, null, false);
        initWidget(view, hourly);

        new MaterialAlertDialogBuilder(activity)
                .setTitle(
                        hourly.getHour(activity)
                                + " - "
                                + new SimpleDateFormat(activity.getString(R.string.date_format_long))
                                .format(hourly.getDate())
                )
                .setView(view)
                .show();
    }

    @SuppressLint({"SetTextI18n", "SimpleDateFormat"})
    private static void initWidget(View view, Hourly hourly) {
        ResourceProvider provider = ResourcesProviderFactory.getNewInstance();

        AnimatableIconView weatherIcon = view.findViewById(R.id.dialog_weather_hourly_icon);

        view.findViewById(R.id.dialog_weather_hourly_weatherContainer).setOnClickListener(v ->
                weatherIcon.startAnimators());

        WeatherCode weatherCode = hourly.getWeatherCode();
        boolean daytime = hourly.isDaylight();
        weatherIcon.setAnimatableIcon(
                ResourceHelper.getWeatherIcons(provider, weatherCode, daytime),
                ResourceHelper.getWeatherAnimators(provider, weatherCode, daytime)
        );

        TextView weatherText = view.findViewById(R.id.dialog_weather_hourly_text);

        SettingsManager settings = SettingsManager.getInstance(view.getContext());
        TemperatureUnit temperatureUnit = settings.getTemperatureUnit();
        PrecipitationUnit precipitationUnit = settings.getPrecipitationUnit();

        StringBuilder builder = new StringBuilder(
                hourly.getWeatherText()
                        + ",  "
                        + hourly.getTemperature().getTemperature(view.getContext(), temperatureUnit)
        );
        if (hourly.getTemperature().getRealFeelTemperature() != null) {
            builder.append("\n")
                    .append(view.getContext().getString(R.string.feels_like))
                    .append(" ")
                    .append(hourly.getTemperature().getRealFeelTemperature(view.getContext(), temperatureUnit));
        }
        if (hourly.getPrecipitation().getTotal() != null) {
            Float p = hourly.getPrecipitation().getTotal();
            builder.append("\n")
                    .append(view.getContext().getString(R.string.precipitation))
                    .append(" : ")
                    .append(precipitationUnit.getValueText(view.getContext(), p));
        }
        if (hourly.getPrecipitationProbability().getTotal() != null
                && hourly.getPrecipitationProbability().getTotal() > 0) {
            Float p = hourly.getPrecipitationProbability().getTotal();
            builder.append("\n")
                    .append(view.getContext().getString(R.string.precipitation_probability))
                    .append(" : ")
                    .append(ProbabilityUnit.PERCENT.getValueText(view.getContext(), (int) (float) p));
        }
        weatherText.setText(builder.toString());
    }
}
