package com.mmg.phonect.main.adapters.main.holder;

import android.annotation.SuppressLint;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.mmg.phonect.R;
import com.mmg.phonect.common.basic.GeoActivity;
import com.mmg.phonect.common.basic.models.Location;
import com.mmg.phonect.common.basic.models.Phone;
import com.mmg.phonect.common.basic.models.options.appearance.AppListTrendDisplay;
import com.mmg.phonect.common.basic.models.weather.Daily;
import com.mmg.phonect.common.basic.models.weather.Device;
import com.mmg.phonect.common.basic.models.weather.Weather;
import com.mmg.phonect.common.ui.adapters.TagAdapter;
import com.mmg.phonect.common.ui.decotarions.GridMarginsDecoration;
import com.mmg.phonect.common.ui.widgets.trend.TrendRecyclerView;
import com.mmg.phonect.common.utils.DisplayUtils;
import com.mmg.phonect.main.adapters.AppListAdapter;
import com.mmg.phonect.main.adapters.DeviceAdapter;
import com.mmg.phonect.main.adapters.main.AppListTag;
import com.mmg.phonect.main.adapters.trend.AppListTrendAdapter;
import com.mmg.phonect.main.layouts.TrendHorizontalLinearLayoutManager;
import com.mmg.phonect.main.utils.MainThemeColorProvider;
import com.mmg.phonect.main.widgets.TrendRecyclerViewScrollBar;
import com.mmg.phonect.settings.SettingsManager;
import com.mmg.phonect.theme.ThemeManager;
import com.mmg.phonect.theme.resource.providers.ResourceProvider;
import com.mmg.phonect.theme.weatherView.WeatherViewController;

import java.util.ArrayList;
import java.util.List;

public class AppListViewHolder extends AbstractMainCardViewHolder {

    private final TextView mTitle;
    private final TextView mSubtitle;
    private final RecyclerView mTagView;

    private final RecyclerView mTrendRecyclerView;
    private final AppListTrendAdapter mTrendAdapter;
//    private final TrendRecyclerViewScrollBar mScrollBar;

    public AppListViewHolder(ViewGroup parent) {
        super(
                LayoutInflater
                        .from(parent.getContext())
                        .inflate(R.layout.container_main_applist_trend_card, parent, false)
        );

        mTitle = itemView.findViewById(R.id.container_main_daily_trend_card_title);
        mSubtitle = itemView.findViewById(R.id.container_main_daily_trend_card_subtitle);
        mTagView = itemView.findViewById(R.id.container_main_daily_trend_card_tagView);

        mTrendRecyclerView = itemView.findViewById(R.id.container_main_applist_recyclerView);
//        mTrendRecyclerView.setHasFixedSize(true);

        mTrendAdapter = new AppListTrendAdapter();
//        mScrollBar = new TrendRecyclerViewScrollBar();
//        mTrendRecyclerView.addItemDecoration(mScrollBar);
    }

    @SuppressLint("NotifyDataSetChanged")
    @Override
    public void onBindView(GeoActivity activity, @NonNull Phone phone,
                           @NonNull ResourceProvider provider,
                           boolean listAnimationEnabled, boolean itemAnimationEnabled, boolean firstCard) {
        super.onBindView(activity, phone, provider, listAnimationEnabled, itemAnimationEnabled, firstCard);

        Device device = phone.getDevice();
        assert device != null;

        int[] colors = ThemeManager
                .getInstance(context)
                .getPhoneCtThemeDelegate()
                .getThemeColors(
                        context,
                        WeatherViewController.getWeatherKind(device),
                        phone.isDaylight()
                );

        mTitle.setTextColor(colors[0]);
        //todo: 判断整个卡片显不显示的判断
//        if (TextUtils.isEmpty(device.getCurrent().getDailyForecast())) {
//            mSubtitle.setVisibility(View.GONE);
//        } else {
            mSubtitle.setVisibility(View.VISIBLE);
            mSubtitle.setText("此处只做显示  不作为风险标识");
//        }

        List<TagAdapter.Tag> tagList = getTagList(device);
        if (tagList.size() < 2) {
            mTagView.setVisibility(View.GONE);
        } else {
            mTagView.setVisibility(View.VISIBLE);
            int decorCount = mTagView.getItemDecorationCount();
            for (int i = 0; i < decorCount; i++) {
                mTagView.removeItemDecorationAt(0);
            }
            mTagView.addItemDecoration(
                    new GridMarginsDecoration(
                            context.getResources().getDimension(R.dimen.little_margin),
                            context.getResources().getDimension(R.dimen.normal_margin),
                            mTagView
                    )
            );

//            mTagView.setLayoutManager(new LinearLayoutManager(context));
//            mTagView.setAdapter(new DetailsAdapter(context, phone));


            mTagView.setLayoutManager(new TrendHorizontalLinearLayoutManager(context));
            mTagView.setAdapter(
                    new TagAdapter(
                            tagList,
                            MainThemeColorProvider.getColor(phone, R.attr.colorOnPrimary),
                            MainThemeColorProvider.getColor(phone, R.attr.colorOnSurface),
                            MainThemeColorProvider.getColor(phone, R.attr.colorPrimary),
                            DisplayUtils.getWidgetSurfaceColor(
                                    DisplayUtils.DEFAULT_CARD_LIST_ITEM_ELEVATION_DP,
                                    MainThemeColorProvider.getColor(phone, R.attr.colorPrimary),
                                    MainThemeColorProvider.getColor(phone, R.attr.colorSurface)
                            ),
                            (checked, oldPosition, newPosition) -> {
                                setTrendAdapterByTag(phone, (AppListTag) tagList.get(newPosition));
                                return false;
                            },
                            0
                    )
            );
        }


        mTrendRecyclerView.setLayoutManager(new LinearLayoutManager(context));
        mTrendRecyclerView.setAdapter(mTrendAdapter);
//        mTrendRecyclerView.setAdapter(new AppListAdapter(context,phone));

//        mTrendRecyclerView.setLayoutManager(
//                new TrendHorizontalLinearLayoutManager(
//                        context,
//                        DisplayUtils.isLandscape(context) ? 7 : 5
//                )
//        );
//        mTrendRecyclerView.setLineColor(MainThemeColorProvider.getColor(phone, R.attr.colorOutline));
//        mTrendRecyclerView.setAdapter(mTrendAdapter);
//        mTrendRecyclerView.setKeyLineVisibility(
//                SettingsManager.getInstance(context).isTrendHorizontalLinesEnabled());
        setTrendAdapterByTag(phone, (AppListTag) tagList.get(0));

//        mScrollBar.resetColor(phone);
    }

    @SuppressLint("NotifyDataSetChanged")
    private void setTrendAdapterByTag(Phone phone, AppListTag tag) {
        switch (tag.getType()) {
            case XPOSEDMODULE:
                mTrendAdapter.xposedModule(
                        context,
                        mTrendRecyclerView,
                        phone,
                        provider,
                        SettingsManager.getInstance(context).getTemperatureUnit()
                );
                break;

            case HOOK_FRAMEWORK:
                mTrendAdapter.hookFramework(
                        context,
                        mTrendRecyclerView,
                        phone,
                        provider,
                        SettingsManager.getInstance(context).getPrecipitationUnit()
                );
                break;


        }
        mTrendAdapter.notifyDataSetChanged();
    }

    private List<TagAdapter.Tag> getTagList(Device device) {
        List<TagAdapter.Tag> tagList = new ArrayList<>();
        List<AppListTrendDisplay> displayList
                = SettingsManager.getInstance(context).getAppListTrendDisplay();
        for (AppListTrendDisplay display : displayList) {
            switch (display) {
                case TAG_XPOSEDMODULE:
                    tagList.add(new AppListTag(context.getString(R.string.xposedmodule), AppListTag.Type.XPOSEDMODULE));
                    break;

                case TAG_HOOK_FRAMEWORK:
                        // todo： 应该判断存不存在
                        if (device.getImei() != null) {
                            tagList.add(new AppListTag(context.getString(R.string.hook_framework), AppListTag.Type.HOOK_FRAMEWORK));
                            break;
                        }

                    break;

            }
        }
        if (tagList.size() == 0) {
            tagList.add(new AppListTag(context.getString(R.string.xposedmodule), AppListTag.Type.XPOSEDMODULE));
        }

        return tagList;
    }

    private List<TagAdapter.Tag> getPrecipitationTagList(Weather weather) {
        int precipitationCount = 0;
        for (Daily d : weather.getDailyForecast()) {
            if ((d.day().getWeatherCode().isPrecipitation() || d.night().getWeatherCode().isPrecipitation())
                    && (d.day().getPrecipitation().isValid() || d.night().getPrecipitation().isValid())) {
                precipitationCount ++;
            }
        }
        if (precipitationCount < 3) {
            return new ArrayList<>();
        } else {
            List<TagAdapter.Tag> list = new ArrayList<>();
            list.add(new AppListTag(context.getString(R.string.xposedmodule), AppListTag.Type.XPOSEDMODULE));
            return list;
        }
    }
}