package com.mmg.phonect.main.adapters.trend.applist;

import static com.mmg.phonect.device.info.HookFrameworkInfo.getHookFrameworkInfo;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;

import androidx.recyclerview.widget.RecyclerView;

import com.mmg.phonect.R;
import com.mmg.phonect.common.basic.models.Location;
import com.mmg.phonect.common.basic.models.Phone;
import com.mmg.phonect.common.basic.models.weather.XposedModule;

import java.util.List;

/**
 * hook 框架列表
 * */
public class AppListHookFrameworkAdapter extends AbsAppListTrendAdapter {



    @SuppressLint("SimpleDateFormat")
    public AppListHookFrameworkAdapter(Context context, RecyclerView parent, Phone phone) {
        super(context, phone);
        Log.d("TAG", "AppListXposedAdapter: ");

//        SettingsManager settings = SettingsManager.getInstance(activity);
        List<XposedModule> xposedModules = getHookFrameworkInfo(context);
        for (XposedModule bean : xposedModules) {
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
//        parent.invalidate();





        //======================================

//        Weather weather = phone.getWeather();
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