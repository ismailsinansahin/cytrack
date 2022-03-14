package com.cydeo.implementation;

import com.cydeo.dto.BatchDTO;
import com.cydeo.dto.GroupDTO;
import com.cydeo.dto.UserDTO;
import com.cydeo.entity.*;
import com.cydeo.enums.BatchStatus;
import com.cydeo.enums.StudentStatus;
import com.cydeo.enums.TaskStatus;
import com.cydeo.mapper.MapperUtil;
import com.cydeo.repository.BatchRepository;
import com.cydeo.repository.GroupRepository;
import com.cydeo.repository.UserRepository;
import com.cydeo.repository.UserRoleRepository;
import com.cydeo.service.GroupService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class GroupServiceImpl implements GroupService {

    private final MapperUtil mapperUtil;
    private final UserRepository userRepository;
    private final GroupRepository groupRepository;
    private final BatchRepository batchRepository;
    private final UserRoleRepository userRoleRepository;

    public GroupServiceImpl(UserRepository userRepository, GroupRepository groupRepository,
                            BatchRepository batchRepository, MapperUtil mapperUtil,
                            UserRoleRepository userRoleRepository) {
        this.userRepository = userRepository;
        this.groupRepository = groupRepository;
        this.batchRepository = batchRepository;
        this.mapperUtil = mapperUtil;
        this.userRoleRepository = userRoleRepository;
    }

    @Override
    public List<GroupDTO> getAllGroups() {
        return groupRepository.findAll()
                .stream()
                .map(obj -> mapperUtil.convert(obj, new GroupDTO()))
                .collect(Collectors.toList());
    }

    @Override
    public List<UserDTO> getAllUsersByRole(String userRoleName) {
        return userRepository.findAllByUserRole(userRoleRepository.findByName(userRoleName))
                .stream()
                .map(obj -> mapperUtil.convert(obj, new UserDTO()))
                .collect(Collectors.toList());
    }

    @Override
    public List<UserDTO> getAllStudentsOfBatch(Long batchId) {
        Batch batch = batchRepository.findById(batchId).get();
        return userRepository.findAllByBatch(batch)
                .stream()
                .map(obj -> mapperUtil.convert(obj, new UserDTO()))
                .collect(Collectors.toList());
    }

    @Override
    public List<UserDTO> getAllStudentsOfGroup(Long groupId) {
        return userRepository.findAllByGroup(groupRepository.findById(groupId).get())
                .stream()
                .sorted(Comparator.comparing(User::getId))
                .map(obj -> mapperUtil.convert(obj, new UserDTO()))
                .collect(Collectors.toList());
    }

    @Override
    public GroupDTO getGroupById(Long id) {
        return mapperUtil.convert(groupRepository.findById(id).get(), new GroupDTO());
    }

    @Override
    public GroupDTO create(GroupDTO groupDTO, Long batchId) {
        Group group = mapperUtil.convert(groupDTO, new Group());
        Batch batch = batchRepository.findById(batchId).get();
        group.setBatch(batch);
        groupRepository.save(group);
        return mapperUtil.convert(group, groupDTO);
    }

    @Override
    public GroupDTO save(GroupDTO groupDTO, Long groupId, Long bathcId) {
        groupDTO.setId(groupId);
        Group group = mapperUtil.convert(groupDTO, new Group());
        group.setBatch(batchRepository.findById(bathcId).get());
        groupRepository.save(group);
        return mapperUtil.convert(group, groupDTO);
    }

    @Override
    public String delete(Long id) {
        Group group = groupRepository.findById(id).get();
        if(isDeletingSafe(group)){
            group.setIsDeleted(true);
            groupRepository.save(group);
            return "success";
        }
        return "failure";
    }

    public Boolean isDeletingSafe(Group group) {
        return (userRepository.findAllByGroup(group).size() < 1);
    }

    @Override
    public void deleteGroupWithoutStudentsAndMentors(Long groupId) {
        Group group = groupRepository.findById(groupId).get();
        List<User> studentList = userRepository.findAllByGroup(group);
        for (User user : studentList) {
            user.setGroup(null);
            userRepository.save(user);
        }
        group.setCydeoMentor(null);
        group.setAlumniMentor(null);
        group.setIsDeleted(true);
        groupRepository.save(group);
    }

    @Override
    public List<GroupDTO> getAllGroupsOfBatch(Long batchId) {
       return groupRepository.findAllByBatch(batchRepository.findById(batchId).get())
                .stream()
                .map(obj -> mapperUtil.convert(obj, new GroupDTO()))
                .peek(groupDTO -> groupDTO.setActiveStudents(getActiveStudents(groupDTO)))
                .peek(groupDTO -> groupDTO.setDroppedTransferredStudents(getDroppedTransferredStudents(groupDTO)))
                .collect(Collectors.toList());
    }

    private int getActiveStudents(GroupDTO groupDTO) {
        return (int) userRepository.findAllByGroup(groupRepository.findById(groupDTO.getId()).get())
                .stream()
                .filter(student -> student.getStudentStatus().equals(StudentStatus.NEW) ||
                        student.getStudentStatus().equals(StudentStatus.RETURNING))
                .count();
    }

    private int getDroppedTransferredStudents(GroupDTO groupDTO) {
        return (int) userRepository.findAllByGroup(groupRepository.findById(groupDTO.getId()).get())
                .stream()
                .filter(student -> student.getStudentStatus().equals(StudentStatus.DROPPED) ||
                        student.getStudentStatus().equals(StudentStatus.TRANSFERRED))
                .count();
    }

    @Override
    public List<BatchDTO> getAllBatches() {
        return batchRepository.findAll()
                .stream()
                .map(obj -> mapperUtil.convert(obj, new BatchDTO()))
                .collect(Collectors.toList());
    }

    @Override
    public BatchDTO getBatchById(Long batchId) {
        return mapperUtil.convert(batchRepository.findById(batchId).get(), new BatchDTO());
    }

    @Override
    public List<BatchDTO> getAllNonCompletedBatches() {
        return batchRepository.findAllByBatchStatusIsNot(BatchStatus.COMPLETED)
                .stream()
                .map(obj -> mapperUtil.convert(obj, new BatchDTO()))
                .collect(Collectors.toList());
    }

    @Override
    public BatchDTO getLastOngoingBatch() {
        Batch batch = batchRepository.findAllByBatchStatusIsNot(BatchStatus.COMPLETED)
                .stream()
                .max(Comparator.comparing(Batch::getBatchStartDate)).get();
        return mapperUtil.convert(batch, new BatchDTO());
    }

    @Override
    public void assignStudentToGroup(UserDTO studentDTO) {
        User student = userRepository.findById(studentDTO.getId()).get();
        Group group = groupRepository.findById(studentDTO.getGroup().getId()).get();
        student.setGroup(group);
        userRepository.save(student);
    }

    @Override
    public UserDTO getUserById(Long userId) {
        return mapperUtil.convert(userRepository.findById(userId), new UserDTO());
    }

    @Override
    public void addStudent(Long studentId, Long groupId) {
        User student = userRepository.findById(studentId).get();
        Group group = groupRepository.findById(groupId).get();
        student.setGroup(group);
        userRepository.save(student); }

    @Override
    public void removeStudent(Long studentId) {
        User student = userRepository.findById(studentId).get();
        student.setGroup(null);
        userRepository.save(student);
    }

}
