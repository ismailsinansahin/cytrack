package com.cydeo.repository;

import com.cydeo.entity.Lesson;
import com.cydeo.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository <User,Long>{

    List<User> findAllByRoleDescriptionIgnoreCase(String role);
    User findByEmail(String email);
    List<User> findAllByLessonSet(Lesson lesson);

}
