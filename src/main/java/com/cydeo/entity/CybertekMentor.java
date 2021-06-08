package com.cydeo.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class CybertekMentor extends User{

    @ManyToMany(mappedBy = "cybertekMentorList")
    private List<Batch> batchList;

//    private List<Group> groupList;

}
