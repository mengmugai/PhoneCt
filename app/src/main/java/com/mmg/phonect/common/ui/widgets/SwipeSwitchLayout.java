package com.mmg.phonect.common.ui.widgets;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.view.HapticFeedbackConstants;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.NestedScrollingParent2;
import androidx.core.view.NestedScrollingParent3;
import androidx.core.view.ViewCompat;
import androidx.dynamicanimation.animation.DynamicAnimation;
import androidx.dynamicanimation.animation.FloatValueHolder;
import androidx.dynamicanimation.animation.SpringAnimation;
import androidx.dynamicanimation.animation.SpringForce;

public class SwipeSwitchLayout extends FrameLayout
        implements NestedScrollingParent2, NestedScrollingParent3 {

    @Nullable private View mTarget;
    @Nullable private SpringAnimation mResetAnimation;
    @Nullable private OnSwitchListener mSwitchListener;
    @Nullable private OnPagerSwipeListener mPageSwipeListener;

    private int mTotalCount = 1;
    private int mPosition = 0;

    private int mSwipeDistance;
    private int mSwipeTrigger;
    private float mNestedScrollingDistance;
    private float mNestedScrollingTrigger;

    private float mLastX, mLastY;
    private int mTouchSlop;

    private boolean mIsBeingTouched;
    private boolean mIsBeingDragged;
    private boolean mIsHorizontalDragged;
    private boolean mIsBeingNestedScrolling;

    private static final float SWIPE_RATIO = 0.4f;
    private static final float NESTED_SCROLLING_RATIO = SWIPE_RATIO; // 0.075f

    public static final int SWIPE_DIRECTION_LEFT = -1;
    public static final int SWIPE_DIRECTION_RIGHT = 1;

    public interface OnSwitchListener {
        void onSwiped(int swipeDirection, float progress);
        void onSwitched(int swipeDirection);
    }

    public interface OnPagerSwipeListener {
        void onPageScrolled(int position, float positionOffset, int positionOffsetPixels);
        void onPageSelected(int position);
    }

    public SwipeSwitchLayout(Context context) {
        super(context);
        initialize();
    }

    public SwipeSwitchLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        initialize();
    }

    public SwipeSwitchLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initialize();
    }

    // init.

    private void initialize() {
        mTarget = null;
        mSwipeDistance = 0;
        mSwipeTrigger = 500;
        mNestedScrollingDistance = 0;
        mNestedScrollingTrigger = 300;
        mTouchSlop = ViewConfiguration.get(getContext()).getScaledTouchSlop();
    }

    // layout.

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        mSwipeTrigger = getMeasuredWidth() / 5;
        mNestedScrollingTrigger = mSwipeTrigger;
    }

    // touch.

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if (!isEnabled()
                || (ev.getAction() != MotionEvent.ACTION_DOWN && mIsBeingNestedScrolling)) {
            return false;
        }

        if (mTarget == null && getChildCount() > 0) {
            mTarget = getChildAt(0);
        }
        if (mTarget == null) {
            return false;
        }

        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                cancelResetAnimation();

                mIsBeingTouched = true;
                mIsBeingDragged = false;
                mIsHorizontalDragged = false;
                mLastX = ev.getX();
                mLastY = ev.getY();
                break;

            case MotionEvent.ACTION_MOVE:
                if (!mIsBeingTouched) {
                    mIsBeingTouched = true;
                    mLastX = ev.getX();
                    mLastY = ev.getY();
                }

                float x = ev.getX();
                float y = ev.getY();

                if (!mIsBeingDragged && !mIsHorizontalDragged) {
                    if (Math.abs(x - mLastX) > mTouchSlop || Math.abs(y - mLastY) > mTouchSlop) {
                        mIsBeingDragged = true;
                        if (Math.abs(x - mLastX) > Math.abs(y - mLastY)) {
                            mLastX += x > mLastX ? mTouchSlop : -mTouchSlop;
                            mIsHorizontalDragged = true;
                        }
                    }
                }
                break;

            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                mIsBeingTouched = false;
                mIsBeingDragged = false;
                mIsHorizontalDragged = false;
                break;
        }

        return mIsBeingDragged && mIsHorizontalDragged;
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if (!isEnabled() || mIsBeingNestedScrolling) {
            return false;
        }

        if (mTarget == null && getChildCount() > 0) {
            mTarget = getChildAt(0);
        }
        if (mTarget == null) {
            return false;
        }

        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                cancelResetAnimation();

                mIsBeingTouched = true;
                mIsBeingDragged = false;
                mIsHorizontalDragged = false;
                mLastX = ev.getX();
                mLastY = ev.getY();
                break;

            case MotionEvent.ACTION_MOVE:
                if (mIsBeingDragged && mIsHorizontalDragged) {
                    mSwipeDistance += (int) (ev.getX() - mLastX);
                    setTranslation(mSwipeTrigger, SWIPE_RATIO);
                }

                mLastX = ev.getX();
                mLastY = ev.getY();
                break;

            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                mIsBeingTouched = false;
                release(mSwipeTrigger);
                break;
        }

        return true;
    }

    // control.

    public void reset() {
        cancelResetAnimation();

        mIsBeingDragged = false;
        mIsHorizontalDragged = false;
        mSwipeDistance = 0;
        mNestedScrollingDistance = 0;

        setTranslation(mSwipeTrigger, SWIPE_RATIO);
    }

    private void cancelResetAnimation() {
        if (mResetAnimation != null) {
            mResetAnimation.cancel();
            mResetAnimation = null;
        }
    }

    private void setTranslation(int triggerDistance, float translateRatio) {
        float realDistance = mSwipeDistance;
        realDistance = Math.min(realDistance, triggerDistance);
        realDistance = Math.max(realDistance, -triggerDistance);

        int swipeDirection = mSwipeDistance < 0 ? SWIPE_DIRECTION_LEFT : SWIPE_DIRECTION_RIGHT;
        float progress = Math.abs(realDistance) / triggerDistance;

        if (mTarget != null) {
            mTarget.setAlpha(1 - progress);
            mTarget.setTranslationX(
                    (float) (
                            swipeDirection * translateRatio * triggerDistance * Math.log10(
                                    1 + 9.0 * Math.abs(mSwipeDistance) / triggerDistance
                            )
                    )
            );
        }

        if (mSwitchListener != null) {
            mSwitchListener.onSwiped(swipeDirection, progress);
        }
        if (mPageSwipeListener != null) {
            if (mSwipeDistance > 0) {
                mPageSwipeListener.onPageScrolled(
                        mPosition - 1,
                        1 - Math.min(1, 1.f * mSwipeDistance / triggerDistance),
                        Math.max(0, triggerDistance - mSwipeDistance)
                );
            } else {
                mPageSwipeListener.onPageScrolled(
                        mPosition,
                        Math.min(1, -1.f * mSwipeDistance / triggerDistance),
                        Math.min(-mSwipeDistance, triggerDistance)
                );
            }
        }
    }

    private void release(int triggerDistance) {
        int swipeDirection = mSwipeDistance < 0 ? SWIPE_DIRECTION_LEFT : SWIPE_DIRECTION_RIGHT;
        if (Math.abs(mSwipeDistance) > Math.abs(triggerDistance)) {
            setPosition(swipeDirection);
            if (mSwitchListener != null) {
                mSwitchListener.onSwitched(swipeDirection);
            }
            if (mPageSwipeListener != null) {
                mPageSwipeListener.onPageSelected(mPosition);
            }
        } else {
            if (mTarget == null) {
                reset();
                return;
            }

            mResetAnimation = new SpringAnimation(new FloatValueHolder(mSwipeDistance));
            mResetAnimation.setSpring(
                    new SpringForce(0)
                            .setDampingRatio(SpringForce.DAMPING_RATIO_MEDIUM_BOUNCY)
            );
            mResetAnimation.addUpdateListener((animation, value, velocity) -> {
                mSwipeDistance = (int) value;
                setTranslation(mSwipeTrigger, SWIPE_RATIO);
            });
            mResetAnimation.start();
        }
    }

    // interface.

    public void setData(int currentIndex, int pageCount) {
        if (currentIndex < 0 || currentIndex >= pageCount) {
            throw new RuntimeException("Invalid current index.");
        }
        mPosition = currentIndex;
        mTotalCount = pageCount;
    }

    private void setPosition(int swipeDirection) {
        switch (swipeDirection) {
            case SWIPE_DIRECTION_LEFT:
                mPosition++;
                break;

            case SWIPE_DIRECTION_RIGHT:
                mPosition--;
                break;
        }
        if (mPosition < 0) {
            mPosition = mTotalCount - 1;
        } else if (mPosition > mTotalCount - 1) {
            mPosition = 0;
        }
    }

    public int getTotalCount() {
        return mTotalCount;
    }

    public int getPosition() {
        return mPosition;
    }

    // interface.

    public void setOnSwitchListener(OnSwitchListener l) {
        mSwitchListener = l;
    }

    public void setOnPageSwipeListener(OnPagerSwipeListener l) {
        mPageSwipeListener = l;
    }

    // nested scrolling parent.

    @Override
    public boolean onStartNestedScroll(@NonNull View child, @NonNull View target, int axes, int type) {
        if (mTarget == null && getChildCount() > 0) {
            mTarget = getChildAt(0);
        }

        return (axes & ViewCompat.SCROLL_AXIS_HORIZONTAL) != 0
                && mSwitchListener != null
                && type == ViewCompat.TYPE_TOUCH
                && isEnabled()
                && mTarget != null;
    }

    @Override
    public void onNestedScrollAccepted(@NonNull View child, @NonNull View target, int axes, int type) {
        if (!mIsBeingNestedScrolling) {
            mIsBeingNestedScrolling = true;
            if ((!target.canScrollHorizontally(-1) && !target.canScrollHorizontally(1))
                    || mSwipeDistance != 0) {
                mNestedScrollingDistance = mNestedScrollingTrigger;
            } else {
                mNestedScrollingDistance = 0;
            }
        }
    }

    @Override
    public void onStopNestedScroll(@NonNull View target, int type) {
        mIsBeingNestedScrolling = false;
        release(mSwipeTrigger);
    }

    @Override
    public void onNestedPreScroll(@NonNull View target, int dx, int dy, @NonNull int[] consumed, int type) {
        if (mSwipeDistance != 0) {
            if ((mSwipeDistance > 0 && mSwipeDistance - dx < 0)
                    || (mSwipeDistance < 0 && mSwipeDistance - dx > 0)) {
                consumed[0] = mSwipeDistance;
            } else {
                consumed[0] = dx;
            }
            innerNestedScroll(consumed[0]);
        }
    }

    @Override
    public void onNestedScroll(@NonNull View target,
                               int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed,
                               int type, @NonNull int[] consumed) {
        innerNestedScroll(dxUnconsumed);
        consumed[0] += dxUnconsumed;
    }

    @Override
    public void onNestedScroll(@NonNull View target,
                               int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed,
                               int type) {
        innerNestedScroll(dxUnconsumed);
    }

    @Override
    public boolean onNestedPreFling(@NonNull View target, float velocityX, float velocityY) {
        return false;
    }

    @Override
    public boolean onNestedFling(@NonNull View target, float velocityX, float velocityY, boolean consumed) {
        return false;
    }

    private void innerNestedScroll(int dxUnconsumed) {
        if (Math.abs(mNestedScrollingDistance) >= mNestedScrollingTrigger) {
            mSwipeDistance -= dxUnconsumed;
        } else {
            mNestedScrollingDistance -= dxUnconsumed;
            mSwipeDistance -= dxUnconsumed / 10f;
            if (Math.abs(mNestedScrollingDistance) >= mNestedScrollingTrigger) {
                performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);
            }
        }
        setTranslation(mSwipeTrigger, NESTED_SCROLLING_RATIO);
    }
}
