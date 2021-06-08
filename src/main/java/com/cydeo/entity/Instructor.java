package com.cydeo.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import java.util.List;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Instructor extends User{

    @ManyToMany(mappedBy = "instructorList")
    private Set<Batch> batchList;

    @ManyToMany//(mappedBy = "instuctorList")
    private Set<Lesson> lessonList;

}
