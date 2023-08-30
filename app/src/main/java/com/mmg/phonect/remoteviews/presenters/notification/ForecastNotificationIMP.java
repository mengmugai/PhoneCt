package com.mmg.phonect.remoteviews.presenters.notification;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.graphics.drawable.Icon;
import android.os.Build;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.mmg.phonect.PhoneCt;
import com.mmg.phonect.R;
import com.mmg.phonect.common.basic.models.Location;
import com.mmg.phonect.common.basic.models.options.unit.TemperatureUnit;
import com.mmg.phonect.common.basic.models.weather.Weather;
import com.mmg.phonect.common.basic.models.weather.WeatherCode;
import com.mmg.phonect.common.utils.LanguageUtils;
import com.mmg.phonect.remoteviews.presenters.AbstractRemoteViewsPresenter;
import com.mmg.phonect.settings.SettingsManager;
import com.mmg.phonect.theme.ThemeManager;
import com.mmg.phonect.theme.resource.ResourceHelper;
import com.mmg.phonect.theme.resource.ResourcesProviderFactory;
import com.mmg.phonect.theme.resource.providers.ResourceProvider;
import com.mmg.phonect.theme.weatherView.WeatherViewController;

/**
 * Forecast notification utils.
 * */

public class ForecastNotificationIMP extends AbstractRemoteViewsPresenter {

    public static void buildForecastAndSendIt(Context context, Location location, boolean today) {
        Weather weather = location.getWeather();
        if (weather == null) {
            return;
        }

        ResourceProvider provider = ResourcesProviderFactory.getNewInstance();

        LanguageUtils.setLanguage(
                context,
                SettingsManager.getInstance(context).getLanguage().getLocale()
        );
        
        // create channel.
        NotificationManagerCompat manager = NotificationManagerCompat.from(context);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    PhoneCt.NOTIFICATION_CHANNEL_ID_FORECAST,
                    PhoneCt.getNotificationChannelName(
                            context, PhoneCt.NOTIFICATION_CHANNEL_ID_FORECAST),
                    NotificationManager.IMPORTANCE_DEFAULT
            );
            channel.setImportance(NotificationManager.IMPORTANCE_HIGH);
            channel.setLockscreenVisibility(NotificationCompat.VISIBILITY_PUBLIC);
            manager.createNotificationChannel(channel);
        }

        // get builder.
        NotificationCompat.Builder builder = new NotificationCompat.Builder(
                context, PhoneCt.NOTIFICATION_CHANNEL_ID_FORECAST);

        // set notification level.
        builder.setPriority(NotificationCompat.PRIORITY_MAX);

        // set notification visibility.
        builder.setVisibility(NotificationCompat.VISIBILITY_PUBLIC);

        WeatherCode weatherCode;
        boolean daytime;
        if (today) {
            daytime = location.isDaylight();
            weatherCode = daytime 
                    ? weather.getDailyForecast().get(0).day().getWeatherCode() 
                    : weather.getDailyForecast().get(0).night().getWeatherCode();
        } else {
            daytime = true;
            weatherCode = weather.getDailyForecast().get(1).day().getWeatherCode() ;
        }

        // set small icon.
        builder.setSmallIcon(
                ResourceHelper.getDefaultMinimalXmlIconId(weatherCode, daytime));

        // large icon.
        builder.setLargeIcon(
                drawableToBitmap(
                        ResourceHelper.getWeatherIcon(provider, weatherCode, daytime)
                )
        );

        // sub text.
        if (today) {
            builder.setSubText(context.getString(R.string.today));
        } else {
            builder.setSubText(context.getString(R.string.tomorrow));
        }

        TemperatureUnit temperatureUnit = SettingsManager.getInstance(context).getTemperatureUnit();

        // title and content.
        if (today) {
            builder.setContentTitle(context.getString(R.string.daytime)
                    + " " + weather.getDailyForecast().get(0).day().getWeatherText()
                    + " " + weather.getDailyForecast().get(0).day().getTemperature().getTemperature(context, temperatureUnit)
            ).setContentText(context.getString(R.string.nighttime)
                    + " " + weather.getDailyForecast().get(0).night().getWeatherText()
                    + " " + weather.getDailyForecast().get(0).night().getTemperature().getTemperature(context, temperatureUnit)
            );
        } else {
            builder.setContentTitle(context.getString(R.string.daytime)
                    + " " + weather.getDailyForecast().get(1).day().getWeatherText()
                    + " " + weather.getDailyForecast().get(1).day().getTemperature().getTemperature(context, temperatureUnit)
            ).setContentText(context.getString(R.string.nighttime)
                    + " " + weather.getDailyForecast().get(1).night().getWeatherText()
                    + " " + weather.getDailyForecast().get(1).night().getTemperature().getTemperature(context, temperatureUnit)
            );
        }

        builder.setColor(
                ThemeManager.getInstance(context).getPhoneCtThemeDelegate().getThemeColors(
                        context, WeatherViewController.getWeatherKind(weather), daytime
                )[0]
        );

        // set intent.
        builder.setContentIntent(
                getWeatherPendingIntent(
                        context,
                        null,
                        today
                                ? PhoneCt.NOTIFICATION_ID_TODAY_FORECAST
                                : PhoneCt.NOTIFICATION_ID_TOMORROW_FORECAST
                )
        );

        // set sound & vibrate.
        builder.setDefaults(Notification.DEFAULT_SOUND | Notification.DEFAULT_VIBRATE);
        builder.setAutoCancel(true);

        // set badge.
        builder.setBadgeIconType(NotificationCompat.BADGE_ICON_SMALL);

        Notification notification = builder.build();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            try {
                notification.getClass()
                        .getMethod("setSmallIcon", Icon.class)
                        .invoke(
                                notification,
                                ResourceHelper.getMinimalIcon(
                                        provider, weather.getCurrent().getWeatherCode(), daytime)
                        );
            } catch (Exception ignore) {
                // do nothing.
            }
        }

        // commit.
        manager.notify(
                today
                        ? PhoneCt.NOTIFICATION_ID_TODAY_FORECAST
                        : PhoneCt.NOTIFICATION_ID_TOMORROW_FORECAST,
                notification
        );
    }

    public static boolean isEnable(Context context, boolean today) {
        if (today) {
            return SettingsManager.getInstance(context).isTodayForecastEnabled();
        } else {
            return SettingsManager.getInstance(context).isTomorrowForecastEnabled();
        }
    }
}
