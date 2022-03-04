package com.cydeo.service;

import com.cydeo.dto.BatchDTO;
import com.cydeo.dto.GroupDTO;
import com.cydeo.dto.UserDTO;

import java.util.List;

public interface StatisticsService {

    List<BatchDTO> getAllBatches();
    List<GroupDTO> getAllGroupsOfBatch(Long batchId);
    List<UserDTO> getAllStudentsOfGroup(Long GroupId);
    UserDTO findStudentById(Long studentId);

}
