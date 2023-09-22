package com.mmg.phonect.main

import android.app.Application
import android.content.pm.PackageManager
import android.os.Build
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import dagger.hilt.android.lifecycle.HiltViewModel
import com.mmg.phonect.common.basic.GeoViewModel
import com.mmg.phonect.common.basic.livedata.BusLiveData
import com.mmg.phonect.common.basic.livedata.EqualtableLiveData
import com.mmg.phonect.common.basic.models.Location
import com.mmg.phonect.common.basic.models.Phone
import com.mmg.phonect.main.utils.StatementManager
import com.mmg.phonect.settings.SettingsManager
import javax.inject.Inject


@HiltViewModel
class MainActivityViewModel @Inject constructor(
    application: Application,
    private val savedStateHandle: SavedStateHandle,
    private val repository: MainActivityRepository,
    val statementManager: StatementManager,
) : GeoViewModel(application),
    MainActivityRepository.WeatherRequestCallback {

    // live data.

    val currentPhone = EqualtableLiveData<DayNightPhone>()
    val validLocationList = MutableLiveData<SelectableLocationList>()
    val totalLocationList = MutableLiveData<SelectableLocationList>()

    val loading = EqualtableLiveData<Boolean>()
    val indicator = EqualtableLiveData<Indicator>()

    val permissionsRequest = MutableLiveData<PermissionsRequest?>()
    val mainMessage = BusLiveData<MainMessage?>(Handler(Looper.getMainLooper()))

    // inner data.

    private var initCompleted = false
    private var updating = false

    companion object {
        private const val KEY_FORMATTED_ID = "formatted_id"
    }

    // life cycle.

    override fun onCleared() {
        super.onCleared()
        repository.destroy()
    }

    @JvmOverloads
    fun init() {
        onCleared()


        // 初始化实时数据。
        val current = repository.initPhone(
            context = getApplication()
        )

//        val validList = Location.excludeInvalidResidentLocation(getApplication(), totalList)
//
//        id = formattedId ?: validList[0].formattedId
//        val current = validList.first { item -> item.formattedId == id }

        initCompleted = false

        currentPhone.setValue(DayNightPhone(phone = current))
//        validLocationList.value = SelectableLocationList(locationList = validList, selectedId = id)
//        totalLocationList.value = SelectableLocationList(locationList = totalList, selectedId = id)

        loading.setValue(false)
//        indicator.setValue(
//            Indicator(
//                total = validList.size,
//                index = validList.indexOfFirst { it.formattedId == id }
//            )
//        )

        permissionsRequest.value = null
        mainMessage.setValue(null)

        // read weather caches.
        repository.getWeatherCacheForLocations(
            context = getApplication(),
            phone = current,
        ) { newList, _ ->
            initCompleted = true
            newList?.let { updateInnerData(it) }
        }
    }

    // 更新内部数据。

    private fun updateInnerData(phone: Phone) {



//        indicator.setValue(Indicator(total = valid.size, index = index))

        // 更新当前手机  貌似没用  以后删除
        setCurrentLocation(phone)

        // check difference in valid locations.
//        val diffInValidLocations = validLocationList.value?.locationList != valid
//        if (
//            diffInValidLocations
//            || validLocationList.value?.selectedId != valid[index].formattedId
//        ) {
//            validLocationList.value = SelectableLocationList(
//                locationList = valid,
//                selectedId = valid[index].formattedId,
//            )
//        }

        // update total locations.
//        totalLocationList.value = SelectableLocationList(
//            locationList = total,
//            selectedId = valid[index].formattedId,
//        )
    }

    private fun setCurrentLocation(phone: Phone) {
        currentPhone.setValue(DayNightPhone(phone = phone))

        checkToUpdateCurrentLocation()
    }

    private fun onUpdateResult(
        phone: Phone,
        locationResult: Boolean,
        weatherUpdateResult: Boolean,
    ) {
        if (!weatherUpdateResult) {
            mainMessage.setValue(MainMessage.WEATHER_REQ_FAILED)
        } else if (!locationResult) {
            mainMessage.setValue(MainMessage.LOCATION_FAILED)
        }

        updateInnerData(phone)

        loading.setValue(false)
        updating = false
    }

    private fun checkToUpdateCurrentLocation() {
        // is not loading
        if (!updating) {


            // if is not valid, we need:
            // update if init completed.
            // otherwise, mark a loading state and wait the init progress complete.
            if (initCompleted) {
                updateWithUpdatingChecking(
                    triggeredByUser = false,
                    checkPermissions = true,
                )
            } else {
                loading.setValue(true)
                updating = false
            }
            return
        }

        // is loading, do nothing.
    }


    // update.

    fun updateWithUpdatingChecking(
        triggeredByUser: Boolean,
        checkPermissions: Boolean,
    ) {
        if (updating) {
            Log.d("mmg", "updateWithUpdatingChecking: 啊啊啊啊啊啊啊啊")
            return
        }
        Log.d("mmg", "updateWithUpdatingChecking: 2啊啊啊啊啊啊啊啊")
        loading.setValue(true)

        // don't need to request any permission -> request data directly.
        // 直接从数据库取出地址
        if (
            Build.VERSION.SDK_INT < Build.VERSION_CODES.M
            || !checkPermissions
        ) {
            updating = true
            repository.getWeather(
                getApplication(),
                currentPhone.value!!.phone,
                true,
                this
            )
            return
        }

        // check permissions. 没有地址需要 获取地址权限
        val permissionList = getDeniedPermissionList()
        if (permissionList.isEmpty()) {
            // already got all permissions -> request data directly.
            updating = true
            repository.getWeather(
                getApplication(),
                currentPhone.value!!.phone,
                true,
                this
            )
            return
        }

        // request permissions.
        updating = false
        permissionsRequest.value = PermissionsRequest(
            permissionList,
            currentPhone.value!!.phone,
            triggeredByUser
        )
    }

    private fun getDeniedPermissionList(): List<String> {
        val permissionList = repository
            .getLocatePermissionList(getApplication())
            .toMutableList()

        for (i in permissionList.indices.reversed()) {
            Log.d("mmg", "getDeniedPermissionList: "+permissionList[i])
            if (
                ActivityCompat.checkSelfPermission(
                    getApplication(),
                    permissionList[i]
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                permissionList.removeAt(i)
            }
        }

        return permissionList
    }

    fun cancelRequest() {
        updating = false
        loading.setValue(false)
        repository.cancelWeatherRequest()
    }

    fun checkToUpdate() {
        checkToUpdateCurrentLocation()
    }

    fun updateLocationFromBackground(phone: Phone) {
        if (!initCompleted) {
            return
        }
//
//        if (currentPhone.value?.phone?.formattedId == location.formattedId) {
//            cancelRequest()
//        }
        updateInnerData(phone)
    }

    // set location.

//    fun setLocation(index: Int) {
//        validLocationList.value?.locationList?.let {
//            setLocation(it[index].formattedId)
//        }
//    }

//    fun setLocation(formattedId: String) {
//        cancelRequest()
//
//        validLocationList.value?.locationList?.let {
//            for (i in it.indices) {
//                if (it[i].formattedId != formattedId) {
//                    continue
//                }
//
//                setCurrentLocation(it[i])
//
//                indicator.setValue(Indicator(total = it.size, index = i))
//
//                totalLocationList.value = SelectableLocationList(
//                    locationList = totalLocationList.value?.locationList ?: emptyList(),
//                    selectedId = formattedId,
//                )
//                validLocationList.value = SelectableLocationList(
//                    locationList = validLocationList.value?.locationList ?: emptyList(),
//                    selectedId = formattedId,
//                )
//                break
//            }
//        }
//    }

    // return true if current location changed.
//    fun offsetLocation(offset: Int): Boolean {
//        cancelRequest()
//
//        val oldFormattedId = currentPhone.value?.phone?.formattedId ?: ""
//
//        // ensure current index.
//        var index = 0
//        validLocationList.value?.locationList?.let {
//            for (i in it.indices) {
//                if (it[i].formattedId == currentPhone.value?.phone?.formattedId) {
//                    index = i
//                    break
//                }
//            }
//        }
//
//        // update index.
//        index = (
//                index + offset + (validLocationList.value?.locationList?.size ?: 0)
//        ) % (
//                validLocationList.value?.locationList?.size ?: 1
//        )
//
//        // update location.
//        setCurrentLocation(validLocationList.value!!.locationList[index])
//
//        indicator.setValue(
//            Indicator(total = validLocationList.value!!.locationList.size, index = index)
//        )
//
//        totalLocationList.value = SelectableLocationList(
//            locationList = totalLocationList.value?.locationList ?: emptyList(),
//            selectedId = currentPhone.value?.phone?.formattedId ?: "",
//        )
//        validLocationList.value = SelectableLocationList(
//            locationList = validLocationList.value?.locationList ?: emptyList(),
//            selectedId = currentPhone.value?.phone?.formattedId ?: "",
//        )
//
//        return currentPhone.value?.phone?.formattedId != oldFormattedId
//    }

    // list.

    // return false if failed.
//    fun addLocation(
//        location: Location,
//        index: Int? = null,
//    ): Boolean {
//        // do not add an existed location.
//        if (totalLocationList.value!!.locationList.firstOrNull {
//                it.formattedId == location.formattedId
//        } != null) {
//            return false
//        }
//
//        val total = ArrayList(totalLocationList.value?.locationList ?: emptyList())
//        total.add(index ?: total.size, location)
//
//        updateInnerData(total)
//        repository.writeLocationList(context = getApplication(), locationList = total)
//
//        return true
//    }

//    fun moveLocation(from: Int, to: Int) {
//        if (from == to) {
//            return
//        }
//
//        val total = ArrayList(totalLocationList.value?.locationList ?: emptyList())
//        total.add(to, total.removeAt(from))
//
//        updateInnerData(total)
//
//        repository.writeLocationList(
//            context = getApplication(),
//            locationList = totalLocationList.value?.locationList ?: emptyList()
//        )
//    }

//    fun updateLocation(phone: Phone) {
//        updateInnerData(phone)
//        repository.writeLocationList(
//            context = getApplication(),
//            locationList = totalLocationList.value?.locationList ?: emptyList(),
//        )
//    }

//    fun deleteLocation(position: Int): Phone {
//        val total = ArrayList(totalLocationList.value?.locationList ?: emptyList())
//        val phone = total.removeAt(position)
//
//        updateInnerData(total)
//        repository.deleteLocation(context = getApplication(), phone = currentPhone.value!!.phone)
//
//        return phone
//    }

    // MARK: - getter.

    fun getValidLocation(): Phone {
        // ensure current index.


        return currentPhone.value!!.phone
    }

    // impl.

    override fun onCompleted(
        phone: Phone,
        locationFailed: Boolean?,
        weatherRequestFailed: Boolean
    ) {
        onUpdateResult(
            phone = phone,
            locationResult = locationFailed != true,
            weatherUpdateResult = !weatherRequestFailed
        )
    }
}