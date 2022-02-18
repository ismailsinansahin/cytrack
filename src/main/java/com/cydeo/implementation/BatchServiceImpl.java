package com.cydeo.implementation;

import com.cydeo.dto.BatchDTO;
import com.cydeo.entity.Batch;
import com.cydeo.enums.BatchStatus;
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
        return batchRepository.findAll()
                .stream()
                .map(obj -> mapperUtil.convert(obj, new BatchDTO()))
                .collect(Collectors.toList());
    }

    @Override
    public List<BatchDTO> listAllNonCompletedBatches() {
        return batchRepository.findAllByBatchStatusIsNot(BatchStatus.COMPLETED)
                .stream()
                .map(obj -> mapperUtil.convert(obj, new BatchDTO()))
                .collect(Collectors.toList());
    }

    @Override
    public BatchDTO save(BatchDTO batchDTO) {
        Batch batch = mapperUtil.convert(batchDTO, new Batch());
        batchRepository.save(batch);
        return mapperUtil.convert(batch, new BatchDTO());
    }

    @Override
    public BatchDTO edit(BatchDTO batchDTO) {
        Batch batch = batchRepository.save(mapperUtil.convert(batchDTO, new Batch()));
        return mapperUtil.convert(batch, new BatchDTO());
    }

    @Override
    public void delete(Long batchId) {
        Batch batch = batchRepository.findById(batchId).get();
        batch.setIsDeleted(true);
        batchRepository.save(batch);
    }

    @Override
    public void start(Long batchId) {
        Batch batch = batchRepository.findById(batchId).get();
        batch.setBatchStatus(BatchStatus.INPROGRESS);
        batchRepository.save(batch);
    }

    @Override
    public void complete(Long batchId) {
        Batch batch = batchRepository.findById(batchId).get();
        batch.setBatchStatus(BatchStatus.COMPLETED);
        batchRepository.save(batch);
    }

    @Override
    public BatchDTO getByBatchId(Long batchId) {
        Batch batch = batchRepository.findById(batchId).get();
        return mapperUtil.convert(batch, new BatchDTO());
    }

}
