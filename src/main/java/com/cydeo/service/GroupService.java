package com.cydeo.service;

import com.cydeo.dto.BatchDTO;
import com.cydeo.dto.GroupDTO;
import com.cydeo.dto.UserDTO;
import com.cydeo.enums.UserRole;

import java.util.List;

public interface GroupService {

    List<GroupDTO> getAllGroups();
    List<UserDTO> getAllUsersByRole(UserRole userRole);
    GroupDTO getGroupById(Long id);
    GroupDTO create(GroupDTO groupDTO);
    GroupDTO save(GroupDTO groupDTO, Long groupId);
    void delete(Long id);
    List<BatchDTO> getAllNonCompletedBatches();

}
