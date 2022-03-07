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
        Group group = groupRepository.findById(groupId).get();
        BatchDTO batchDTO = mapperUtil.convert(group.getBatch(), new BatchDTO());
        UserDTO cydeoMentorDTO = mapperUtil.convert(group.getCydeoMentor(), new UserDTO());
        UserDTO alumniMentorDTO = mapperUtil.convert(group.getAlumniMentor(), new UserDTO());
        List<User> studentList = userRepository.findAllByGroup(group);
        List<UserDTO> studentDTOList = new ArrayList<>();
        for(User student : studentList){
            GroupDTO groupDTO = mapperUtil.convert(group, new GroupDTO());
            UserDTO studentDTO = mapperUtil.convert(student, new UserDTO());
            groupDTO.setBatch(batchDTO);
            groupDTO.setCydeoMentor(cydeoMentorDTO);
            groupDTO.setAlumniMentor(alumniMentorDTO);
            studentDTO.setGroup(groupDTO);
            studentDTOList.add(studentDTO);
        }
        return studentDTOList;
    }

}
