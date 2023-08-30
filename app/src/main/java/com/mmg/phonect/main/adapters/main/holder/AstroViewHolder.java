package com.mmg.phonect.main.adapters.main.holder;

import android.animation.AnimatorSet;
import android.animation.FloatEvaluator;
import android.animation.TypeEvaluator;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.OvershootInterpolator;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.Size;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.ColorUtils;

import java.util.Calendar;
import java.util.TimeZone;

import com.mmg.phonect.R;
import com.mmg.phonect.common.basic.GeoActivity;
import com.mmg.phonect.common.basic.models.Location;
import com.mmg.phonect.common.basic.models.weather.Daily;
import com.mmg.phonect.common.basic.models.weather.Weather;
import com.mmg.phonect.common.ui.widgets.astro.MoonPhaseView;
import com.mmg.phonect.common.ui.widgets.astro.SunMoonView;
import com.mmg.phonect.main.utils.MainThemeColorProvider;
import com.mmg.phonect.theme.ThemeManager;
import com.mmg.phonect.theme.resource.ResourceHelper;
import com.mmg.phonect.theme.resource.providers.ResourceProvider;
import com.mmg.phonect.theme.weatherView.WeatherViewController;

public class AstroViewHolder extends AbstractMainCardViewHolder {

    private final TextView mTitle;
    private final TextView mPhaseText;
    private final MoonPhaseView mPhaseView;
    private final SunMoonView mSunMoonView;

    private final RelativeLayout mSunContainer;
    private final TextView mSunTxt;
    private final RelativeLayout mMoonContainer;
    private final TextView mMoonTxt;

    @Nullable private Weather mWeather;
    private TimeZone mTimeZone;

    @Size(2) private long[] mStartTimes;
    @Size(2) private long[] mEndTimes;
    @Size(2) private long[] mCurrentTimes;
    @Size(2) private long[] mAnimCurrentTimes;
    private int mPhaseAngle;

    @Size(3) private final AnimatorSet[] mAttachAnimatorSets;

    public AstroViewHolder(ViewGroup parent) {
        super(
                LayoutInflater
                        .from(parent.getContext())
                        .inflate(R.layout.container_main_sun_moon, parent, false)
        );

        mTitle = itemView.findViewById(R.id.container_main_sun_moon_title);
        mPhaseText = itemView.findViewById(R.id.container_main_sun_moon_phaseText);
        mPhaseView = itemView.findViewById(R.id.container_main_sun_moon_phaseView);
        mSunMoonView = itemView.findViewById(R.id.container_main_sun_moon_controlView);
        mSunContainer = itemView.findViewById(R.id.container_main_sun_moon_sunContainer);
        mSunTxt = itemView.findViewById(R.id.container_main_sun_moon_sunrise_sunset);
        mMoonContainer = itemView.findViewById(R.id.container_main_sun_moon_moonContainer);
        mMoonTxt = itemView.findViewById(R.id.container_main_sun_moon_moonrise_moonset);

        mAttachAnimatorSets = new AnimatorSet[] {null, null, null};
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindView(GeoActivity activity, @NonNull Location location,
                           @NonNull ResourceProvider provider,
                           boolean listAnimationEnabled, boolean itemAnimationEnabled, boolean firstCard) {
        super.onBindView(activity, location, provider,
                listAnimationEnabled, itemAnimationEnabled, firstCard);

        mWeather = location.getWeather();
        mTimeZone = location.getTimeZone();
        assert mWeather != null;

        int[] themeColors = ThemeManager
                .getInstance(context)
                .getPhoneCtThemeDelegate()
                .getThemeColors(
                        context,
                        WeatherViewController.getWeatherKind(location.getWeather()),
                        location.isDaylight()
                );
        mTitle.setTextColor(themeColors[0]);

        StringBuilder talkBackBuilder = new StringBuilder(mTitle.getText());

        ensureTime(mWeather);
        ensurePhaseAngle(mWeather);

        if (!mWeather.getDailyForecast().get(0).getMoonPhase().isValid()) {
            mPhaseText.setVisibility(View.GONE);
            mPhaseView.setVisibility(View.GONE);
        } else {
            mPhaseText.setVisibility(View.VISIBLE);
            mPhaseView.setVisibility(View.VISIBLE);

            mPhaseText.setTextColor(MainThemeColorProvider.getColor(location, R.attr.colorBodyText));
            mPhaseView.setColor(
                    ContextCompat.getColor(context, R.color.colorTextLight2nd),
                    ContextCompat.getColor(context, R.color.colorTextDark2nd),
                    MainThemeColorProvider.getColor(location, R.attr.colorBodyText)
            );

            mPhaseText.setText(mWeather.getDailyForecast().get(0).getMoonPhase().getMoonPhase(context));
            talkBackBuilder.append(", ").append(mPhaseText.getText());
        }

        mSunMoonView.setSunDrawable(ResourceHelper.getSunDrawable(provider));
        mSunMoonView.setMoonDrawable(ResourceHelper.getMoonDrawable(provider));

        if (MainThemeColorProvider.isLightTheme(context, location)) {
            mSunMoonView.setColors(
                    themeColors[0],
                    ColorUtils.setAlphaComponent(themeColors[1], (int) (0.66 * 255)),
                    ColorUtils.setAlphaComponent(themeColors[1], (int) (0.33 * 255)),
                    MainThemeColorProvider.getColor(location, R.attr.colorMainCardBackground),
                    true
            );
        } else {
            mSunMoonView.setColors(
                    themeColors[2],
                    ColorUtils.setAlphaComponent(themeColors[2], (int) (0.5 * 255)),
                    ColorUtils.setAlphaComponent(themeColors[2], (int) (0.2 * 255)),
                    MainThemeColorProvider.getColor(location, R.attr.colorMainCardBackground),
                    false
            );
        }

        if (itemAnimationEnabled) {
            mSunMoonView.setTime(mStartTimes, mEndTimes, mStartTimes);
            mSunMoonView.setDayIndicatorRotation(0);
            mSunMoonView.setNightIndicatorRotation(0);
            mPhaseView.setSurfaceAngle(0);
        } else {
            mSunMoonView.post(() -> mSunMoonView.setTime(mStartTimes, mEndTimes, mCurrentTimes));
            mSunMoonView.setDayIndicatorRotation(0);
            mSunMoonView.setNightIndicatorRotation(0);
            mPhaseView.setSurfaceAngle(mPhaseAngle);
        }

        if (mWeather.getDailyForecast().get(0).sun().isValid()) {
            String sunriseTime = mWeather.getDailyForecast().get(0).sun().getRiseTime(context, mTimeZone);
            String sunsetTime = mWeather.getDailyForecast().get(0).sun().getSetTime(context, mTimeZone);

            mSunContainer.setVisibility(View.VISIBLE);
            mSunTxt.setText(sunriseTime + "↑" + "\n" + sunsetTime + "↓");

            assert sunriseTime != null && sunsetTime != null;
            talkBackBuilder
                    .append(", ")
                    .append(activity.getString(R.string.content_des_sunrise).replace("$", sunriseTime))
                    .append(", ")
                    .append(activity.getString(R.string.content_des_sunset).replace("$", sunsetTime));
        } else {
            mSunContainer.setVisibility(View.GONE);
        }
        if (mWeather.getDailyForecast().get(0).moon().isValid()) {
            String moonriseTime = mWeather.getDailyForecast().get(0).moon().getRiseTime(context, mTimeZone);
            String moonsetTime = mWeather.getDailyForecast().get(0).moon().getSetTime(context, mTimeZone);

            mMoonContainer.setVisibility(View.VISIBLE);
            mMoonTxt.setText(moonriseTime + "↑" + "\n" + moonsetTime + "↓");

            assert moonriseTime != null && moonsetTime != null;
            talkBackBuilder
                    .append(", ")
                    .append(activity.getString(R.string.content_des_moonrise).replace("$", moonriseTime))
                    .append(", ")
                    .append(activity.getString(R.string.content_des_moonset).replace("$", moonsetTime));
        } else {
            mMoonContainer.setVisibility(View.GONE);
        }

        itemView.setContentDescription(talkBackBuilder.toString());
    }

    private static class LongEvaluator implements TypeEvaluator<Long> {

        @Override
        public Long evaluate(float fraction, Long startValue, Long endValue) {
            return startValue + (long) ((endValue - startValue) * fraction);
        }
    }

    @SuppressLint("Recycle")
    @Override
    public void onEnterScreen() {
        if (itemAnimationEnabled && mWeather != null) {
            ValueAnimator timeDay = ValueAnimator.ofObject(new LongEvaluator(), mStartTimes[0], mCurrentTimes[0]);
            timeDay.addUpdateListener(animation -> {
                mAnimCurrentTimes[0] = (Long) animation.getAnimatedValue();
                mSunMoonView.setTime(mStartTimes, mEndTimes, mAnimCurrentTimes);
            });

            double totalRotationDay = 360.0 * 7 * (mCurrentTimes[0] - mStartTimes[0]) / (mEndTimes[0] - mStartTimes[0]);
            ValueAnimator rotateDay = ValueAnimator.ofObject(
                    new FloatEvaluator(), 0, (int) (totalRotationDay - totalRotationDay % 360)
            );
            rotateDay.addUpdateListener(animation ->
                    mSunMoonView.setDayIndicatorRotation((Float) animation.getAnimatedValue())
            );

            mAttachAnimatorSets[0] = new AnimatorSet();
            mAttachAnimatorSets[0].playTogether(timeDay, rotateDay);
            mAttachAnimatorSets[0].setInterpolator(new OvershootInterpolator(1f));
            mAttachAnimatorSets[0].setDuration(getPathAnimatorDuration(0));
            mAttachAnimatorSets[0].start();

            ValueAnimator timeNight = ValueAnimator.ofObject(new LongEvaluator(), mStartTimes[1], mCurrentTimes[1]);
            timeNight.addUpdateListener(animation -> {
                mAnimCurrentTimes[1] = (Long) animation.getAnimatedValue();
                mSunMoonView.setTime(mStartTimes, mEndTimes, mAnimCurrentTimes);
            });

            double totalRotationNight = 360.0 * 4 * (mCurrentTimes[1] - mStartTimes[1]) / (mEndTimes[1] - mStartTimes[1]);
            ValueAnimator rotateNight = ValueAnimator.ofObject(
                    new FloatEvaluator(), 0, (int) (totalRotationNight - totalRotationNight % 360)
            );
            rotateNight.addUpdateListener(animation ->
                    mSunMoonView.setNightIndicatorRotation(-1 * (Float) animation.getAnimatedValue())
            );

            mAttachAnimatorSets[1] = new AnimatorSet();
            mAttachAnimatorSets[1].playTogether(timeNight, rotateNight);
            mAttachAnimatorSets[1].setInterpolator(new OvershootInterpolator(1f));
            mAttachAnimatorSets[1].setDuration(getPathAnimatorDuration(1));
            mAttachAnimatorSets[1].start();

            if (mPhaseAngle > 0) {
                ValueAnimator moonAngle = ValueAnimator.ofObject(new FloatEvaluator(), 0, mPhaseAngle);
                moonAngle.addUpdateListener(animation ->
                        mPhaseView.setSurfaceAngle((Float) animation.getAnimatedValue())
                );

                mAttachAnimatorSets[2] = new AnimatorSet();
                mAttachAnimatorSets[2].playTogether(moonAngle);
                mAttachAnimatorSets[2].setInterpolator(new DecelerateInterpolator());
                mAttachAnimatorSets[2].setDuration(getPhaseAnimatorDuration());
                mAttachAnimatorSets[2].start();
            }
        }
    }

    @Override
    public void onRecycleView() {
        super.onRecycleView();
        for (int i = 0; i < mAttachAnimatorSets.length; i ++) {
            if (mAttachAnimatorSets[i] != null && mAttachAnimatorSets[i].isRunning()) {
                mAttachAnimatorSets[i].cancel();
            }
            mAttachAnimatorSets[i] = null;
        }
    }

    private void ensureTime(@NonNull Weather weather) {
        Daily today = weather.getDailyForecast().get(0);

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeZone(mTimeZone);
        long currentTime = calendar.getTime().getTime();

        mStartTimes = new long[2];
        mEndTimes = new long[2];
        mCurrentTimes = new long[] {currentTime, currentTime};

        // sun.
        if (today.sun().getRiseDate() == null || today.sun().getSetDate() == null) {
            mStartTimes[0] = currentTime + 1;
            mEndTimes[0] = currentTime + 1;
        } else {
            mStartTimes[0] = today.sun().getRiseDate().getTime();
            mEndTimes[0] = today.sun().getSetDate().getTime();
        }

        // moon.
        if (today.moon().getRiseDate() == null || today.moon().getSetDate() == null) {
            mStartTimes[1] = currentTime + 1;
            mEndTimes[1] = currentTime + 1;
        } else {
            mStartTimes[1] = today.moon().getRiseDate().getTime();
            mEndTimes[1] = today.moon().getSetDate().getTime();
        }

        mAnimCurrentTimes = new long[] {mCurrentTimes[0], mCurrentTimes[1]};
    }

    private void ensurePhaseAngle(@NonNull Weather weather) {
        Integer angle = weather.getDailyForecast().get(0).getMoonPhase().getAngle();
        mPhaseAngle = angle == null ? 0 : angle;
    }

    private long getPathAnimatorDuration(int index) {
        long duration = (long) Math.max(
                1000 + 3000.0
                        * (mCurrentTimes[index] - mStartTimes[index])
                        / (mEndTimes[index] - mStartTimes[index]),
                0
        );
        return Math.min(duration, 4000);
    }

    private long getPhaseAnimatorDuration() {
        long duration = (long) Math.max(0, mPhaseAngle / 360.0 * 1000 + 1000);
        return Math.min(duration, 2000);
    }
}
