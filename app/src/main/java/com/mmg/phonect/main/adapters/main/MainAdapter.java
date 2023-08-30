package com.mmg.phonect.main.adapters.main;

import android.animation.Animator;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import com.mmg.phonect.common.basic.GeoActivity;
import com.mmg.phonect.common.basic.models.Location;
import com.mmg.phonect.common.basic.models.options.appearance.CardDisplay;
import com.mmg.phonect.common.basic.models.weather.Weather;
import com.mmg.phonect.main.adapters.main.holder.DeviceViewHolder;
import com.mmg.phonect.theme.weatherView.WeatherView;
import com.mmg.phonect.main.adapters.main.holder.AbstractMainCardViewHolder;
import com.mmg.phonect.main.adapters.main.holder.AbstractMainViewHolder;
import com.mmg.phonect.main.adapters.main.holder.AirQualityViewHolder;
import com.mmg.phonect.main.adapters.main.holder.AllergenViewHolder;
import com.mmg.phonect.main.adapters.main.holder.AstroViewHolder;
import com.mmg.phonect.main.adapters.main.holder.DailyViewHolder;
import com.mmg.phonect.main.adapters.main.holder.DetailsViewHolder;
import com.mmg.phonect.main.adapters.main.holder.FooterViewHolder;
import com.mmg.phonect.main.adapters.main.holder.HeaderViewHolder;
import com.mmg.phonect.main.adapters.main.holder.HourlyViewHolder;
import com.mmg.phonect.theme.resource.providers.ResourceProvider;
import com.mmg.phonect.settings.SettingsManager;

public class MainAdapter extends RecyclerView.Adapter<AbstractMainViewHolder> {

    private GeoActivity mActivity;
    private RecyclerView mHost;
    private WeatherView mWeatherView;
    private @Nullable Location mLocation;
    private ResourceProvider mProvider;

    private List<Integer> mViewTypeList;
    private @Nullable Integer mFirstCardPosition;
    private List<Animator> mPendingAnimatorList;
    private int mHeaderCurrentTemperatureTextHeight;
    private boolean mListAnimationEnabled;
    private boolean mItemAnimationEnabled;

    public MainAdapter(@NonNull GeoActivity activity, @NonNull RecyclerView host,
                       @NonNull WeatherView weatherView, @Nullable Location location,
                       @NonNull ResourceProvider provider,
                       boolean listAnimationEnabled, boolean itemAnimationEnabled) {
        update(activity, host, weatherView, location, provider, listAnimationEnabled, itemAnimationEnabled);
    }

    public void update(@NonNull GeoActivity activity, @NonNull RecyclerView host,
                       @NonNull WeatherView weatherView, @Nullable Location location,
                       @NonNull ResourceProvider provider,
                       boolean listAnimationEnabled, boolean itemAnimationEnabled) {
        mActivity = activity;
        mHost = host;
        mWeatherView = weatherView;
        mLocation = location;
        mProvider = provider;

        mViewTypeList = new ArrayList<>();
        mFirstCardPosition = null;
        mPendingAnimatorList = new ArrayList<>();
        mHeaderCurrentTemperatureTextHeight = -1;
        mListAnimationEnabled = listAnimationEnabled;
        mItemAnimationEnabled = itemAnimationEnabled;

        if (location != null && location.getWeather() != null) {
            Weather weather = location.getWeather();
            List<CardDisplay> cardDisplayList = SettingsManager.getInstance(activity).getCardDisplayList();
            mViewTypeList.add(ViewType.HEADER);
            for (CardDisplay c : cardDisplayList) {
                if (c == CardDisplay.CARD_AIR_QUALITY
                        && !weather.getCurrent().getAirQuality().isValid()) {
                    continue;
                }
                if (c == CardDisplay.CARD_ALLERGEN
                        && !weather.getDailyForecast().get(0).getPollen().isValid()) {
                    continue;
                }
                if (c == CardDisplay.CARD_SUNRISE_SUNSET
                        && (weather.getDailyForecast().size() == 0
                        || !weather.getDailyForecast().get(0).sun().isValid())) {
                    continue;
                }
                mViewTypeList.add(getViewType(c));
            }
            mViewTypeList.add(ViewType.FOOTER);

            ensureFirstCard();
        }
    }

    public void setNullWeather() {
        mViewTypeList = new ArrayList<>();
        ensureFirstCard();
    }

    @NonNull
    @Override
    public AbstractMainViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        switch (viewType) {
            case ViewType.HEADER:
                return new HeaderViewHolder(parent, mWeatherView);

            case ViewType.DAILY:
                return new DailyViewHolder(parent);

            case ViewType.HOURLY:
                return new HourlyViewHolder(parent);

            case ViewType.AIR_QUALITY:
                return new AirQualityViewHolder(parent);

            case ViewType.ALLERGEN:
                return new AllergenViewHolder(parent);

            case ViewType.ASTRO:
                return new AstroViewHolder(parent);

            case ViewType.DETAILS:
                return new DetailsViewHolder(parent);

            case ViewType.DEVICE:

                return new DeviceViewHolder(parent);

            default: // FOOTER.
                return new FooterViewHolder(parent);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull AbstractMainViewHolder holder, int position) {
        assert mLocation != null;
        if (holder instanceof AbstractMainCardViewHolder) {
            ((AbstractMainCardViewHolder) holder).onBindView(
                    mActivity,
                    mLocation,
                    mProvider,
                    mListAnimationEnabled,
                    mItemAnimationEnabled,
                    mFirstCardPosition != null && mFirstCardPosition == position
            );
        } else {
            holder.onBindView(mActivity, mLocation, mProvider, mListAnimationEnabled, mItemAnimationEnabled);
        }
        mHost.post(() -> holder.checkEnterScreen(mHost, mPendingAnimatorList, mListAnimationEnabled));
    }

    @Override
    public void onViewRecycled(@NonNull AbstractMainViewHolder holder) {
        holder.onRecycleView();
    }

    @Override
    public int getItemCount() {
        return mViewTypeList.size();
    }

    @Override
    public int getItemViewType(int position) {
        return mViewTypeList.get(position);
    }

    private void ensureFirstCard() {
        mFirstCardPosition = null;
        for (int i = 0; i < getItemCount(); i ++) {
            int type = getItemViewType(i);
            if (type == ViewType.DAILY
                    || type == ViewType.HOURLY
                    || type == ViewType.AIR_QUALITY
                    || type == ViewType.ALLERGEN
                    || type == ViewType.ASTRO
                    || type == ViewType.DETAILS) {
                mFirstCardPosition = i;
                return;
            }
        }
    }

    public int getCurrentTemperatureTextHeight() {
        if (mHeaderCurrentTemperatureTextHeight <= 0 && getItemCount() > 0) {
            AbstractMainViewHolder holder = (AbstractMainViewHolder) mHost.findViewHolderForAdapterPosition(0);
            if (holder instanceof HeaderViewHolder) {
                mHeaderCurrentTemperatureTextHeight
                        = ((HeaderViewHolder) holder).getCurrentTemperatureHeight();
            }
        }
        return mHeaderCurrentTemperatureTextHeight;
    }

    public void onScroll() {
        AbstractMainViewHolder holder;
        for (int i = 0; i < getItemCount(); i ++) {
            holder = (AbstractMainViewHolder) mHost.findViewHolderForAdapterPosition(i);
            if (holder != null) {
                holder.checkEnterScreen(mHost, mPendingAnimatorList, mListAnimationEnabled);
            }
        }
    }

    private static int getViewType(CardDisplay cardDisplay) {
        switch (cardDisplay) {
            case CARD_DAILY_OVERVIEW:
                return ViewType.DAILY;

            case CARD_HOURLY_OVERVIEW:
                return ViewType.HOURLY;

            case CARD_AIR_QUALITY:
                return ViewType.AIR_QUALITY;

            case CARD_ALLERGEN:
                return ViewType.ALLERGEN;

            case CARD_SUNRISE_SUNSET:
                return ViewType.ASTRO;
            case CARD_DEVICE_INFO:
                return ViewType.DEVICE;

            default: // CARD_LIFE_DETAILS.
                return ViewType.DETAILS;
        }
    }
}
