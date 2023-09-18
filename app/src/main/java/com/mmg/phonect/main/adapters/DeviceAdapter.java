package com.mmg.phonect.main.adapters;

import android.content.Context;
import android.content.res.ColorStateList;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.core.widget.ImageViewCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import com.mmg.phonect.R;
import com.mmg.phonect.common.basic.models.Location;
import com.mmg.phonect.common.basic.models.options.unit.CloudCoverUnit;
import com.mmg.phonect.common.basic.models.options.unit.RelativeHumidityUnit;
import com.mmg.phonect.common.basic.models.options.unit.SpeedUnit;
import com.mmg.phonect.common.basic.models.weather.Weather;
import com.mmg.phonect.common.basic.models.weather.Device;
import com.mmg.phonect.main.utils.MainThemeColorProvider;
import com.mmg.phonect.settings.SettingsManager;

public class DeviceAdapter extends RecyclerView.Adapter<DeviceAdapter.ViewHolder> {

    private final boolean mLightTheme;
    private final List<Index> mIndexList;

    private static class Index {
        @DrawableRes int iconId;
        String title;
        String content;
        String talkBack;

        Index(@DrawableRes int iconId, String title, String content) {
            this.iconId = iconId;
            this.title = title;
            this.content = content;
            this.talkBack = title + ", " + content;
        }

        Index(@DrawableRes int iconId, String title, String content, String talkBack) {
            this.iconId = iconId;
            this.title = title;
            this.content = content;
            this.talkBack = talkBack;
        }
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        private final AppCompatImageView mIcon;
        private final TextView mTitle;
        private final TextView mContent;

        ViewHolder(View itemView) {
            super(itemView);
            mIcon = itemView.findViewById(R.id.item_details_icon);
            mTitle = itemView.findViewById(R.id.item_details_title);
            mContent = itemView.findViewById(R.id.item_details_content);
        }

        void onBindView(boolean lightTheme, Index index) {
            itemView.setContentDescription(index.talkBack);

            mIcon.setImageResource(index.iconId);
            mTitle.setText(index.title);
            mContent.setText(index.content);

            ImageViewCompat.setImageTintList(
                    mIcon,
                    ColorStateList.valueOf(
                            MainThemeColorProvider.getColor(lightTheme, R.attr.colorTitleText)
                    )
            );
            mTitle.setTextColor(
                    MainThemeColorProvider.getColor(lightTheme, R.attr.colorTitleText)
            );
            mContent.setTextColor(
                    MainThemeColorProvider.getColor(lightTheme, R.attr.colorBodyText)
            );
        }
    }

    public DeviceAdapter(Context context, Location location) {
        mLightTheme = MainThemeColorProvider.isLightTheme(context, location);

        mIndexList = new ArrayList<>();
        SettingsManager settings = SettingsManager.getInstance(context);
        SpeedUnit speedUnit = settings.getSpeedUnit();
        Weather weather = location.getWeather();
        assert weather != null;

        String windTitle = context.getString(R.string.live)
                + " : "
                + weather.getCurrent().getWind().getWindDescription(context, speedUnit);
        String windContent = context.getString(R.string.daytime)
                + " : "
                + weather.getDailyForecast().get(0).day().getWind().getWindDescription(context, speedUnit)
                + "\n"
                + context.getString(R.string.nighttime)
                + " : "
                + weather.getDailyForecast().get(0).night().getWind().getWindDescription(context, speedUnit);
        mIndexList.add(
                new Index(
                        R.drawable.ic_wind,
                        windTitle,
                        windContent,
                        context.getString(R.string.wind)
                                + ", " + windTitle
                                + ", " + windContent.replace("\n", ", ")
                )
        );

        if (weather.getCurrent().getDevice() != null) {
            mIndexList.add(
                    new Index(
                            R.drawable.ic_water_percent,
                            context.getString(R.string.android_id),
                            weather.getCurrent().getDevice().getAndroidid()

                    )
            );
        }

        if (weather.getCurrent().getUV().isValid()) {
            mIndexList.add(
                    new Index(
                            R.drawable.ic_uv,
                            context.getString(R.string.uv_index),
                            weather.getCurrent().getUV().getUVDescription()
                    )
            );
        }

        if (weather.getCurrent().getPressure() != null) {
            mIndexList.add(
                    new Index(
                            R.drawable.ic_gauge,
                            context.getString(R.string.pressure),
                            settings.getPressureUnit().getValueText(context, weather.getCurrent().getPressure()),
                            context.getString(R.string.pressure)
                                    + ", " + settings.getPressureUnit().getValueVoice(context, weather.getCurrent().getPressure())
                    )
            );
        }

        if (weather.getCurrent().getVisibility() != null) {
            mIndexList.add(
                    new Index(
                            R.drawable.ic_eye,
                            context.getString(R.string.visibility),
                            settings.getDistanceUnit().getValueText(context, weather.getCurrent().getVisibility()),
                            context.getString(R.string.visibility)
                                    + ", " + settings.getDistanceUnit().getValueVoice(context, weather.getCurrent().getVisibility())
                    )
            );
        }

        if (weather.getCurrent().getDewPoint() != null) {
            mIndexList.add(
                    new Index(
                            R.drawable.ic_water,
                            context.getString(R.string.dew_point),
                            settings.getTemperatureUnit().getValueText(
                                    context,
                                    weather.getCurrent().getDewPoint()
                            )
                    )
            );
        }

        if (weather.getCurrent().getCloudCover() != null) {
            mIndexList.add(
                    new Index(
                            R.drawable.ic_cloud,
                            context.getString(R.string.cloud_cover),
                            CloudCoverUnit.PERCENT.getValueText(
                                    context,
                                    weather.getCurrent().getCloudCover()
                            )
                    )
            );
        }

        if (weather.getCurrent().getCeiling() != null) {
            mIndexList.add(
                    new Index(
                            R.drawable.ic_top,
                            context.getString(R.string.ceiling),
                            settings.getDistanceUnit().getValueText(
                                    context,
                                    weather.getCurrent().getCeiling()
                            ),
                            context.getString(R.string.ceiling) + ", " + settings.getDistanceUnit().getValueVoice(
                                    context, weather.getCurrent().getCeiling())
                    )
            );
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_details, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.onBindView(mLightTheme, mIndexList.get(position));
    }

    @Override
    public int getItemCount() {
        return mIndexList.size();
    }
}