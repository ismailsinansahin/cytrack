package com.cydeo.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Where;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "tasks")
@Getter
@Setter
@NoArgsConstructor
@Where(clause="is_deleted=false")
public class Task extends BaseEntity{

    private String name;
    private String type;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate assignDate;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate dueDate;

    @ManyToOne
    private Lesson lesson;

    @ManyToMany
    @JoinTable(name = "task_student_rel",
            joinColumns = {@JoinColumn(name = "task_id")},
            inverseJoinColumns = {@JoinColumn(name = "student_id")})
    private Set<Task> studentSet = new HashSet<>();

}
