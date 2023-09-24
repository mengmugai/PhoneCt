package com.mmg.phonect.main.adapters.main.holder;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.mmg.phonect.R;
import com.mmg.phonect.common.basic.GeoActivity;
import com.mmg.phonect.common.basic.models.Phone;
import com.mmg.phonect.main.adapters.AbnormalAdapter;
import com.mmg.phonect.main.adapters.DeviceAdapter;
import com.mmg.phonect.theme.ThemeManager;
import com.mmg.phonect.theme.resource.providers.ResourceProvider;
import com.mmg.phonect.theme.weatherView.WeatherViewController;

public class AbnormalViewHolder extends AbstractMainCardViewHolder {

    private final TextView mTitle;
    private final RecyclerView mDeviceRecyclerView;

    public AbnormalViewHolder(ViewGroup parent) {
        super(
                LayoutInflater
                        .from(parent.getContext())
                        .inflate(R.layout.container_main_device, parent, false)
        );

        mTitle = itemView.findViewById(R.id.container_main_details_device);
        mDeviceRecyclerView = itemView.findViewById(R.id.container_main_device_recyclerView);
    }

    @Override
    public void onBindView(GeoActivity activity, @NonNull Phone phone, @NonNull ResourceProvider provider,
                           boolean listAnimationEnabled, boolean itemAnimationEnabled, boolean firstCard) {
        super.onBindView(activity, phone, provider,
                listAnimationEnabled, itemAnimationEnabled, firstCard);

        if (phone.getDevice() != null) {
            mTitle.setTextColor(
                    ThemeManager
                            .getInstance(context)
                            .getPhoneCtThemeDelegate()
                            .getThemeColors(
                                    context,
                                    WeatherViewController.getWeatherKind(phone.getDevice()),
                                    phone.isDaylight()
                            )[0]
            );
            Log.d("mmg", "onBindView: Abnormal");

            mDeviceRecyclerView.setLayoutManager(new LinearLayoutManager(context));
            mDeviceRecyclerView.setAdapter(new AbnormalAdapter(context, phone));
        }
    }
}
