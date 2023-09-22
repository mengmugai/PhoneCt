package com.mmg.phonect.main

import com.mmg.phonect.common.basic.models.Location
import com.mmg.phonect.common.basic.models.Phone

class Indicator(val total: Int, val index: Int) {

    override fun equals(other: Any?): Boolean {
        return if (other is Indicator) {
            other.index == index && other.total == total
        } else {
            false
        }
    }

    override fun hashCode(): Int {
        var result = total
        result = 31 * result + index
        return result
    }
}

class PermissionsRequest(
    val permissionList: List<String>,
    val target: Phone?,
    val triggeredByUser: Boolean
) {

    private var consumed = false

    fun consume(): Boolean {
        if (consumed) {
            return false
        }

        consumed = true
        return true
    }
}

class SelectableLocationList(
    val locationList: List<Phone>,
    val selectedId: String,
) {

    override fun equals(other: Any?): Boolean {
        if (other is SelectableLocationList) {
            return locationList == other.locationList
                    && selectedId == other.selectedId
        }
        return false
    }

    override fun hashCode(): Int {
        var result = locationList.hashCode()
        result = 31 * result + selectedId.hashCode()
        return result
    }
}

enum class MainMessage {
    LOCATION_FAILED,
    WEATHER_REQ_FAILED,
}

class DayNightPhone(
    val phone: Phone,
    val daylight: Boolean = phone.isDaylight
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) {
            return true
        }

        if (other is DayNightPhone) {
            return phone == other.phone
                    && daylight == other.daylight
        }

        return false
    }

    override fun hashCode(): Int {
        var result = phone.hashCode()
        result = 31 * result + daylight.hashCode()
        return result
    }
}