package com.cydeo.converter;

import com.cydeo.enums.Gender;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter(autoApply = true)
public class GenderEnumConverter implements AttributeConverter<Gender, Integer> {

    @Override
    public Integer convertToDatabaseColumn(Gender gender) {
        return gender.getId();
    }

    @Override
    public Gender convertToEntityAttribute(Integer id) {
        return Gender.getWithId(id);
    }

}
