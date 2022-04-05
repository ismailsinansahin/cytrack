package com.cydeo.implementation;

import com.cydeo.dto.BatchDTO;
import com.cydeo.dto.BatchGroupStudentDTO;
import com.cydeo.dto.GroupDTO;
import com.cydeo.dto.UserDTO;
import com.cydeo.entity.*;
import com.cydeo.enums.BatchStatus;
import com.cydeo.enums.StudentStatus;
import com.cydeo.mapper.MapperUtil;
import com.cydeo.repository.*;
import com.cydeo.service.GroupService;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class GroupServiceImpl implements GroupService {

    private final MapperUtil mapperUtil;
    private final UserRepository userRepository;
    private final GroupRepository groupRepository;
    private final BatchRepository batchRepository;
    private final UserRoleRepository userRoleRepository;
    private final BatchGroupStudentRepository batchGroupStudentRepository;

    public GroupServiceImpl(UserRepository userRepository, GroupRepository groupRepository,
                            BatchRepository batchRepository, MapperUtil mapperUtil,
                            UserRoleRepository userRoleRepository,
                            BatchGroupStudentRepository batchGroupStudentRepository) {
        this.userRepository = userRepository;
        this.groupRepository = groupRepository;
        this.batchRepository = batchRepository;
        this.mapperUtil = mapperUtil;
        this.userRoleRepository = userRoleRepository;
        this.batchGroupStudentRepository = batchGroupStudentRepository;
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
        return batchGroupStudentRepository.findAllByBatch(batch)
                .stream()
                .map(BatchGroupStudent::getStudent)
                .filter(Objects::nonNull)
                .map(obj -> mapperUtil.convert(obj, new UserDTO()))
                .collect(Collectors.toList());
    }

    @Override
    public Map<GroupDTO, List<Integer>> getGroupsWithNumberOfStudentsMap(Long batchId) {
        Map<GroupDTO, List<Integer>> groupsWithNumberOfStudentsMap = new HashMap<>();
        Batch batch = batchRepository.findById(batchId).get();
        List<Group> allGroups = batchGroupStudentRepository.findAllByBatch(batch)
                .stream()
                .map(BatchGroupStudent::getGroup)
                .filter(Objects::nonNull)
                .distinct()
                .filter(group -> group.getId() != 1L)
                .collect(Collectors.toList());
        for (Group group : allGroups) {
            int activeStudents = getActiveStudents(batch, group);
            int droppedTransferredStudents = getDroppedTransferredStudents(batch, group);
            List<Integer> numberOfStudentsList = Arrays.asList(activeStudents, droppedTransferredStudents);
            GroupDTO groupDTO = mapperUtil.convert(group, new GroupDTO());
            groupsWithNumberOfStudentsMap.put(groupDTO, numberOfStudentsList);
        }
        return groupsWithNumberOfStudentsMap;
    }

    private int getActiveStudents(Batch batch, Group group) {
        List<BatchGroupStudent> batchGroupStudentList = batchGroupStudentRepository.findAllByBatchAndGroup(batch, group);
        return (int) batchGroupStudentList
                .stream()
                .filter(batchGroupStudent -> batchGroupStudent.getStudentStatus()  == StudentStatus.ACTIVE || batchGroupStudent.getStudentStatus() == StudentStatus.ALUMNI)
                .count();
    }

    private int getDroppedTransferredStudents(Batch batch, Group group) {
        List<BatchGroupStudent> batchGroupStudentList = batchGroupStudentRepository.findAllByBatchAndGroup(batch, group);
        return (int) batchGroupStudentList
                .stream()
                .filter(batchGroupStudent -> batchGroupStudent.getStudentStatus() == StudentStatus.DROPPED || batchGroupStudent.getStudentStatus() == StudentStatus.TRANSFERRED)
                .count();
    }

    @Override
    public GroupDTO getGroupById(Long id) {
        return mapperUtil.convert(groupRepository.findById(id).get(), new GroupDTO());
    }

    @Override
    public GroupDTO create(GroupDTO groupDTO, Long batchId) {
        Batch batch = batchRepository.findById(batchId).get();
        Group group = mapperUtil.convert(groupDTO, new Group());
        groupRepository.save(group);
        batchGroupStudentRepository.save(new BatchGroupStudent(batch, group, null, null));
        return mapperUtil.convert(group, groupDTO);
    }

    @Override
    public GroupDTO save(GroupDTO groupDTO, Long groupId, Long batchId) {
        groupDTO.setId(groupId);
        Group group = mapperUtil.convert(groupDTO, new Group());
        groupRepository.save(group);
        return mapperUtil.convert(group, groupDTO);
    }

    @Override
    public Boolean isDeletionSafe(Long groupId) {
        Group group = groupRepository.findById(groupId).get();
        long size = 0;
        try{
            size = batchGroupStudentRepository.findAllByGroup(group)
                    .stream()
                    .filter(batchGroupStudent -> batchGroupStudent.getStudent() != null)
                    .count();
        }catch (NullPointerException e){
            return true;
        }
        return size < 1;
    }

    @Override
    public void delete(Long id) {
        Group group = groupRepository.findById(id).get();
        deleteBatchGroupStudentOfGroup(group);
        group.setIsDeleted(true);
        groupRepository.save(group);
    }

    private void deleteBatchGroupStudentOfGroup(Group group) {
        List<BatchGroupStudent> batchGroupStudentList = batchGroupStudentRepository.findAllByGroup(group);
        for (BatchGroupStudent batchGroupStudent : batchGroupStudentList) {
            batchGroupStudent.setIsDeleted(true);
            batchGroupStudentRepository.save(batchGroupStudent);
        }
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
    public BatchDTO getBatchByGroup(GroupDTO groupDTO) {
        Group group = groupRepository.findById(groupDTO.getId()).get();
        return batchGroupStudentRepository.findAllByGroup(group)
                .stream()
                .map(BatchGroupStudent::getBatch)
                .map(obj -> mapperUtil.convert(obj, new BatchDTO()))
                .findFirst().get();
    }

    @Override
    public List<BatchDTO> getAllNonCompletedBatches() {
        return batchRepository.findAllByBatchStatusIsNot(BatchStatus.COMPLETED)
                .stream()
                .map(obj -> mapperUtil.convert(obj, new BatchDTO()))
                .collect(Collectors.toList());
    }

    @Override
    public void assignStudentToGroup(UserDTO studentDTO, Long batchId) {
        Batch batch = batchRepository.findById(batchId).get();
        Group newGroup = groupRepository.findById(studentDTO.getCurrentGroup().getId()).get();
        User student = userRepository.findById(studentDTO.getId()).get();
        BatchGroupStudent batchGroupStudent = batchGroupStudentRepository.findByBatchAndStudent(batch, student);
        batchGroupStudent.setGroup(newGroup);
        batchGroupStudentRepository.save(batchGroupStudent);
        student.setCurrentGroup(newGroup);
        userRepository.save(student);
    }

    @Override
    public List<BatchGroupStudentDTO> getBatchGroupStudentsOfBatch(Long batchId) {
        return batchGroupStudentRepository.findAllByBatch(batchRepository.findById(batchId).get())
                .stream()
                .map(obj -> mapperUtil.convert(obj, new BatchGroupStudentDTO()))
                .collect(Collectors.toList());
    }

    @Override
    public Map<Long, GroupDTO> getBatchGroupStudentMap(Long batchId) {
        Map<Long, GroupDTO> batchGroupStudentMap = new HashMap<>();
        Batch batch = batchRepository.findById(batchId).get();
        List<User> studentsOfBatch = batchGroupStudentRepository.findAllByBatch(batchRepository.findById(batchId).get())
                .stream()
                .map(BatchGroupStudent::getStudent)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
        for (User student : studentsOfBatch) {
            GroupDTO groupDTO;
            Group group;
            try{
                group = batchGroupStudentRepository.findByBatchAndStudent(batch, student).getGroup();
                groupDTO = mapperUtil.convert(group, new GroupDTO());
            }catch (IllegalArgumentException e){
                continue;
            }

            batchGroupStudentMap.put(student.getId(), groupDTO);
        }
        return batchGroupStudentMap;
    }

    @Override
    public GroupDTO getCurrentGroup(Long studentId) {
        User student = userRepository.findById(studentId).get();
        return batchGroupStudentRepository.findAllByStudent(student)
                .stream()
                .filter(obj -> obj.getBatch().getBatchStatus().equals(BatchStatus.INPROGRESS))
                .map(BatchGroupStudent::getGroup)
                .map(obj -> mapperUtil.convert(obj, new GroupDTO()))
                .findFirst().get();
    }

    @Override
    public List<GroupDTO> getAllGroupsOfBatch(Long batchId) {
        return batchGroupStudentRepository.findAllByBatch(batchRepository.findById(batchId).get())
                .stream()
                .map(BatchGroupStudent::getGroup)
                .filter(Objects::nonNull)
                .distinct()
                .filter(group -> group.getId() != 1L)
                .map(obj -> mapperUtil.convert(obj, new GroupDTO()))
                .collect(Collectors.toList());
    }

}