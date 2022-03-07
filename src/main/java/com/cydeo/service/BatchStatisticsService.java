package com.cydeo.service;

import com.cydeo.dto.BatchDTO;
import com.cydeo.dto.GroupDTO;

import java.util.List;

public interface BatchStatisticsService {

    List<BatchDTO> getAllBatches();
    List<GroupDTO> getAllGroupsOfBatch(Long batchId);

}
