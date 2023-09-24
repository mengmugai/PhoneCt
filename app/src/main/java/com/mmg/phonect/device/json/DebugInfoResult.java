package com.mmg.phonect.device.json;

public class DebugInfoResult {


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


    public DebugInfoResult(String deviceLock, String fridaCheck, String xposedCheck, String vmCheck, String rootCheck, String signCheck, String debugOpen, String usbDebugStatus, String tracerPid, String debugVersion, String debugConnected, String allowMockLocation) {
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
}
