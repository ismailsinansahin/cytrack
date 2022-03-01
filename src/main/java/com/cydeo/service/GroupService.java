package com.cydeo.service;

import com.cydeo.dto.BatchDTO;
import com.cydeo.dto.GroupDTO;
import com.cydeo.dto.UserDTO;
import com.cydeo.entity.UserRole;

import java.util.List;

public interface GroupService {

    List<GroupDTO> getAllGroups();
    List<UserDTO> getAllUsersByRole(String userRoleName);
    List<UserDTO> getAllStudentsOfBatch(Long batchId);
    UserDTO getUserById(Long userId);
    GroupDTO getGroupById(Long id);
    GroupDTO create(GroupDTO groupDTO);
    GroupDTO save(GroupDTO groupDTO, Long groupId);
    void delete(Long id);
    List<BatchDTO> getAllGroupsOfBatch(Long batchId);
    List<BatchDTO> getAllNonCompletedBatches();
    BatchDTO getLastOngoingBatch();
    List<BatchDTO> getAllBatches();
    void assignStudentToGroup(UserDTO studentDTO);

}
