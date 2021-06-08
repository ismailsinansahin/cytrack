package com.cydeo.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

//@Entity -> we do not use entity, because this class will not be persisted in the db itself
@Getter
@Setter
@NoArgsConstructor
@MappedSuperclass //Inheritance is only evident in the class, but not in the entity model
public class BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable=false, updatable=false)
    private LocalDateTime insertDateTime;

    @Column(nullable=false, updatable=false)
    private Long insertUserId;

    @Column(nullable=false)
    private LocalDateTime lastUpdateDateTime;

    @Column(nullable=false)
    private Long updateUserId;

    private Boolean isDeleted = false;

    @PrePersist
    private void onPrePersist(){
        this.insertDateTime = LocalDateTime.now();
        this.insertUserId = 1L;
        this.lastUpdateDateTime = LocalDateTime.now();
        this.updateUserId = 1L;
    }

    @PreUpdate
    private void onPreUpdate(){
        this.lastUpdateDateTime = LocalDateTime.now();
        this.updateUserId = 1L;
    }

}
