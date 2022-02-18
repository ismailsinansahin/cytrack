package com.cydeo.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class InstructorLessonDTO {

    private Long id;
    private UserDTO userDTO;
    private LessonDTO lessonDTO;

}