package com.mmg.phonect.settings.compose

import android.content.Context
import androidx.compose.runtime.Composable
import com.mmg.phonect.R
import com.mmg.phonect.settings.SettingsManager
import com.mmg.phonect.settings.preference.bottomInsetItem
import com.mmg.phonect.settings.preference.composables.EditTextPreferenceView
import com.mmg.phonect.settings.preference.composables.PreferenceScreen
import com.mmg.phonect.settings.preference.editTextPreferenceItem
import com.mmg.phonect.settings.preference.sectionFooterItem
import com.mmg.phonect.settings.preference.sectionHeaderItem

@Composable
fun SettingsProviderAdvancedSettingsScreen(
    context: Context
) = PreferenceScreen {
    sectionHeaderItem(R.string.settings_provider_accu_weather)
    editTextPreferenceItem(R.string.settings_provider_accu_weather_key) { id ->
        EditTextPreferenceView(
            titleId = id,
            summary = { context, content ->
                content.ifEmpty {
                    context.getString(R.string.settings_provider_default_value)
                }
            },
            content = SettingsManager.getInstance(context).customAccuWeatherKey,
            onValueChanged = {
                SettingsManager.getInstance(context).customAccuWeatherKey = it
            }
        )
    }
    editTextPreferenceItem(R.string.settings_provider_accu_current_key) { id ->
        EditTextPreferenceView(
            titleId = id,
            summary = { context, content ->
                content.ifEmpty {
                    context.getString(R.string.settings_provider_default_value)
                }
            },
            content = SettingsManager.getInstance(context).customAccuCurrentKey,
            onValueChanged = {
                SettingsManager.getInstance(context).customAccuCurrentKey = it
            }
        )
    }
    editTextPreferenceItem(R.string.settings_provider_accu_aqi_key) { id ->
        EditTextPreferenceView(
            titleId = id,
            summary = { context, content ->
                content.ifEmpty {
                    context.getString(R.string.settings_provider_default_value)
                }
            },
            content = SettingsManager.getInstance(context).customAccuAqiKey,
            onValueChanged = {
                SettingsManager.getInstance(context).customAccuAqiKey = it
            }
        )
    }
    sectionFooterItem(R.string.settings_provider_accu_weather)

    sectionHeaderItem(R.string.settings_provider_owm)
    editTextPreferenceItem(R.string.settings_provider_owm_key) { id ->
        EditTextPreferenceView(
            titleId = id,
            summary = { context, content ->
                content.ifEmpty {
                    context.getString(R.string.settings_provider_default_value)
                }
            },
            content = SettingsManager.getInstance(context).customOwmKey,
            onValueChanged = {
                SettingsManager.getInstance(context).customOwmKey = it
            }
        )
    }
    sectionFooterItem(R.string.settings_provider_owm)

    sectionHeaderItem(R.string.settings_provider_baidu_ip_location)
    editTextPreferenceItem(R.string.settings_provider_baidu_ip_location) { id ->
        EditTextPreferenceView(
            titleId = id,
            summary = { context, content ->
                content.ifEmpty {
                    context.getString(R.string.settings_provider_default_value)
                }
            },
            content = SettingsManager.getInstance(context).customBaiduIpLocationAk,
            onValueChanged = {
                SettingsManager.getInstance(context).customBaiduIpLocationAk = it
            }
        )
    }
    sectionFooterItem(R.string.settings_provider_baidu_ip_location)

    sectionHeaderItem(R.string.settings_provider_mf)
    editTextPreferenceItem(R.string.settings_provider_mf_wsft_key) { id ->
        EditTextPreferenceView(
            titleId = id,
            summary = { context, content ->
                content.ifEmpty {
                    context.getString(R.string.settings_provider_default_value)
                }
            },
            content = SettingsManager.getInstance(context).customMfWsftKey,
            onValueChanged = {
                SettingsManager.getInstance(context).customMfWsftKey = it
            }
        )
    }
    editTextPreferenceItem(R.string.settings_provider_iqa_air_parif_key) { id ->
        EditTextPreferenceView(
            titleId = id,
            summary = { context, content ->
                content.ifEmpty {
                    context.getString(R.string.settings_provider_default_value)
                }
            },
            content = SettingsManager.getInstance(context).customIqaAirParifKey,
            onValueChanged = {
                SettingsManager.getInstance(context).customIqaAirParifKey = it
            }
        )
    }
    editTextPreferenceItem(R.string.settings_provider_iqa_atmo_aura_key) { id ->
        EditTextPreferenceView(
            titleId = id,
            summary = { context, content ->
                content.ifEmpty {
                    context.getString(R.string.settings_provider_default_value)
                }
            },
            content = SettingsManager.getInstance(context).customIqaAtmoAuraKey,
            onValueChanged = {
                SettingsManager.getInstance(context).customIqaAtmoAuraKey = it
            }
        )
    }
    sectionFooterItem(R.string.settings_provider_mf)

    bottomInsetItem()
}