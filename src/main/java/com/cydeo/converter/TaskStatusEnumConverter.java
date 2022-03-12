package com.cydeo.converter;

import com.cydeo.enums.TaskStatus;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter(autoApply = true)
public class TaskStatusEnumConverter implements AttributeConverter<TaskStatus, Integer> {

    @Override
    public Integer convertToDatabaseColumn(TaskStatus taskStatus) {
        return taskStatus.getId();
    }

    @Override
    public TaskStatus convertToEntityAttribute(Integer id) {
        return TaskStatus.getWithId(id);
    }

}
