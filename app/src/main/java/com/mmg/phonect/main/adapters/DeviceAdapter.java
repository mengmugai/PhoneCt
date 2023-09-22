package com.mmg.phonect.main.adapters;

import android.content.Context;
import android.content.res.ColorStateList;
import android.util.Log;
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
import com.mmg.phonect.common.basic.models.Phone;
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

    public DeviceAdapter(Context context, Phone phone) {
        mLightTheme = MainThemeColorProvider.isLightTheme(context, phone);

        mIndexList = new ArrayList<>();
//        SettingsManager settings = SettingsManager.getInstance(context);
//        Device device = phone.getDevice();
//        assert weather != null;

        Log.d("mmg", "DeviceAdapter: "+phone.getDevice().getBootid());

        if (phone.getDevice() != null) {

            String windTitle = "IMEI";
            String windContent = "imei 1"
                    + " : "
                    + phone.getDevice().getImei()
                    + "\n"
                    + "imei2"
                    + " : "
                    +phone.getDevice().getImei2();
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
            mIndexList.add(
                    new Index(
                            R.drawable.ic_water_percent,
                            context.getString(R.string.android_id),
                            phone.getDevice().getAndroidid()

                    )
            );
            // todo: 这里都该改成R.string 的形式
            mIndexList.add(
                    new Index(
                            R.drawable.ic_gauge,
                            "booid",
                            phone.getDevice().getBootid()
                    )
            );
            mIndexList.add(
                    new Index(
                            R.drawable.ic_alipay,
                            "Serial",
                            phone.getDevice().getSerial()
                    )
            );
            mIndexList.add(
                    new Index(
                            R.drawable.ic_aqi,
                            "Ua",
                            phone.getDevice().getUa()
                    )
            );
            mIndexList.add(
                    new Index(
                            R.drawable.ic_briefing,
                            "meid",
                            "meid:"+phone.getDevice().getMeid()+"\nmeid2:"+phone.getDevice().getMeid2()
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