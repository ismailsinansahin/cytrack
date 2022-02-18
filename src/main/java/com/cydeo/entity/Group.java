package com.cydeo.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "groups")
@Getter
@Setter
@NoArgsConstructor
@Where(clause="is_deleted=false")
public class Group extends BaseEntity{

    private String name;

    @ManyToOne
    @JoinColumn(name="batch_id")
    private Batch batch;

    @ManyToOne
    @JoinColumn(name = "cydeoMentor_id")
    private User cydeoMentor;

    @ManyToOne
    @JoinColumn(name = "alumniMentor_id")
    private User alumniMentor;

}
