package com.mmg.phonect.main.adapters.main.holder;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.interpolator.view.animation.FastOutSlowInInterpolator;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import io.reactivex.disposables.Disposable;
import com.mmg.phonect.R;
import com.mmg.phonect.common.basic.models.Phone;
import com.mmg.phonect.common.basic.models.options.unit.TemperatureUnit;
import com.mmg.phonect.common.ui.widgets.NumberAnimTextView;
import com.mmg.phonect.settings.SettingsManager;
import com.mmg.phonect.theme.ThemeManager;
import com.mmg.phonect.theme.resource.providers.ResourceProvider;
import com.mmg.phonect.theme.weatherView.WeatherView;

public class HeaderViewHolder extends AbstractMainViewHolder {

    private final LinearLayout mContainer;
    private final NumberAnimTextView mTemperature;
    private final TextView mWeather;
    private final TextView mAqiOrWind;

    private int mTemperatureCFrom;
    private int mTemperatureCTo;
    private TemperatureUnit mTemperatureUnit;
    private @Nullable Disposable mDisposable;

    public HeaderViewHolder(ViewGroup parent, WeatherView weatherView) {
        super(
                LayoutInflater
                        .from(parent.getContext())
                        .inflate(R.layout.container_main_header, parent, false)
        );

        mContainer = itemView.findViewById(R.id.container_main_header);
        mTemperature = itemView.findViewById(R.id.container_main_header_tempTxt);
        mWeather = itemView.findViewById(R.id.container_main_header_weatherTxt);
        mAqiOrWind = itemView.findViewById(R.id.container_main_header_aqiOrWindTxt);

        mTemperatureCFrom = 0;
        mTemperatureCTo = 0;
        mTemperatureUnit = null;
        mDisposable = null;

        mContainer.setOnClickListener(v -> weatherView.onClick());
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindView(Context context, @NonNull Phone phone, @NonNull ResourceProvider provider,
                           boolean listAnimationEnabled, boolean itemAnimationEnabled) {
        super.onBindView(context, phone, provider, listAnimationEnabled, itemAnimationEnabled);

        ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) mContainer.getLayoutParams();
        params.height = ThemeManager
                .getInstance(context)
                .getPhoneCtThemeDelegate()
                .getHeaderHeight(context);
        mContainer.setLayoutParams(params);

        int textColor = ThemeManager
                .getInstance(context)
                .getPhoneCtThemeDelegate()
                .getHeaderTextColor(context);
        mTemperature.setTextColor(textColor);
        mWeather.setTextColor(textColor);
        mAqiOrWind.setTextColor(textColor);

        mTemperatureUnit = SettingsManager.getInstance(context).getTemperatureUnit();
        if (phone.getDevice() != null) {
            mTemperatureCFrom = mTemperatureCTo;
            mTemperatureCTo = phone.getDevice().getScore();

            mTemperature.setEnableAnim(itemAnimationEnabled);
            mTemperature.setDuration(
                    (long) Math.min(
                            2000, // no longer than 2 seconds.
                            Math.abs(mTemperatureCTo - mTemperatureCFrom) / 10f * 1000
                    )
            );
            mTemperature.setPostfixString(mTemperatureUnit.getShortName(context));
            String disease = "满分100分   ";
            if(mTemperatureCTo == 100){
                disease += "设备没有检查出异常  可以出院了";
            }
            else if (mTemperatureCTo< 100 && mTemperatureCTo>80) {
                disease += "你有点不正常哦~";
            }else {
                disease += "你这有点病危了！！！！";
            }

//            StringBuilder title = new StringBuilder(phone.getBrand() + "暂时就这");
            StringBuilder title = new StringBuilder(disease);
//            if (phone.getModel() != null) {
//                title.append(", ")
//                        .append(context.getString(R.string.feels_like))
//                        .append(" ")
//                        .append(phone.getModel());
//            }
            mWeather.setText(title.toString());

            if (phone.getBrand() == null) {
                mAqiOrWind.setText(
                        context.getString(R.string.wind)
                                + " - "
                                + phone.getBrand()
                );
            }
            itemView.setContentDescription(phone.getBrand()
                    + ", " + phone.getModel()
                    + ", " + mWeather.getText()
                    + ", " + mAqiOrWind.getText());
        }
    }

    @NotNull
    @Override
    protected Animator getEnterAnimator(List<Animator> pendingAnimatorList) {
        Animator a = ObjectAnimator.ofFloat(itemView, "alpha", 0f, 1f);
        a.setDuration(300);
        a.setStartDelay(100);
        a.setInterpolator(new FastOutSlowInInterpolator());
        return a;
    }

    @SuppressLint("DefaultLocale")
    @Override
    public void onEnterScreen() {
        super.onEnterScreen();
        mTemperature.setNumberString(
                String.format("%d", mTemperatureUnit.getValueWithoutUnit(mTemperatureCFrom)),
                String.format("%d", mTemperatureUnit.getValueWithoutUnit(mTemperatureCTo))
        );
    }

    @Override
    public void onRecycleView() {
        if (mDisposable != null) {
            mDisposable.dispose();
            mDisposable = null;
        }
    }

    public int getCurrentTemperatureHeight() {
        return mContainer.getMeasuredHeight() - mTemperature.getTop();
    }
}
