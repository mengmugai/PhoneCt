package com.mmg.phonect.common.basic.models.options

import android.content.Context
import com.mmg.phonect.R
import com.mmg.phonect.common.basic.models.options._basic.BaseEnum
import com.mmg.phonect.common.basic.models.options._basic.Utils

enum class DarkMode(
    override val id: String
): BaseEnum {

    AUTO("auto"),
    SYSTEM("system"),
    LIGHT("light"),
    DARK("dark");

    companion object {

        @JvmStatic
        fun getInstance(
            value: String
        ) = when (value) {
            "system" -> SYSTEM
            "light" -> LIGHT
            "dark" -> DARK
            else -> AUTO
        }
    }

    override val valueArrayId = R.array.dark_mode_values
    override val nameArrayId = R.array.dark_modes

    override fun getName(context: Context) = Utils.getName(context, this)
}