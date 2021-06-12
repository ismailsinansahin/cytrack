package com.cydeo.entity;

import com.cydeo.enums.Country;
import com.cydeo.enums.Gender;
import com.cydeo.enums.Status;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

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
    private boolean enabled;
    private LocalDate birthday;

    @Enumerated(EnumType.STRING)
    private Country country;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    @Enumerated(EnumType.STRING)
    private Status status;

    @ManyToOne
    private Role role;

    @ManyToMany(mappedBy = "instructorSet")
    private Set<Lesson> lessonSet = new HashSet<>();

    @ManyToOne
    private Group group;

    @ManyToMany(mappedBy = "studentSet")
    private Set<Task> taskSet = new HashSet<>();

}
