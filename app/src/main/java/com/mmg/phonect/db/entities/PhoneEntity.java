package com.mmg.phonect.db.entities;

import com.mmg.phonect.common.basic.models.Phone;
import com.mmg.phonect.common.basic.models.options.provider.WeatherSource;
import com.mmg.phonect.db.converters.TimeZoneConverter;
import com.mmg.phonect.db.converters.WeatherSourceConverter;

import org.greenrobot.greendao.annotation.Convert;
import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;

import java.util.TimeZone;

/**
 * Location entity.
 *
 * {@link Phone}.
 * */

@Entity
public class PhoneEntity {

    @Id public String formattedId;


    @Convert(converter = TimeZoneConverter.class, columnType = String.class)
    public TimeZone timeZone;
    public String brand;
    public String model;
    public String country;
    public String province;
    public String city;
    public String district;

    @Convert(converter = WeatherSourceConverter.class, columnType = String.class)
    public WeatherSource weatherSource;

    @Generated(hash = 111509493)
    public PhoneEntity(String formattedId, TimeZone timeZone, String brand,
            String model, String country, String province, String city,
            String district, WeatherSource weatherSource) {
        this.formattedId = formattedId;
        this.timeZone = timeZone;
        this.brand = brand;
        this.model = model;
        this.country = country;
        this.province = province;
        this.city = city;
        this.district = district;
        this.weatherSource = weatherSource;
    }

    @Generated(hash = 1183810361)
    public PhoneEntity() {
    }

    public String getFormattedId() {
        return this.formattedId;
    }

    public void setFormattedId(String formattedId) {
        this.formattedId = formattedId;
    }

    public TimeZone getTimeZone() {
        return this.timeZone;
    }

    public void setTimeZone(TimeZone timeZone) {
        this.timeZone = timeZone;
    }

    public String getBrand() {
        return this.brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getModel() {
        return this.model;
    }

    public void setModel(String model) {
        this.model = model;
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



}