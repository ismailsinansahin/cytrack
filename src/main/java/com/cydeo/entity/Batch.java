package com.cydeo.entity;

import com.cydeo.enums.BatchStatus;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Where;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Date;

@Entity
@Table(name = "batches")
@Getter
@Setter
@NoArgsConstructor
@Where(clause="is_deleted=false")
public class Batch extends BaseEntity{

    private String name;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate batchStartDate;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate batchEndDate;

    @Column(columnDefinition = "text")
    private String notes;

    @Enumerated(EnumType.STRING)
    private BatchStatus batchStatus;

}
