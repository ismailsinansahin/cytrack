package com.cydeo.entity;

import com.cydeo.enums.Country;
import com.cydeo.enums.Gender;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;
import java.time.LocalDate;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
public class User extends BaseEntity{

    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private String phone;

    private boolean enabled = true;

    @DateTimeFormat
    private LocalDate birthday;
    @Enumerated(EnumType.STRING)
    private Country country;
    @Enumerated(EnumType.STRING)
    private Gender gender;

//    private Role role;

//    private Task task;

}
