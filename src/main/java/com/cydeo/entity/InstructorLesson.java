package com.cydeo.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Where;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "instructor_lesson")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Where(clause="is_deleted=false")
public class InstructorLesson extends BaseEntity{

    @ManyToOne
    @JoinColumn(name = "instructor_id")
    private User instructor;

    @ManyToOne
    @JoinColumn(name = "lesson_id")
    private Lesson lesson;

}
