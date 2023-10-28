package com.mmg.phonect.main

//import com.mmg.phonect.device.DeviceHelper.OnRequestWeatherListener
import android.content.Context
import android.os.Build
import android.util.Log
import com.mmg.phonect.common.basic.models.Phone
import com.mmg.phonect.device.DeviceHelper
import com.mmg.phonect.device.DeviceHelper.OnRequestDeviceListener
//import com.mmg.phonect.location.LocationHelper
import java.util.*
import java.util.concurrent.Executors
import javax.inject.Inject

class MainActivityRepository @Inject constructor(
//    private val locationHelper: LocationHelper,
    private val deviceHelper: DeviceHelper
) {
    private val singleThreadExecutor = Executors.newSingleThreadExecutor()

    interface WeatherRequestCallback {
        fun onCompleted(
            phone: Phone,
            locationFailed: Boolean?,
            weatherRequestFailed: Boolean
        )
    }

    fun destroy() {
        cancelWeatherRequest()
    }

    fun initPhone(context: Context): Phone {
//        var phone = viewModel.currentPhone.value!!.phone
//        var phone = DatabaseHelper.getInstance(context).readPhone()
//        if (phone == null){
        var phone = Phone.buildPhone()
//        }

        return phone
    }

//    fun getWeatherCacheForLocations(
//        context: Context,
//        phone: Phone,
//        callback: AsyncHelper.Callback<Phone>
//    ) {
//        AsyncHelper.runOnExecutor({ emitter ->
//            emitter.send(
//
//                        Phone.copy(
//                            src = phone,
//                            device = DatabaseHelper.getInstance(context).readDevice(phone)
//                        ),
//
//                true
//            )
//        }, callback, singleThreadExecutor)
//    }

//    fun writeLocationList(context: Context, phoneList: List<Phone>) {
//        AsyncHelper.runOnExecutor({
//            DatabaseHelper.getInstance(context).writeLocationList(phoneList)
//        }, singleThreadExecutor)
//    }
//
//    fun deleteLocation(context: Context, phone: Phone) {
//        AsyncHelper.runOnExecutor({
//            DatabaseHelper.getInstance(context).deleteLocation(phone)
//            DatabaseHelper.getInstance(context).deleteWeather(phone)
//        }, singleThreadExecutor)
//    }

    fun getWeather(
        context: Context,
        phone: Phone,
        locate: Boolean,
        callback: WeatherRequestCallback,
    ) {
//        if (locate) {
//            //确保有效的位置信息
//            Log.d("tag","onUpdateResult   0")
//            ensureValidLocationInformation(context, phone, callback)
//        } else {

        // 获取phone 数据
        //timeZone: TimeZone? = null,
        //        brand: String? = null,
        //        model: String? = null,
        //        country: String? = null,
        //        province: String? = null,
        //        city: String? = null,
        //        district: String? = null,
        //        device: Device? = null,
        //        weatherSource: WeatherSource? = null,
            phone.copy(
                TimeZone.getTimeZone("Asia/Shanghai"),
                Build.BRAND,
                Build.MODEL
            )
            Log.d("tag","onUpdateResult   1")
            //获取具有有效位置信息的天气
            getWeatherWithValidLocationInformation(context, phone, null, callback)
//        }
    }

//    private fun ensureValidLocationInformation(
//        context: Context,
//        phone: Phone,
//        callback: WeatherRequestCallback,
//    ) = locationHelper.requestLocation(
//        context,
//        phone,
//        false,
//        object : LocationHelper.OnRequestPhoneListener {
//
//            override fun requestPhoneSuccess(requestPhone: Phone) {
//                Log.d("tag","onUpdateResult   0-1")
//                getWeatherWithValidLocationInformation(
//                    context,
//                    requestPhone,
//                    false,
//                    callback
//                )
//            }
//
//
//        }
//    )

    private fun getWeatherWithValidLocationInformation(
        context: Context,
        phone: Phone,
        locationFailed: Boolean?,
        callback: WeatherRequestCallback,
    ) = deviceHelper.requestWeather(
        context,
        phone,
        object : OnRequestDeviceListener {
            override fun requestDeviceSuccess(requestPhone: Phone) {
                Log.d("tag","onUpdateResult   0-1-0")
                callback.onCompleted(
                    requestPhone,
                    locationFailed = locationFailed,
                    weatherRequestFailed = false
                )
            }

            override fun requestDeviceFailed(requestPhone: Phone) {
                Log.d("tag","onUpdateResult   0-1-1")
                callback.onCompleted(
                    requestPhone,
                    locationFailed = locationFailed,
                    weatherRequestFailed = true
                )
            }
        }
    )

    fun getLocatePermissionList(context: Context) = deviceHelper.getPermissions().toList()

    fun cancelWeatherRequest() {
//        locationHelper.cancel()
        deviceHelper.cancel()
    }
}