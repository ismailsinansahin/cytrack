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
        return batchGroupStudentRepository.findAllByBatch(batch)
                .stream()
                .map(BatchGroupStudent::getStudent)
                .filter(Objects::nonNull)
                .map(student -> mapperUtil.convert(student, new UserDTO()))
                .collect(Collectors.toList());
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
        Batch oldBatch = userRepository.findById(userDTO.getId()).get().getCurrentBatch();
        userDTO.setUserName(userDTO.getEmail());
        userDTO.setEnabled(true);
        User student = mapperUtil.convert(userDTO, new User());
        student.setUserRole(userRoleRepository.findByName("Student"));
        userRepository.save(student);
        if(student.getCurrentBatch() != oldBatch){
            Group noGroup = groupRepository.findById(1L).get();
            batchGroupStudentRepository.save(new BatchGroupStudent(student.getCurrentBatch(), noGroup, student, StudentStatus.ACTIVE));
        }
        return mapperUtil.convert(student, new UserDTO());
    }

    @Override
    public UserDTO createBatchStudent(UserDTO userDTO, Long batchId) {
        Batch batch = batchRepository.findById(batchId).get();
        Group group = (userDTO.getCurrentGroup() == null)
                ? null
                : groupRepository.findById(userDTO.getCurrentGroup().getId()).get();
        userDTO.setUserName(userDTO.getEmail());
        userDTO.setEnabled(true);
        User student = mapperUtil.convert(userDTO, new User());
        student.setUserRole(userRoleRepository.findByName("Student"));
        student.setPassword(passwordEncoder.encode(student.getPassword()));
        userRepository.save(student);
        student.setCurrentBatch(batch);
        student.setCurrentGroup(group);
        batchGroupStudentRepository.save(new BatchGroupStudent(batch, group, student, StudentStatus.ACTIVE));
        return mapperUtil.convert(student, new UserDTO());
    }

    @Override
    public UserDTO updateBatchStudent(UserDTO userDTO, Long batchId, Long groupId) {
        User student = userRepository.findByUserName(userDTO.getEmail());
        userDTO.setId(student.getId());
        Batch batch = batchRepository.findById(batchId).get();
        Group group = groupRepository.findById(groupId).get();
        userDTO.setUserName(userDTO.getEmail());
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
    public UserDTO drop(Long batchId, Long studentId) {
        User student = userRepository.findById(studentId).get();
        Batch noBatch = batchRepository.findById(1L).get();
        Group noGroup = groupRepository.findById(1L).get();
        student.setCurrentBatch(noBatch);
        student.setCurrentGroup(noGroup);
        userRepository.save(student);
        Batch batch = batchRepository.findById(batchId).get();
        BatchGroupStudent batchGroupStudent = batchGroupStudentRepository.findByBatchAndStudent(batch, student);
        batchGroupStudent.setStudentStatus(StudentStatus.DROPPED);
        batchGroupStudentRepository.save(batchGroupStudent);
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
        return batchRepository.findAllByBatchStatusIsNot(BatchStatus.COMPLETED)
                .stream()
                .map(batch -> mapperUtil.convert(batch, new BatchDTO()))
                .collect(Collectors.toList());
    }

    @Override
    public BatchDTO getBatchById(Long batchId) {
        return mapperUtil.convert(batchRepository.findById(batchId).get(), new BatchDTO());
    }

    @Override
    public Map<UserDTO, List<Object>> getStudentsWithGroupNumbersAndStudentStatusMap(Long batchId) {
        Map<UserDTO, List<Object>> studentsWithGroupNumbersAndStudentStatusMap = new HashMap<>();
        Batch batch = batchRepository.findById(batchId).get();
        List<User> allStudentsOfBatch = batchGroupStudentRepository.findAllByBatch(batch)
                .stream()
                .map(BatchGroupStudent::getStudent)
                .filter(Objects::nonNull)
                .distinct()
                .collect(Collectors.toList());
        for (User student : allStudentsOfBatch) {
            UserDTO studentDTO = mapperUtil.convert(student, new UserDTO());
            StudentStatus studentStatus = null;
            try{
                studentStatus = batchGroupStudentRepository.findAllByBatchAndStudent(batch, student)
                        .stream()
                        .max(Comparator.comparing(BatchGroupStudent::getLastUpdateDateTime)).get()
                        .getStudentStatus();
                Group group = batchGroupStudentRepository.findAllByBatchAndStudent(batch, student)
                        .stream()
                        .max(Comparator.comparing(BatchGroupStudent::getLastUpdateDateTime)).get()
                        .getGroup();
                GroupDTO groupDTO = mapperUtil.convert(group, new GroupDTO());
                List<Object> groupAndStudentStatusList = Arrays.asList(groupDTO, studentStatus);
                studentsWithGroupNumbersAndStudentStatusMap.put(studentDTO, groupAndStudentStatusList);
            }catch (IllegalArgumentException e){
                List<Object> groupAndStudentStatusList = Arrays.asList(null, studentStatus);
                studentsWithGroupNumbersAndStudentStatusMap.put(studentDTO, groupAndStudentStatusList);
            }
        }
        return studentsWithGroupNumbersAndStudentStatusMap;
    }

    @Override
    public Map<UserDTO, StudentStatus> getStudentsWithStudentStatusMap() {
        Map<UserDTO, StudentStatus> studentsWithStudentStatusMap = new HashMap<>();
        UserRole studentRole = userRoleRepository.findByName("Student");
        List<User> allStudents = userRepository.findAllByUserRole(studentRole);
        for (User student : allStudents) {
            StudentStatus studentStatus = batchGroupStudentRepository.findAllByStudent(student)
                    .stream()
                    .max(Comparator.comparing(BatchGroupStudent::getLastUpdateDateTime)).get()
                    .getStudentStatus();
            try{
                studentsWithStudentStatusMap.put(mapperUtil.convert(student, new UserDTO()), studentStatus);
            }catch (IllegalArgumentException e){
                studentsWithStudentStatusMap.put(mapperUtil.convert(student, new UserDTO()), null);
            }
        }
        return studentsWithStudentStatusMap;
    }

    @Override
    public Map<UserDTO, StudentStatus> getStudentsWithStudentStatusMap(Long batchId, Long groupId) {
        Map<UserDTO, StudentStatus> studentsWithStudentStatusMap = new HashMap<>();
        Batch batch = batchRepository.findById(batchId).get();
        Group group = groupRepository.findById(groupId).get();
        List<BatchGroupStudent> batchGroupStudentList = batchGroupStudentRepository.findAllByBatchAndGroup(batch, group)
                .stream()
                .filter(batchGroupStudent -> batchGroupStudent.getStudent() != null)
                .collect(Collectors.toList());
        for (BatchGroupStudent batchGroupStudent : batchGroupStudentList) {
            studentsWithStudentStatusMap.put(mapperUtil.convert(batchGroupStudent.getStudent(), new UserDTO()), batchGroupStudent.getStudentStatus());
        }
        return studentsWithStudentStatusMap;
    }

    @Override
    public StudentStatus getStudentWithStudentStatus(Long studentId) {
        User student = userRepository.findById(studentId).get();
        List<StudentStatus> studentStatusesOfStudent = batchGroupStudentRepository.findAllByStudent(student)
                .stream()
                .map(BatchGroupStudent::getStudentStatus)
                .collect(Collectors.toList());
        if(studentStatusesOfStudent.contains(StudentStatus.ACTIVE)) return StudentStatus.ACTIVE;
        else if(studentStatusesOfStudent.contains(StudentStatus.ALUMNI)) return StudentStatus.ALUMNI;
        else if(studentStatusesOfStudent.contains(StudentStatus.DROPPED)) return StudentStatus.DROPPED;
        else return null;
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

    @Override
    public StudentStatus getStudentStatus(Long studentId) {
        User student = userRepository.findById(studentId).get();
        Batch noBatch = batchRepository.findById(1L).get();
        if(student.getCurrentBatch() != noBatch) return StudentStatus.ACTIVE;
        else if(batchGroupStudentRepository.findAllByStudent(student)
                .stream()
                .map(BatchGroupStudent::getStudentStatus)
                .anyMatch(obj -> obj == StudentStatus.ALUMNI)) return StudentStatus.ALUMNI;
        else return StudentStatus.DROPPED;
    }

}
