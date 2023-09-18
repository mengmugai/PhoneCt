package com.mmg.phonect.device.json;

public class DeviceResult {
    public String androidid;
    public String imei;
    public String imei2;
    public String meid;
    public String ua;
    public String bootid;

    public String serial;

    public DeviceResult(String androidid, String imei, String imei2, String meid, String ua, String bootid, String serial) {
        this.androidid = androidid;
        this.imei = imei;
        this.imei2 = imei2;
        this.meid = meid;
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

    public String getUa() {
        return ua;
    }

    public String getBootid() {
        return bootid;
    }

    public String getSerial() {
        return serial;
    }


}
