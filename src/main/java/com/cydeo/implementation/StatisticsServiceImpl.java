package com.cydeo.implementation;

import com.cydeo.dto.BatchDTO;
import com.cydeo.dto.GroupDTO;
import com.cydeo.dto.UserDTO;
import com.cydeo.entity.Batch;
import com.cydeo.entity.Group;
import com.cydeo.mapper.MapperUtil;
import com.cydeo.repository.*;
import com.cydeo.service.StatisticsService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class StatisticsServiceImpl implements StatisticsService {

    private final MapperUtil mapperUtil;
    private final BatchRepository batchRepository;
    private final GroupRepository groupRepository;
    private final UserRepository userRepository;
    private final LessonRepository lessonRepository;
    private final TaskRepository taskRepository;

    public StatisticsServiceImpl(MapperUtil mapperUtil, BatchRepository batchRepository, GroupRepository groupRepository,
                                 UserRepository userRepository, LessonRepository lessonRepository,
                                 TaskRepository taskRepository) {
        this.mapperUtil = mapperUtil;
        this.batchRepository = batchRepository;
        this.groupRepository = groupRepository;
        this.userRepository = userRepository;
        this.lessonRepository = lessonRepository;
        this.taskRepository = taskRepository;
    }

    @Override
    public List<BatchDTO> getAllBatches() {
        return batchRepository.findAll()
                .stream()
                .map(obj -> mapperUtil.convert(obj, new BatchDTO()))
                .collect(Collectors.toList());
    }

    @Override
    public List<GroupDTO> getAllGroupsOfBatch(Long batchId) {
        Batch batch = batchRepository.findById(batchId).get();
        return groupRepository.findAllByBatch(batch)
                .stream()
                .map(obj -> mapperUtil.convert(obj, new GroupDTO()))
                .collect(Collectors.toList());
    }

    @Override
    public List<UserDTO> getAllStudentsOfGroup(Long groupId) {
        Group group = groupRepository.findById(groupId).get();
        return userRepository.findAllByGroup(group)
                .stream()
                .map(obj -> mapperUtil.convert(obj, new UserDTO()))
                .collect(Collectors.toList());
    }

    @Override
    public UserDTO findStudentById(Long studentId) {
        return mapperUtil.convert(userRepository.findById(studentId).get(), new UserDTO());
    }

}
