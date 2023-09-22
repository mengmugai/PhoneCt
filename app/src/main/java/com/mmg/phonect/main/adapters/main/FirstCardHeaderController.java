package com.mmg.phonect.main.adapters.main;

import android.annotation.SuppressLint;
import android.content.res.ColorStateList;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextClock;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.core.widget.ImageViewCompat;

import java.text.DateFormat;
import java.util.Date;
import java.util.TimeZone;

import com.mmg.phonect.R;
import com.mmg.phonect.common.basic.GeoActivity;
import com.mmg.phonect.common.basic.models.Phone;
import com.mmg.phonect.common.basic.models.weather.Base;
import com.mmg.phonect.common.basic.models.weather.Device;
import com.mmg.phonect.common.basic.models.weather.Weather;
import com.mmg.phonect.main.MainActivity;
import com.mmg.phonect.main.utils.MainThemeColorProvider;

public class FirstCardHeaderController{

    private final GeoActivity mActivity;
    private final View mView;
//    private final String mFormattedId;

    private @Nullable LinearLayout mContainer;

    @SuppressLint({"SetTextI18n", "InflateParams"})
    public FirstCardHeaderController(@NonNull GeoActivity activity, @NonNull Phone phone) {
        mActivity = activity;
        mView = LayoutInflater.from(activity).inflate(R.layout.container_main_first_card_header, null);
//        mFormattedId = phone.getFormattedId();

        AppCompatImageView timeIcon = mView.findViewById(R.id.container_main_first_card_header_timeIcon);
        TextView refreshTime = mView.findViewById(R.id.container_main_first_card_header_timeText);
        TextClock localTime = mView.findViewById(R.id.container_main_first_card_header_localTimeText);
        TextView alert = mView.findViewById(R.id.container_main_first_card_header_alert);
        View line = mView.findViewById(R.id.container_main_first_card_header_line);

        if (phone.getDevice() != null) {
            Device device = phone.getDevice();

            mView.setOnClickListener(v -> ((MainActivity) activity).setManagementFragmentVisibility(true));
            // 此处可以判断设备是否处于风险状态   没风险则显示时间图标  有风险展示感叹号
            if (0 == 0) {
                timeIcon.setEnabled(false);
                timeIcon.setImageResource(R.drawable.ic_time);
            } else {
                timeIcon.setEnabled(true);
                timeIcon.setImageResource(R.drawable.ic_alert);
            }
            timeIcon.setContentDescription(
                    activity.getString(R.string.content_desc_weather_alert_button)
                            .replace("$", "" + "这里展示风险条数")
            );
            ImageViewCompat.setImageTintList(
                    timeIcon,
                    ColorStateList.valueOf(
                            MainThemeColorProvider.getColor(phone, R.attr.colorTitleText)
                    )
            );
//            timeIcon.setOnClickListener(this);
            // todo: 这里应该是更新时间  不该是当前时间
            refreshTime.setText(
                    activity.getString(R.string.refresh_at)
                            + " "
                            + Base.getTime(activity, new Date())
            );
            refreshTime.setTextColor(MainThemeColorProvider.getColor(phone, R.attr.colorTitleText));

            long time = System.currentTimeMillis();
            if (TimeZone.getDefault().getOffset(time) == phone.getTimeZone().getOffset(time)) {
                // same time zone.
                localTime.setVisibility(View.GONE);
            } else {
                localTime.setVisibility(View.VISIBLE);
                localTime.setTimeZone(phone.getTimeZone().getID());
                localTime.setFormat12Hour(
                        activity.getString(R.string.date_format_widget_long) + ", h:mm aa"
                );
                localTime.setFormat24Hour(
                        activity.getString(R.string.date_format_widget_long) + ", HH:mm"
                );
                localTime.setTextColor(MainThemeColorProvider.getColor(phone, R.attr.colorCaptionText));
            }

            if (0 == 0) {
                alert.setVisibility(View.GONE);
                line.setVisibility(View.GONE);
            } else {
                alert.setVisibility(View.VISIBLE);
                StringBuilder builder = new StringBuilder();
//                for (int i = 0; i < device.getAndroidid().toString().size(); i ++) {
//                    builder.append(device.getAlertList().get(i).getDescription())
//                            .append(", ")
//                            .append(
//                                    DateFormat.getDateTimeInstance(
//                                            DateFormat.LONG,
//                                            DateFormat.DEFAULT
//                                    ).format(device.getAlertList().get(i).getDate())
//                            );
//                    if (i != device.getAlertList().size() - 1) {
//                        builder.append("\n");
//                    }
//                }
                builder.append("这里应该是各个检测出现的问题  比如： 1，xxx \n 2,xxx");
                alert.setText(builder.toString());
                alert.setTextColor(MainThemeColorProvider.getColor(phone, R.attr.colorBodyText));

                line.setVisibility(View.VISIBLE);
                line.setBackgroundColor(MainThemeColorProvider.getColor(phone, R.attr.colorSurface));
            }
//            alert.setOnClickListener(this);
        }
    }

    public void bind(LinearLayout firstCardContainer) {
        mContainer = firstCardContainer;
        mContainer.addView(mView, 0);
    }

    public void unbind() {
        if (mContainer != null) {
            mContainer.removeViewAt(0);
            mContainer = null;
        }
    }

    // interface.
    //第一个卡片上方更新时间的按钮   原本是跳到选择城市界面
//    @SuppressLint("NonConstantResourceId")
//    @Override
//    public void onClick(View v) {
//        switch (v.getId()) {
//            case R.id.container_main_first_card_header_timeIcon:
//            case R.id.container_main_first_card_header_alert:
//                IntentHelper.startAlertActivity(mActivity, mFormattedId);
//                break;
//        }
//    }
}
