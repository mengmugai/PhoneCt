package com.mmg.phonect.common.basic.models.weather;

import android.content.Context;

import androidx.annotation.ColorInt;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import java.io.Serializable;

import wangdaye.com.geometricweather.R;

/**
 * DailyPollen.
 * */
public class Pollen implements Serializable {

    @Nullable private final Integer grassIndex;
    @Nullable private final Integer grassLevel;
    @Nullable private final String grassDescription;

    @Nullable private final Integer moldIndex;
    @Nullable private final Integer moldLevel;
    @Nullable private final String moldDescription;

    @Nullable private final Integer ragweedIndex;
    @Nullable private final Integer ragweedLevel;
    @Nullable private final String ragweedDescription;

    @Nullable private final Integer treeIndex;
    @Nullable private final Integer treeLevel;
    @Nullable private final String treeDescription;

    public Pollen(@Nullable Integer grassIndex, @Nullable Integer grassLevel, @Nullable String grassDescription,
                  @Nullable Integer moldIndex, @Nullable Integer moldLevel, @Nullable String moldDescription,
                  @Nullable Integer ragweedIndex, @Nullable Integer ragweedLevel, @Nullable String ragweedDescription,
                  @Nullable Integer treeIndex, @Nullable Integer treeLevel, @Nullable String treeDescription) {
        this.grassIndex = grassIndex;
        this.grassLevel = grassLevel;
        this.grassDescription = grassDescription;
        this.moldIndex = moldIndex;
        this.moldLevel = moldLevel;
        this.moldDescription = moldDescription;
        this.ragweedIndex = ragweedIndex;
        this.ragweedLevel = ragweedLevel;
        this.ragweedDescription = ragweedDescription;
        this.treeIndex = treeIndex;
        this.treeLevel = treeLevel;
        this.treeDescription = treeDescription;
    }

    @Nullable
    public Integer getGrassIndex() {
        return grassIndex;
    }

    @Nullable
    public Integer getGrassLevel() {
        return grassLevel;
    }

    @Nullable
    public String getGrassDescription() {
        return grassDescription;
    }

    @Nullable
    public Integer getMoldIndex() {
        return moldIndex;
    }

    @Nullable
    public Integer getMoldLevel() {
        return moldLevel;
    }

    @Nullable
    public String getMoldDescription() {
        return moldDescription;
    }

    @Nullable
    public Integer getRagweedIndex() {
        return ragweedIndex;
    }

    @Nullable
    public Integer getRagweedLevel() {
        return ragweedLevel;
    }

    @Nullable
    public String getRagweedDescription() {
        return ragweedDescription;
    }

    @Nullable
    public Integer getTreeIndex() {
        return treeIndex;
    }

    @Nullable
    public Integer getTreeLevel() {
        return treeLevel;
    }

    @Nullable
    public String getTreeDescription() {
        return treeDescription;
    }

    public boolean isValid() {
        return (grassIndex != null && grassIndex > 0 && grassLevel != null)
                || (moldIndex != null && moldIndex > 0 && moldLevel != null)
                || (ragweedIndex != null && ragweedIndex > 0 && ragweedLevel != null)
                || (treeIndex != null && treeIndex > 0 && treeLevel != null);
    }

    @ColorInt
    public static int getPollenColor(Context context, Integer level) {
        if (level == null) {
            return ContextCompat.getColor(context, R.color.colorLevel_1);
        } else if (level <= 1) {
            return ContextCompat.getColor(context, R.color.colorLevel_1);
        } else if (level <= 2) {
            return ContextCompat.getColor(context, R.color.colorLevel_2);
        } else if (level <= 3) {
            return ContextCompat.getColor(context, R.color.colorLevel_3);
        } else if (level <= 4) {
            return ContextCompat.getColor(context, R.color.colorLevel_4);
        } else if (level <= 5) {
            return ContextCompat.getColor(context, R.color.colorLevel_5);
        } else {
            return ContextCompat.getColor(context, R.color.colorLevel_6);
        }
    }
}
