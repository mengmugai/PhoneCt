package com.mmg.phonect.device.utils;

import static androidx.core.content.ContextCompat.checkSelfPermission;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.location.Criteria;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Build;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.util.Log;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Enumeration;
import java.util.Locale;

/**
 * Created by chensongsong on 2020/7/14.
 */
public class DebugUtils {

    static {
        System.loadLibrary("phonect");
    }

    /**
     * 是否开启debug模式
     *
     * @param context
     * @return
     */
    public static boolean isOpenDebug(Context context) {
        try {
            return (Settings.Secure.getInt(context.getContentResolver(), Settings.Secure.ADB_ENABLED, 0) > 0);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * APP 是否是 debug 版本
     *
     * @param context
     * @return
     */
    public static boolean isDebugVersion(Context context) {
        try {
            return (context.getApplicationInfo().flags & ApplicationInfo.FLAG_DEBUGGABLE) != 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 是否正在调试
     *
     * @return
     */
    public static boolean isDebugConnected() {
        try {
            return android.os.Debug.isDebuggerConnected();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 读取当前 USB 调试状态
     *
     * @return
     */
    public static String getUsbDebugStatus() {
        return CommandUtils.execute("getprop init.svc.adbd");
    }

    /**
     * 判断是否打开了允许虚拟位置
     *
     * @param context
     * @return
     */
    public static boolean isAllowMockLocation(Context context) {
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP_MR1) {
            try {
                LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
                String providerStr = LocationManager.GPS_PROVIDER;
                LocationProvider provider = locationManager.getProvider(providerStr);
                if (provider != null) {
                    locationManager.addTestProvider(
                            provider.getName()
                            , provider.requiresNetwork()
                            , provider.requiresSatellite()
                            , provider.requiresCell()
                            , provider.hasMonetaryCost()
                            , provider.supportsAltitude()
                            , provider.supportsSpeed()
                            , provider.supportsBearing()
                            , provider.getPowerRequirement()
                            , provider.getAccuracy());
                } else {
                    locationManager.addTestProvider(
                            providerStr
                            , true, true, false, false, true, true, true
                            , Criteria.POWER_HIGH, Criteria.ACCURACY_FINE);
                }
                locationManager.setTestProviderEnabled(providerStr, true);
                locationManager.setTestProviderStatus(providerStr, LocationProvider.AVAILABLE, null, System.currentTimeMillis());
                // 模拟位置可用
                return true;
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        } else {
            try {
                return Settings.Secure.getInt(context.getContentResolver(), Settings.Secure.ALLOW_MOCK_LOCATION, 0) != 0;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    public static String simulator_files_check() {
        if (FileUtils.exists("/system/bin/androVM-prop")) {//检测androidVM
            return "/system/bin/androVM-prop";
        } else if (FileUtils.exists("/system/bin/microvirt-prop")) {//检测逍遥模拟器--新版本找不到特征
            return "/system/bin/microvirt-prop";
        } else if (FileUtils.exists("/system/lib/libdroid4x.so")) {//检测海马模拟器
            return "/system/lib/libdroid4x.so";
        } else if (FileUtils.exists("/system/bin/windroyed")) {//检测文卓爷模拟器
            return "/system/bin/windroyed";
        } else if (FileUtils.exists("/system/bin/nox-prop")) {//检测夜神模拟器--某些版本找不到特征
            return "/system/bin/nox-prop";
        } else if (FileUtils.exists("system/lib/libnoxspeedup.so")) {//检测夜神模拟器
            return "system/lib/libnoxspeedup.so";
        } else if (FileUtils.exists("/system/bin/ttVM-prop")) {//检测天天模拟器
            return "/system/bin/ttVM-prop";
        } else if (FileUtils.exists("/data/.bluestacks.prop")) {//检测bluestacks模拟器  51模拟器
            return "/data/.bluestacks.prop";
        } else if (FileUtils.exists("/system/bin/duosconfig")) {//检测AMIDuOS模拟器
            return "/system/bin/duosconfig";
        } else if (FileUtils.exists("/system/etc/xxzs_prop.sh")) {//检测星星模拟器
            return "/system/etc/xxzs_prop.sh";
        } else if (FileUtils.exists("/system/etc/mumu-configs/device-prop-configs/mumu.config")) {//网易MuMu模拟器
            return "/system/etc/mumu-configs/device-prop-configs/mumu.config";
        } else if (FileUtils.exists("/system/priv-app/ldAppStore")) {//雷电模拟器
            return "/system/priv-app/ldAppStore";
        } else if (FileUtils.exists("system/bin/ldinit") && FileUtils.exists("system/bin/ldmountsf")) {//雷电模拟器
            return "system/bin/ldinit";
        } else if (FileUtils.exists("/system/app/AntStore") && FileUtils.exists("/system/app/AntLauncher")) {//小蚁模拟器
            return "/system/app/AntStore";
        } else if (FileUtils.exists("vmos.prop")) {//vmos虚拟机
            return "vmos.prop";
        } else if (FileUtils.exists("fstab.titan") && FileUtils.exists("init.titan.rc")) {//光速虚拟机
            return "fstab.titan";
        } else if (FileUtils.exists("x8.prop")) {//x8沙箱和51虚拟机
            return "x8.prop";
        } else if (FileUtils.exists("/system/lib/libc_malloc_debug_qemu.so")) {//AVD QEMU
            return "/system/lib/libc_malloc_debug_qemu.so";
        }
        Log.d("mmg", "simulator file check info not find  ");
        return "";
    }
    public static String checkEmulator(Context context) {
//        ArrayList<String> choose = new ArrayList<>();
//        try {
//            String[] strArr = {
//                    "/boot/bstmods/vboxguest.ko",
//                    "/boot/bstmods/vboxsf.ko",
//                    "/dev/mtp_usb",
//                    "/dev/qemu_pipe",
//                    "/dev/socket/baseband_genyd",
//                    "/dev/socket/genyd",
//                    "/dev/socket/qemud",
//                    "/dev/socket/windroyed-audio",
//                    "/dev/socket/windroyed-camera",
//                    "/dev/socket/windroyed-gps",
//                    "/dev/socket/windroyed-sensors",
//                    "/dev/vboxguest",
//                    "/dev/vboxpci",
//                    "/dev/vboxuser",
//                    "/fstab.goldfish",
//                    "/fstab.nox",
//                    "/fstab.ranchu-encrypt",
//                    "/fstab.ranchu-noencrypt",
//                    "/fstab.ttVM_x86",
//                    "/fstab.vbox86",
//                    "/init.goldfish.rc",
//                    "/init.magisk.rc",
//                    "/init.nox.rc",
//                    "/init.ranchu-encrypt.rc",
//                    "/init.ranchu-noencrypt.rc",
//                    "/init.ranchu.rc",
//                    "/init.ttVM_x86.rc",
//                    "/init.vbox86.rc",
//                    "/init.vbox86p.rc",
//                    "/init.windroye.rc",
//                    "/init.windroye.sh",
//                    "/init.x86.rc",
//                    "/proc/irq/20/vboxguest",
//                    "/sdcard/Android/data/com.redfinger.gamemanage",
//                    "/stab.andy",
//                    "/sys/bus/pci/drivers/vboxguest",
//                    "/sys/bus/pci/drivers/vboxpci",
//                    "/sys/bus/platform/drivers/qemu_pipe",
//                    "/sys/bus/platform/drivers/qemu_pipe/qemu_pipe",
//                    "/sys/bus/platform/drivers/qemu_trace",
//                    "/sys/bus/virtio/drivers/itolsvmlgtp",
//                    "/sys/bus/virtio/drivers/itoolsvmhft",
//                    "/sys/class/bdi/vboxsf-1",
//                    "/sys/class/bdi/vboxsf-2",
//                    "/sys/class/bdi/vboxsf-3",
//                    "/sys/class/misc/qemu_pipe",
//                    "/sys/class/misc/vboxguest",
//                    "/sys/class/misc/vboxuser",
//                    "/sys/devices/platform/qemu_pipe",
//                    "/sys/devices/virtual/bdi/vboxsf-1",
//                    "/sys/devices/virtual/bdi/vboxsf-2",
//                    "/sys/devices/virtual/bdi/vboxsf-3",
//                    "/sys/devices/virtual/misc/qemu_pipe",
//                    "/sys/devices/virtual/misc/vboxguest",
//                    "/sys/devices/virtual/misc/vboxpci",
//                    "/sys/devices/virtual/misc/vboxuser",
//                    "/sys/fs/selinux/booleans/in_qemu",
//                    "/sys/kernel/debug/bdi/vboxsf-1",
//                    "/sys/kernel/debug/bdi/vboxsf-2",
//                    "/sys/kernel/debug/x86",
//                    "/sys/module/qemu_trace_sysfs",
//                    "/sys/module/vboxguest",
//                    "/sys/module/vboxguest/drivers/pci:vboxguest",
//                    "/sys/module/vboxpcism",
//                    "/sys/module/vboxsf",
//                    "/sys/module/vboxvideo",
//                    "/sys/module/virtio_pt/drivers/virtio:itoolsvmhft",
//                    "/sys/module/virtio_pt_ie/drivers/virtio:itoolsvmlgtp",
//                    "/sys/qemu_trace",
//                    "/system/app/GenymotionLayout",
//                    "/system/bin/OpenglService",
//                    "/system/bin/androVM-vbox-sf",
//                    "/system/bin/droid4x",
//                    "/system/bin/droid4x-prop",
//                    "/system/bin/droid4x-vbox-sf",
//                    "/system/bin/droid4x_setprop",
//                    "/system/bin/enable_nox",
//                    "/system/bin/genymotion-vbox-sf",
//                    "/system/bin/microvirt-prop",
//                    "/system/bin/microvirt-vbox-sf",
//                    "/system/bin/microvirt_setprop",
//                    "/system/bin/microvirtd",
//                    "/system/bin/mount.vboxsf",
//                    "/system/bin/nox",
//                    "/system/bin/nox-prop",
//                    "/system/bin/nox-vbox-sf",
//                    "/system/bin/nox_setprop",
//                    "/system/bin/noxd",
//                    "/system/bin/noxscreen",
//                    "/system/bin/noxspeedup",
//                    "/system/bin/qemu-props",
//                    "/system/bin/qemud",
//                    "/system/bin/shellnox",
//                    "/system/bin/ttVM-prop",
//                    "/system/bin/windroyed",
//                    "/system/droid4x",
//                    "/system/etc/init.droid4x.sh",
//                    "/system/etc/init.tiantian.sh",
//                    "/system/lib/egl/libEGL_emulation.so",
//                    "/system/lib/egl/libEGL_tiantianVM.so",
//                    "/system/lib/egl/libEGL_windroye.so",
//                    "/system/lib/egl/libGLESv1_CM_emulation.so",
//                    "/system/lib/egl/libGLESv1_CM_tiantianVM.so",
//                    "/system/lib/egl/libGLESv1_CM_windroye.so",
//                    "/system/lib/egl/libGLESv2_emulation.so",
//                    "/system/lib/egl/libGLESv2_tiantianVM.so",
//                    "/system/lib/egl/libGLESv2_windroye.so",
//                    "/system/lib/hw/audio.primary.vbox86.so",
//                    "/system/lib/hw/audio.primary.windroye.so",
//                    "/system/lib/hw/audio.primary.x86.so",
//                    "/system/lib/hw/autio.primary.nox.so",
//                    "/system/lib/hw/camera.vbox86.so",
//                    "/system/lib/hw/camera.windroye.jpeg.so",
//                    "/system/lib/hw/camera.windroye.so",
//                    "/system/lib/hw/camera.x86.so",
//                    "/system/lib/hw/gps.nox.so",
//                    "/system/lib/hw/gps.vbox86.so",
//                    "/system/lib/hw/gps.windroye.so",
//                    "/system/lib/hw/gralloc.nox.so",
//                    "/system/lib/hw/gralloc.vbox86.so",
//                    "/system/lib/hw/gralloc.windroye.so",
//                    "/system/lib/hw/sensors.nox.so",
//                    "/system/lib/hw/sensors.vbox86.so",
//                    "/system/lib/hw/sensors.windroye.so",
//                    "/system/lib/init.nox.sh",
//                    "/system/lib/libGM_OpenglSystemCommon.so",
//                    "/system/lib/libc_malloc_debug_qemu.so",
//                    "/system/lib/libclcore_x86.bc",
//                    "/system/lib/libdroid4x.so",
//                    "/system/lib/libnoxd.so",
//                    "/system/lib/libnoxspeedup.so",
//                    "/system/lib/modules/3.10.30-android-x86.hd+",
//                    "/system/lib/vboxguest.ko",
//                    "/system/lib/vboxpcism.ko",
//                    "/system/lib/vboxsf.ko",
//                    "/system/lib/vboxvideo.ko",
//                    "/system/lib64/egl/libEGL_emulation.so",
//                    "/system/lib64/egl/libGLESv1_CM_emulation.so",
//                    "/system/lib64/egl/libGLESv2_emulation.so",
//                    "/vendor/lib64/egl/libEGL_emulation.so",
//                    "/vendor/lib64/egl/libGLESv1_CM_emulation.so",
//                    "/vendor/lib64/egl/libGLESv2_emulation.so",
//                    "/vendor/lib64/libandroidemu.so",
//                    "/system/lib64/hw/gralloc.ranchu.so",
//                    "/system/lib64/libc_malloc_debug_qemu.so",
//                    "/system/usr/Keylayout/droid4x_Virtual_Input.kl",
//                    "/system/usr/idc/Genymotion_Virtual_Input.idc",
//                    "/system/usr/idc/droid4x_Virtual_Input.idc",
//                    "/system/usr/idc/nox_Virtual_Input.idc",
//                    "/system/usr/idc/windroye.idc",
//                    "/system/usr/keychars/nox_gpio.kcm",
//                    "/system/usr/keychars/windroye.kcm",
//                    "/system/usr/keylayout/Genymotion_Virtual_Input.kl",
//                    "/system/usr/keylayout/nox_Virtual_Input.kl",
//                    "/system/usr/keylayout/nox_gpio.kl",
//                    "/system/usr/keylayout/windroye.kl",
//                    "system/etc/init/ndk_translation_arm64.rc",
//                    "/system/xbin/noxsu",
//                    "/ueventd.android_x86.rc",
//                    "/ueventd.andy.rc",
//                    "/ueventd.goldfish.rc",
//                    "/ueventd.nox.rc",
//                    "/ueventd.ranchu.rc",
//                    "/ueventd.ttVM_x86.rc",
//                    "/ueventd.vbox86.rc",
//                    "/vendor/lib64/libgoldfish-ril.so",
//                    "/vendor/lib64/libgoldfish_codecs_common.so",
//                    "/vendor/lib64/libstagefright_goldfish_avcdec.so",
//                    "/vendor/lib64/libstagefright_goldfish_vpxdec.so",
//                    "/x86.prop"
//            };
//            for (int i = 0; i < 7; i++) {
//                String f = strArr[i];
//                if (new File(f).exists())
//                    choose.add(f);
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }

        try {
            String[] myArr = {
                    "generic",
                    "vbox"
            };
            for (String str : myArr) {
                if (Build.FINGERPRINT.contains(str))
                    return "FINGERPRINT 存在虚拟环境痕迹";
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            String[] myArr = {
                    "google_sdk",
                    "emulator",
                    "android sdk built for",
                    "droid4x"
            };
            for (String str : myArr) {
                if (Build.MODEL.contains(str))
                    return "MODEL 存在虚拟环境痕迹";
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            String[] myArr = {
                    "Genymotion"
            };
            for (String str : myArr) {
                if (Build.MANUFACTURER.contains(str))
                    return "MANUFACTURER 存在虚拟环境痕迹";
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            String[] myArr = {
                    "google_sdk", "sdk_phone", "sdk_x86", "vbox86p", "nox"
            };
            for (String str : myArr) {
                if (Build.PRODUCT.toLowerCase(Locale.ROOT).contains(str))
                    return "PRODUCT 存在虚拟环境痕迹";
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            String[] myArr = {
                    "nox"
            };
            for (String str : myArr) {
                if (Build.BOARD.toLowerCase(Locale.ROOT).contains(str))
                    return "BOARD 存在虚拟环境痕迹";
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            String[] myArr = {
                    "nox"
            };
            for (String str : myArr) {
                if (Build.BOOTLOADER.toLowerCase(Locale.ROOT).contains(str))
                    return "BOOTLOADER 存在虚拟环境痕迹";
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            String[] myArr = {
                    "ranchu", "vbox86", "goldfish"
            };
            for (String str : myArr) {
                if (Build.HARDWARE.equalsIgnoreCase(str))
                    return "HARDWARE 存在虚拟环境痕迹";
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            Enumeration<NetworkInterface> networkInterfaces = NetworkInterface.getNetworkInterfaces();
            while (networkInterfaces.hasMoreElements()) {
                NetworkInterface ele = networkInterfaces.nextElement();
                if (ele != null) {
                    Enumeration<InetAddress> inetAddresses = ele.getInetAddresses();
                    while (inetAddresses.hasMoreElements()) {
                        InetAddress nextElement = inetAddresses.nextElement();
                        if (!nextElement.isLoopbackAddress() &&
                                (nextElement instanceof Inet4Address)) {
                            String ip = nextElement.getHostAddress();
                            if (ip == null) continue;
                            if (ip.equalsIgnoreCase("10.0.2.15") ||
                                    ip.equalsIgnoreCase("10.0.2.16")) {
                                return "ip 存在虚拟环境痕迹";
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

//        try {
//            String[] qemuProps = {
//                    "ro.kernel.qemu.avd_name",
//                    "ro.kernel.qemu.gles",
//                    "ro.kernel.qemu.gltransport",
//                    "ro.kernel.qemu.opengles.version",
//                    "ro.kernel.qemu.uirenderer",
//                    "ro.kernel.qemu.vsync",
//                    "ro.qemu.initrc",
//                    "init.svc.qemu-props",
//                    "qemu.adb.secure",
//                    "qemu.cmdline",
//                    "qemu.hw.mainkeys",
//                    "qemu.logcat",
//                    "ro.adb.qemud",
//                    "qemu.sf.fake_camera",
//                    "qemu.sf.lcd_density",
//                    "qemu.timezone",
//                    "init.svc.goldfish-logcat",
//                    "ro.boottime.goldfish-logcat",
//                    "ro.hardware.audio.primary",
//                    "init.svc.ranchu-net",
//                    "init.svc.ranchu-setup",
//                    "ro.boottime.ranchu-net",
//                    "ro.boottime.ranchu-setup",
//                    "init.svc.droid4x",
//                    "init.svc.noxd",
//                    "init.svc.qemud",
//                    "init.svc.goldfish-setup",
//                    "init.svc.goldfish-logcat",
//                    "init.svc.ttVM_x86-setup",
//                    "vmos.browser.home",
//                    "vmos.camera.enable",
//                    "ro.trd_yehuo_searchbox",
//                    "init.svc.microvirtd",
//                    "init.svc.vbox86-setup",
//                    "ro.ndk_translation.version",
//                    "redroid.width",
//                    "redroid.height",
//                    "redroid.fps",
//                    "ro.rf.vmname"
//            };
//
//            for (String str : qemuProps) {
//                String val = SystemPropertiesUtils.getProperty(str, null);
//                if (val != null) {
//                    choose.add(str);
//                }
//            }
//        } catch (Throwable e) {
//            e.printStackTrace();
//        }
        //判断是否存在指定硬件
        PackageManager pm = null;
        try {
            pm = context.getPackageManager();
            String[] features = {
                    //PackageManager.FEATURE_RAM_NORMAL,//这个存在问题,自己组装的手机可能导致这个痕迹找不到
                    PackageManager.FEATURE_BLUETOOTH,
                    PackageManager.FEATURE_CAMERA_FLASH,
                    PackageManager.FEATURE_TELEPHONY
            };
            for (String feature : features) {
                if (!pm.hasSystemFeature(feature)) {
                    return "PackageManager 存在虚拟环境痕迹";
                }
            }
        } catch (Throwable ignored) {
        }

        try {
            String[] emuPkgs = {
                    "com.google.android.launcher.layouts.genymotion",
                    "com.bluestacks",
                    "com.bignox.app"
            };

            for (String pkg : emuPkgs) {
                try {
                    if (pm != null) {
                        pm.getPackageInfo(pkg, 0);
                    }
                    return "PackageInfo 存在虚拟环境痕迹";
                } catch (Throwable e) {
                    //e.printStackTrace();
                }
            }
        } catch (Throwable ignored) {

        }

        try {
            SensorManager sensor = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
            int sensorSize = sensor.getSensorList(Sensor.TYPE_ALL).size();
            for (int i = 0; i < sensorSize; i++) {
                Sensor s = sensor.getDefaultSensor(i);
                if (s != null && s.getName().contains("Goldfish")) {
                    return "SensorManager:"+s.getName()+" 存在虚拟环境痕迹";
                }
            }
        } catch (Throwable ignored) {

        }

        try {
            if (checkSelfPermission(context, "android.permission.READ_SMS") == 0 ||
                    checkSelfPermission(context, "android.permission.READ_PHONE_NUMBERS") == 0 ||
                    checkSelfPermission(context, "android.permission.READ_PHONE_STATE") == 0) {
                TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
                String phoneNumber = telephonyManager.getLine1Number();

                String[] phoneNumbers = {
                        "15555215554",
                        "15555215556",
                        "15555215558",
                        "15555215560",
                        "15555215562",
                        "15555215564",
                        "15555215566",
                        "15555215568",
                        "15555215570",
                        "15555215572",
                        "15555215574",
                        "15555215576",
                        "15555215578",
                        "15555215580",
                        "15555215582",
                        "15555215584"
                };
                if(phoneNumber!=null) {
                    for (String phone : phoneNumbers) {
                        if (phoneNumber.equalsIgnoreCase(phone)) {
                            return "phoneNumber: 存在虚拟环境痕迹";
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


        return null;
    }


    public static String checkVM(Context context){
        String vmCheckResult = simulator_files_check();
        if (vmCheckResult != ""){
            return vmCheckResult;
        }
        vmCheckResult = checkEmulator(context);
        if (vmCheckResult != ""){
            return vmCheckResult;
        }
        return "";
    }

    public static native int getTracerPid();

    public static native String checkFrida();

}