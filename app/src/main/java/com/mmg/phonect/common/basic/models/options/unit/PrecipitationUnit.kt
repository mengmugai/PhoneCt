package com.mmg.phonect.common.basic.models.options.unit

import android.content.Context
import com.mmg.phonect.R
import com.mmg.phonect.common.basic.models.options._basic.UnitEnum
import com.mmg.phonect.common.basic.models.options._basic.Utils
import com.mmg.phonect.common.utils.DisplayUtils

// actual precipitation = precipitation(mm) * factor.
enum class PrecipitationUnit(
    override val id: String,
    override val unitFactor: Float
): UnitEnum<Float> {

    MM("mm", 1f),
    CM("cm", 0.1f),
    IN("in", 0.0394f),
    LPSQM("lpsqm", 1f);

    companion object {

        @JvmStatic
        fun getInstance(
            value: String
        ) = when (value) {
            "cm" -> CM
            "in" -> IN
            "lpsqm" -> LPSQM
            else -> MM
        }
    }

    override val valueArrayId = R.array.precipitation_unit_values
    override val nameArrayId = R.array.precipitation_units
    override val voiceArrayId = R.array.precipitation_unit_voices

    override fun getName(context: Context) = Utils.getName(context, this)

    override fun getVoice(context: Context) = Utils.getVoice(context, this)

    override fun getValueWithoutUnit(valueInDefaultUnit: Float) = valueInDefaultUnit * unitFactor

    override fun getValueInDefaultUnit(valueInCurrentUnit: Float) = valueInCurrentUnit / unitFactor

    override fun getValueTextWithoutUnit(
        valueInDefaultUnit: Float
    ) = Utils.getValueTextWithoutUnit(this, valueInDefaultUnit, 1)!!

    override fun getValueText(
        context: Context,
        valueInDefaultUnit: Float
    ) = getValueText(context, valueInDefaultUnit, DisplayUtils.isRtl(context))

    override fun getValueText(
        context: Context,
        valueInDefaultUnit: Float,
        rtl: Boolean
    ) = Utils.getValueText(
        context = context,
        enum = this,
        valueInDefaultUnit = valueInDefaultUnit,
        decimalNumber = 1,
        rtl = rtl
    )

    override fun getValueVoice(
        context: Context,
        valueInDefaultUnit: Float
    ) = getValueVoice(context, valueInDefaultUnit, DisplayUtils.isRtl(context))

    override fun getValueVoice(
        context: Context,
        valueInDefaultUnit: Float,
        rtl: Boolean
    ) = Utils.getVoiceText(
        context = context,
        enum = this,
        valueInDefaultUnit = valueInDefaultUnit,
        decimalNumber = 1,
        rtl = rtl
    )
}