//package com.mmg.phonect.daily.adapter.holder;
//
//import android.annotation.SuppressLint;
//import android.view.LayoutInflater;
//import android.view.ViewGroup;
//import android.widget.TextView;
//
//import androidx.core.graphics.ColorUtils;
//
//import com.mmg.phonect.R;
//import com.mmg.phonect.common.basic.models.weather.AirQuality;
//import com.mmg.phonect.common.ui.widgets.RoundProgress;
//import com.mmg.phonect.daily.adapter.DailyWeatherAdapter;
//import com.mmg.phonect.daily.adapter.model.DailyAirQuality;
//
//public class AirQualityHolder extends DailyWeatherAdapter.ViewHolder {
//
//    private final RoundProgress mProgress;
//    private final TextView mContent;
//
//    public AirQualityHolder(ViewGroup parent) {
//        super(LayoutInflater.from(parent.getContext())
//                .inflate(R.layout.item_weather_daily_air, parent, false));
//        mProgress = itemView.findViewById(R.id.item_weather_daily_air_progress);
//        mContent = itemView.findViewById(R.id.item_weather_daily_air_content);
//    }
//
//    @SuppressLint("SetTextI18n")
//    @Override
//    public void onBindView(DailyWeatherAdapter.ViewModel model, int position) {
//        AirQuality airQuality = ((DailyAirQuality) model).getAirQuality();
//
//        int aqi = airQuality.getAqiIndex();
//        int color = airQuality.getAqiColor(itemView.getContext());
//
//        mProgress.setMax(400);
//        mProgress.setProgress(aqi);
//        mProgress.setProgressColor(color);
//        mProgress.setProgressBackgroundColor(
//                ColorUtils.setAlphaComponent(color, (int) (255 * 0.1))
//        );
//
//        mContent.setText(aqi + " / " + airQuality.getAqiText());
//    }
//}