package com.cydeo.converter;

import com.cydeo.enums.TaskType;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter(autoApply = true)
public class TaskTypeEnumConverter implements AttributeConverter<TaskType, Integer> {

    @Override
    public Integer convertToDatabaseColumn(TaskType taskType) {
        return taskType.getId();
    }

    @Override
    public TaskType convertToEntityAttribute(Integer id) {
        return TaskType.getWithId(id);
    }

}
