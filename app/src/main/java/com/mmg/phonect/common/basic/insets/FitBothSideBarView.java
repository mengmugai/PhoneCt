package com.mmg.phonect.common.basic.insets;

import androidx.annotation.IntDef;

import java.lang.annotation.Retention;

import static java.lang.annotation.RetentionPolicy.SOURCE;

public interface FitBothSideBarView {

    int SIDE_TOP = 1;
    int SIDE_BOTTOM = 1 << 1;

    @Retention(SOURCE)
    @IntDef({SIDE_TOP, SIDE_BOTTOM})
    @interface FitSide {
    }

    void addFitSide(@FitSide int side);

    void removeFitSide(@FitSide int side);

    void setFitSystemBarEnabled(boolean top, boolean bottom);

    int getTopWindowInset();

    int getBottomWindowInset();
}