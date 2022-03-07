package com.cydeo.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class StudentTaskDTO {

    private Long id;
    private String name;
    private boolean isCompleted;
    private UserDTO studentDTO;
    private TaskDTO taskDTO;

}
