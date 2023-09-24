package com.mmg.phonect.device.info;

import static com.mmg.phonect.device.utils.DebugUtils.checkVM;
import static com.mmg.phonect.device.utils.RootUtils.isRoot;
import static com.tg.android.anti.NativeLib.*;
import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;
import android.webkit.WebSettings;


import androidx.core.content.ContextCompat;
import androidx.core.util.Pair;

import com.mmg.phonect.PhoneCt;
import com.mmg.phonect.device.json.DebugInfoResult;
import com.mmg.phonect.device.json.DeviceResult;
import com.mmg.phonect.device.utils.CommandUtils;
import com.mmg.phonect.device.utils.DebugUtils;
import com.mmg.phonect.device.utils.FileUtils;
import com.mmg.phonect.device.utils.HookUtils;

import java.lang.reflect.Method;
import java.util.Random;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class DeviceInfo {




    public static Observable<DeviceResult> getDeviceInfoObservable(final Context context) {
        Log.d("mmg", "androidId: ++++++++++++++++++++++++");
        return Observable.create(new ObservableOnSubscribe<DeviceResult>() {
            @Override
            public void subscribe(ObservableEmitter<DeviceResult> emitter) throws Exception {

                try {
                    Log.d("mmg", "androidId: -----------------------------");
                    // 获取设备信息
                    TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
                    Log.d("mmg", "androidId: -----------------------------1");
                    String imei = "";
                    String imei2 = "";
                    String meid = "";
                    String meid2 = "";
                    int readIMEI= ContextCompat.checkSelfPermission(context,
                            Manifest.permission.READ_PHONE_STATE);

                    if (readIMEI == PackageManager.PERMISSION_GRANTED) {
                        try {
                            imei = telephonyManager.getDeviceId();
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

                                imei2 = telephonyManager.getImei(1);

                                meid = telephonyManager.getMeid();
                                meid2 = telephonyManager.getMeid(1);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            imei = "未知";
                            imei2 = "未知";
                            meid = CommandUtils.getProperty("persist.sys.meid");
                            Log.d("mmg", "meid: ---------"+meid+"--------------");
                            meid2 = "未知";
                        }

                    }else {
                        Log.d("mmg", "androidId: ---------权限不足--------------");
                    }
                    Log.d("mmg", "androidId: -----------------------------"+imei);

                    String ua = getDefaultUserAgent(context);
                    Log.d("mmg", "androidId: -----------------------------3");
                    String bootid = FileUtils.readFile("/proc/sys/kernel/random/boot_id");
                    String serial = getSerial();
                    Log.d("mmg", "androidId: ------------"+bootid+"-----------------4");



                    Log.d("mmg", "androidId: -----------------------------5");
                    String androidId = Settings.System.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
                    Log.d("mmg", "androidId: "+androidId);




                    // 发射设备信息
                    emitter.onNext(new DeviceResult(androidId, imei,imei2, meid, meid2, ua, bootid, serial));
                    emitter.onComplete();
                } catch (Exception e) {
                    Log.e("mmg", e.getMessage() );
                    emitter.onError(e);
                }
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }

    public static Observable<DebugInfoResult> getDebugInfoObservable(final Context context) {
        Log.d("mmg", "debuginfo: ++++++++++++++++++++++++");
        return Observable.create(new ObservableOnSubscribe<DebugInfoResult>() {
            @Override
            public void subscribe(ObservableEmitter<DebugInfoResult> emitter) throws Exception {

                try {

                    String debugOpen = DebugUtils.isOpenDebug(context) + "";
                    String usbDebugStatus = DebugUtils.getUsbDebugStatus();
                    String tracerPid = DebugUtils.getTracerPid() + "";
                    String debugVersion = DebugUtils.isDebugVersion(context) + "";
                    String debugConnected = DebugUtils.isDebugConnected() + "";
                    String allowMockLocation = DebugUtils.isAllowMockLocation(context) + "";
//                    String allowMockLocation =  "";
                    String deviceLock = CommandUtils.getProperty("ro.boot.verifiedbootstate");
                    String fridaCheck = HookUtils.checkFrida(context);
//                    String fridaCheck = AntiFrida();
                    String xposedCheck = AntiXposed();
//                    String xposedCheck = "";
                    String vmCheck = checkVM(context);
                    String rootCheck = isRoot(context);
                    String signCheck = "功能还没做";




                    // 发射设备信息
                    emitter.onNext(new DebugInfoResult(
                            deviceLock  ,
                            fridaCheck  ,
                            xposedCheck  ,
                            vmCheck  ,
                            rootCheck  ,
                            signCheck  ,

                            debugOpen,
                            usbDebugStatus,
                            tracerPid,
                            debugVersion,
                            debugConnected,
                            allowMockLocation



                    ));
                    emitter.onComplete();
                } catch (Exception e) {
                    Log.e("mmg", e.getMessage() );
                    emitter.onError(e);
                }
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }

//    public static Observable<DeviceResult> getDeviceInfoObservable(Context context) {
//
//
//        return Observable.fromCallable(() -> {
//            TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
//            String imei = telephonyManager.getDeviceId();
//            String meid = null;
//            String ua = getDefaultUserAgent(context);
//            String bootid = null;
//            String serial = getSerial();
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//                meid = telephonyManager.getMeid();
//            }
//            String androidId = Settings.System.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
//            Log.d("mmg", "androidId: "+androidId);
//
//
//            return  new DeviceResult(androidId, imei,null, meid, ua, bootid, serial);
//        });
//
//
////        Observable<String> androidIdObservable = getAndroidIdObservable(context);
////        Observable<String> imeiOrMeidObservable = getImeiObservable(context);
////
////        return Observable.zip(androidIdObservable, imeiOrMeidObservable,
////                        (androidId, imeiOrMeid) -> new DeviceResult(androidId, imeiOrMeid))
////
////                .subscribeOn(Schedulers.io());
//    }

    private static String getSerial() {
        try {
            String serial = Build.SERIAL;
            if (TextUtils.isEmpty(serial)) {
                serial = CommandUtils.getProperty("no.such.thing");
            }
            if (TextUtils.isEmpty(serial)) {
                serial = CommandUtils.getProperty("ro.serialno");
            }
            if (TextUtils.isEmpty(serial)) {
                serial = CommandUtils.getProperty("ro.boot.serialno");
            }
            if (TextUtils.isEmpty(serial)) {
                serial = PhoneCt.UNKNOWN;
            }
            return serial;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return PhoneCt.UNKNOWN;
    }

    private static String getDefaultUserAgent(Context context) {
        String ua = null;
        try {
            ua = System.getProperty("http.agent");
            if (TextUtils.isEmpty(ua)) {
                Method localMethod = WebSettings.class.getDeclaredMethod("getDefaultUserAgent", new Class[]{Context.class});
                ua = (String) localMethod.invoke(WebSettings.class, new Object[]{context});
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return TextUtils.isEmpty(ua) ? PhoneCt.UNKNOWN : ua;
    }

}
