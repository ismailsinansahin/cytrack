package com.cydeo.repository;

import com.cydeo.entity.StudentTask;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StudentTaskRepository extends JpaRepository<StudentTask, Long> {
}
