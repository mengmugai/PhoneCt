package com.mmg.phonect.settings.activities;

import android.annotation.SuppressLint;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import com.google.android.material.appbar.MaterialToolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import com.mmg.phonect.R;
import com.mmg.phonect.common.basic.GeoActivity;
import com.mmg.phonect.common.basic.models.weather.WeatherCode;
import com.mmg.phonect.common.ui.widgets.insets.FitSystemBarAppBarLayout;
import com.mmg.phonect.theme.ThemeManager;
import com.mmg.phonect.theme.resource.ResourceHelper;
import com.mmg.phonect.theme.resource.providers.DefaultResourceProvider;
import com.mmg.phonect.theme.resource.providers.PixelResourcesProvider;
import com.mmg.phonect.theme.resource.providers.ResourceProvider;
import com.mmg.phonect.theme.resource.ResourcesProviderFactory;
import com.mmg.phonect.settings.adapters.WeatherIconAdapter;
import com.mmg.phonect.settings.dialogs.AdaptiveIconDialog;
import com.mmg.phonect.settings.dialogs.AnimatableIconDialog;
import com.mmg.phonect.settings.dialogs.MinimalIconDialog;
import com.mmg.phonect.common.utils.DisplayUtils;
import com.mmg.phonect.common.utils.helpers.IntentHelper;

public class PreviewIconActivity extends GeoActivity {

    private ResourceProvider mProvider;
    private List<WeatherIconAdapter.Item> mItemList;

    public static final String KEY_ICON_PREVIEW_ACTIVITY_PACKAGE_NAME
            = "ICON_PREVIEW_ACTIVITY_PACKAGE_NAME";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preview_icon);
        initData();
        initWidget();
    }

    @SuppressLint("MissingSuperCall")
    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        // do nothing.
    }

    private void initData() {
        mProvider = ResourcesProviderFactory.getNewInstance(
                getIntent().getStringExtra(KEY_ICON_PREVIEW_ACTIVITY_PACKAGE_NAME)
        );
        mItemList = new ArrayList<>();

        mItemList.add(new WeatherIconAdapter.Title(getString(R.string.daytime)));
        mItemList.add(new WeatherIcon(mProvider, WeatherCode.CLEAR, true));
        mItemList.add(new WeatherIcon(mProvider, WeatherCode.PARTLY_CLOUDY, true));
        mItemList.add(new WeatherIcon(mProvider, WeatherCode.CLOUDY, true));
        mItemList.add(new WeatherIcon(mProvider, WeatherCode.WIND, true));
        mItemList.add(new WeatherIcon(mProvider, WeatherCode.RAIN, true));
        mItemList.add(new WeatherIcon(mProvider, WeatherCode.SNOW, true));
        mItemList.add(new WeatherIcon(mProvider, WeatherCode.SLEET, true));
        mItemList.add(new WeatherIcon(mProvider, WeatherCode.HAIL, true));
        mItemList.add(new WeatherIcon(mProvider, WeatherCode.THUNDER, true));
        mItemList.add(new WeatherIcon(mProvider, WeatherCode.THUNDERSTORM, true));
        mItemList.add(new WeatherIcon(mProvider, WeatherCode.FOG, true));
        mItemList.add(new WeatherIcon(mProvider, WeatherCode.HAZE, true));
        mItemList.add(new WeatherIconAdapter.Line());

        mItemList.add(new WeatherIconAdapter.Title(getString(R.string.nighttime)));
        mItemList.add(new WeatherIcon(mProvider, WeatherCode.CLEAR, false));
        mItemList.add(new WeatherIcon(mProvider, WeatherCode.PARTLY_CLOUDY, false));
        mItemList.add(new WeatherIcon(mProvider, WeatherCode.CLOUDY, false));
        mItemList.add(new WeatherIcon(mProvider, WeatherCode.WIND, false));
        mItemList.add(new WeatherIcon(mProvider, WeatherCode.RAIN, false));
        mItemList.add(new WeatherIcon(mProvider, WeatherCode.SNOW, false));
        mItemList.add(new WeatherIcon(mProvider, WeatherCode.SLEET, false));
        mItemList.add(new WeatherIcon(mProvider, WeatherCode.HAIL, false));
        mItemList.add(new WeatherIcon(mProvider, WeatherCode.THUNDER, false));
        mItemList.add(new WeatherIcon(mProvider, WeatherCode.THUNDERSTORM, false));
        mItemList.add(new WeatherIcon(mProvider, WeatherCode.FOG, false));
        mItemList.add(new WeatherIcon(mProvider, WeatherCode.HAZE, false));
        mItemList.add(new WeatherIconAdapter.Line());

        boolean darkMode = DisplayUtils.isDarkMode(this);

        mItemList.add(new WeatherIconAdapter.Title("Minimal " + getString(R.string.daytime)));
        mItemList.add(new MinimalIcon(mProvider, WeatherCode.CLEAR, true, darkMode));
        mItemList.add(new MinimalIcon(mProvider, WeatherCode.PARTLY_CLOUDY, true, darkMode));
        mItemList.add(new MinimalIcon(mProvider, WeatherCode.CLOUDY, true, darkMode));
        mItemList.add(new MinimalIcon(mProvider, WeatherCode.WIND, true, darkMode));
        mItemList.add(new MinimalIcon(mProvider, WeatherCode.RAIN, true, darkMode));
        mItemList.add(new MinimalIcon(mProvider, WeatherCode.SNOW, true, darkMode));
        mItemList.add(new MinimalIcon(mProvider, WeatherCode.SLEET, true, darkMode));
        mItemList.add(new MinimalIcon(mProvider, WeatherCode.HAIL, true, darkMode));
        mItemList.add(new MinimalIcon(mProvider, WeatherCode.THUNDER, true, darkMode));
        mItemList.add(new MinimalIcon(mProvider, WeatherCode.THUNDERSTORM, true, darkMode));
        mItemList.add(new MinimalIcon(mProvider, WeatherCode.FOG, true, darkMode));
        mItemList.add(new MinimalIcon(mProvider, WeatherCode.HAZE, true, darkMode));
        mItemList.add(new WeatherIconAdapter.Line());

        mItemList.add(new WeatherIconAdapter.Title("Minimal " + getString(R.string.nighttime)));
        mItemList.add(new MinimalIcon(mProvider, WeatherCode.CLEAR, false, darkMode));
        mItemList.add(new MinimalIcon(mProvider, WeatherCode.PARTLY_CLOUDY, false, darkMode));
        mItemList.add(new MinimalIcon(mProvider, WeatherCode.CLOUDY, false, darkMode));
        mItemList.add(new MinimalIcon(mProvider, WeatherCode.WIND, false, darkMode));
        mItemList.add(new MinimalIcon(mProvider, WeatherCode.RAIN, false, darkMode));
        mItemList.add(new MinimalIcon(mProvider, WeatherCode.SNOW, false, darkMode));
        mItemList.add(new MinimalIcon(mProvider, WeatherCode.SLEET, false, darkMode));
        mItemList.add(new MinimalIcon(mProvider, WeatherCode.HAIL, false, darkMode));
        mItemList.add(new MinimalIcon(mProvider, WeatherCode.THUNDER, false, darkMode));
        mItemList.add(new MinimalIcon(mProvider, WeatherCode.THUNDERSTORM, false, darkMode));
        mItemList.add(new MinimalIcon(mProvider, WeatherCode.FOG, false, darkMode));
        mItemList.add(new MinimalIcon(mProvider, WeatherCode.HAZE, false, darkMode));
        mItemList.add(new WeatherIconAdapter.Line());

        mItemList.add(new WeatherIconAdapter.Title("Shortcuts " + getString(R.string.daytime)));
        mItemList.add(new ShortcutIcon(mProvider, WeatherCode.CLEAR, true));
        mItemList.add(new ShortcutIcon(mProvider, WeatherCode.PARTLY_CLOUDY, true));
        mItemList.add(new ShortcutIcon(mProvider, WeatherCode.CLOUDY, true));
        mItemList.add(new ShortcutIcon(mProvider, WeatherCode.WIND, true));
        mItemList.add(new ShortcutIcon(mProvider, WeatherCode.RAIN, true));
        mItemList.add(new ShortcutIcon(mProvider, WeatherCode.SNOW, true));
        mItemList.add(new ShortcutIcon(mProvider, WeatherCode.SLEET, true));
        mItemList.add(new ShortcutIcon(mProvider, WeatherCode.HAIL, true));
        mItemList.add(new ShortcutIcon(mProvider, WeatherCode.THUNDER, true));
        mItemList.add(new ShortcutIcon(mProvider, WeatherCode.THUNDERSTORM, true));
        mItemList.add(new ShortcutIcon(mProvider, WeatherCode.FOG, true));
        mItemList.add(new ShortcutIcon(mProvider, WeatherCode.HAZE, true));
        mItemList.add(new WeatherIconAdapter.Line());

        mItemList.add(new WeatherIconAdapter.Title("Shortcuts " + getString(R.string.nighttime)));
        mItemList.add(new ShortcutIcon(mProvider, WeatherCode.CLEAR, false));
        mItemList.add(new ShortcutIcon(mProvider, WeatherCode.PARTLY_CLOUDY, false));
        mItemList.add(new ShortcutIcon(mProvider, WeatherCode.CLOUDY, false));
        mItemList.add(new ShortcutIcon(mProvider, WeatherCode.WIND, false));
        mItemList.add(new ShortcutIcon(mProvider, WeatherCode.RAIN, false));
        mItemList.add(new ShortcutIcon(mProvider, WeatherCode.SNOW, false));
        mItemList.add(new ShortcutIcon(mProvider, WeatherCode.SLEET, false));
        mItemList.add(new ShortcutIcon(mProvider, WeatherCode.HAIL, false));
        mItemList.add(new ShortcutIcon(mProvider, WeatherCode.THUNDER, false));
        mItemList.add(new ShortcutIcon(mProvider, WeatherCode.THUNDERSTORM, false));
        mItemList.add(new ShortcutIcon(mProvider, WeatherCode.FOG, false));
        mItemList.add(new ShortcutIcon(mProvider, WeatherCode.HAZE, false));
        mItemList.add(new WeatherIconAdapter.Line());

        mItemList.add(new WeatherIconAdapter.Title(getString(R.string.sunrise_sunset)));
        mItemList.add(new SunIcon(mProvider));
        mItemList.add(new MoonIcon(mProvider));
    }

    @SuppressLint("NonConstantResourceId")
    private void initWidget() {
        FitSystemBarAppBarLayout appBarLayout = findViewById(R.id.activity_preview_icon_appBar);
        appBarLayout.injectDefaultSurfaceTintColor();

        MaterialToolbar toolbar = findViewById(R.id.activity_preview_icon_toolbar);
        toolbar.setTitle(mProvider.getProviderName());
        toolbar.setNavigationOnClickListener(v -> finish());
        toolbar.inflateMenu(R.menu.activity_preview_icon);
        toolbar.setOnMenuItemClickListener(item -> {
            if (item.getItemId() == R.id.action_about) {
                if (mProvider instanceof DefaultResourceProvider
                        || mProvider instanceof PixelResourcesProvider) {
                    IntentHelper.startApplicationDetailsActivity(this);
                } else {
                    IntentHelper.startApplicationDetailsActivity(this, mProvider.getPackageName());
                }
            }
            return true;
        });
        toolbar.setBackgroundColor(
                DisplayUtils.getWidgetSurfaceColor(
                        6f,
                        ThemeManager.getInstance(this).getThemeColor(this, R.attr.colorPrimary),
                        ThemeManager.getInstance(this).getThemeColor(this, R.attr.colorSurface)
                )
        );

        RecyclerView recyclerView = findViewById(R.id.activity_preview_icon_recyclerView);
        GridLayoutManager manager = new GridLayoutManager(this, 4);
        manager.setSpanSizeLookup(WeatherIconAdapter.getSpanSizeLookup(4, mItemList));
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(new WeatherIconAdapter(this, mItemList));
    }
}

abstract class BaseWeatherIcon extends WeatherIconAdapter.WeatherIcon {

    protected ResourceProvider provider;
    protected WeatherCode weatherCode;
    protected boolean daytime;

    BaseWeatherIcon(ResourceProvider provider, WeatherCode weatherCode, boolean daytime) {
        this.provider = provider;
        this.weatherCode = weatherCode;
        this.daytime = daytime;
    }

    @Override
    public String getContentDescription() {
        String name = weatherCode.name().toLowerCase().replace("_", " ");
        return name.substring(0, 1).toUpperCase() + name.substring(1);
    }
}

class WeatherIcon extends BaseWeatherIcon {

    WeatherIcon(ResourceProvider provider, WeatherCode weatherCode, boolean daytime) {
        super(provider, weatherCode, daytime);
    }

    @Override
    public Drawable getDrawable() {
        return ResourceHelper.getWeatherIcon(provider, weatherCode, daytime);
    }

    @Override
    public void onItemClicked(GeoActivity activity) {
        AnimatableIconDialog.show(activity, weatherCode, daytime, provider);
    }
}

class MinimalIcon extends BaseWeatherIcon {

    private final boolean mDarkMode;

    MinimalIcon(ResourceProvider provider, WeatherCode weatherCode, boolean daytime, boolean darkMode) {
        super(provider, weatherCode, daytime);
        mDarkMode = darkMode;
    }

    @Override
    public Drawable getDrawable() {
        return ResourceHelper.getWidgetNotificationIcon(
                provider, weatherCode, daytime, true, !mDarkMode);
    }

    @Override
    public void onItemClicked(GeoActivity activity) {
        MinimalIconDialog.show(activity, weatherCode, daytime, provider);
    }
}

class ShortcutIcon extends BaseWeatherIcon {

    ShortcutIcon(ResourceProvider provider, WeatherCode weatherCode, boolean daytime) {
        super(provider, weatherCode, daytime);
    }

    @Override
    public Drawable getDrawable() {
        return ResourceHelper.getShortcutsIcon(provider, weatherCode, daytime);
    }

    @Override
    public void onItemClicked(GeoActivity activity) {
        AdaptiveIconDialog.show(activity, weatherCode, daytime, provider);
    }
}

class SunIcon extends WeatherIconAdapter.WeatherIcon {

    protected ResourceProvider provider;
    
    SunIcon(ResourceProvider provider) {
        this.provider = provider;
    }

    @Override
    public Drawable getDrawable() {
        return ResourceHelper.getSunDrawable(provider);
    }

    @Override
    public String getContentDescription() {
        return "Sun";
    }

    @Override
    public void onItemClicked(GeoActivity activity) {
        // do nothing.
    }
}

class MoonIcon extends SunIcon {

    MoonIcon(ResourceProvider provider) {
        super(provider);
    }

    @Override
    public Drawable getDrawable() {
        return ResourceHelper.getMoonDrawable(provider);
    }

    @Override
    public String getContentDescription() {
        return "Moon";
    }
}