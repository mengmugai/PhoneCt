package com.mmg.phonect.db.entities;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;

@Entity
public class DeviceEntity {
    @Id
    public Long id;

    public String androidid;
    public String imei;
    public String imei2;
    public String meid;
    public String meid2;
    public String ua;
    public String bootid;

    public String serial;

    @Generated(hash = 1742342683)
    public DeviceEntity(Long id, String androidid, String imei, String imei2,
            String meid, String meid2, String ua, String bootid, String serial) {
        this.id = id;
        this.androidid = androidid;
        this.imei = imei;
        this.imei2 = imei2;
        this.meid = meid;
        this.meid2 = meid2;
        this.ua = ua;
        this.bootid = bootid;
        this.serial = serial;
    }

    @Generated(hash = 1449836520)
    public DeviceEntity() {
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAndroidid() {
        return this.androidid;
    }

    public void setAndroidid(String androidid) {
        this.androidid = androidid;
    }

    public String getImei() {
        return this.imei;
    }

    public void setImei(String imei) {
        this.imei = imei;
    }

    public String getImei2() {
        return this.imei2;
    }

    public void setImei2(String imei2) {
        this.imei2 = imei2;
    }

    public String getMeid() {
        return this.meid;
    }

    public void setMeid(String meid) {
        this.meid = meid;
    }

    public String getMeid2() {
        return this.meid2;
    }

    public void setMeid2(String meid2) {
        this.meid2 = meid2;
    }

    public String getUa() {
        return this.ua;
    }

    public void setUa(String ua) {
        this.ua = ua;
    }

    public String getBootid() {
        return this.bootid;
    }

    public void setBootid(String bootid) {
        this.bootid = bootid;
    }

    public String getSerial() {
        return this.serial;
    }

    public void setSerial(String serial) {
        this.serial = serial;
    }
}
