package com.cydeo.converter;

import com.cydeo.enums.StudentStatus;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter(autoApply = true)
public class StudentStatusEnumConverter implements AttributeConverter<StudentStatus, Integer> {

    @Override
    public Integer convertToDatabaseColumn(StudentStatus studentStatus) {
        return studentStatus.getId();
    }

    @Override
    public StudentStatus convertToEntityAttribute(Integer id) {
        if(id==null) return null;
        else return StudentStatus.getWithId(id);
    }

}
