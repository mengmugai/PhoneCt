package com.mmg.phonect.common.basic.models.options.appearance

import android.content.Context
import android.text.TextUtils
import androidx.annotation.StringRes
import com.mmg.phonect.R
import com.mmg.phonect.common.basic.models.options._basic.BaseEnum

enum class CardDisplay(
    override val id: String,
    @StringRes private val nameId: Int
): BaseEnum {

//    CARD_DAILY_OVERVIEW("daily_overview", R.string.daily_overview),
//    CARD_HOURLY_OVERVIEW("hourly_overview", R.string.hourly_overview),
//    CARD_AIR_QUALITY("air_quality", R.string.air_quality),
//    CARD_ALLERGEN("allergen", R.string.allergen),
//    CARD_SUNRISE_SUNSET("sunrise_sunset", R.string.sunrise_sunset),
//    CARD_LIFE_DETAILS("life_details", R.string.life_details),
    CARD_DEVICE_INFO("device_info", R.string.device_info),
    CARD_APP_LIST("app_list", R.string.app_list);

    companion object {

        @JvmStatic
        fun toCardDisplayList(
            value: String
        ) = if (TextUtils.isEmpty(value)) {
            ArrayList()
        } else try {
            val cards = value.split("&").toTypedArray()
            val list = ArrayList<CardDisplay>()
            for (card in cards) {
                when (card) {
//                    "daily_overview" -> list.add(CARD_DAILY_OVERVIEW)
//                    "hourly_overview" -> list.add(CARD_HOURLY_OVERVIEW)
//                    "air_quality" -> list.add(CARD_AIR_QUALITY)
//                    "allergen" -> list.add(CARD_ALLERGEN)
//                    "life_details" -> list.add(CARD_LIFE_DETAILS)
                    "device_info" -> list.add(CARD_DEVICE_INFO)
                    "app_list" -> list.add(CARD_APP_LIST)
//                    "sunrise_sunset" -> list.add(CARD_SUNRISE_SUNSET)
                }
            }

            list
        } catch (e: Exception) {
            emptyList()
        }

        @JvmStatic
        fun toValue(list: List<CardDisplay>): String {
            val builder = StringBuilder()
            for (v in list) {
                builder.append("&").append(v.id)
            }
            if (builder.isNotEmpty() && builder[0] == '&') {
                builder.deleteCharAt(0)
            }
            return builder.toString()
        }

        @JvmStatic
        fun getSummary(context: Context, list: List<CardDisplay>): String {
            val builder = StringBuilder()
            for (item in list) {
                builder.append(",").append(item.getName(context))
            }
            if (builder.isNotEmpty() && builder[0] == ',') {
                builder.deleteCharAt(0)
            }
            return builder.toString().replace(",", ", ")
        }
    }

    override val valueArrayId = 0
    override val nameArrayId = 0

    override fun getName(context: Context) = context.getString(nameId)
}