package com.cydeo.service;

import com.cydeo.dto.BatchDTO;
import com.cydeo.dto.StudentTaskDTO;
import com.cydeo.dto.UserDTO;
import com.cydeo.entity.StudentTask;
import com.cydeo.entity.User;
import com.cydeo.repository.StudentTaskRepository;

import java.util.List;
import java.util.Map;

public interface DashboardService {

    UserDTO getCurrentUser();
    List<StudentTaskDTO> getAllTasksOfCurrentStudent();
    Map<String, Integer> getTaskBasedNumbers();
    Map<String, Integer> getWeekBasedNumbers();
    void completeStudentTask(Long batchId);
    void uncompleteStudentTask(Long batchId);
    List<BatchDTO> getAllBatches();

}
