package com.cydeo.implementation;

import com.cydeo.dto.BatchDTO;
import com.cydeo.dto.GroupDTO;
import com.cydeo.dto.UserDTO;
import com.cydeo.entity.Group;
import com.cydeo.entity.User;
import com.cydeo.mapper.MapperUtil;
import com.cydeo.repository.GroupRepository;
import com.cydeo.repository.UserRepository;
import com.cydeo.service.GroupStatisticsService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class GroupStatisticsServiceImpl implements GroupStatisticsService {

    private final MapperUtil mapperUtil;
    private final UserRepository userRepository;
    private final GroupRepository groupRepository;

    public GroupStatisticsServiceImpl(MapperUtil mapperUtil, UserRepository userRepository,
                                      GroupRepository groupRepository) {
        this.mapperUtil = mapperUtil;
        this.userRepository = userRepository;
        this.groupRepository = groupRepository;
    }

    @Override
    public List<UserDTO> getAllStudentsOfGroup(Long groupId) {
        return userRepository.findAllByGroup(groupRepository.findById(groupId).get())
                .stream()
                .map(obj -> mapperUtil.convert(obj, new UserDTO()))
                .collect(Collectors.toList());
    }

}
