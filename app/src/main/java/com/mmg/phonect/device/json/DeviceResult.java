package com.mmg.phonect.device.json;

public class DeviceResult {
    public String androidid;
    public String imei;
    public String imei2;



    public String meid;
    public String meid2;
    public String ua;
    public String bootid;

    public String serial;


    public DeviceResult(String androidid, String imei, String imei2, String meid, String meid2, String ua, String bootid, String serial) {
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

    public void setAndroidid(String androidid) {
        this.androidid = androidid;
    }

    public String getImei() {
        return imei;
    }

    public void setImei(String imei) {
        this.imei = imei;
    }

    public String getImei2() {
        return imei2;
    }

    public void setImei2(String imei2) {
        this.imei2 = imei2;
    }

    public String getMeid() {
        return meid;
    }

    public void setMeid(String meid) {
        this.meid = meid;
    }

    public String getMeid2() {
        return meid2;
    }

    public void setMeid2(String meid2) {
        this.meid2 = meid2;
    }

    public String getUa() {
        return ua;
    }

    public void setUa(String ua) {
        this.ua = ua;
    }

    public String getBootid() {
        return bootid;
    }

    public void setBootid(String bootid) {
        this.bootid = bootid;
    }

    public String getSerial() {
        return serial;
    }

    public void setSerial(String serial) {
        this.serial = serial;
    }
}
