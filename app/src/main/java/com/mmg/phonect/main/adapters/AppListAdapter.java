package com.mmg.phonect.main.adapters;

import com.mmg.phonect.device.info.AppListInfo;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.core.widget.ImageViewCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.mmg.phonect.R;
import com.mmg.phonect.common.basic.models.Location;
import com.mmg.phonect.common.basic.models.Phone;
import com.mmg.phonect.common.basic.models.options.unit.CloudCoverUnit;
import com.mmg.phonect.common.basic.models.options.unit.SpeedUnit;
import com.mmg.phonect.common.basic.models.weather.Weather;
import com.mmg.phonect.common.basic.models.weather.XposedModule;
import com.mmg.phonect.main.utils.MainThemeColorProvider;
import com.mmg.phonect.settings.SettingsManager;

import java.util.ArrayList;
import java.util.List;

public class AppListAdapter extends RecyclerView.Adapter<AppListAdapter.ViewHolder> {

    private final boolean mLightTheme;
    private final List<Index> mIndexList;

    private static class Index {
        Drawable icon;
        String title;
        String content;
        String talkBack;

        Index( Drawable icon, String title, String content) {
            this.icon = icon;
            this.title = title;
            this.content = content;
            this.talkBack = title + ", " + content;
        }

        Index(Drawable icon, String title, String content, String talkBack) {
            this.icon = icon;
            this.title = title;
            this.content = content;
            this.talkBack = talkBack;
        }
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        private final ImageView mIcon;
        private final TextView mTitle;
        private final TextView mContent;

        ViewHolder(View itemView) {
            super(itemView);
            mIcon = itemView.findViewById(R.id.item_apps_icon);
            mTitle = itemView.findViewById(R.id.item_apps_title);
            mContent = itemView.findViewById(R.id.item_apps_content);
        }

        void onBindView(boolean lightTheme, Index index) {
            itemView.setContentDescription(index.talkBack);

            mIcon.setImageDrawable(index.icon);
            mTitle.setText(index.title);
            mContent.setText(index.content);

//            ImageViewCompat.setImageTintList(
//                    mIcon,
//                    ColorStateList.valueOf(
//                            MainThemeColorProvider.getColor(lightTheme, R.attr.colorTitleText)
//                    )
//            );
            mTitle.setTextColor(
                    MainThemeColorProvider.getColor(lightTheme, R.attr.colorTitleText)
            );
            mContent.setTextColor(
                    MainThemeColorProvider.getColor(lightTheme, R.attr.colorBodyText)
            );
        }
    }

    public AppListAdapter(Context context, Phone phone) {
        mLightTheme = MainThemeColorProvider.isLightTheme(context, phone);

        mIndexList = new ArrayList<>();
        SettingsManager settings = SettingsManager.getInstance(context);
//        SpeedUnit speedUnit = settings.getSpeedUnit();
//        Weather weather = phone.getWeather();
//        assert weather != null;

        List<XposedModule> list = AppListInfo.getAppListInfo(context);
        for (XposedModule bean : list) {
            try {
                mIndexList.add(
                        new Index(
                                bean.getIcon(),
                                bean.getName(),
                                bean.getPackageName(),
                                context.getString(R.string.wind)
                                        + ", " + bean.getName()
                                        + ", " + bean.getPackageName().replace("\n", ", ")
                        )
                );

            } catch (Exception e) {
                e.printStackTrace();
            }
        }



    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_apps, parent, false);
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