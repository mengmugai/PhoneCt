package com.mmg.phonect.daily.adapter.holder;

import android.annotation.SuppressLint;
import android.content.res.ColorStateList;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.widget.AppCompatImageView;

import com.mmg.phonect.R;
import com.mmg.phonect.common.basic.models.options.unit.SpeedUnit;
import com.mmg.phonect.common.basic.models.weather.Wind;
import com.mmg.phonect.daily.adapter.DailyWeatherAdapter;
import com.mmg.phonect.daily.adapter.model.DailyWind;
import com.mmg.phonect.settings.SettingsManager;

public class WindHolder extends DailyWeatherAdapter.ViewHolder {

    private final AppCompatImageView mIcon;

    private final TextView mDirectionText;

    private final LinearLayout mSpeed;
    private final TextView mSpeedText;

    private final TextView mGaugeText;

    private final SpeedUnit mSpeedUnit;

    public WindHolder(ViewGroup parent) {
        super(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_weather_daily_wind, parent, false));

        mIcon = itemView.findViewById(R.id.item_weather_daily_wind_arrow);
        mDirectionText = itemView.findViewById(R.id.item_weather_daily_wind_directionValue);
        mSpeed = itemView.findViewById(R.id.item_weather_daily_wind_speed);
        mSpeedText = itemView.findViewById(R.id.item_weather_daily_wind_speedValue);
        mGaugeText = itemView.findViewById(R.id.item_weather_daily_wind_levelValue);

        mSpeedUnit = SettingsManager.getInstance(parent.getContext()).getSpeedUnit();
    }

    @SuppressLint({"SetTextI18n", "RestrictedApi"})
    @Override
    public void onBindView(DailyWeatherAdapter.ViewModel model, int position) {
        Wind wind = ((DailyWind) model).getWind();
        StringBuilder talkBackBuilder = new StringBuilder(
                itemView.getContext().getString(R.string.wind));

        mIcon.setSupportImageTintList(ColorStateList.valueOf(wind.getWindColor(itemView.getContext())));
        mIcon.setRotation(wind.getDegree().getDegree() + 180);

        talkBackBuilder.append(", ").append(wind.getDirection());
        if (wind.getDegree().isNoDirection() || wind.getDegree().getDegree() % 45 == 0) {
            mDirectionText.setText(wind.getDirection());
        } else {
            mDirectionText.setText(wind.getDirection()
                    + " (" + (int) (wind.getDegree().getDegree() % 360) + "°)");
        }

        if (wind.getSpeed() != null && wind.getSpeed() > 0) {
            talkBackBuilder.append(", ")
                    .append(mSpeedUnit.getValueText(mSpeedText.getContext(), wind.getSpeed()));

            mSpeed.setVisibility(View.VISIBLE);
            mSpeedText.setText(mSpeedUnit.getValueText(mSpeedText.getContext(), wind.getSpeed()));
        } else {
            mSpeed.setVisibility(View.GONE);
        }

        talkBackBuilder.append(", ").append(wind.getLevel());
        mGaugeText.setText(wind.getLevel());

        itemView.setContentDescription(talkBackBuilder.toString());
    }
}
