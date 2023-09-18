package com.mmg.phonect.common.basic.models.weather;

import android.graphics.drawable.Drawable;

import androidx.annotation.DrawableRes;

import org.json.JSONObject;

import java.io.Serializable;

/**
 * xposed 模块.
 *
 * All properties are {@link androidx.annotation.NonNull}.
 * */
public final class XposedModule implements Serializable {



    private String name;
    private String packageName;
    private String version;
    private int buildVersion;
    private Drawable icon;
    private boolean systemApp;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public int getBuildVersion() {
        return buildVersion;
    }

    public void setBuildVersion(int buildVersion) {
        this.buildVersion = buildVersion;
    }

    public Drawable getIcon() {
        return icon;
    }

    public void setIcon(Drawable icon) {
        this.icon = icon;
    }

    public boolean isSystemApp() {
        return systemApp;
    }

    public void setSystemApp(boolean systemApp) {
        this.systemApp = systemApp;
    }

    @Override
    public String toString() {
        return "ApplicationBean{" +
                "name='" + name + '\'' +
                ", packageName='" + packageName + '\'' +
                ", version='" + version + '\'' +
                ", buildVersion=" + buildVersion +
                ", icon=" + icon +
                ", systemApp=" + systemApp +
                '}';
    }

    public JSONObject toJSON() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("name", name);
            jsonObject.put("packageName", packageName);
            jsonObject.put("version", version);
            jsonObject.put("buildVersion", buildVersion);
            jsonObject.put("icon", icon);
            jsonObject.put("systemApp", systemApp);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonObject;
    }

}
