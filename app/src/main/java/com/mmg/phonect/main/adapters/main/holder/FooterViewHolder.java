package com.mmg.phonect.main.adapters.main.holder;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.interpolator.view.animation.FastOutSlowInInterpolator;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import com.mmg.phonect.R;
import com.mmg.phonect.common.basic.models.Location;
import com.mmg.phonect.common.utils.helpers.IntentHelper;
import com.mmg.phonect.theme.ThemeManager;
import com.mmg.phonect.theme.resource.providers.ResourceProvider;

public class FooterViewHolder extends AbstractMainViewHolder {

    private final TextView mTitle;
    private final Button mEditButton;

    public FooterViewHolder(ViewGroup parent) {
        super(
                LayoutInflater
                        .from(parent.getContext())
                        .inflate(R.layout.container_main_footer, parent, false)
        );

        mTitle = itemView.findViewById(R.id.container_main_footer_title);
        mEditButton = itemView.findViewById(R.id.container_main_footer_editButton);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindView(Context context, @NonNull Location location, @NonNull ResourceProvider provider,
                           boolean listAnimationEnabled, boolean itemAnimationEnabled) {
        super.onBindView(context, location, provider, listAnimationEnabled, itemAnimationEnabled);

        float cardMarginsVertical = ThemeManager
                .getInstance(context)
                .getPhoneCtThemeDelegate()
                .getHomeCardMargins(context);
        ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) itemView.getLayoutParams();
        if (cardMarginsVertical != 0) {
            params.setMargins(0, (int) -cardMarginsVertical, 0, 0);
        }
        itemView.setLayoutParams(params);

        mTitle.setTextColor(
                ThemeManager
                        .getInstance(context)
                        .getPhoneCtThemeDelegate()
                        .getHeaderTextColor(mTitle.getContext())
        );
        mTitle.setText("* Powered by " + location.getWeatherSource().getSourceUrl());

        mEditButton.setTextColor(
                ThemeManager
                        .getInstance(context)
                        .getPhoneCtThemeDelegate()
                        .getHeaderTextColor(mTitle.getContext())
        );
        mEditButton.setOnClickListener(v ->
                IntentHelper.startCardDisplayManageActivity((Activity) context)
        );
    }

    @NotNull
    @Override
    protected Animator getEnterAnimator(List<Animator> pendingAnimatorList) {
        Animator a = ObjectAnimator.ofFloat(itemView, "alpha", 0f, 1f);
        a.setDuration(450);
        a.setInterpolator(new FastOutSlowInInterpolator());
        a.setStartDelay(pendingAnimatorList.size() * 150L);
        return a;
    }
}
