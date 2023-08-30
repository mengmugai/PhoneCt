package com.mmg.phonect.main

import android.content.Context
import com.mmg.phonect.common.basic.models.Location
import com.mmg.phonect.common.utils.helpers.AsyncHelper
import com.mmg.phonect.db.DatabaseHelper
import com.mmg.phonect.location.LocationHelper
import com.mmg.phonect.device.DeviceHelper
import com.mmg.phonect.device.DeviceHelper.OnRequestWeatherListener
import java.util.concurrent.Executors
import javax.inject.Inject

class MainActivityRepository @Inject constructor(
    private val locationHelper: LocationHelper,
    private val deviceHelper: DeviceHelper
) {
    private val singleThreadExecutor = Executors.newSingleThreadExecutor()

    interface WeatherRequestCallback {
        fun onCompleted(
            location: Location,
            locationFailed: Boolean?,
            weatherRequestFailed: Boolean
        )
    }

    fun destroy() {
        cancelWeatherRequest()
    }

    fun initLocations(context: Context, formattedId: String): List<Location> {
        val list = DatabaseHelper.getInstance(context).readLocationList()

        var index = 0
        for (i in list.indices) {
            if (list[i].formattedId == formattedId) {
                index = i
                break
            }
        }

        list[index] = Location.copy(
            src = list[index],
            weather = DatabaseHelper.getInstance(context).readWeather(list[index])
        )
        return list
    }

    fun getWeatherCacheForLocations(
        context: Context,
        oldList: List<Location>,
        ignoredFormattedId: String,
        callback: AsyncHelper.Callback<List<Location>>
    ) {
        AsyncHelper.runOnExecutor({ emitter ->
            emitter.send(
                oldList.map {
                    if (it.formattedId == ignoredFormattedId) {
                        it
                    } else {
                        Location.copy(
                            src = it,
                            weather = DatabaseHelper.getInstance(context).readWeather(it)
                        )
                    }
                },
                true
            )
        }, callback, singleThreadExecutor)
    }

    fun writeLocationList(context: Context, locationList: List<Location>) {
        AsyncHelper.runOnExecutor({ 
            DatabaseHelper.getInstance(context).writeLocationList(locationList)
        }, singleThreadExecutor)
    }

    fun deleteLocation(context: Context, location: Location) {
        AsyncHelper.runOnExecutor({
            DatabaseHelper.getInstance(context).deleteLocation(location)
            DatabaseHelper.getInstance(context).deleteWeather(location)
        }, singleThreadExecutor)
    }

    fun getWeather(
        context: Context,
        location: Location,
        locate: Boolean,
        callback: WeatherRequestCallback,
    ) {
        if (locate) {
            ensureValidLocationInformation(context, location, callback)
        } else {
            getWeatherWithValidLocationInformation(context, location, null, callback)
        }
    }

    private fun ensureValidLocationInformation(
        context: Context,
        location: Location,
        callback: WeatherRequestCallback,
    ) = locationHelper.requestLocation(
        context,
        location,
        false,
        object : LocationHelper.OnRequestLocationListener {

            override fun requestLocationSuccess(requestLocation: Location) {
                if (requestLocation.formattedId != location.formattedId) {
                    return
                }
                getWeatherWithValidLocationInformation(
                    context,
                    requestLocation,
                    false,
                    callback
                )
            }

            override fun requestLocationFailed(requestLocation: Location) {
                if (requestLocation.formattedId != location.formattedId) {
                    return
                }
                getWeatherWithValidLocationInformation(
                    context,
                    requestLocation,
                    true,
                    callback
                )
            }
        }
    )

    private fun getWeatherWithValidLocationInformation(
        context: Context,
        location: Location,
        locationFailed: Boolean?,
        callback: WeatherRequestCallback,
    ) = deviceHelper.requestWeather(
        context,
        location,
        object : OnRequestWeatherListener {
            override fun requestWeatherSuccess(requestLocation: Location) {
                if (requestLocation.formattedId != location.formattedId) {
                    return
                }
                callback.onCompleted(
                    requestLocation,
                    locationFailed = locationFailed,
                    weatherRequestFailed = false
                )
            }

            override fun requestWeatherFailed(requestLocation: Location) {
                if (requestLocation.formattedId != location.formattedId) {
                    return
                }
                callback.onCompleted(
                    requestLocation,
                    locationFailed = locationFailed,
                    weatherRequestFailed = true
                )
            }
        }
    )

    fun getLocatePermissionList(context: Context) = locationHelper
        .getPermissions(context)
        .toList()

    fun cancelWeatherRequest() {
        locationHelper.cancel()
        deviceHelper.cancel()
    }
}