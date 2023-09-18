package com.mmg.phonect.device.info;

import android.content.Context;
import android.os.Build;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;
import android.webkit.WebSettings;


import com.mmg.phonect.PhoneCt;
import com.mmg.phonect.device.json.DeviceResult;
import com.mmg.phonect.device.utils.CommandUtils;

import java.lang.reflect.Method;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class DeviceInfo {


    public static Observable<String> getAndroidIdObservable(Context context) {
        return Observable.fromCallable(() -> {

            return Settings.System.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
        }).subscribeOn(Schedulers.io());
    }

    // 获取 IMEI
    public static Observable<String> getImeiObservable(Context context) {
        return Observable.fromCallable(() -> {
            TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
//            String imei = telephonyManager.getImei();
            return  telephonyManager.getDeviceId();
        }).subscribeOn(Schedulers.io());
    }

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
//                    String imei = telephonyManager.getDeviceId();
                    String imei = "telephonyManager.getDeviceId()";
                    Log.d("mmg", "androidId: -----------------------------2");
                    String meid = null;
                    String ua = getDefaultUserAgent(context);
                    Log.d("mmg", "androidId: -----------------------------3");
                    String bootid = null;
                    String serial = getSerial();
                    Log.d("mmg", "androidId: -----------------------------4");
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        meid = telephonyManager.getMeid();
//                        meid = "telephonyManager.getMeid();";
                    }
                    Log.d("mmg", "androidId: -----------------------------5");
                    String androidId = Settings.System.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
                    Log.d("mmg", "androidId: "+androidId);




                    // 发射设备信息
                    emitter.onNext(new DeviceResult(androidId, imei,null, meid, ua, bootid, serial));
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
