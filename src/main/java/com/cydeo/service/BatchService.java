package com.cydeo.service;

import com.cydeo.dto.BatchDTO;

import java.util.List;

public interface BatchService {

    List<BatchDTO> listAllBatches();
    BatchDTO getByBatchId(Long batchId);
    BatchDTO create(BatchDTO batchDTO);
    BatchDTO save(BatchDTO batchDTO, Long batchId);
    void delete(Long batchId);
    void start(Long batchId);
    void complete(Long batchId);

}
