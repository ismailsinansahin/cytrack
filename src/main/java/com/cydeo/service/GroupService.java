package com.cydeo.service;

import com.cydeo.dto.BatchDTO;
import com.cydeo.dto.GroupDTO;
import com.cydeo.dto.UserDTO;
import com.cydeo.entity.UserRole;

import java.util.List;

public interface GroupService {

    GroupDTO getGroupById(Long id);
    List<GroupDTO> getAllGroups();
    List<UserDTO> getAllUsersByRole(String userRoleName);
    List<UserDTO> getAllStudentsOfGroup(Long groupId);
    List<UserDTO> getAllStudentsOfBatch(Long batchId);
    GroupDTO create(GroupDTO groupDTO);
    GroupDTO save(GroupDTO groupDTO, Long groupId);
    void delete(Long id);
    void addStudent(Long studentId, Long groupId);
    void removeStudent(Long studentId);
    List<BatchDTO> getAllNonCompletedBatches();
    BatchDTO getLastOngoingBatch();
    List<BatchDTO> getAllBatches();
    void assignStudentToGroup(UserDTO studentDTO);
    UserDTO getUserById(Long userId);
    List<BatchDTO> getAllGroupsOfBatch(Long batchId);

}
