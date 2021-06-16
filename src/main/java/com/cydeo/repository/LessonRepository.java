package com.cydeo.repository;

import com.cydeo.entity.Lesson;
import com.cydeo.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface LessonRepository extends JpaRepository<Lesson,Long> {

    List<Lesson> findAllByInstructorSet (User user);

//    @Query("select b from Book b where b.publisher.idd = ?1")
//
//    @Query("select b from Blog b join fetch b.tags where b.name = :name")
//    Blog getBlog(@Param("name") String blogName);
//
//    @Query("select l from Lesson l join fetch l.instructorSet where l.=?1")
//    Optional<Lesson> getLessonOfInstuctor(String username);

}
