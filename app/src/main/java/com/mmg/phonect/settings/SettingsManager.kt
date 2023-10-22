package com.mmg.phonect.settings

import android.content.Context
import com.mmg.phonect.BuildConfig
import com.mmg.phonect.PhoneCt
import com.mmg.phonect.common.basic.models.options.DarkMode
import com.mmg.phonect.common.basic.models.options.NotificationStyle
import com.mmg.phonect.common.basic.models.options.UpdateInterval
import com.mmg.phonect.common.basic.models.options.WidgetWeekIconMode
import com.mmg.phonect.common.basic.models.options.appearance.*
import com.mmg.phonect.common.basic.models.options.provider.LocationProvider
import com.mmg.phonect.common.basic.models.options.provider.WeatherSource
import com.mmg.phonect.common.basic.models.options.unit.*
import com.mmg.phonect.common.bus.EventBus

class SettingsChangedMessage

class SettingsManager private constructor(context: Context) {

    companion object {

        @Volatile
        private var instance: SettingsManager? = null

        @JvmStatic
        fun getInstance(context: Context): SettingsManager {
            if (instance == null) {
                synchronized(SettingsManager::class) {
                    if (instance == null) {
                        instance = SettingsManager(context)
                    }
                }
            }
            return instance!!
        }

        private const val DEFAULT_CARD_DISPLAY = ("abnormal_info"
                + "&device_info"
                + "&app_list")
        private const val DEFAULT_DAILY_TREND_DISPLAY = ("temperature"
                + "&air_quality"
                + "&wind"
                + "&uv_index"
                + "&precipitation")
        private const val DEFAULT_HOURLY_TREND_DISPLAY = ("temperature"
                + "&wind"
                + "&uv_index"
                + "&precipitation")

        private const val DEFAULT_APP_LIST_TREND_DISPLAY = ("xposedmodule"
                + "&hook_framework")

        const val DEFAULT_TODAY_FORECAST_TIME = "07:00"
        const val DEFAULT_TOMORROW_FORECAST_TIME = "21:00"
    }

    private val config = ConfigStore.getInstance(context)

    // basic.

    var isBackgroundFree: Boolean
        set(value) {
            config.edit().putBoolean("background_free_3", value).apply()
            notifySettingsChanged()
        }
        get() = config.getBoolean("background_free_3", true)

    var isAlertPushEnabled: Boolean
        set(value) {
            config.edit().putBoolean("alert_notification_switch", value).apply()
            notifySettingsChanged()
        }
        get() = config.getBoolean("alert_notification_switch", true)

    var isPrecipitationPushEnabled: Boolean
        set(value) {
            config.edit().putBoolean("precipitation_notification_switch", value).apply()
            notifySettingsChanged()
        }
        get() = config.getBoolean("precipitation_notification_switch", false)

    var updateInterval: UpdateInterval
        set(value) {
            config.edit().putString("refresh_rate", value.id).apply()
            notifySettingsChanged()
        }
        get() = UpdateInterval.getInstance(
            config.getString("refresh_rate", "1:30") ?: ""
        )

    var darkMode: DarkMode
        set(value) {
            config.edit().putString("dark_mode", value.id).apply()
            notifySettingsChanged()
        }
        get() = DarkMode.getInstance(
            config.getString("dark_mode", "auto") ?: ""
        )

    // service providers.

    var weatherSource: WeatherSource
        set(value) {
            config.edit().putString("weather_source", value.id).apply()
            notifySettingsChanged()
        }
        get() = WeatherSource.getInstance(
            config.getString("weather_source", "accu") ?: ""
        )

    var locationProvider: LocationProvider
        set(value) {
            config.edit().putString("location_service", value.id).apply()
            notifySettingsChanged()
        }
        get() = LocationProvider.getInstance(
            config.getString("location_service", "native") ?: ""
        )

    // unit.

    var temperatureUnit: TemperatureUnit
        set(value) {
            config.edit().putString("temperature_unit", value.id).apply()
            notifySettingsChanged()
        }
        get() = TemperatureUnit.getInstance(
            config.getString("temperature_unit", "c") ?: ""
        )

    var distanceUnit: DistanceUnit
        set(value) {
            config.edit().putString("distance_unit", value.id).apply()
            notifySettingsChanged()
        }
        get() = DistanceUnit.getInstance(
            config.getString("distance_unit", "km") ?: ""
        )

    var precipitationUnit: PrecipitationUnit
        set(value) {
            config.edit().putString("precipitation_unit", value.id).apply()
            notifySettingsChanged()
        }
        get() = PrecipitationUnit.getInstance(
            config.getString("precipitation_unit", "mm") ?: ""
        )

    var pressureUnit: PressureUnit
        set(value) {
            config.edit().putString("pressure_unit", value.id).apply()
            notifySettingsChanged()
        }
        get() = PressureUnit.getInstance(
            config.getString("pressure_unit", "mb") ?: ""
        )

    var speedUnit: SpeedUnit
        set(value) {
            config.edit().putString("speed_unit", value.id).apply()
            notifySettingsChanged()
        }
        get() = SpeedUnit.getInstance(
            config.getString("speed_unit", "mps") ?: ""
        )

    // appearance.

    var iconProvider: String
        set(value) {
            config
                .edit()
                .putString("iconProvider", value)
                .apply()
            notifySettingsChanged()
        }
        get() = config.getString("iconProvider", PhoneCt.instance.packageName) ?: ""

    var cardDisplayList: List<CardDisplay>
        set(value) {
            config
                .edit()
                .putString("card_display_2", CardDisplay.toValue(value))
                .apply()
            notifySettingsChanged()
        }
        get() = CardDisplay
            .toCardDisplayList(
                config.getString("card_display_2", DEFAULT_CARD_DISPLAY) ?: ""
            )
            .toList()

    var dailyTrendDisplayList: List<DailyTrendDisplay>
        set(value) {
            config
                .edit()
                .putString("daily_trend_display", DailyTrendDisplay.toValue(value))
                .apply()
            notifySettingsChanged()
        }
        get() = DailyTrendDisplay
            .toDailyTrendDisplayList(
                config.getString("daily_trend_display", DEFAULT_DAILY_TREND_DISPLAY) ?: ""
            )
            .toList()

    var hourlyTrendDisplayList: List<HourlyTrendDisplay>
        set(value) {
            config
                .edit()
                .putString("hourly_trend_display", HourlyTrendDisplay.toValue(value))
                .apply()
            notifySettingsChanged()
        }
        get() = HourlyTrendDisplay
            .toHourlyTrendDisplayList(
                config.getString("hourly_trend_display", DEFAULT_HOURLY_TREND_DISPLAY) ?: ""
            )
            .toList()

    var appListTrendDisplay: List<AppListTrendDisplay>
        set(value) {
            config
                .edit()
                .putString("applist_trend_display", AppListTrendDisplay.toValue(value))
                .apply()
            notifySettingsChanged()
        }
        get() = AppListTrendDisplay
            .toAppListTrendDisplayList(
                config.getString("applist_trend_display", DEFAULT_APP_LIST_TREND_DISPLAY) ?: ""
            )
            .toList()

    var isTrendHorizontalLinesEnabled: Boolean
        set(value) {
            config.edit().putBoolean("trend_horizontal_line_switch", value).apply()
            notifySettingsChanged()
        }
        get() = config.getBoolean("trend_horizontal_line_switch", true)

    var isExchangeDayNightTempEnabled: Boolean
        set(value) {
            config.edit().putBoolean("exchange_day_night_temp_switch", value).apply()
            notifySettingsChanged()
        }
        get() = config.getBoolean("exchange_day_night_temp_switch", false)

    var isGravitySensorEnabled: Boolean
        set(value) {
            config.edit().putBoolean("gravity_sensor_switch", value).apply()
            notifySettingsChanged()
        }
        get() = config.getBoolean("gravity_sensor_switch", true)

    var isListAnimationEnabled: Boolean
        set(value) {
            config.edit().putBoolean("list_animation_switch", value).apply()
            notifySettingsChanged()
        }
        get() = config.getBoolean("list_animation_switch", true)

    var isItemAnimationEnabled: Boolean
        set(value) {
            config.edit().putBoolean("item_animation_switch", value).apply()
            notifySettingsChanged()
        }
        get() = config.getBoolean("item_animation_switch", true)

    var language: Language
        set(value) {
            config.edit().putString("language", value.id).apply()
            notifySettingsChanged()
        }
        get() = Language.getInstance(
            config.getString("language", "follow_system") ?: ""
        )

    // forecast.

    var isTodayForecastEnabled: Boolean
        set(value) {
            config.edit().putBoolean("timing_forecast_switch_today", value).apply()
            notifySettingsChanged()
        }
        get() = config.getBoolean("timing_forecast_switch_today", false)

    var todayForecastTime: String
        set(value) {
            config.edit().putString("forecast_time_today", value).apply()
            notifySettingsChanged()
        }
        get() = config
            .getString("forecast_time_today", DEFAULT_TODAY_FORECAST_TIME)
            ?: DEFAULT_TODAY_FORECAST_TIME

    var isTomorrowForecastEnabled: Boolean
        set(value) {
            config.edit().putBoolean("timing_forecast_switch_tomorrow", value).apply()
            notifySettingsChanged()
        }
        get() = config.getBoolean("timing_forecast_switch_tomorrow", false)

    var tomorrowForecastTime: String
        set(value) {
            config.edit().putString("forecast_time_tomorrow", value).apply()
            notifySettingsChanged()
        }
        get() = config
            .getString("forecast_time_tomorrow", DEFAULT_TOMORROW_FORECAST_TIME)
            ?: DEFAULT_TOMORROW_FORECAST_TIME

    // widget.

    var widgetWeekIconMode: WidgetWeekIconMode
        set(value) {
            config.edit().putString("week_icon_mode", value.id).apply()
            notifySettingsChanged()
        }
        get() = WidgetWeekIconMode.getInstance(
            config.getString("week_icon_mode", "auto") ?: ""
        )

    var isWidgetMinimalIconEnabled: Boolean
        set(value) {
            config.edit().putBoolean("widget_minimal_icon", value).apply()
            notifySettingsChanged()
        }
        get() = config.getBoolean("widget_minimal_icon", false)

    // notification.

    var isNotificationFeelsLike: Boolean
        set(value) {
            config.edit().putBoolean("notification_feelslike", value).apply()
            notifySettingsChanged()
        }
        get() = config.getBoolean("notification_feelslike", false)

    var isNotificationEnabled: Boolean
        set(value) {
            config.edit().putBoolean("notification_switch", value).apply()
            notifySettingsChanged()
        }
        get() = config.getBoolean("notification_switch", false)

    var notificationStyle: NotificationStyle
        set(value) {
            config.edit().putString("notification_style", value.id).apply()
            notifySettingsChanged()
        }
        get() = NotificationStyle.getInstance(
            config.getString("notification_style", "daily") ?: ""
        )

    var isNotificationTemperatureIconEnabled: Boolean
        set(value) {
            config.edit().putBoolean("notification_temp_icon_switch", value).apply()
            notifySettingsChanged()
        }
        get() = config.getBoolean("notification_temp_icon_switch", false)

    var isNotificationCanBeClearedEnabled: Boolean
        set(value) {
            config.edit().putBoolean("notification_can_clear_switch", value).apply()
            notifySettingsChanged()
        }
        get() = config.getBoolean("notification_can_clear_switch", false)

    // service provider advanced.

    var customAccuWeatherKey: String
        set(value) {
            config.edit().putString("provider_accu_weather_key", value).apply()
            notifySettingsChanged()
        }
        get() = config.getString("provider_accu_weather_key", "") ?: ""

    var customAccuCurrentKey: String
        set(value) {
            config.edit().putString("provider_accu_current_key", value).apply()
            notifySettingsChanged()
        }
        get() = config.getString("provider_accu_current_key", "") ?: ""

    var customAccuAqiKey: String
        set(value) {
            config.edit().putString("provider_accu_aqi_key", value).apply()
            notifySettingsChanged()
        }
        get() = config.getString("provider_accu_aqi_key", "") ?: ""

    var customOwmKey: String
        set(value) {
            config.edit().putString("provider_owm_key", value).apply()
            notifySettingsChanged()
        }
        get() = config.getString("provider_owm_key", "") ?: ""

    var customBaiduIpLocationAk: String
        set(value) {
            config.edit().putString("provider_baidu_ip_location_ak", value).apply()
            notifySettingsChanged()
        }
        get() = config.getString("provider_baidu_ip_location_ak", "") ?: ""

    var customMfWsftKey: String
        set(value) {
            config.edit().putString("provider_mf_wsft_key", value).apply()
            notifySettingsChanged()
        }
        get() = config.getString("provider_mf_wsft_key", "") ?: ""

    var customIqaAirParifKey: String
        set(value) {
            config.edit().putString("provider_iqa_air_parif_key", value).apply()
            notifySettingsChanged()
        }
        get() = config.getString("provider_iqa_air_parif_key", "") ?: ""

    var customIqaAtmoAuraKey: String
        set(value) {
            config.edit().putString("provider_iqa_atmo_aura_key", value).apply()
            notifySettingsChanged()
        }
        get() = config.getString("provider_iqa_atmo_aura_key", "") ?: ""

//    val providerAccuWeatherKey: String
//        get() = getProviderSettingValue(
//            customValue = customAccuWeatherKey,
//            defaultValue = BuildConfig.ACCU_WEATHER_KEY,
//        )
//
//    val providerAccuCurrentKey: String
//        get() = getProviderSettingValue(
//            customValue = customAccuCurrentKey,
//            defaultValue = BuildConfig.ACCU_CURRENT_KEY,
//        )




    val providerBaiduIpLocationAk: String
        get() = getProviderSettingValue(
            customValue = customBaiduIpLocationAk,
            defaultValue = "GM1evulovGN5E41p6NC72LW3ql5d0nNG",
        )



    private fun getProviderSettingValue(
        customValue: String,
        defaultValue: String,
    ) = customValue.ifEmpty { defaultValue }

    private fun notifySettingsChanged() {
        EventBus
            .instance
            .with(SettingsChangedMessage::class.java)
            .postValue(SettingsChangedMessage())
    }
}
