package com.cydeo.converter;

import com.cydeo.enums.Country;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter(autoApply = true)
public class CountryEnumConverter implements AttributeConverter<Country, Integer> {

    @Override
    public Integer convertToDatabaseColumn(Country country) {
        return country.getId();
    }

    @Override
    public Country convertToEntityAttribute(Integer id) {
        return Country.getWithId(id);
    }

}
