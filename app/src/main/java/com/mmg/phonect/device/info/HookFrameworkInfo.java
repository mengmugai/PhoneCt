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
public class HookFrameworkInfo {


    // 声明你的第一个列表
    private static List<String> packageList = new ArrayList<String>() {{
        add("com.topjohnwu.magisk");
        add("io.github.vvb2060.magisk");
        add("io.github.vvb2060.magisk.lite");
        add("de.robv.android.xposed.installer");
        add("org.meowcat.edxposed.manager");
        add("org.lsposed.manager");
        add("top.canyie.dreamland.manager");
        add("me.weishu.exp");
        add("com.tsng.hidemyapplist");
        add("cn.geektang.privacyspace");
        add("moe.shizuku.redirectstorage");
        add("com.saurik.substrate");
        add("com.virjar.ratel.manager");
        add("org.lsposed.lspatch");
    }};

    public static XposedModule getXposedModuleInfo(PackageInfo info,PackageManager packageManager){
        XposedModule bean = new XposedModule();
//            if (!app.enabled)
//                continue;


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
        return bean;
    }

    /**
     * 获取应用列表
     *
     * @param context
     * @return
     */



    public static List<XposedModule> getHookFrameworkInfo(Context context) {
        List<XposedModule> list = new ArrayList<>();
        Log.d("TAG", "AppListXposedAdapter: 0000000000");
        PackageManager packageManager = context.getApplicationContext().getPackageManager();
        Log.d("TAG", "AppListXposedAdapter:0000000111");
        List<PackageInfo> installedPackages = packageManager.getInstalledPackages(PackageManager.GET_META_DATA);
        Log.d("TAG", "AppListXposedAdapter:00000222222");


        for (PackageInfo item : installedPackages) {
            if (packageList.contains(item.packageName)) {
                list.add(getXposedModuleInfo(item,packageManager));
            }
        }
        return list;
    }

}
