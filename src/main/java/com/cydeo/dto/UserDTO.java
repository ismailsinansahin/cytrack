package com.cydeo.dto;

import com.cydeo.enums.Country;
import com.cydeo.enums.Gender;
import com.cydeo.enums.StudentStatus;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Data
public class UserDTO {

    private Long id;
    private String firstName;
    private String lastName;
    private String userName;
    private String email;
    private String password;
    private String confirmPassword;
    private String phone;
    private boolean enabled;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate birthday;
    private Country country;
    private Gender gender;
    private UserRoleDTO userRole;

    private int studentProgress;
    private BatchDTO currentBatch;
    private GroupDTO currentGroup;

}
