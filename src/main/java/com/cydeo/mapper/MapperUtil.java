package com.cydeo.mapper;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.lang.reflect.Type;

@Component
public class MapperUtil {

    private final ModelMapper modelMapper;

    public MapperUtil(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    public <T> T convert(Object objectToBeConverted, T convertedObject){
        return modelMapper.map(objectToBeConverted,(Type) convertedObject.getClass());
    }

//    public T convertToEntity(DTO dto){
//        T entity = null;
//        return modelMapper.map(dto, (Type) entity.getClass());
//    }
//
//    public DTO convertToDTO(T entity){
//        DTO dto = null;
//        modelMapper.map(entity,dto);
//        return dto;
//    }

}
