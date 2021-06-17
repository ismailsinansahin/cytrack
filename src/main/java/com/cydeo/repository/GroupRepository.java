package com.cydeo.repository;

import com.cydeo.entity.Group;
import com.cydeo.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface GroupRepository extends JpaRepository<Group, Long> {

    List<Group> findAllByCybertekMentorEmail(String username);
    List<Group> findAllByAlumniMentorEmail(String username);

}
