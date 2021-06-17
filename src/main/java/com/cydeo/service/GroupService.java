package com.cydeo.service;

import com.cydeo.dto.GroupDTO;

import java.util.List;

public interface GroupService {

    List<GroupDTO> listAllGroups();
    List<GroupDTO> listAllGroupsOfCybertekMentor(String username);
    List<GroupDTO> listAllGroupsOfAlumniMentor(String username);

}
