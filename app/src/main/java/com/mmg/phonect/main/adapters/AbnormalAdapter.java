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

import com.mmg.phonect.R;
import com.mmg.phonect.common.basic.models.Phone;
import com.mmg.phonect.main.utils.MainThemeColorProvider;

import java.util.ArrayList;
import java.util.List;

public class AbnormalAdapter extends RecyclerView.Adapter<AbnormalAdapter.ViewHolder> {

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

    public AbnormalAdapter(Context context, Phone phone) {
        mLightTheme = MainThemeColorProvider.isLightTheme(context, phone);

        mIndexList = new ArrayList<>();
//        SettingsManager settings = SettingsManager.getInstance(context);
//        Device device = phone.getDevice();
//        assert weather != null;

        Log.d("mmg", "AbnormalAdapter: "+phone.getDevice().getBootid());

        if (phone.getDevice() != null) {

            String windTitle = "root";
            String windContent = phone.getDevice().getRootCheck();
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
                            "frida",
                            phone.getDevice().getFridaCheck()

                    )
            );
            // todo: 这里都该改成R.string 的形式
            mIndexList.add(
                    new Index(
                            R.drawable.ic_gauge,
                            "TracerPid",
                            phone.getDevice().getTracerPid()
                    )
            );
            mIndexList.add(
                    new Index(
                            R.drawable.ic_alipay,
                            "xposed",
                            phone.getDevice().getXposedCheck()
                    )
            );
            mIndexList.add(
                    new Index(
                            R.drawable.ic_aqi,
                            "VM",
                            phone.getDevice().getVmCheck()
                    )
            );
            mIndexList.add(
                    new Index(
                            R.drawable.ic_briefing,
                            "设备锁",
                            phone.getDevice().getDeviceLock()
                    )
            );
            mIndexList.add(
                    new Index(
                            R.drawable.ic_cloud,
                            "调试状态",
                            phone.getDevice().getDebugOpen()
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