package com.cydeo.entity;

import com.cydeo.enums.BatchStatus;
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
public class Batch extends BaseEntity{

    private String batchName;
    private String batchStartDate;
    private String endDate;
    private String notes;

    @Enumerated
    private BatchStatus batchStatus;

    @ManyToMany
    @JoinTable(name = "batch_instructor_rel",
    joinColumns = {@JoinColumn(name = "batch_id")},
    inverseJoinColumns = {@JoinColumn(name = "instructor_id")})
    private Set<Instructor> instructorList;

    @ManyToMany
    @JoinTable(name = "batch_cybertekMentor_rel",
    joinColumns = {@JoinColumn(name = "batch_id")},
    inverseJoinColumns = {@JoinColumn(name = "cybertekMentor_id")})
    private Set<CybertekMentor> cybertekMentorList;

    @ManyToMany
    @JoinTable(name = "batch_alumniMentor_rel",
    joinColumns = {@JoinColumn(name = "batch_id")},
    inverseJoinColumns = {@JoinColumn(name = "alumniMentor_id")})
    private Set<AlumniMentor> alumniMentorList;

//    private List<Group> groupList;

//    private List<Student> studentList;

}
