package com.mmg.phonect.db.generators;

import com.mmg.phonect.common.basic.models.Location;
import com.mmg.phonect.common.basic.models.Phone;
//import com.mmg.phonect.db.entities.LocationEntity;
import com.mmg.phonect.db.entities.PhoneEntity;

import java.util.ArrayList;
import java.util.List;

public class PhoneEntityGenerator {

    public static PhoneEntity generate(Phone phone) {
        PhoneEntity entity = new PhoneEntity();
        entity.formattedId = phone.getFormattedId();
        entity.timeZone = phone.getTimeZone();
        entity.country = phone.getCountry();
        entity.province = phone.getProvince();
        entity.city = phone.getCity();
        entity.district = phone.getDistrict();
        entity.weatherSource = phone.getWeatherSource();
        return entity;
    }

    public static List<PhoneEntity> generateEntityList(List<Phone> locationList) {
        List<PhoneEntity> entityList = new ArrayList<>(locationList.size());
        for (int i = 0; i < locationList.size(); i ++) {
            entityList.add(generate(locationList.get(i)));
        }
        return entityList;
    }

    public static Phone generate(PhoneEntity entity) {
        return new Phone(
                entity.timeZone,
                GeneratorUtils.nonNull(entity.brand),
                GeneratorUtils.nonNull(entity.model),
                GeneratorUtils.nonNull(entity.country),
                GeneratorUtils.nonNull(entity.province),
                GeneratorUtils.nonNull(entity.city),
                GeneratorUtils.nonNull(entity.district),
                null,
                entity.weatherSource
        );
    }

    public static List<Phone> generateModuleList(List<PhoneEntity> entityList) {
        List<Phone> phoneList = new ArrayList<>(entityList.size());
        for (PhoneEntity entity : entityList) {
            phoneList.add(generate(entity));
        }
        return phoneList;
    }
}
