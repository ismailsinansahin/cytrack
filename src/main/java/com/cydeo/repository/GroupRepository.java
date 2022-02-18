package com.cydeo.repository;

import com.cydeo.entity.Group;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface GroupRepository extends JpaRepository<Group, Long> {

    List<Group> findAll();
    List<Group> findAllByCydeoMentorEmail(String username);
    List<Group> findAllByAlumniMentorEmail(String username);

}
