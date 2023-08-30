package com.mmg.phonect.main.adapters.main.holder;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.mmg.phonect.R;
import com.mmg.phonect.common.basic.GeoActivity;
import com.mmg.phonect.common.basic.models.Location;
import com.mmg.phonect.main.adapters.DeviceAdapter;
import com.mmg.phonect.theme.ThemeManager;
import com.mmg.phonect.theme.resource.providers.ResourceProvider;
import com.mmg.phonect.theme.weatherView.WeatherViewController;

public class DeviceViewHolder extends AbstractMainCardViewHolder {

    private final TextView mTitle;
    private final RecyclerView mDeviceRecyclerView;

    public DeviceViewHolder(ViewGroup parent) {
        super(
                LayoutInflater
                        .from(parent.getContext())
                        .inflate(R.layout.container_main_device, parent, false)
        );

        mTitle = itemView.findViewById(R.id.container_main_details_device);
        mDeviceRecyclerView = itemView.findViewById(R.id.container_main_device_recyclerView);
    }

    @Override
    public void onBindView(GeoActivity activity, @NonNull Location location, @NonNull ResourceProvider provider,
                           boolean listAnimationEnabled, boolean itemAnimationEnabled, boolean firstCard) {
        super.onBindView(activity, location, provider,
                listAnimationEnabled, itemAnimationEnabled, firstCard);

        if (location.getWeather() != null) {
            mTitle.setTextColor(
                    ThemeManager
                            .getInstance(context)
                            .getPhoneCtThemeDelegate()
                            .getThemeColors(
                                    context,
                                    WeatherViewController.getWeatherKind(location.getWeather()),
                                    location.isDaylight()
                            )[0]
            );

            mDeviceRecyclerView.setLayoutManager(new LinearLayoutManager(context));
            mDeviceRecyclerView.setAdapter(new DeviceAdapter(context, location));
        }
    }
}
