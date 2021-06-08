package com.cydeo.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Lesson extends BaseEntity{

    private String name;

    @ManyToOne
    @JoinColumn(name = "batch_id")
    private Batch batch;

    @ManyToMany
    @JoinTable(name = "lesson_instructor_rel",
            joinColumns = {@JoinColumn(name = "lesson_id")},
            inverseJoinColumns = {@JoinColumn(name = "instructor_id")})
    private Set<Instructor> instructorList;

//    private List<Task> taskList;

}
