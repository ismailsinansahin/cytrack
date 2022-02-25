package com.cydeo.service;

import com.cydeo.dto.BatchDTO;
import com.cydeo.dto.UserDTO;
import com.cydeo.enums.UserRole;

import java.util.List;
import java.util.Map;

public interface UserService {

    List<UserDTO> listAllUsers();
    List<UserDTO> getAllStaffs();
    List<UserDTO> getAllStudents();
    UserDTO getUserById(Long id);
    UserDTO save(UserDTO userDTO);
    void delete(Long id);
    List<BatchDTO> getAllBatches();
    UserDTO getUserByEmail(String email);

}
