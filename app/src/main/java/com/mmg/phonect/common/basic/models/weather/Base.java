package com.mmg.phonect.common.basic.models.weather;

import android.annotation.SuppressLint;
import android.content.Context;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.mmg.phonect.common.utils.DisplayUtils;

/**
 * Base.
 *
 * All properties are {@link androidx.annotation.NonNull}.
 * */
public class Base implements Serializable {

    private final String cityId;
    private final long timeStamp;

    private final Date publishDate; // device time.
    private final long publishTime; // device time.

    private final Date updateDate; // device time.
    private final long updateTime; // device time.

    public Base(String cityId, long timeStamp,
                Date publishDate, long publishTime,
                Date updateDate, long updateTime) {
        this.cityId = cityId;
        this.timeStamp = timeStamp;
        this.publishDate = publishDate;
        this.publishTime = publishTime;
        this.updateDate = updateDate;
        this.updateTime = updateTime;
    }

    public String getCityId() {
        return cityId;
    }

    public long getTimeStamp() {
        return timeStamp;
    }

    public Date getPublishDate() {
        return publishDate;
    }

    public Date getUpdateDate() {
        return updateDate;
    }

    public long getPublishTime() {
        return publishTime;
    }

    public long getUpdateTime() {
        return updateTime;
    }

    @SuppressLint("SimpleDateFormat")
    public static String getTime(Context context, Date date) {
        return getTime(date, DisplayUtils.is12Hour(context));
    }

    @SuppressLint("SimpleDateFormat")
    private static String getTime(Date date, boolean twelveHour) {
        if (twelveHour) {
            return new SimpleDateFormat("h:mm aa").format(date);
        } else {
            return new SimpleDateFormat("HH:mm").format(date);
        }
    }
}
