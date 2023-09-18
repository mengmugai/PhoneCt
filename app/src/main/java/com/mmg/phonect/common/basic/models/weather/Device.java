package com.mmg.phonect.common.basic.models.weather;

import java.io.Serializable;

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
    private final String ua;
    private final String bootid;

    private final String serial;

    public String getSerial() {
        return serial;
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

    public String getUa() {
        return ua;
    }

    public String getBootid() {
        return bootid;
    }


    public Device(String androidid, String imei, String imei2, String meid, String ua, String bootid, String serial) {
        this.androidid = androidid;
        this.imei = imei;
        this.imei2 = imei2;
        this.meid = meid;
        this.ua = ua;
        this.bootid = bootid;
        this.serial = serial;
    }
}
