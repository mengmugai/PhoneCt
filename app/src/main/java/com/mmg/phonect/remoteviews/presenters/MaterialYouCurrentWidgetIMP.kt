package com.mmg.phonect.remoteviews.presenters

import android.appwidget.AppWidgetManager
import android.content.ComponentName
import android.content.Context
import android.widget.RemoteViews
import androidx.annotation.LayoutRes
import com.mmg.phonect.PhoneCt
import com.mmg.phonect.R
import com.mmg.phonect.background.receiver.widget.WidgetMaterialYouCurrentProvider
import com.mmg.phonect.common.basic.models.Location
import com.mmg.phonect.common.basic.models.options.NotificationTextColor
import com.mmg.phonect.settings.SettingsManager
import com.mmg.phonect.theme.resource.ResourceHelper
import com.mmg.phonect.theme.resource.ResourcesProviderFactory

class MaterialYouCurrentWidgetIMP: AbstractRemoteViewsPresenter() {

    companion object {

        @JvmStatic
        fun isEnable(context: Context): Boolean {
            return AppWidgetManager.getInstance(
                context
            ).getAppWidgetIds(
                ComponentName(
                    context,
                    WidgetMaterialYouCurrentProvider::class.java
                )
            ).isNotEmpty()
        }

        @JvmStatic
        fun updateWidgetView(context: Context, location: Location) {
            AppWidgetManager.getInstance(context).updateAppWidget(
                ComponentName(context, WidgetMaterialYouCurrentProvider::class.java),
                buildRemoteViews(context, location, R.layout.widget_material_you_current)
            )
        }
    }
}

private fun buildRemoteViews(
    context: Context,
    location: Location,
    @LayoutRes layoutId: Int,
): RemoteViews {

    val views = RemoteViews(
        context.packageName,
        layoutId
    )

    val weather = location.weather
    val dayTime = location.isDaylight

    val provider = ResourcesProviderFactory.getNewInstance()

    val settings = SettingsManager.getInstance(context)
    val temperatureUnit = settings.temperatureUnit

    if (weather == null) {
        return views
    }

    // current.

    views.setImageViewUri(
        R.id.widget_material_you_current_currentIcon,
        ResourceHelper.getWidgetNotificationIconUri(
            provider,
            weather.current.weatherCode,
            dayTime,
            false,
            NotificationTextColor.LIGHT
        )
    )

    views.setTextViewText(
        R.id.widget_material_you_current_currentTemperature,
        weather.current.temperature.getShortTemperature(context, temperatureUnit)
    )

    // pending intent.
    views.setOnClickPendingIntent(
        android.R.id.background,
        AbstractRemoteViewsPresenter.getWeatherPendingIntent(
            context,
            location,
            PhoneCt.WIDGET_MATERIAL_YOU_CURRENT_PENDING_INTENT_CODE_WEATHER
        )
    )

    return views
}