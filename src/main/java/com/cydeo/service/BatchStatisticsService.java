package com.cydeo.service;

import com.cydeo.dto.BatchDTO;
import com.cydeo.dto.GroupDTO;

import java.util.List;
import java.util.Map;

public interface BatchStatisticsService {

    BatchDTO getBatchById(Long batchId);
    List<GroupDTO> getAllGroupsOfBatch(Long batchId);
    Map<String, Integer> getTaskBasedNumbers(Long batchId);
    Map<String, Integer> getWeekBasedNumbers(Long batchId);
    BatchDTO getBatchWithNumberOfStudents(Long batchId);

}
