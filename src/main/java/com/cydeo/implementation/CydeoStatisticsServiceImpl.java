package com.cydeo.implementation;

import com.cydeo.dto.BatchDTO;
import com.cydeo.mapper.MapperUtil;
import com.cydeo.repository.BatchRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CydeoStatisticsServiceImpl implements com.cydeo.service.CydeoStatisticsService {

    private final MapperUtil mapperUtil;
    private final BatchRepository batchRepository;

    public CydeoStatisticsServiceImpl(MapperUtil mapperUtil, BatchRepository batchRepository) {
        this.mapperUtil = mapperUtil;
        this.batchRepository = batchRepository;
    }

    @Override
    public List<BatchDTO> getAllBatches() {
        return batchRepository.findAll()
                .stream()
                .map(obj -> mapperUtil.convert(obj, new BatchDTO()))
                .collect(Collectors.toList());
    }

}
