package com.cydeo.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "groups")
@Getter
@Setter
@NoArgsConstructor
public class Group extends BaseEntity{

    private String groupName;
    private String mascot;

    @ManyToOne
    @JoinColumn(name="batch_id")
    private Batch batch;

    @ManyToOne
    @JoinColumn(name = "cybertekMentor_id")
    private CybertekMentor cybertekMentor;

    @ManyToOne
    @JoinColumn(name = "alumniMentor_id")
    private AlumniMentor alumniMentor;

//    private List<Student> studentList;

}
