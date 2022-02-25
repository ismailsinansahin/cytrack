package com.cydeo.repository;

import com.cydeo.entity.StudentTask;
import com.cydeo.entity.Task;
import com.cydeo.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StudentTaskRepository extends JpaRepository<StudentTask, Long> {

    List<StudentTask> findAllByStudent(User Student);
    StudentTask findByStudentAndTask(User student, Task task);

}
