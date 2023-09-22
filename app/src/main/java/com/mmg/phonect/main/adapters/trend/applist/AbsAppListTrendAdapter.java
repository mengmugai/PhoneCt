package com.mmg.phonect.main.adapters.trend.applist;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.core.widget.ImageViewCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.mmg.phonect.R;
import com.mmg.phonect.common.basic.GeoActivity;
import com.mmg.phonect.common.basic.models.Location;
import com.mmg.phonect.common.basic.models.Phone;
import com.mmg.phonect.common.ui.widgets.trend.TrendRecyclerViewAdapter;
import com.mmg.phonect.main.adapters.AppListAdapter;
import com.mmg.phonect.main.adapters.DeviceAdapter;
//import com.mmg.phonect.main.dialogs.HourlyWeatherDialog;
import com.mmg.phonect.main.utils.MainThemeColorProvider;

import java.util.ArrayList;
import java.util.List;
import java.util.TimeZone;

public abstract class AbsAppListTrendAdapter extends RecyclerView.Adapter<AbsAppListTrendAdapter.ViewHolder>  {

//    extends RecyclerView.Adapter<DeviceAdapter.ViewHolder>

//    private final GeoActivity mActivity;


    final boolean mLightTheme;
    final List<Index> mIndexList;

    static class Index {
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

    public static class ViewHolder extends RecyclerView.ViewHolder {


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

    public AbsAppListTrendAdapter(Context context, Phone phone) {
//        super(location);
//        mActivity = activity;
        mIndexList = new ArrayList<>();
        mLightTheme = MainThemeColorProvider.isLightTheme(context, phone);
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

    protected static void onItemClicked(GeoActivity activity,
                                        Location location,
                                        int adapterPosition) {
        if (activity.isActivityResumed()) {
//            HourlyWeatherDialog.show(
//                    activity,
//                    location.getWeather().getHourlyForecast().get(adapterPosition)
//            );
        }
    }

//    public GeoActivity getActivity() {
//        return mActivity;
//    }
}