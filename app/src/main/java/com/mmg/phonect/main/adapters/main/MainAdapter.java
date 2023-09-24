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
import com.mmg.phonect.common.basic.models.Phone;
import com.mmg.phonect.common.basic.models.options.appearance.CardDisplay;
import com.mmg.phonect.common.basic.models.weather.Device;
import com.mmg.phonect.common.basic.models.weather.Weather;
import com.mmg.phonect.main.adapters.main.holder.AbnormalViewHolder;
import com.mmg.phonect.main.adapters.main.holder.DeviceViewHolder;
import com.mmg.phonect.main.adapters.main.holder.AppListViewHolder;
import com.mmg.phonect.theme.weatherView.WeatherView;
import com.mmg.phonect.main.adapters.main.holder.AbstractMainCardViewHolder;
import com.mmg.phonect.main.adapters.main.holder.AbstractMainViewHolder;
//import com.mmg.phonect.main.adapters.main.holder.DailyViewHolder;
import com.mmg.phonect.main.adapters.main.holder.FooterViewHolder;
import com.mmg.phonect.main.adapters.main.holder.HeaderViewHolder;
import com.mmg.phonect.theme.resource.providers.ResourceProvider;
import com.mmg.phonect.settings.SettingsManager;

public class MainAdapter extends RecyclerView.Adapter<AbstractMainViewHolder> {

    private GeoActivity mActivity;
    private RecyclerView mHost;
    private WeatherView mWeatherView;
    private @Nullable Location mLocation;

    private @Nullable Phone mPhone;
    private ResourceProvider mProvider;

    private List<Integer> mViewTypeList;
    private @Nullable Integer mFirstCardPosition;
    private List<Animator> mPendingAnimatorList;
    private int mHeaderCurrentTemperatureTextHeight;
    private boolean mListAnimationEnabled;
    private boolean mItemAnimationEnabled;

    public MainAdapter(@NonNull GeoActivity activity, @NonNull RecyclerView host,
                       @NonNull WeatherView weatherView, @Nullable Phone phone,
                       @NonNull ResourceProvider provider,
                       boolean listAnimationEnabled, boolean itemAnimationEnabled) {
        update(activity, host, weatherView, phone, provider, listAnimationEnabled, itemAnimationEnabled);
    }

    public void update(@NonNull GeoActivity activity, @NonNull RecyclerView host,
                       @NonNull WeatherView weatherView, @Nullable Phone phone,
                       @NonNull ResourceProvider provider,
                       boolean listAnimationEnabled, boolean itemAnimationEnabled) {
        mActivity = activity;
        mHost = host;
        mWeatherView = weatherView;
        mPhone = phone;
        mProvider = provider;

        mViewTypeList = new ArrayList<>();
        mFirstCardPosition = null;
        mPendingAnimatorList = new ArrayList<>();
        mHeaderCurrentTemperatureTextHeight = -1;
        mListAnimationEnabled = listAnimationEnabled;
        mItemAnimationEnabled = itemAnimationEnabled;

        if (phone != null && phone.getDevice() != null) {
            Device device = phone.getDevice();
            List<CardDisplay> cardDisplayList = SettingsManager.getInstance(activity).getCardDisplayList();
            mViewTypeList.add(ViewType.HEADER);
            for (CardDisplay c : cardDisplayList) {
                 // 此处检测为没有就不展示卡片了
//                if (c == CardDisplay.CARD_APP_LIST
//                        && !weather.getCurrent().getAirQuality().isValid()) {
//                    continue;
//                }
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

//            case ViewType.DAILY:
//                return new DailyViewHolder(parent);

//            case ViewType.HOURLY:
//                return new HourlyViewHolder(parent);

//            case ViewType.AIR_QUALITY:
//                return new AirQualityViewHolder(parent);

//            case ViewType.ALLERGEN:
//                return new AllergenViewHolder(parent);

//            case ViewType.ASTRO:
//                return new AstroViewHolder(parent);

            case ViewType.ABNORMAL:
                return new AbnormalViewHolder(parent);

            case ViewType.DEVICE:

                return new DeviceViewHolder(parent);

            case ViewType.APPLIST:

                return new AppListViewHolder(parent);

            default: // FOOTER.
                return new FooterViewHolder(parent);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull AbstractMainViewHolder holder, int position) {
        assert mPhone != null;
        if (holder instanceof AbstractMainCardViewHolder) {
            ((AbstractMainCardViewHolder) holder).onBindView(
                    mActivity,
                    mPhone,
                    mProvider,
                    mListAnimationEnabled,
                    mItemAnimationEnabled,
                    mFirstCardPosition != null && mFirstCardPosition == position
            );
        } else {
            holder.onBindView(mActivity, mPhone, mProvider, mListAnimationEnabled, mItemAnimationEnabled);
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
            if (
//                    type == ViewType.DAILY
//                    || type == ViewType.HOURLY
//                    || type == ViewType.AIR_QUALITY
//                    || type == ViewType.ALLERGEN
//                    || type == ViewType.ASTRO
                    type == ViewType.ABNORMAL
                    || type == ViewType.DEVICE
                    || type == ViewType.APPLIST) {
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
//            case CARD_DAILY_OVERVIEW:
//                return ViewType.DAILY;

            case CARD_ABNORMAL_INFO:
                return ViewType.ABNORMAL;
//
//            case CARD_AIR_QUALITY:
//                return ViewType.AIR_QUALITY;
//
//            case CARD_ALLERGEN:
//                return ViewType.ALLERGEN;
//
//            case CARD_SUNRISE_SUNSET:
//                return ViewType.ASTRO;
            case CARD_DEVICE_INFO:
                return ViewType.DEVICE;

            case CARD_APP_LIST:
                return ViewType.APPLIST;

            default: // CARD_LIFE_DETAILS.
                return ViewType.DEVICE;
        }
    }
}
