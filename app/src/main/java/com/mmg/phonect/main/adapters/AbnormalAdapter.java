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
import com.mmg.phonect.common.basic.models.weather.Disease;
import com.mmg.phonect.main.utils.MainThemeColorProvider;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

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

//        Log.d("mmg", "AbnormalAdapter: "+phone.getDevice().getBootid());
        List<Disease> allDiseaseInfo= phone.getDevice().getAllDiseaseInfo();
        Map<String, Integer> drawableMap = new HashMap<>();
        drawableMap.put("root", R.drawable.ic_root);
        drawableMap.put("frida", R.drawable.ic_frida);
        drawableMap.put("TracerPid", R.drawable.ic_tracerpid);
        drawableMap.put("xposed", R.drawable.ic_xposed);
        drawableMap.put("VM", R.drawable.ic_vm);
        drawableMap.put("调试状态", R.drawable.ic_usbdebug);
        drawableMap.put("OEM锁", R.drawable.ic_oem);
        drawableMap.put("网络代理", R.drawable.ic_vpn);
//        drawableMap.put("riru", R.drawable.ic_vpn);
        drawableMap.put("环境", R.drawable.ic_env);
        drawableMap.put("多开", R.drawable.ic_dual);


        for (Disease disease : allDiseaseInfo) {
            try {
                String diseaseName = disease.getDiseaseName();
                String diseaseInfo = disease.getDiseaseInfo();
                mIndexList.add(
                        new Index(
                                drawableMap.get(diseaseName),
                                diseaseName,
                                diseaseInfo
                        )
                );
            }catch (Exception exception){
                Log.d("mmg",exception.getMessage()+disease.getDiseaseName());
            }


        }

//        if (phone.getDevice() != null) {
//
//            String windTitle = "root";
//            String windContent = phone.getDevice().getRootCheck();
//            mIndexList.add(
//                    new Index(
//                            R.drawable.ic_root,
//                            windTitle,
//                            windContent
//                    )
//            );
//            mIndexList.add(
//                    new Index(
//                            R.drawable.ic_frida,
//                            "frida",
//                            phone.getDevice().getFridaCheck()
//
//                    )
//            );
//            // todo: 这里都该改成R.string 的形式
//            mIndexList.add(
//                    new Index(
//                            R.drawable.ic_tracerpid,
//                            "TracerPid",
//                            phone.getDevice().getTracerPid()
//                    )
//            );
//            mIndexList.add(
//                    new Index(
//                            R.drawable.ic_xposed,
//                            "xposed",
//                            phone.getDevice().getXposedCheck()
//                    )
//            );
//            mIndexList.add(
//                    new Index(
//                            R.drawable.ic_vm,
//                            "VM",
//                            phone.getDevice().getVmCheck()
//                    )
//            );
//            mIndexList.add(
//                    new Index(
//                            R.drawable.ic_oem,
//                            "OEM锁",
//                            phone.getDevice().getDeviceLock()
//                    )
//            );
//            mIndexList.add(
//                    new Index(
//                            R.drawable.ic_usbdebug,
//                            "调试状态",
//                            phone.getDevice().getUsbDebugStatus()
//                    )
//            );
//        }


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