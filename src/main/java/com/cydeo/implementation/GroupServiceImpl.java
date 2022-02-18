package com.cydeo.implementation;

import com.cydeo.dto.GroupDTO;
import com.cydeo.entity.Group;
import com.cydeo.mapper.MapperUtil;
import com.cydeo.repository.GroupRepository;
import com.cydeo.repository.UserRepository;
import com.cydeo.service.GroupService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
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
        List<Group> groups = groupRepository.findAll();
        return groups.stream().map(obj -> mapperUtil.convert(obj, new GroupDTO())).collect(Collectors.toList());
    }

    @Override
    public List<GroupDTO> listAllGroupsOfCydeoMentor(String username) {
        List<Group> groups = groupRepository.findAllByCydeoMentorEmail(username);
        return groups.stream().map(obj -> mapperUtil.convert(obj, new GroupDTO())).collect(Collectors.toList());
    }

    @Override
    public List<GroupDTO> listAllGroupsOfAlumniMentor(String username) {
        List<Group> groups = groupRepository.findAllByAlumniMentorEmail(username);
        return groups.stream().map(obj -> mapperUtil.convert(obj, new GroupDTO())).collect(Collectors.toList());
    }

    @Override
    public GroupDTO getGroupById(Long id) {
        return mapperUtil.convert(groupRepository.findById(id).get(), new GroupDTO());
    }

    @Override
    public GroupDTO save(GroupDTO groupDTO) {
        Group group = mapperUtil.convert(groupDTO, new Group());
        groupRepository.save(group);
        return mapperUtil.convert(group, groupDTO);
    }

    @Override
    public void delete(Long id) {
        Group group = groupRepository.findById(id).get();
        group.setIsDeleted(true);
        groupRepository.save(group);
    }
}
