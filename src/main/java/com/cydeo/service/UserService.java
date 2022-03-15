package com.cydeo.service;

import com.cydeo.dto.BatchDTO;
import com.cydeo.dto.UserDTO;
import com.cydeo.dto.UserRoleDTO;

import java.util.List;

public interface UserService {

    List<UserDTO> listAllUsers();
    List<UserRoleDTO> getAllUserRoles();
    List<UserDTO> getAllStaffs();
    List<UserDTO> getAllStudents();
    List<UserDTO> getAllStudentsByBatch(Long batchId);
    UserDTO getUserById(Long id);
    BatchDTO getBatchById(Long id);
    UserDTO save(UserDTO userDTO);
    UserDTO save(UserDTO userDTO, Long batchId);
    String delete(Long id);
    UserDTO drop(Long id);
    List<BatchDTO> getAllBatches();
    String getDeleteErrorMessage(Long id);

}
