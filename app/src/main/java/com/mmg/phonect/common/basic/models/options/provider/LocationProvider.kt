package com.mmg.phonect.common.basic.models.options.provider

import android.content.Context
import com.mmg.phonect.R
import com.mmg.phonect.common.basic.models.options._basic.BaseEnum
import com.mmg.phonect.common.basic.models.options._basic.Utils

enum class LocationProvider(
    override val id: String
): BaseEnum {

    BAIDU("baidu"),
    BAIDU_IP("baidu_ip"),
    AMAP("amap"),
    NATIVE("native");

    companion object {

        @JvmStatic
        fun getInstance(
            value: String
        ) = when (value) {
            "baidu_ip" -> BAIDU_IP
            "baidu" -> BAIDU
            "amap" -> AMAP
            else -> NATIVE
        }
    }

    override val valueArrayId = R.array.location_service_values
    override val nameArrayId = R.array.location_services

    override fun getName(context: Context) = Utils.getName(context, this)
}