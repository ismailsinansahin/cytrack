package com.cydeo.implementation;

import com.cydeo.dto.BatchDTO;
import com.cydeo.dto.GroupDTO;
import com.cydeo.dto.UserDTO;
import com.cydeo.dto.UserRoleDTO;
import com.cydeo.entity.*;
import com.cydeo.enums.BatchStatus;
import com.cydeo.enums.StudentStatus;
import com.cydeo.mapper.MapperUtil;
import com.cydeo.repository.*;
import com.cydeo.service.UserService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    private final MapperUtil mapperUtil;
    private final UserRepository userRepository;
    private final BatchRepository batchRepository;
    private final UserRoleRepository userRoleRepository;
    private final InstructorLessonRepository instructorLessonRepository;
    private final GroupRepository groupRepository;
    private final PasswordEncoder passwordEncoder;
    private final BatchGroupStudentRepository batchGroupStudentRepository;

    public UserServiceImpl(MapperUtil mapperUtil, UserRepository userRepository, BatchRepository batchRepository,
                           UserRoleRepository userRoleRepository, InstructorLessonRepository instructorLessonRepository,
                           GroupRepository groupRepository, PasswordEncoder passwordEncoder,
                           BatchGroupStudentRepository batchGroupStudentRepository) {
        this.mapperUtil = mapperUtil;
        this.userRepository = userRepository;
        this.batchRepository = batchRepository;
        this.userRoleRepository = userRoleRepository;
        this.instructorLessonRepository = instructorLessonRepository;
        this.groupRepository = groupRepository;
        this.passwordEncoder = passwordEncoder;
        this.batchGroupStudentRepository = batchGroupStudentRepository;
    }

    @Override
    public List<UserRoleDTO> getAllUserRoles() {
        return userRoleRepository.findAll()
                .stream()
                .map(obj -> mapperUtil.convert(obj, new UserRoleDTO()))
                .collect(Collectors.toList());
    }

    @Override
    public List<UserDTO> getAllStaffs() {
        return userRepository.findAllByUserRoleNot(userRoleRepository.findByName("Student"))
                .stream()
                .map(obj -> mapperUtil.convert(obj, new UserDTO()))
                .collect(Collectors.toList());
    }

    @Override
    public List<UserDTO> getAllStudents() {
        return userRepository.findAllByUserRole(userRoleRepository.findByName("Student"))
                .stream()
                .map(student -> mapperUtil.convert(student, new UserDTO()))
                .collect(Collectors.toList());
    }

    @Override
    public List<UserDTO> getAllStudentsByBatch(Long batchId) {
        Batch batch = batchRepository.findById(batchId).get();
        if(batch.getBatchStatus().getValue().equals("In Progress")){
            return userRepository.findAllByCurrentBatch(batch)
                    .stream()
                    .map(student -> mapperUtil.convert(student, new UserDTO()))
                    .collect(Collectors.toList());
        }
        else {
            return batchGroupStudentRepository.findAllByBatch(batch)
                    .stream()
                    .map(BatchGroupStudent::getStudent)
                    .filter(Objects::nonNull)
                    .map(student -> mapperUtil.convert(student, new UserDTO()))
                    .collect(Collectors.toList());
        }
    }

    @Override
    public List<UserDTO> getAllStudentsByGroup(Long groupId) {
        return batchGroupStudentRepository.findAllByGroup(groupRepository.findById(groupId).get())
                .stream()
                .map(BatchGroupStudent::getStudent)
                .filter(Objects::nonNull)
                .map(student -> mapperUtil.convert(student, new UserDTO()))
                .collect(Collectors.toList());
    }

    @Override
    public UserDTO getUserById(Long id) {
        return mapperUtil.convert(userRepository.findById(id).get(), new UserDTO());
    }

    @Override
    public UserDTO saveStaff(UserDTO userDTO) {
        User user = (userDTO.getId() != null) ? userRepository.findById(userDTO.getId()).get() : new User();
        userDTO.setUserName(userDTO.getEmail());
        userDTO.setEnabled(true);
        user = mapperUtil.convert(userDTO, user);
        user.setUserRole(userRoleRepository.findByName(userDTO.getUserRole().getName()));
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
        return mapperUtil.convert(user, new UserDTO());
    }

    @Override
    public UserDTO updateStudent(UserDTO userDTO) {
        userDTO.setUserName(userDTO.getEmail());
        userDTO.setEnabled(true);
        userDTO.setStudentStatus(StudentStatus.DROPPED);
        User student = mapperUtil.convert(userDTO, new User());
        student.setUserRole(userRoleRepository.findByName("Student"));
        userRepository.save(student);
        if(!batchGroupStudentRepository.findAllByStudent(student)
                .stream()
                .map(BatchGroupStudent::getBatch)
                .collect(Collectors.toList())
                .contains(student.getCurrentBatch()))
            batchGroupStudentRepository.save(new BatchGroupStudent(student.getCurrentBatch(), null, student));
        return mapperUtil.convert(student, new UserDTO());
    }

    @Override
    public UserDTO createBatchStudent(UserDTO userDTO, Long batchId) {
        Batch batch = batchRepository.findById(batchId).get();
        Group group = (userDTO.getCurrentGroup() == null)
                ? null
                : groupRepository.findById(userDTO.getCurrentGroup().getId()).get();
        userDTO.setUserName(userDTO.getEmail());
        userDTO.setStudentStatus(StudentStatus.NEW);
        userDTO.setEnabled(true);
        User student = mapperUtil.convert(userDTO, new User());
        student.setUserRole(userRoleRepository.findByName("Student"));
        student.setPassword(passwordEncoder.encode(student.getPassword()));
        userRepository.save(student);
        if(batch.getBatchStatus().getValue().equals("In Progress")){
            student.setCurrentBatch(batch);
            student.setCurrentGroup(group);
        }else{
            student.setCurrentBatch(null);
            student.setCurrentGroup(null);
        }
        batchGroupStudentRepository.save(new BatchGroupStudent(batch, group, student));
        return mapperUtil.convert(student, new UserDTO());
    }

    @Override
    public UserDTO updateBatchStudent(UserDTO userDTO, Long batchId, Long groupId) {
        User student = userRepository.findByUserName(userDTO.getEmail());
        userDTO.setId(student.getId());
        Batch batch = batchRepository.findById(batchId).get();
        Group group = groupRepository.findById(groupId).get();
        userDTO.setUserName(userDTO.getEmail());
        userDTO.setStudentStatus(StudentStatus.NEW);
        userDTO.setEnabled(true);
        student = mapperUtil.convert(userDTO, userRepository.findByUserName(userDTO.getEmail()));
        student.setUserRole(userRoleRepository.findByName("Student"));
        student.setPassword(passwordEncoder.encode(student.getPassword()));
        student.setCurrentBatch(batch);
        student.setCurrentGroup(group);
        userRepository.save(student);
        return mapperUtil.convert(student, new UserDTO());
    }

    @Override
    public UserDTO drop(Long studentId) {
        User student = userRepository.findById(studentId).get();
        student.setCurrentBatch(null);
        student.setCurrentGroup(null);
        student.setStudentStatus(StudentStatus.DROPPED);
        userRepository.save(student);
        return mapperUtil.convert(student, new UserDTO());
    }

    @Override
    public Boolean isDeletionSafe(Long userId) {
        User user = userRepository.findById(userId).get();
        UserRole userRole = user.getUserRole();
        if(user.getUserRole().getName().equals("Admin")){
            return userRepository.findAllByUserRole(userRole).size() > 1;
        }
        else if(user.getUserRole().getName().equals("Instructor")){
            return instructorLessonRepository.findAllByInstructor(user).size() == 0;
        }
        else if(user.getUserRole().getName().equals("Cydeo Mentor")){
            return groupRepository.findAllByCydeoMentor(user).size() == 0;
        }
        else if(user.getUserRole().getName().equals("Alumni Mentor")){
            return groupRepository.findAllByAlumniMentor(user).size() == 0;
        }else {
            return true;
        }
    }

    @Override
    public void deleteStaff(Long staffId) {
        User user = userRepository.findById(staffId).get();
        user.setIsDeleted(true);
        userRepository.save(user);
    }

    @Override
    public void deleteStudent(Long studentId) {
        User student = userRepository.findById(studentId).get();
        List<BatchGroupStudent> batchGroupStudentList = batchGroupStudentRepository.findAllByStudent(student);
        for (BatchGroupStudent batchGroupStudent : batchGroupStudentList) {
            batchGroupStudent.setIsDeleted(true);
            batchGroupStudentRepository.save(batchGroupStudent);
        }
        student.setIsDeleted(true);
        userRepository.save(student);
    }

    @Override
    public String getDeleteErrorMessage(Long id) {
        User user = userRepository.findById(id).get();
        if(user.getUserRole().getName().equals("Admin")){
            return "The last admin cannot be deleted!";
        }
        else if(user.getUserRole().getName().equals("Instructor")){
            return "The instructor cannot be deleted, (s)he has lesson(s)!";
        }
        else{
            return "The mentor cannot be deleted, (s)he has group(s)!";
        }
    }

    @Override
    public List<BatchDTO> getStudentPossibleBatches(Long studentId) {
        List<BatchDTO> possibleBatchList = new ArrayList<>();
        User student= userRepository.findById(studentId).get();
        if(student.getCurrentBatch() != null) {
            possibleBatchList.add(mapperUtil.convert(student.getCurrentBatch(), new BatchDTO()));
        }else{
            possibleBatchList.addAll(batchRepository.findAllByBatchStatusIsNot(BatchStatus.COMPLETED)
                    .stream()
                    .map(batch -> mapperUtil.convert(batch, new BatchDTO()))
                    .collect(Collectors.toList()));
        }
        return possibleBatchList;
    }

    @Override
    public BatchDTO getBatchById(Long batchId) {
        return mapperUtil.convert(batchRepository.findById(batchId).get(), new BatchDTO());
    }

    @Override
    public Map<Long, GroupDTO> getStudentsGroupNumbersMap(Long batchId) {
        Map<Long, GroupDTO> studentsGroupNumbersMap = new HashMap<>();
        Batch batch = batchRepository.findById(batchId).get();
        List<BatchGroupStudent> batchGroupStudentList = batchGroupStudentRepository.findAllByBatch(batch)
                .stream()
                .filter(obj -> obj.getStudent()!=null)
                .collect(Collectors.toList());
        for (BatchGroupStudent batchGroupStudent : batchGroupStudentList) {
            Long userId = batchGroupStudent.getStudent().getId();
            try{
                GroupDTO groupDTO = mapperUtil.convert(batchGroupStudent.getGroup(), new GroupDTO());
                studentsGroupNumbersMap.put(userId, groupDTO);
            }catch (IllegalArgumentException e){
                studentsGroupNumbersMap.put(userId, null);
            }
        }
        return studentsGroupNumbersMap;
    }

    @Override
    public List<GroupDTO> getAllGroupsOfBatch(Long batchId) {
        return batchGroupStudentRepository.findAllByBatch(batchRepository.findById(batchId).get())
                .stream()
                .map(BatchGroupStudent::getGroup)
                .distinct()
                .filter(Objects::nonNull)
                .map(obj -> mapperUtil.convert(obj, new GroupDTO()))
                .collect(Collectors.toList());
    }

    @Override
    public GroupDTO getGroupById(Long groupId) {
        return mapperUtil.convert(groupRepository.findById(groupId).get(), new GroupDTO());
    }

    @Override
    public GroupDTO getStudentGroup(Long batchId, Long studentId) {
        Batch batch = batchRepository.findById(batchId).get();
        User student = userRepository.findById(studentId).get();
        try {
            return mapperUtil.convert(batchGroupStudentRepository.findByBatchAndStudent(batch, student).getGroup(), new GroupDTO());
        }catch (IllegalArgumentException e){
            return null;
        }
    }

}
