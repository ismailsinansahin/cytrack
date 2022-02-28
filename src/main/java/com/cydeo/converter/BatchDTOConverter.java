package com.cydeo.converter;

import com.cydeo.dto.BatchDTO;
import com.cydeo.dto.UserDTO;
import com.cydeo.service.BatchService;
import com.cydeo.service.UserService;
import org.springframework.boot.context.properties.ConfigurationPropertiesBinding;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
@ConfigurationPropertiesBinding
public class BatchDTOConverter implements Converter<String, BatchDTO> {

    private final BatchService batchService;

    public BatchDTOConverter(@Lazy BatchService batchService) {
        this.batchService = batchService;
    }

    @Override
    public BatchDTO convert(String s) {
        return batchService.getByBatchId(Long.parseLong(s));
    }

}
