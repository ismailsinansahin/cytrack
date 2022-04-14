package com.cydeo.implementation;

import com.cydeo.dto.BatchDTO;
import com.cydeo.entity.Batch;
import com.cydeo.enums.BatchStatus;
import com.cydeo.mapper.MapperUtil;
import com.cydeo.repository.BatchRepository;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class BatchServiceImplTest {

    @InjectMocks
    BatchServiceImpl batchService;

    @Mock
    MapperUtil mapperUtil;
    @Mock
    BatchRepository batchRepository;

    @Test
    void listAllBatches() {
        when(batchRepository.findAll()).thenReturn(List.of(new Batch(),new Batch()));
        when(mapperUtil.convert(any(Batch.class),any(BatchDTO.class))).thenReturn(new BatchDTO());

        List<BatchDTO> result = batchService.listAllBatches();

        verify(batchRepository).findAll();
        verify(mapperUtil,times(2)).convert(any(Batch.class),any(BatchDTO.class));

        assertEquals(2,result.size());

    }

    @Test
    void create() {
        BatchDTO batchDTO = new BatchDTO();
        batchDTO.setName("testing batchDto");
        Batch batch = new Batch();
        when(mapperUtil.convert(any(BatchDTO.class),any(Batch.class))).thenReturn(batch);
        batch.setBatchStatus(BatchStatus.PLANNED);
        when(batchRepository.save(batch)).thenReturn(batch);
        when(mapperUtil.convert(any(Batch.class),any(BatchDTO.class))).thenReturn(batchDTO);

        BatchDTO result = batchService.create(batchDTO);

        verify(mapperUtil).convert(any(BatchDTO.class),any(Batch.class));
        verify(batchRepository).save(batch);
        verify(mapperUtil).convert(any(Batch.class),any(BatchDTO.class));

        assertEquals("testing batchDto",result.getName());
        assertNotNull(result);
    }

    @Test
    void save() {
    }

    @Test
    void delete() {
    }

    @Test
    void start() {
    }

    @Test
    void complete() {
    }

    @Test
    void getByBatchId() {
    }
}