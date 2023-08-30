package com.mmg.phonect.db.entities;

import org.greenrobot.greendao.annotation.Convert;
import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;

import java.util.TimeZone;

import com.mmg.phonect.common.basic.models.Location;
import com.mmg.phonect.common.basic.models.options.provider.WeatherSource;
import com.mmg.phonect.db.converters.TimeZoneConverter;
import com.mmg.phonect.db.converters.WeatherSourceConverter;

/**
 * Location entity.
 *
 * {@link Location}.
 * */

@Entity
public class LocationEntity {

    @Id public String formattedId;

    public String cityId;

    public float latitude;
    public float longitude;

    @Convert(converter = TimeZoneConverter.class, columnType = String.class)
    public TimeZone timeZone;

    public String country;
    public String province;
    public String city;
    public String district;

    @Convert(converter = WeatherSourceConverter.class, columnType = String.class)
    public WeatherSource weatherSource;

    public boolean currentPosition;
    public boolean residentPosition;
    public boolean china;

    @Generated(hash = 1125075138)
    public LocationEntity(String formattedId, String cityId, float latitude,
            float longitude, TimeZone timeZone, String country, String province,
            String city, String district, WeatherSource weatherSource,
            boolean currentPosition, boolean residentPosition, boolean china) {
        this.formattedId = formattedId;
        this.cityId = cityId;
        this.latitude = latitude;
        this.longitude = longitude;
        this.timeZone = timeZone;
        this.country = country;
        this.province = province;
        this.city = city;
        this.district = district;
        this.weatherSource = weatherSource;
        this.currentPosition = currentPosition;
        this.residentPosition = residentPosition;
        this.china = china;
    }
    @Generated(hash = 1723987110)
    public LocationEntity() {
    }
    public String getFormattedId() {
        return this.formattedId;
    }
    public void setFormattedId(String formattedId) {
        this.formattedId = formattedId;
    }
    public String getCityId() {
        return this.cityId;
    }
    public void setCityId(String cityId) {
        this.cityId = cityId;
    }
    public float getLatitude() {
        return this.latitude;
    }
    public void setLatitude(float latitude) {
        this.latitude = latitude;
    }
    public float getLongitude() {
        return this.longitude;
    }
    public void setLongitude(float longitude) {
        this.longitude = longitude;
    }
    public TimeZone getTimeZone() {
        return this.timeZone;
    }
    public void setTimeZone(TimeZone timeZone) {
        this.timeZone = timeZone;
    }
    public String getCountry() {
        return this.country;
    }
    public void setCountry(String country) {
        this.country = country;
    }
    public String getProvince() {
        return this.province;
    }
    public void setProvince(String province) {
        this.province = province;
    }
    public String getCity() {
        return this.city;
    }
    public void setCity(String city) {
        this.city = city;
    }
    public String getDistrict() {
        return this.district;
    }
    public void setDistrict(String district) {
        this.district = district;
    }
    public WeatherSource getWeatherSource() {
        return this.weatherSource;
    }
    public void setWeatherSource(WeatherSource weatherSource) {
        this.weatherSource = weatherSource;
    }
    public boolean getCurrentPosition() {
        return this.currentPosition;
    }
    public void setCurrentPosition(boolean currentPosition) {
        this.currentPosition = currentPosition;
    }
    public boolean getResidentPosition() {
        return this.residentPosition;
    }
    public void setResidentPosition(boolean residentPosition) {
        this.residentPosition = residentPosition;
    }
    public boolean getChina() {
        return this.china;
    }
    public void setChina(boolean china) {
        this.china = china;
    }
}