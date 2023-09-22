package com.mmg.phonect.common.basic.models.weather;

import com.mmg.phonect.common.utils.DisplayUtils;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

/**
 * Device.
 *
 * All properties are {@link androidx.annotation.NonNull}.
 * */
public final class Device implements Serializable {


    private final String androidid;
    private final String imei;
    private final String imei2;
    private final String meid;



    private final String meid2;
    private final String ua;
    private final String bootid;

    private final String serial;


    public Device(String androidid, String imei, String imei2, String meid, String meid2, String ua, String bootid, String serial) {
        this.androidid = androidid;
        this.imei = imei;
        this.imei2 = imei2;
        this.meid = meid;
        this.meid2 = meid2;
        this.ua = ua;
        this.bootid = bootid;
        this.serial = serial;
    }

    public String getAndroidid() {
        return androidid;
    }

    public String getImei() {
        return imei;
    }

    public String getImei2() {
        return imei2;
    }

    public String getMeid() {
        return meid;
    }

    public String getMeid2() {
        return meid2;
    }

    public String getUa() {
        return ua;
    }

    public String getBootid() {
        return bootid;
    }

    public String getSerial() {
        return serial;
    }

    public boolean isDaylight(TimeZone timeZone) {

        return DisplayUtils.isDaylight(timeZone);
    }
}
