package com.cydeo.implementation;

import com.cydeo.dto.GroupDTO;
import com.cydeo.entity.Group;
import com.cydeo.mapper.MapperUtil;
import com.cydeo.repository.GroupRepository;
import com.cydeo.repository.UserRepository;
import com.cydeo.service.GroupService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class GroupServiceImpl implements GroupService {

    UserRepository userRepository;
    GroupRepository groupRepository;
    MapperUtil mapperUtil;

    public GroupServiceImpl(UserRepository userRepository, GroupRepository groupRepository, MapperUtil mapperUtil) {
        this.userRepository = userRepository;
        this.groupRepository = groupRepository;
        this.mapperUtil = mapperUtil;
    }

    @Override
    public List<GroupDTO> listAllGroups(){
        return groupRepository.findAll().stream().map(obj -> mapperUtil.convert(obj, new GroupDTO())).collect(Collectors.toList());
    }

    @Override
    public List<GroupDTO> listAllGroupsOfCybertekMentor(String username) {
        List<Group> groups = groupRepository.findAllByCybertekMentorEmail(username);
        return groups.stream().map(obj -> mapperUtil.convert(obj, new GroupDTO())).collect(Collectors.toList());
    }

    @Override
    public List<GroupDTO> listAllGroupsOfAlumniMentor(String username) {
        List<Group> groups = groupRepository.findAllByAlumniMentorEmail(username);
        return groups.stream().map(obj -> mapperUtil.convert(obj, new GroupDTO())).collect(Collectors.toList());
    }

}
