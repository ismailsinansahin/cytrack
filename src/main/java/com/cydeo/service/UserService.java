package com.cydeo.service;

import com.cydeo.dto.BatchDTO;
import com.cydeo.dto.GroupDTO;
import com.cydeo.dto.UserDTO;
import com.cydeo.dto.UserRoleDTO;
import com.cydeo.enums.StudentStatus;

import java.util.List;
import java.util.Map;

public interface UserService {

    List<UserRoleDTO> getAllUserRoles();
    List<UserDTO> getAllStaffs();
    List<UserDTO> getAllStudents();
    List<UserDTO> getAllStudentsByBatch(Long batchId);
    List<UserDTO> getAllStudentsByGroup(Long groupId);
    UserDTO getUserById(Long userId);
    UserDTO saveStaff(UserDTO userDTO);
    UserDTO updateStudent(UserDTO userDTO);
    UserDTO createBatchStudent(UserDTO userDTO, Long batchId);
    UserDTO updateBatchStudent(UserDTO userDTO, Long batchId, Long groupId);
    UserDTO drop(Long batchId, Long studentId);
    Boolean isDeletionSafe(Long userId);
    void deleteStaff(Long userId);
    void deleteStudent(Long userId);
    String getDeleteErrorMessage(Long userId);
    List<BatchDTO> getStudentPossibleBatches(Long studentId);
    BatchDTO getBatchById(Long batchId);
    Map<UserDTO, List<Object>> getStudentsWithGroupNumbersAndStudentStatusMap(Long batchId);
    Map<UserDTO, StudentStatus> getStudentsWithStudentStatusMap();
    Map<UserDTO, StudentStatus> getStudentsWithStudentStatusMap(Long batchId, Long groupId);
    StudentStatus getStudentWithStudentStatus(Long studentId);
    List<GroupDTO> getAllGroupsOfBatch(Long batchId);
    GroupDTO getGroupById(Long groupId);
    GroupDTO getStudentGroup(Long batchId, Long studentId);
    StudentStatus getStudentStatus(Long studentId);

}
