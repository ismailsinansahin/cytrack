package com.cydeo.entity;

import com.cydeo.enums.Status;
import com.cydeo.enums.WorkingStatus;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Student extends User{

    @Enumerated(EnumType.STRING)
    private WorkingStatus workingStatus;

    @Enumerated(EnumType.STRING)
    private Status status;

    @ManyToOne
    @JoinColumn(name = "group_id")
    private Group group;

    @ManyToMany(mappedBy = "studentList")
    private List<Task> taskList;

}
