package com.cydeo.dto;

import com.cydeo.enums.BatchStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class BatchDTO {

    private String name;
    private LocalDate batchStartDate;
    private LocalDate batchEndDate;
    private String notes;
    private BatchStatus batchStatus;

}
