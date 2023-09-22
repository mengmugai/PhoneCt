package com.mmg.phonect.common.basic.models

import android.content.Context
import android.os.Parcel
import android.os.Parcelable
import android.text.TextUtils
import com.mmg.phonect.common.basic.models.options.provider.WeatherSource
import com.mmg.phonect.common.basic.models.weather.Device
import com.mmg.phonect.common.basic.models.weather.Weather
import com.mmg.phonect.common.utils.DisplayUtils
import java.util.*

class Phone(
//    val cityId: String,

//    val latitude: Float,
//    val longitude: Float,

    val timeZone: TimeZone,
    //品牌
    val brand: String,
    val model: String,
    val country: String,
    val province: String,
    val city: String,
    val district: String,

    val device: Device? = null,
    val weatherSource: WeatherSource,

//    val isCurrentPosition: Boolean,
//    val isResidentPosition: Boolean,
//    val isChina: Boolean,
) : Parcelable {

    val formattedId: String
        get() = brand + "-" + model


    val isDaylight: Boolean
        get() = device?.isDaylight(timeZone) ?: DisplayUtils.isDaylight(timeZone)

//    val isUsable: Boolean
//        get() = cityId != NULL_ID

    companion object {
        private const val NULL_ID = "NULL_ID"

        const val CURRENT_POSITION_ID = "CURRENT_POSITION"

        @JvmStatic
        fun buildPhone(): Phone {
            return Phone(
                timeZone = TimeZone.getDefault(),
                brand = "菠萝",
                model = "300s",
                country = "",
                province = "",
                city = "",
                district = "",
                weatherSource = WeatherSource.OWM,
            )
        }

        @JvmStatic
        fun buildDefaultPhone(weatherSource: WeatherSource): Phone {
            return Phone(

                timeZone = TimeZone.getTimeZone("Asia/Shanghai"),
                brand = "菠萝",
                model = "300s",
                country = "中国",
                province = "直辖市",
                city = "北京",
                district = "",
                weatherSource = weatherSource,
            )
        }

        private fun isEquals(a: String?, b: String?): Boolean {
            return if (TextUtils.isEmpty(a) && TextUtils.isEmpty(b)) {
                true
            } else if (!TextUtils.isEmpty(a) && !TextUtils.isEmpty(b)) {
                a == b
            } else {
                false
            }
        }



        @JvmStatic
        fun copy(
            src: Phone,
            device: Device?,
        ) = src.copy(
            device = device
        )

        @JvmStatic
        fun copy(
            src: Phone,
            weatherSource: WeatherSource,
        ) = src.copy(
            weatherSource = weatherSource
        )
        
        

        @JvmStatic
        fun copy(
            src: Phone,
        ) = src.copy(
        )

        @JvmStatic
        fun copy(
            src: Phone,
            timeZone: TimeZone,
        ) = src.copy(
            timeZone = timeZone,
        )

        @JvmField
        val CREATOR = object: Parcelable.Creator<Phone> {

            override fun createFromParcel(parcel: Parcel): Phone {
                return Phone(parcel)
            }

            override fun newArray(size: Int): Array<Phone?> {
                return arrayOfNulls(size)
            }
        }
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
//        parcel.writeString(cityId)
//        parcel.writeFloat(latitude)
//        parcel.writeFloat(longitude)
        parcel.writeSerializable(timeZone)
        parcel.writeString(brand)
        parcel.writeString(model)
        parcel.writeString(country)
        parcel.writeString(province)
        parcel.writeString(city)
        parcel.writeString(district)
        parcel.writeInt(weatherSource.ordinal)
//        parcel.writeByte(if (isCurrentPosition) 1 else 0)
//        parcel.writeByte(if (isResidentPosition) 1 else 0)
//        parcel.writeByte(if (isChina) 1 else 0)
    }

    override fun describeContents(): Int {
        return 0
    }

    constructor(parcel: Parcel) : this(
        timeZone = parcel.readSerializable()!! as TimeZone,
        brand = parcel.readString()!!,
        model = parcel.readString()!!,
        country = parcel.readString()!!,
        province = parcel.readString()!!,
        city = parcel.readString()!!,
        district = parcel.readString()!!,
        weatherSource = WeatherSource.values()[parcel.readInt()],
    )

    @JvmOverloads
    fun copy(
        timeZone: TimeZone? = null,
        brand: String? = null,
        model: String? = null,
        country: String? = null,
        province: String? = null,
        city: String? = null,
        district: String? = null,
        device: Device? = null,
        weatherSource: WeatherSource? = null,
    ) = Phone(
        timeZone = timeZone ?: this.timeZone,
        brand = brand ?: this.brand,
        model = model ?: this.model,
        country = country ?: this.country,
        province = province ?: this.province,
        city = city ?: this.city,
        district = district ?: this.district,
        device = device ?: this.device,
        weatherSource = weatherSource ?: this.weatherSource,
    )

    override fun equals(other: Any?): Boolean {
        return false
//        if (this === other) {
//            return true
//        }
//        if (other == null || other !is Phone) {
//            return false
//        }
//
//
//
//
//        if (weatherSource != other.weatherSource) {
//            return false
//        }
//
//        val thisDevice = device
//        val otherDevice = other.device
//        if (thisDevice == null && otherDevice == null) {
//            return true
//        }
//        return if (thisDevice != null && otherDevice != null) {
//            thisDevice.androidid == otherDevice.androidid
//        } else {
//            false
//        }
    }



    fun getPhoneName(context: Context): String {

        return this.brand + "-" + this.model
    }

    override fun toString(): String {
        val builder = StringBuilder("$country $province")
        if (province != city
            && !TextUtils.isEmpty(city)
        ) {
            builder.append(" ").append(city)
        }
        if (city != district
            && !TextUtils.isEmpty(district)
        ) {
            builder.append(" ").append(district)
        }
        return builder.toString()
    }

    fun hasGeocodeInformation(): Boolean {
        return (!TextUtils.isEmpty(country)
                || !TextUtils.isEmpty(province)
                || !TextUtils.isEmpty(city)
                || !TextUtils.isEmpty(district))
    }


}