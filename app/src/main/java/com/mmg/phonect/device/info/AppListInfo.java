package com.mmg.phonect.device.info;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.util.Log;

import com.mmg.phonect.common.basic.models.weather.XposedModule;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by chensongsong on 2020/6/3.
 */
public class AppListInfo {

    /**
     * 获取应用列表
     *
     * @param context
     * @return
     */
    public static List<XposedModule> getAppListInfo(Context context) {
        List<XposedModule> list = new ArrayList<>();
        Log.d("TAG", "AppListXposedAdapter: 0000000000");
        PackageManager packageManager = context.getApplicationContext().getPackageManager();
        Log.d("TAG", "AppListXposedAdapter:0000000111");
        List<PackageInfo> installedPackages = packageManager.getInstalledPackages(PackageManager.GET_META_DATA);
        Log.d("TAG", "AppListXposedAdapter:00000222222");
        for (PackageInfo info : installedPackages) {
            Log.d("TAG", "AppListXposedAdapter:000033333");

            XposedModule bean = new XposedModule();
            ApplicationInfo app = info.applicationInfo;
//            if (!app.enabled)
//                continue;

            if (app.metaData != null && app.metaData.containsKey("xposedmodule")) {
                bean.setName(info.applicationInfo.loadLabel(packageManager).toString());
                bean.setPackageName(info.packageName);
                bean.setVersion(info.versionName);
                bean.setIcon(info.applicationInfo.loadIcon(packageManager));
                bean.setBuildVersion(info.applicationInfo.targetSdkVersion);
                if ((ApplicationInfo.FLAG_SYSTEM & info.applicationInfo.flags) == 0) {
                    bean.setSystemApp(false);
                } else {
                    bean.setSystemApp(true);
                }
                list.add(bean);
            }
        }
        return list;
    }

}
