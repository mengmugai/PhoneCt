package com.mmg.phonect.device.utils;

import static com.mmg.phonect.device.utils.FileUtils.readFile;

import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by chensongsong on 2020/7/16.
 */
public class HookUtils {


    public static String checkFrida(Context context){
        if (checkRunningProcesses(context)){
            return "查找到frida 进程";
        } if(mCheckFridaTcp()){
            return "tcp存在端口特征";
        }else  {

            return DebugUtils.checkFrida();
        }

    }

//    frida-server启动后/proc/net/tcp和/proc/net/tcp6中会有特殊标识:69a2，可以通过搜索tcp中的字符串来检测frida是否启动
//    27024的16进制版本
    public static boolean mCheckFridaTcp(){
        String[] stringArrayTcp6;
        String[] stringArrayTcp;
        String tcpStringTcp6 = readFile("/proc/net/tcp6");
        String tcpStringTcp = readFile("/proc/net/tcp");
        boolean isFridaExits = false;
        if(null != tcpStringTcp6 && !"".equals(tcpStringTcp6)){
            stringArrayTcp6 = tcpStringTcp6.split("\n");
            for(String sa : stringArrayTcp6){
                if(sa.toLowerCase().contains(":69a2")){
                    Log.e("TAG","tcp文件中发现Frida特征");
                    isFridaExits = true;
                }
            }
        }
        if(null != tcpStringTcp && !"".equals(tcpStringTcp)){
            stringArrayTcp = tcpStringTcp.split("\n");
            for(String sa : stringArrayTcp){
                if(sa.toLowerCase().contains(":69a2")){
                    Log.e("TAG","tcp文件中发现Frida特征");
                    isFridaExits = true;
                }
            }
        }
        return isFridaExits;
    }

    
    

    public static boolean checkRunningProcesses(Context context) {
        boolean returnValue = false;
        // Get currently running application processes
        ActivityManager activityManager = (ActivityManager) (context.getSystemService(Context.ACTIVITY_SERVICE));

        List<ActivityManager.RunningServiceInfo> list = null;
        if (activityManager != null) {
            list = activityManager.getRunningServices(300);
        }
        if (list != null) {
            String tempName;
            Log.d("mmg","process  list.size:" +  list.size());
            for (int i = 0; i < list.size(); ++i) {
                tempName = list.get(i).process;
                Log.d("mmg","process name:"+ tempName);
                if (tempName.contains("fridaserver")) {
                    returnValue = true;
                }
            }
        }
        else {
            Log.d("mmg","process name:  一条没有");
        }
        return returnValue;
    }







    /**
     * 检查包名是否存在
     *
     * @param context
     * @return
     */
    public static String chargeXposedPackage(Context context) {
        PackageManager packageManager = context.getApplicationContext().getPackageManager();
        List<ApplicationInfo> appliacationInfoList = packageManager.getInstalledApplications(PackageManager.GET_META_DATA);
        if (appliacationInfoList == null) {
            return null;
        }
        for (ApplicationInfo item : appliacationInfoList) {
            if ("de.robv.android.xposed.installer".equals(item.packageName)) {
                return item.packageName;
            }
            if ("com.saurik.substrate".equals(item.packageName)) {
                return item.packageName;
            }
            if ("org.meowcat.edxposed.manager".equals(item.packageName)) {
                return item.packageName;
            }
        }
        return null;
    }

    /**
     * 检测调用栈中的可疑方法
     */
    public static String chargeXposedHookMethod() {
        try {
            throw new Exception("Deteck hook");
        } catch (Exception e) {
            int zygoteInitCallCount = 0;
            for (StackTraceElement item : e.getStackTrace()) {
                // 检测"com.android.internal.os.ZygoteInit"是否出现两次，如果出现两次，则表明Substrate框架已经安装
                if ("com.android.internal.os.ZygoteInit".equals(item.getClassName())) {
                    zygoteInitCallCount++;
                    if (zygoteInitCallCount == 2) {
//                        Log.i(TAG, "Substrate is active on the device.");
                        return "com.saurik.substrate";
                    }
                }

                if ("com.saurik.substrate.MS$2".equals(item.getClassName()) && "invoke".equals(item.getMethodName())) {
//                    Log.i(TAG, "A method on the stack trace has been hooked using Substrate.");
                    return "com.saurik.substrate";
                }

                if ("de.robv.android.xposed.XposedBridge".equals(item.getClassName())
                        && "main".equals(item.getMethodName())) {
//                    Log.i(TAG, "Xposed is active on the device.");
                    return "de.robv.android.xposed.XposedBridge";
                }
                if ("de.robv.android.xposed.XposedBridge".equals(item.getClassName())
                        && "handleHookedMethod".equals(item.getMethodName())) {
//                    Log.i(TAG, "A method on the stack trace has been hooked using Xposed.");
                    return "de.robv.android.xposed.XposedBridge";
                }
            }
            return null;
        }
    }

    /**
     * 检测内存中可疑的jars
     */
    public static String chargeXposedJars() {
        Set<String> libraries = new HashSet<String>();
        String mapsFilename = "/proc/" + android.os.Process.myPid() + "/maps";
        try {
            BufferedReader reader = new BufferedReader(new FileReader(mapsFilename));
            String line;
            while ((line = reader.readLine()) != null) {
//                LogUtils.d("maps line: " + line);
                if (line.toLowerCase().contains("frida")) {
                    return "frida";
                }
                if (line.endsWith(".so") || line.endsWith(".jar")) {
                    int n = line.lastIndexOf(" ");
                    libraries.add(line.substring(n + 1));
                }
            }
            for (String library : libraries) {
                if (library.startsWith("/system/framework/ed") || library.contains("Xposed")) {
                    Log.d("mmg","libraries line: " + library);
                }
                if (library.contains("com.saurik.substrate")) {
//                    Log.i(TAG, "Substrate shared object found: " + library);
                    return "com.saurik.substrate";
                }
                if (library.contains("XposedBridge.jar") || library.contains("edxp.jar")) {
//                    Log.i(TAG, "Xposed JAR found: " + library);
                    return "XposedBridge.jar";
                }
            }
            reader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static boolean classCheck() {
        try {
            ClassLoader classLoader = ClassLoader.getSystemClassLoader();
            classLoader.loadClass("de.robv.android.xposed.XposedHelpers").newInstance();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
    public static boolean riruCheck()  {
        File riruModuleDir = new File("/data/adb/modules/riru");
        if (riruModuleDir.exists() && riruModuleDir.isDirectory()) {
            return true;
        } else {

            File file64 = new File("/system/lib64/libriruloader.so");
            if (file64.exists()) {
                // 文件存在
                return true;
            }
            File file = new File("/system/lib/libriruloader.so");
            return file.exists();
        }
    }

    public static boolean riru2Check(Context context) {
        boolean returnValue = false;
        // Get currently running application processes
        ActivityManager activityManager = (ActivityManager) (context.getSystemService(Context.ACTIVITY_SERVICE));

        List<ActivityManager.RunningServiceInfo> list = null;
        if (activityManager != null) {
            list = activityManager.getRunningServices(300);
        }
        if (list != null) {
            String tempName;
            for (int i = 0; i < list.size(); ++i) {
                tempName = list.get(i).process;
                if (tempName.contains("riru")) {
                    returnValue = true;
                }
            }
        }
        return returnValue;
    }

    public static boolean rooterify(Context context){
//        Runtime mRuntime = Runtime.getRuntime();
        try {
            Runtime.getRuntime().exec("su");
            return true;
        }
        catch (Exception e){
//                Looper.prepare();
//                Toast.makeText(context, "获取ROOT权限时出错!", Toast.LENGTH_LONG).show();
//
//                Looper.loop();
                return false;
            }


    }
}
