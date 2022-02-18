package com.cydeo.dto;

import lombok.Data;

@Data
public class TaskDTO {

    private Long id;
    private String name;
    private LessonDTO lessonDTO;

}
