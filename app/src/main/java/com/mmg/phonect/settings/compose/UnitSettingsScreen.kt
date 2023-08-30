package com.mmg.phonect.settings.compose

import android.content.Context
import androidx.compose.runtime.Composable
import com.mmg.phonect.R
import com.mmg.phonect.common.basic.models.options.unit.*
import com.mmg.phonect.settings.SettingsManager
import com.mmg.phonect.settings.preference.bottomInsetItem
import com.mmg.phonect.settings.preference.composables.ListPreferenceView
import com.mmg.phonect.settings.preference.composables.PreferenceScreen
import com.mmg.phonect.settings.preference.listPreferenceItem

@Composable
fun UnitSettingsScreen(
    context: Context
) = PreferenceScreen {
    listPreferenceItem(R.string.settings_title_temperature_unit) { id ->
        ListPreferenceView(
            titleId = id,
            selectedKey = SettingsManager.getInstance(context).temperatureUnit.id,
            valueArrayId = R.array.temperature_unit_values,
            nameArrayId = R.array.temperature_units,
            onValueChanged = {
                SettingsManager
                    .getInstance(context)
                    .temperatureUnit = TemperatureUnit.getInstance(it)
            },
        )
    }
    listPreferenceItem(R.string.settings_title_distance_unit) { id ->
        ListPreferenceView(
            titleId = id,
            selectedKey = SettingsManager.getInstance(context).distanceUnit.id,
            valueArrayId = R.array.distance_unit_values,
            nameArrayId = R.array.distance_units,
            onValueChanged = {
                SettingsManager
                    .getInstance(context)
                    .distanceUnit = DistanceUnit.getInstance(it)
            },
        )
    }
    listPreferenceItem(R.string.settings_title_precipitation_unit) { id ->
        ListPreferenceView(
            titleId = id,
            selectedKey = SettingsManager.getInstance(context).precipitationUnit.id,
            valueArrayId = R.array.precipitation_unit_values,
            nameArrayId = R.array.precipitation_units,
            onValueChanged = {
                SettingsManager
                    .getInstance(context)
                    .precipitationUnit = PrecipitationUnit.getInstance(it)
            },
        )
    }
    listPreferenceItem(R.string.settings_title_pressure_unit) { id ->
        ListPreferenceView(
            titleId = id,
            selectedKey = SettingsManager.getInstance(context).pressureUnit.id,
            valueArrayId = R.array.pressure_unit_values,
            nameArrayId = R.array.pressure_units,
            onValueChanged = {
                SettingsManager
                    .getInstance(context)
                    .pressureUnit = PressureUnit.getInstance(it)
            },
        )
    }
    listPreferenceItem(R.string.settings_title_speed_unit) { id ->
        ListPreferenceView(
            titleId = id,
            selectedKey = SettingsManager.getInstance(context).speedUnit.id,
            valueArrayId = R.array.speed_unit_values,
            nameArrayId = R.array.speed_units,
            onValueChanged = {
                SettingsManager
                    .getInstance(context)
                    .speedUnit = SpeedUnit.getInstance(it)
            },
        )
    }

    bottomInsetItem()
}