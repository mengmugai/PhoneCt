package com.mmg.phonect.common.basic.models.weather;

import com.mmg.phonect.common.utils.DisplayUtils;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;
import java.util.Objects;
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



    // 保存所有异常信息
    public String allDiseaseInfo;

    public int getScore() {
        return score;
    }

    public int score = 100;

    public Device(String androidid, String imei, String imei2, String meid, String meid2, String ua, String bootid, String serial, String deviceLock, String fridaCheck, String xposedCheck, String vmCheck, String rootCheck, String signCheck, String debugOpen, String usbDebugStatus, String tracerPid, String debugVersion, String debugConnected, String allowMockLocation, String allDiseaseInfo) {
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
        this.allDiseaseInfo = allDiseaseInfo;

        calculateScore();
    }

    //计算分数
    private void calculateScore(){
        if (!Objects.equals(tracerPid, "0")){
            score -= 10;
        }
        if (!Objects.equals(rootCheck, "")){
            score -= 10;
        }
        if (!Objects.equals(fridaCheck, "")){
            score -= 10;
        }
        if (!Objects.equals(xposedCheck, "")){
            score -= 10;
        }
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

    public String getDeviceLock() {
        return deviceLock;
    }

    public void setDeviceLock(String deviceLock) {
        this.deviceLock = deviceLock;
    }

    public String getFridaCheck() {
        return fridaCheck;
    }

    public void setFridaCheck(String fridaCheck) {
        this.fridaCheck = fridaCheck;
    }

    public String getXposedCheck() {
        return xposedCheck;
    }

    public void setXposedCheck(String xposedCheck) {
        this.xposedCheck = xposedCheck;
    }

    public String getVmCheck() {
        return vmCheck;
    }

    public void setVmCheck(String vmCheck) {
        this.vmCheck = vmCheck;
    }

    public String getRootCheck() {
        return rootCheck;
    }

    public void setRootCheck(String rootCheck) {
        this.rootCheck = rootCheck;
    }

    public String getSignCheck() {
        return signCheck;
    }

    public void setSignCheck(String signCheck) {
        this.signCheck = signCheck;
    }

    public String getDebugOpen() {
        return debugOpen;
    }

    public void setDebugOpen(String debugOpen) {
        this.debugOpen = debugOpen;
    }

    public String getUsbDebugStatus() {
        return usbDebugStatus;
    }

    public void setUsbDebugStatus(String usbDebugStatus) {
        this.usbDebugStatus = usbDebugStatus;
    }

    public String getTracerPid() {
        return tracerPid;
    }

    public void setTracerPid(String tracerPid) {
        this.tracerPid = tracerPid;
    }

    public String getDebugVersion() {
        return debugVersion;
    }

    public void setDebugVersion(String debugVersion) {
        this.debugVersion = debugVersion;
    }

    public String getDebugConnected() {
        return debugConnected;
    }

    public void setDebugConnected(String debugConnected) {
        this.debugConnected = debugConnected;
    }

    public String getAllowMockLocation() {
        return allowMockLocation;
    }

    public void setAllowMockLocation(String allowMockLocation) {
        this.allowMockLocation = allowMockLocation;
    }

    public String getAllDiseaseInfo() {
        return allDiseaseInfo;
    }

    public void setAllDiseaseInfo(String allDiseaseInfo) {
        this.allDiseaseInfo = allDiseaseInfo;
    }

    public boolean isDaylight(TimeZone timeZone) {

        return DisplayUtils.isDaylight(timeZone);
    }
}
