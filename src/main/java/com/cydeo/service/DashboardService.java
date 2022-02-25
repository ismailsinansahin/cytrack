package com.cydeo.service;

import com.cydeo.entity.StudentTask;
import com.cydeo.entity.User;
import com.cydeo.repository.StudentTaskRepository;

import java.util.List;

public interface DashboardService {

    List<StudentTask> getAllTasksOfStudent(Long currentStudentId);
    void completeStudentTask(Long studentId, Long bathcId);
    void uncompleteStudentTask(Long studentId, Long bathcId);

    List<User> getAllStudentsOfMentor(Long mentorId);

}
