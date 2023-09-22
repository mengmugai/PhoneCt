package com.mmg.phonect.device.json;

public class DebugInfoResult {
    public String debugOpen;
    public String usbDebugStatus;
    public String tracerPid;
    public String debugVersion;

    public String debugConnected;
    public String allowMockLocation;

    public DebugInfoResult(String debugOpen, String usbDebugStatus, String tracerPid, String debugVersion, String debugConnected, String allowMockLocation) {
        this.debugOpen = debugOpen;
        this.usbDebugStatus = usbDebugStatus;
        this.tracerPid = tracerPid;
        this.debugVersion = debugVersion;
        this.debugConnected = debugConnected;
        this.allowMockLocation = allowMockLocation;
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
