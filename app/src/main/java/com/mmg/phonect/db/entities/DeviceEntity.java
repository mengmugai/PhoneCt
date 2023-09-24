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


    // 解锁状态
    public String deviceLock;
    public String fridaCheck;
    public String xposedCheck;
    public String vmCheck;
    public String rootCheck;
    public String signCheck;

    // 调试状态
    public String debugOpen;
    //  USB 调试状态
    public String usbDebugStatus;
    public String tracerPid;
    //APP 是否是 debug 版本
    public String debugVersion;
    //是否正在调试
    public String debugConnected;
    // 虚拟位置
    public String allowMockLocation;



    @Generated(hash = 717815474)
    public DeviceEntity(Long id, String androidid, String imei, String imei2,
            String meid, String meid2, String ua, String bootid, String serial,
            String deviceLock, String fridaCheck, String xposedCheck,
            String vmCheck, String rootCheck, String signCheck, String debugOpen,
            String usbDebugStatus, String tracerPid, String debugVersion,
            String debugConnected, String allowMockLocation) {
        this.id = id;
        this.androidid = androidid;
        this.imei = imei;
        this.imei2 = imei2;
        this.meid = meid;
        this.meid2 = meid2;
        this.ua = ua;
        this.bootid = bootid;
        this.serial = serial;
        this.deviceLock = deviceLock;
        this.fridaCheck = fridaCheck;
        this.xposedCheck = xposedCheck;
        this.vmCheck = vmCheck;
        this.rootCheck = rootCheck;
        this.signCheck = signCheck;
        this.debugOpen = debugOpen;
        this.usbDebugStatus = usbDebugStatus;
        this.tracerPid = tracerPid;
        this.debugVersion = debugVersion;
        this.debugConnected = debugConnected;
        this.allowMockLocation = allowMockLocation;
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

    public String getDeviceLock() {
        return this.deviceLock;
    }

    public void setDeviceLock(String deviceLock) {
        this.deviceLock = deviceLock;
    }

    public String getFridaCheck() {
        return this.fridaCheck;
    }

    public void setFridaCheck(String fridaCheck) {
        this.fridaCheck = fridaCheck;
    }

    public String getXposedCheck() {
        return this.xposedCheck;
    }

    public void setXposedCheck(String xposedCheck) {
        this.xposedCheck = xposedCheck;
    }

    public String getVmCheck() {
        return this.vmCheck;
    }

    public void setVmCheck(String vmCheck) {
        this.vmCheck = vmCheck;
    }

    public String getRootCheck() {
        return this.rootCheck;
    }

    public void setRootCheck(String rootCheck) {
        this.rootCheck = rootCheck;
    }

    public String getSignCheck() {
        return this.signCheck;
    }

    public void setSignCheck(String signCheck) {
        this.signCheck = signCheck;
    }

    public String getDebugOpen() {
        return this.debugOpen;
    }

    public void setDebugOpen(String debugOpen) {
        this.debugOpen = debugOpen;
    }

    public String getUsbDebugStatus() {
        return this.usbDebugStatus;
    }

    public void setUsbDebugStatus(String usbDebugStatus) {
        this.usbDebugStatus = usbDebugStatus;
    }

    public String getTracerPid() {
        return this.tracerPid;
    }

    public void setTracerPid(String tracerPid) {
        this.tracerPid = tracerPid;
    }

    public String getDebugVersion() {
        return this.debugVersion;
    }

    public void setDebugVersion(String debugVersion) {
        this.debugVersion = debugVersion;
    }

    public String getDebugConnected() {
        return this.debugConnected;
    }

    public void setDebugConnected(String debugConnected) {
        this.debugConnected = debugConnected;
    }

    public String getAllowMockLocation() {
        return this.allowMockLocation;
    }

    public void setAllowMockLocation(String allowMockLocation) {
        this.allowMockLocation = allowMockLocation;
    }
}
