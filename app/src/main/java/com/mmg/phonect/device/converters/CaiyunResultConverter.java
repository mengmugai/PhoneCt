package com.mmg.phonect.device.converters;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.text.TextUtils;

import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import com.mmg.phonect.common.basic.models.Phone;
import com.mmg.phonect.common.basic.models.weather.Alert;
import com.mmg.phonect.common.basic.models.weather.Device;
import com.mmg.phonect.common.basic.models.weather.WeatherCode;
import com.mmg.phonect.device.json.DebugInfoResult;
import com.mmg.phonect.device.json.DeviceResult;
import com.mmg.phonect.device.json.caiyun.CaiYunMainlyResult;
import com.mmg.phonect.device.services.DeviceService;

public class CaiyunResultConverter {

    @NonNull
    public static DeviceService.WeatherResultWrapper convert(Context context, Phone phone,
                                                             DebugInfoResult debugInfoResult,
                                                             DeviceResult deviceResult) {
        try {

            //todo: debugInfoResult 还没搞
            Device device = new Device(deviceResult.androidid,
                    deviceResult.imei,
                    deviceResult.imei2,
                    deviceResult.meid,
                    deviceResult.meid2,
                    deviceResult.ua,
                    deviceResult.bootid,
                    deviceResult.serial,

                    // 解锁状态
                    debugInfoResult.deviceLock,
                    debugInfoResult.fridaCheck,
                    debugInfoResult.xposedCheck,
                    debugInfoResult.vmCheck,
                    debugInfoResult.rootCheck,
                    debugInfoResult.signCheck,

                    // 调试状态
                    debugInfoResult.debugOpen,
                    //  USB 调试状态
                    debugInfoResult.usbDebugStatus,
                    debugInfoResult.tracerPid,
                    //APP 是否是 debug 版本
                    debugInfoResult.debugVersion,
                    //是否正在调试
                    debugInfoResult.debugConnected,
                        // 虚拟位置
                    debugInfoResult.allowMockLocation,

                    debugInfoResult.allDiseaseInfo

            );
            return new DeviceService.WeatherResultWrapper(device);
        } catch (Exception e) {
            e.printStackTrace();
            return new DeviceService.WeatherResultWrapper(null);
        }
    }


    private static boolean isPrecipitation(WeatherCode code) {
        return code == WeatherCode.RAIN
                || code == WeatherCode.SNOW
                || code == WeatherCode.HAIL
                || code == WeatherCode.SLEET
                || code == WeatherCode.THUNDERSTORM;
    }

    private static List<Alert> getAlertList(CaiYunMainlyResult result) {
        List<Alert> alertList = new ArrayList<>(result.alerts.size());
        for (CaiYunMainlyResult.AlertsBean a : result.alerts) {
            alertList.add(
                    new Alert(
                            a.pubTime.getTime(),
                            a.pubTime,
                            a.pubTime.getTime(),
                            a.title,
                            a.detail,
                            a.type,
                            getAlertPriority(a.level),
                            getAlertColor(a.level)
                    )
            );
        }
        Alert.deduplication(alertList);
        Alert.descByTime(alertList);
        return alertList;
    }

    private static String getWeatherText(String icon) {
        if (TextUtils.isEmpty(icon)) {
            return "未知";
        }

        switch (icon) {
            case "0":
            case "00":
                return "晴";

            case "1":
            case "01":
                return "多云";

            case "2":
            case "02":
                return "阴";

            case "3":
            case "03":
                return "阵雨";

            case "4":
            case "04":
                return "雷阵雨";

            case "5":
            case "05":
                return "雷阵雨伴有冰雹";

            case "6":
            case "06":
                return "雨夹雪";

            case "7":
            case "07":
                return "小雨";

            case "8":
            case "08":
                return  "中雨";

            case "9":
            case "09":
                return  "大雨";

            case "10":
                return  "暴雨";

            case "11":
                return  "大暴雨";

            case "12":
                return  "特大暴雨";

            case "13":
                return  "阵雪";

            case "14":
                return  "小雪";

            case "15":
                return  "中雪";

            case "16":
                return  "大雪";

            case "17":
                return  "暴雪";

            case "18":
                return  "雾";

            case "19":
                return  "冻雨";

            case "20":
                return  "沙尘暴";

            case "21":
                return  "小到中雨";

            case "22":
                return  "中到大雨";

            case "23":
                return  "大到暴雨";

            case "24":
                return  "暴雨到大暴雨";

            case "25":
                return  "大暴雨到特大暴雨";

            case "26":
                return  "小到中雪";

            case "27":
                return  "中到大雪";

            case "28":
                return  "大到暴雪";

            case "29":
                return  "浮尘";

            case "30":
                return  "扬沙";

            case "31":
                return  "强沙尘暴";

            case "53":
            case "54":
            case "55":
            case "56":
                return  "霾";

            default:
                return "未知";
        }
    }

    private static WeatherCode getWeatherCode(String icon) {
        if (TextUtils.isEmpty(icon)) {
            return WeatherCode.CLOUDY;
        }

        switch (icon) {
            case "0":
            case "00":
                return WeatherCode.CLEAR;

            case "1":
            case "01":
                return WeatherCode.PARTLY_CLOUDY;

            case "3":
            case "7":
            case "8":
            case "9":
            case "03":
            case "07":
            case "08":
            case "09":
            case "10":
            case "11":
            case "12":
            case "21":
            case "22":
            case "23":
            case "24":
            case "25":
                return WeatherCode.RAIN;

            case "4":
            case "04":
                return WeatherCode.THUNDERSTORM;

            case "5":
            case "05":
                return WeatherCode.HAIL;

            case "6":
            case "06":
            case "19":
                return WeatherCode.SLEET;

            case "13":
            case "14":
            case "15":
            case "16":
            case "17":
            case "26":
            case "27":
            case "28":
                return WeatherCode.SNOW;

            case "18":
            case "32":
            case "49":
            case "57":
                return WeatherCode.FOG;

            case "20":
            case "29":
            case "30":
                return WeatherCode.WIND;

            case "53":
            case "54":
            case "55":
            case "56":
                return WeatherCode.HAZE;

            default:
                return WeatherCode.CLOUDY;
        }
    }

    private static String getWindDirection(float degree) {
        if (degree < 0) {
            return "无风向";
        }if (22.5 < degree && degree <= 67.5) {
            return "东北风";
        } else if (67.5 < degree && degree <= 112.5) {
            return "东风";
        } else if (112.5 < degree && degree <= 157.5) {
            return "东南风";
        } else if (157.5 < degree && degree <= 202.5) {
            return "南风";
        } else if (202.5 < degree && degree <= 247.5) {
            return "西南风";
        } else if (247.5 < degree && degree <= 292.5) {
            return "西风";
        } else if (292. < degree && degree <= 337.5) {
            return "西北风";
        } else {
            return "北风";
        }
    }

    @Nullable
    private static String getUVDescription(String index) {
        try {
            int num = Integer.parseInt(index);
            if (num <= 2) {
                return "最弱";
            } else if (num <= 4) {
                return "弱";
            } else if (num <= 6) {
                return "中等";
            } else if (num <= 9) {
                return "强";
            } else {
                return "很强";
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @ColorInt
    private static int getAlertPriority(@Nullable String color) {
        if (TextUtils.isEmpty(color)) {
            return 0;
        }
        switch (color) {
            case "蓝":
            case "蓝色":
                return 1;

            case "黄":
            case "黄色":
                return 2;

            case "橙":
            case "橙色":
            case "橘":
            case "橘色":
            case "橘黄":
            case "橘黄色":
                return 3;

            case "红":
            case "红色":
                return 4;
        }

        return 0;
    }

    @ColorInt
    private static int getAlertColor(@Nullable String color) {
        if (TextUtils.isEmpty(color)) {
            return Color.TRANSPARENT;
        }
        switch (color) {
            case "蓝":
            case "蓝色":
                return Color.rgb(51, 100, 255);

            case "黄":
            case "黄色":
                return Color.rgb(250, 237, 36);

            case "橙":
            case "橙色":
            case "橘":
            case "橘色":
            case "橘黄":
            case "橘黄色":
                return Color.rgb(249, 138, 30);

            case "红":
            case "红色":
                return Color.rgb(215, 48, 42);
        }

        return Color.TRANSPARENT;
    }
}