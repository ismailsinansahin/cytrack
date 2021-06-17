package com.cydeo.implementation;

import com.cydeo.dto.BatchDTO;
import com.cydeo.mapper.MapperUtil;
import com.cydeo.repository.BatchRepository;
import com.cydeo.service.BatchService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class BatchServiceImpl implements BatchService {

    BatchRepository batchRepository;
    MapperUtil mapperUtil;

    public BatchServiceImpl(BatchRepository batchRepository, MapperUtil mapperUtil) {
        this.batchRepository = batchRepository;
        this.mapperUtil = mapperUtil;
    }

    @Override
    public List<BatchDTO> listAllBatches() {
        return batchRepository.findAll().stream().map(obj -> mapperUtil.convert(obj, new BatchDTO()))
                .collect(Collectors.toList());
    }

}
