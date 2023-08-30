package com.mmg.phonect.db.converters;

import org.greenrobot.greendao.converter.PropertyConverter;

import java.util.TimeZone;

public class TimeZoneConverter implements PropertyConverter<TimeZone, String> {

    @Override
    public TimeZone convertToEntityProperty(String databaseValue) {
        return TimeZone.getTimeZone(databaseValue);
    }

    @Override
    public String convertToDatabaseValue(TimeZone entityProperty) {
        return entityProperty.getID();
    }
}
