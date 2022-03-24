package com.cydeo.dto;

import com.cydeo.enums.StudentStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class BatchGroupStudentDTO {

    private Long id;
    private BatchDTO batch;
    private GroupDTO group;
    private UserDTO student;
    private StudentStatus studentStatus;

}
