package com.cydeo.entity;

import com.cydeo.enums.Country;
import com.cydeo.enums.Gender;
import com.cydeo.enums.StudentStatus;
import com.cydeo.enums.UserRole;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Where;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@Where(clause="is_deleted=false")
public class User extends BaseEntity{

    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private String phone;
    private boolean enabled;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate birthday;

    @Enumerated(EnumType.STRING)
    private Country country;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    @Enumerated(EnumType.STRING)
    private StudentStatus studentStatus;

    @Enumerated(EnumType.STRING)
    private UserRole userRole;

    @ManyToOne
    private Group group;

}
