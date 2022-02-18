package com.cydeo.service;

import com.cydeo.dto.GroupDTO;

import java.util.List;

public interface GroupService {

    List<GroupDTO> listAllGroups();
    List<GroupDTO> listAllGroupsOfCydeoMentor(String username);
    List<GroupDTO> listAllGroupsOfAlumniMentor(String username);
    GroupDTO getGroupById(Long id);
    GroupDTO save(GroupDTO groupDTO);
    void delete(Long id);

}
