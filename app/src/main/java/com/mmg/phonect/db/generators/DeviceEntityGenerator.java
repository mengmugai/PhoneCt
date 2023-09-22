package com.mmg.phonect.db.generators;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import androidx.annotation.Nullable;

import com.mmg.phonect.common.basic.models.Phone;
import com.mmg.phonect.common.basic.models.weather.Device;
import com.mmg.phonect.db.entities.DeviceEntity;

import java.util.ArrayList;
import java.util.List;

public class DeviceEntityGenerator {

    public static DeviceEntity generate(Phone location, Device device) {
        DeviceEntity entity = new DeviceEntity();



        entity.androidid = device.getAndroidid();
        entity.bootid = device.getBootid();
        entity.imei = device.getImei();
        entity.imei2 = device.getImei2();
        entity.ua = device.getUa();
        entity.meid = device.getMeid();
        entity.meid2 = device.getMeid2();
        entity.serial = device.getSerial();


        return entity;
    }

    public static Device generate(@Nullable DeviceEntity deviceEntity) {
        if (deviceEntity == null) {
            return null;
        }

        return new Device(
                deviceEntity.getAndroidid(),
                deviceEntity.getImei(),
                deviceEntity.getImei2(),
                deviceEntity.getMeid(),
                deviceEntity.getMeid2(),
                deviceEntity.getUa(),
                deviceEntity.getBootid(),
                deviceEntity.getSerial()

        );
    }

}
