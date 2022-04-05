package com.cydeo.service;

import com.cydeo.dto.GroupDTO;
import com.cydeo.dto.StudentTaskDTO;
import com.cydeo.dto.UserDTO;
import com.cydeo.enums.StudentStatus;

import java.util.List;
import java.util.Map;

public interface GroupStatisticsService {

    GroupDTO getGroupById(Long groupId);
    Map<UserDTO, StudentStatus> getStudentsWithStudentStatusMap(Long batchId, Long groupId);
    List<UserDTO> getAllStudentsOfGroup(Long groupId);
    Map<String, Integer> getTaskBasedNumbers(Long groupId);
    Map<String, Integer> getWeekBasedNumbers(Long groupId);
    GroupDTO getGroupWithNumberOfStudents(Long groupId);

}
