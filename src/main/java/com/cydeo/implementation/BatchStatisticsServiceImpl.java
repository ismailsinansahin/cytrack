package com.cydeo.implementation;

import com.cydeo.dto.BatchDTO;
import com.cydeo.dto.GroupDTO;
import com.cydeo.dto.UserDTO;
import com.cydeo.entity.Batch;
import com.cydeo.entity.Group;
import com.cydeo.mapper.MapperUtil;
import com.cydeo.repository.BatchRepository;
import com.cydeo.repository.GroupRepository;
import com.cydeo.service.BatchStatisticsService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class BatchStatisticsServiceImpl implements BatchStatisticsService {

    private final MapperUtil mapperUtil;
    private final BatchRepository batchRepository;
    private final GroupRepository groupRepository;

    public BatchStatisticsServiceImpl(MapperUtil mapperUtil, BatchRepository batchRepository,
                                      GroupRepository groupRepository) {
        this.mapperUtil = mapperUtil;
        this.batchRepository = batchRepository;
        this.groupRepository = groupRepository;
    }

    @Override
    public List<BatchDTO> getAllBatches() {
        return batchRepository.findAll()
                .stream()
                .map(obj -> mapperUtil.convert(obj, new BatchDTO()))
                .collect(Collectors.toList());
    }

    @Override
    public List<GroupDTO> getAllGroupsOfBatch(Long batchId) {
        return groupRepository.findAllByBatch(batchRepository.findById(batchId).get())
                .stream()
                .map(obj -> mapperUtil.convert(obj, new GroupDTO()))
                .collect(Collectors.toList());
    }

}
