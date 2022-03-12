package com.cydeo.converter;

import com.cydeo.enums.BatchStatus;
import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter(autoApply = true)
public class BatchStatusEnumConverter implements AttributeConverter<BatchStatus, Integer> {

    @Override
    public Integer convertToDatabaseColumn(BatchStatus batchStatus) {
        return batchStatus.getId();
    }

    @Override
    public BatchStatus convertToEntityAttribute(Integer id) {
        return BatchStatus.getWithId(id);
    }

}

