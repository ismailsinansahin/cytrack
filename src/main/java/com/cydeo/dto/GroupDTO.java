package com.cydeo.dto;

import com.cydeo.entity.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class GroupDTO {

    private Long id;
    private String name;
    private BatchDTO batch;
    private User cydeoMentor;
    private User alumniMentor;

}
