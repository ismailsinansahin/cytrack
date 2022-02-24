package com.cydeo.dto;

import com.cydeo.entity.Batch;
import com.cydeo.entity.Lesson;
import com.cydeo.enums.TaskStatus;
import com.cydeo.enums.TaskType;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class TaskDTO {

    private Long id;
    private String name;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate publishingDate;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate dueDate;
    private TaskStatus taskStatus;
    private TaskType taskType;
    private Batch batch;
    private Lesson lesson;

}
