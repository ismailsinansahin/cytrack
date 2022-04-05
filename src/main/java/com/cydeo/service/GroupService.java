package com.cydeo.service;

import com.cydeo.dto.BatchDTO;
import com.cydeo.dto.BatchGroupStudentDTO;
import com.cydeo.dto.GroupDTO;
import com.cydeo.dto.UserDTO;
import com.cydeo.entity.BatchGroupStudent;
import com.cydeo.entity.UserRole;

import java.util.List;
import java.util.Map;

public interface GroupService {

    GroupDTO getGroupById(Long id);
    Map<GroupDTO, List<Integer>> getGroupsWithNumberOfStudentsMap(Long batchId);
    GroupDTO getCurrentGroup(Long studentId);
    List<GroupDTO> getAllGroupsOfBatch(Long batchId);
    GroupDTO create(GroupDTO groupDTO, Long batchId);
    GroupDTO save(GroupDTO groupDTO, Long groupId, Long batchId);
    void delete(Long id);
    Boolean isDeletionSafe(Long groupId);

    List<BatchDTO> getAllNonCompletedBatches();
    BatchDTO getBatchById(Long batchId);
    BatchDTO getBatchByGroup(GroupDTO groupDTO);
    List<BatchDTO> getAllBatches();
    List<UserDTO> getAllUsersByRole(String userRoleName);
    List<UserDTO> getAllStudentsOfBatch(Long batchId);

    void assignStudentToGroup(UserDTO studentDTO, Long batchId);
    List<BatchGroupStudentDTO> getBatchGroupStudentsOfBatch(Long batchId);
    Map<Long, GroupDTO> getBatchGroupStudentMap(Long batchId);

}
