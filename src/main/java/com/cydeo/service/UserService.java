package com.cydeo.service;

import com.cydeo.dto.UserDTO;
import com.cydeo.enums.UserRole;

import java.util.List;
import java.util.Map;

public interface UserService {

    List<UserDTO> listAllUsers();
    List<UserDTO> listAllUsersByRole(UserRole userRole);
    UserDTO getUserById(Long id);
    UserDTO save(UserDTO userDTO);
    void delete(Long id);

    List<UserDTO> listAllInstructorsOfLesson(Long lessonId);
//    Map<UserDTO, String> getCydeoMentorsAndGroupsMap();
//    Map<UserDTO, String> getAlumniMentorsAndGroupsMap();

}
