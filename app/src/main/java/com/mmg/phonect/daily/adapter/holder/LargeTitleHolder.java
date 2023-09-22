//package com.mmg.phonect.daily.adapter.holder;
//
//import android.view.LayoutInflater;
//import android.view.ViewGroup;
//import android.widget.TextView;
//
//import com.mmg.phonect.R;
//import com.mmg.phonect.daily.adapter.DailyWeatherAdapter;
//import com.mmg.phonect.daily.adapter.model.LargeTitle;
//
//public class LargeTitleHolder extends DailyWeatherAdapter.ViewHolder {
//
//    public LargeTitleHolder(ViewGroup parent) {
//        super(LayoutInflater.from(parent.getContext())
//                .inflate(R.layout.item_weather_daily_title_large, parent, false));
//    }
//
//    @Override
//    public void onBindView(DailyWeatherAdapter.ViewModel model, int position) {
//        LargeTitle title = (LargeTitle) model;
//        ((TextView) itemView).setText(title.getTitle());
//    }
//}
