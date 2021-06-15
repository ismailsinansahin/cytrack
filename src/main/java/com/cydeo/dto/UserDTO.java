package com.cydeo.dto;

import com.cydeo.entity.Group;
import com.cydeo.entity.Role;
import com.cydeo.entity.Task;
import com.cydeo.enums.Country;
import com.cydeo.enums.Gender;
import com.cydeo.enums.Status;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class UserDTO {

    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private String confirmPassword;
    private String phone;
    private boolean enabled;
    private LocalDate birthday;
    private Country country;
    private Gender gender;
    private Status status;
    private Role role;

}
