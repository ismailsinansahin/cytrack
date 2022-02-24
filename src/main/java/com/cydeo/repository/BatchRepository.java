package com.cydeo.repository;

import com.cydeo.entity.Batch;
import com.cydeo.enums.BatchStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface BatchRepository extends JpaRepository<Batch, Long> {

    List<Batch> findAllByBatchStatusIsNot(BatchStatus batchStatus);

}
