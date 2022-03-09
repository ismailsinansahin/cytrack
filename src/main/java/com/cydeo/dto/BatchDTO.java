package com.cydeo.dto;

import com.cydeo.enums.BatchStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.MapKeyEnumerated;
import java.time.LocalDate;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class BatchDTO {

    private Long id;
    private String name;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate batchStartDate;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate batchEndDate;
    private String notes;
    private BatchStatus batchStatus;

    private int completion;
    private int activeStudents;
    private int droppedTransferredStudents;
    private int studentProgress;

    public BatchDTO(BatchStatus batchStatus) {
        this.batchStatus = batchStatus;
    }

}
