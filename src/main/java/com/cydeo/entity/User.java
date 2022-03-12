package com.cydeo.entity;

import com.cydeo.enums.Country;
import com.cydeo.enums.Gender;
import com.cydeo.enums.StudentStatus;
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
    private String userName;
    private String password;
    private String phone;
    private boolean enabled;
    @Column(nullable = false, unique = true)
    private String email;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate birthday;
    private Country country;
    private Gender gender;
    private StudentStatus studentStatus;

    @ManyToOne
    private UserRole userRole;

    @ManyToOne
    private Batch batch;

    @ManyToOne
    private Group group;

}
