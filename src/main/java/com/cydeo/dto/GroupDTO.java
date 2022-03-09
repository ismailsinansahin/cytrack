package com.cydeo.dto;

import com.cydeo.entity.User;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Data
public class GroupDTO {

    private Long id;
    private String name;
    private BatchDTO batch;
    private UserDTO cydeoMentor;
    private UserDTO alumniMentor;

    private int activeStudents;
    private int droppedTransferredStudents;
    private int studentProgress;

}
