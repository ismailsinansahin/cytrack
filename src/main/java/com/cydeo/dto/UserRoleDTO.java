package com.cydeo.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class UserRoleDTO {

    private Long id;
    private String name;

    public UserRoleDTO(String name) {
        this.name = name;
    }

}
