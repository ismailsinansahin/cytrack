package com.cydeo.service;

import com.cydeo.dto.BatchDTO;

import java.util.List;

public interface BatchService {

    List<BatchDTO> listAllBatches();
    List<BatchDTO> listAllNonCompletedBatches();
    BatchDTO save(BatchDTO batchDTO);
    BatchDTO edit(BatchDTO batchDTO);
    void delete(Long batchId);
    void start(Long batchId);
    void complete(Long batchId);
    BatchDTO getByBatchId(Long batchId);

}
