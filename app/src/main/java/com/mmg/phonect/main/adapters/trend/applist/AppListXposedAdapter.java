package com.mmg.phonect.main.adapters.trend.applist;

import static com.mmg.phonect.device.info.AppListInfo.getAppListInfo;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.mmg.phonect.R;
import com.mmg.phonect.common.basic.GeoActivity;
import com.mmg.phonect.common.basic.models.Location;
import com.mmg.phonect.common.basic.models.options.unit.CloudCoverUnit;
import com.mmg.phonect.common.basic.models.options.unit.SpeedUnit;
import com.mmg.phonect.common.basic.models.weather.Hourly;
import com.mmg.phonect.common.basic.models.weather.Weather;
import com.mmg.phonect.common.basic.models.weather.Wind;
import com.mmg.phonect.common.basic.models.weather.XposedModule;
import com.mmg.phonect.common.ui.images.RotateDrawable;
import com.mmg.phonect.common.ui.widgets.trend.TrendRecyclerView;
import com.mmg.phonect.common.ui.widgets.trend.chart.PolylineAndHistogramView;
import com.mmg.phonect.main.adapters.AppListAdapter;
import com.mmg.phonect.main.adapters.DeviceAdapter;
import com.mmg.phonect.main.utils.MainThemeColorProvider;
import com.mmg.phonect.settings.SettingsManager;

import java.util.ArrayList;
import java.util.List;

/**
 * xposed模块列表
 * */
public class AppListXposedAdapter extends AbsAppListTrendAdapter {


//    class ViewHolder extends AbsAppListTrendAdapter.ViewHolder {
//
//        private final PolylineAndHistogramView mPolylineAndHistogramView;
//
//        ViewHolder(View itemView) {
//            super(itemView);
//
//            mPolylineAndHistogramView = new PolylineAndHistogramView(itemView.getContext());
////            hourlyItem.setChartItemView(mPolylineAndHistogramView);
//        }
//
//        @SuppressLint("SetTextI18n, InflateParams")
//        void onBindView(GeoActivity activity, Location location, int position) {
//            StringBuilder talkBackBuilder = new StringBuilder(activity.getString(R.string.tag_wind));
//
//            super.onBindView(activity, location, talkBackBuilder, position);
//
//            Weather weather = location.getWeather();
//            assert weather != null;
//            Hourly hourly = weather.getHourlyForecast().get(position);
//
//            talkBackBuilder
//                    .append(", ").append(activity.getString(R.string.tag_wind))
//                    .append(" : ").append("啥啥啥");
//
//            int daytimeWindColor = hourly.getWind().getWindColor(activity);
//
//            RotateDrawable dayIcon = hourly.getWind().isValidSpeed()
//                    ? new RotateDrawable(ContextCompat.getDrawable(activity, R.drawable.ic_navigation))
//                    : new RotateDrawable(ContextCompat.getDrawable(activity, R.drawable.ic_circle_medium));
//            dayIcon.rotate(hourly.getWind().getDegree().getDegree() + 180);
//            dayIcon.setColorFilter(new PorterDuffColorFilter(daytimeWindColor, PorterDuff.Mode.SRC_ATOP));
////            hourlyItem.setIconDrawable(dayIcon);
//
//            Float daytimeWindSpeed = weather.getHourlyForecast().get(position).getWind().getSpeed();
////            mPolylineAndHistogramView.setData(
////                    null, null,
////                    null, null,
////                    null, null,
////                    weather.getHourlyForecast().get(position).getWind().getSpeed(),
////                    mSpeedUnit.getValueTextWithoutUnit(daytimeWindSpeed == null ? 0 : daytimeWindSpeed),
////                    mHighestWindSpeed, 0f
////            );
//            mPolylineAndHistogramView.setLineColors(
//                    daytimeWindColor,
//                    daytimeWindColor,
//                    MainThemeColorProvider.getColor(location, R.attr.colorOutline)
//            );
//            mPolylineAndHistogramView.setTextColors(
//                    MainThemeColorProvider.getColor(location, R.attr.colorTitleText),
//                    MainThemeColorProvider.getColor(location, R.attr.colorBodyText),
//                    MainThemeColorProvider.getColor(location, R.attr.colorTitleText)
//            );
//            mPolylineAndHistogramView.setHistogramAlpha(1f);
//
////            hourlyItem.setContentDescription(talkBackBuilder.toString());
//        }
//    }

    @SuppressLint("SimpleDateFormat")
    public AppListXposedAdapter(Context context,RecyclerView parent, Location location) {
        super(context, location);
        Log.d("TAG", "AppListXposedAdapter: ");

//        SettingsManager settings = SettingsManager.getInstance(activity);
        List<XposedModule> xposedModules = getAppListInfo(context);
        for (com.mmg.phonect.common.basic.models.weather.XposedModule bean : xposedModules) {
            try {
                Log.d("TAG", "AppListXposedAdapter:11111111");
                mIndexList.add(
                        new Index(
                                bean.getIcon(),
                                bean.getName(),
                                bean.getPackageName(),
                                 bean.getName()
                                        + ", " + bean.getPackageName().replace("\n", ", ")
                        )
                );
                Log.d("TAG", "AppListXposedAdapter:"+bean.getName());

            } catch (Exception e) {
                Log.d("TAG", "AppListXposedAdapter: "+e.getMessage());
                e.printStackTrace();
            }
        }
        parent.invalidate();





        //======================================

//        Weather weather = location.getWeather();
//        assert weather != null;
////        mSpeedUnit = unit;
//
//        mHighestWindSpeed = Integer.MIN_VALUE;
//        Float daytimeWindSpeed;
//        boolean valid = false;
//        for (int i = weather.getHourlyForecast().size() - 1; i >= 0; i --) {
//            daytimeWindSpeed = weather.getHourlyForecast().get(i).getWind().getSpeed();
//            if (daytimeWindSpeed != null && daytimeWindSpeed > mHighestWindSpeed) {
//                mHighestWindSpeed = daytimeWindSpeed;
//            }
//            if ((daytimeWindSpeed != null && daytimeWindSpeed != 0)
//                    || valid) {
//                valid = true;
//                mSize++;
//            }
//        }
//        if (mHighestWindSpeed == 0) {
//            mHighestWindSpeed = Wind.WIND_SPEED_11;
//        }
//
//        List<TrendRecyclerView.KeyLine> keyLineList = new ArrayList<>();
//        // 这里本来是设置风级的线的  7级  3级 3级 7级
//        parent.setData(keyLineList, mHighestWindSpeed, 0);

    }




}