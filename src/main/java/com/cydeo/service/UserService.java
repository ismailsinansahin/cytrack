package com.cydeo.service;

import com.cydeo.dto.UserDTO;

import java.util.List;
import java.util.Map;

public interface UserService {

    List<UserDTO> listAllUsersByRole(String role);
    Map<UserDTO, String> getInstructorsAndLessonsMap();
    Map<UserDTO, String> getCybertekMentorsAndGroupsMap();
    Map<UserDTO, String> getAlumniMentorsAndGroupsMap();

}
