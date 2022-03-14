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
    UserDTO getUserById(Long id);
    UserDTO save(UserDTO userDTO);
    String delete(Long id);
    UserDTO drop(Long id);
    List<BatchDTO> getAllBatches();
    String getDeleteErrorMessage(Long id);

}
