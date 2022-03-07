package com.cydeo.service;

import com.cydeo.dto.UserDTO;

import java.util.List;

public interface GroupStatisticsService {

    List<UserDTO> getAllStudentsOfGroup(Long groupId);

}
