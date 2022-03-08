package com.cydeo.service;

import com.cydeo.dto.GroupDTO;
import com.cydeo.dto.StudentTaskDTO;
import com.cydeo.dto.UserDTO;

import java.util.List;
import java.util.Map;

public interface GroupStatisticsService {

    GroupDTO getGroupById(Long groupId);
    List<UserDTO> getAllStudentsOfGroup(Long groupId);
    Map<String, Integer> getTaskBasedNumbers(Long groupId);
    Map<String, Integer> getWeekBasedNumbers(Long groupId);

}
