package com.cydeo.dto;

import com.cydeo.enums.Country;
import com.cydeo.enums.Gender;
import com.cydeo.enums.Status;
import com.cydeo.enums.UserRole;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

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
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate birthday;
    private Country country;
    private Gender gender;
    private Status status;
    private UserRole userRole;
    private GroupDTO group;

}
