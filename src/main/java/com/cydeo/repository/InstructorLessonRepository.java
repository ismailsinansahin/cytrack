package com.cydeo.repository;

import com.cydeo.entity.InstructorLesson;
import com.cydeo.entity.Lesson;
import com.cydeo.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InstructorLessonRepository extends JpaRepository<InstructorLesson, Long> {

    List<InstructorLesson> findAllByLesson(Lesson lesson);
    InstructorLesson findByLessonAndInstructor(Lesson lesson, User instructor);

}
