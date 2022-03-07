package com.cydeo.service;

import com.cydeo.dto.StudentTaskDTO;
import com.cydeo.dto.TaskDTO;
import com.cydeo.dto.UserDTO;

import java.util.List;
import java.util.Map;

public interface StudentStatisticsService {

    UserDTO findStudentById(Long studentId);
    List<StudentTaskDTO> getAllTasksOfStudent(Long studentId);
    Map<String, Integer> getTaskBasedNumbers(Long studentId);
    Map<String, Integer> getWeekBasedNumbers(Long studentId);

}
