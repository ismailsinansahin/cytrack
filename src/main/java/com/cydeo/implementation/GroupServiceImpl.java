package com.cydeo.implementation;

import com.cydeo.dto.BatchDTO;
import com.cydeo.dto.GroupDTO;
import com.cydeo.dto.UserDTO;
import com.cydeo.entity.Batch;
import com.cydeo.entity.Group;
import com.cydeo.entity.User;
import com.cydeo.entity.UserRole;
import com.cydeo.enums.BatchStatus;
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
    public List<GroupDTO> getAllGroups(){
//        List<GroupDTO> groupDTOList = new ArrayList<>();
        List<Group> groups = groupRepository.findAll();
//        for (Group group : groups){
//            BatchDTO batchDTO = mapperUtil.convert(group.getBatch(), new BatchDTO());
//            UserDTO cydeoMentorDTO = mapperUtil.convert(group.getCydeoMentor(), new UserDTO());
//            UserDTO alumniMentorDTO = mapperUtil.convert(group.getAlumniMentor(), new UserDTO());
//            GroupDTO groupDTO = mapperUtil.convert(group, new GroupDTO());
//            groupDTO.setBatch(batchDTO);
//            groupDTO.setCydeoMentor(cydeoMentorDTO);
//            groupDTO.setAlumniMentor(alumniMentorDTO);
//            groupDTOList.add(groupDTO);
//        }
        return groups.stream()
                .map(group -> mapperUtil.convert(group,new GroupDTO()))
                .collect(Collectors.toList());
    }

    @Override
    public List<UserDTO> getAllUsersByRole(String userRoleName) {
        UserRole userRole = userRoleRepository.findByName(userRoleName);
        return userRepository.findAllByUserRole(userRole)
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
    public GroupDTO getGroupById(Long id) {
        return mapperUtil.convert(groupRepository.findById(id).get(), new GroupDTO());
    }

    @Override
    public GroupDTO create(GroupDTO groupDTO) {
        Group group = mapperUtil.convert(groupDTO, new Group());
        groupRepository.save(group);
        return mapperUtil.convert(group, groupDTO);
    }

    @Override
    public GroupDTO save(GroupDTO groupDTO, Long groupId) {
        groupDTO.setId(groupId);
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

    @Override
    public List<BatchDTO> getAllGroupsOfBatch(Long batchId) {
        Batch batch = batchRepository.findById(batchId).get();
        return groupRepository.findAllByBatch(batch)
                .stream()
                .map(obj -> mapperUtil.convert(obj, new BatchDTO()))
                .collect(Collectors.toList());
    }

    @Override
    public List<BatchDTO> getAllBatches() {
        return batchRepository.findAll()
                .stream()
                .map(obj -> mapperUtil.convert(obj, new BatchDTO()))
                .collect(Collectors.toList());
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
        Group group = mapperUtil.convert(studentDTO.getGroup(), new Group());
        student.setGroup(group);
        userRepository.save(student);
    }

    @Override
    public UserDTO getUserById(Long userId) {
        return mapperUtil.convert(userRepository.findById(userId), new UserDTO());
    }

}
