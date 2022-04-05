package com.cydeo.service;

import com.cydeo.dto.BatchDTO;

import java.util.List;
import java.util.Map;

public interface BatchService {

    List<BatchDTO> listAllBatches();
    Map<BatchDTO, List<Integer>> getBatchesWithNumberOfStudentsMap();
    BatchDTO getByBatchId(Long batchId);
    BatchDTO create(BatchDTO batchDTO);
    BatchDTO save(BatchDTO batchDTO, Long batchId);
    void delete(Long batchId);
    void start(Long batchId);
    void complete(Long batchId);

}
