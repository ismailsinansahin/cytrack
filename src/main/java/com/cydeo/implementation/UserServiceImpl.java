package com.cydeo.implementation;

import com.cydeo.dto.BatchDTO;
import com.cydeo.dto.GroupDTO;
import com.cydeo.dto.UserDTO;
import com.cydeo.dto.UserRoleDTO;
import com.cydeo.entity.User;
import com.cydeo.entity.UserRole;
import com.cydeo.enums.StudentStatus;
import com.cydeo.mapper.MapperUtil;
import com.cydeo.repository.*;
import com.cydeo.service.UserService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
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

    public UserServiceImpl(MapperUtil mapperUtil, UserRepository userRepository, BatchRepository batchRepository,
                           UserRoleRepository userRoleRepository, InstructorLessonRepository instructorLessonRepository,
                           GroupRepository groupRepository, PasswordEncoder passwordEncoder) {
        this.mapperUtil = mapperUtil;
        this.userRepository = userRepository;
        this.batchRepository = batchRepository;
        this.userRoleRepository = userRoleRepository;
        this.instructorLessonRepository = instructorLessonRepository;
        this.groupRepository = groupRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public List<UserDTO> listAllUsers() {
        return userRepository.findAll()
                .stream()
                .map(obj -> mapperUtil.convert(obj, new UserDTO()))
                .collect(Collectors.toList());
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
                .map(obj -> mapperUtil.convert(obj, new UserDTO()))
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
    public UserDTO getUserById(Long id) {
        return mapperUtil.convert(userRepository.findById(id).get(), new UserDTO());
    }

    @Override
    public UserDTO save(UserDTO userDTO) {
        User user = (userDTO.getId() != null) ? userRepository.findById(userDTO.getId()).get() : new User();
        if(userDTO.getUserRole() == null) userDTO.setUserRole(new UserRoleDTO("Student"));
        if(user.getGroup() != null) userDTO.setGroup(mapperUtil.convert(user.getGroup(), new GroupDTO()));
        userDTO.setUserName(userDTO.getEmail());
        userDTO.setEnabled(true);
        user = mapperUtil.convert(userDTO, user);
        user.setUserRole(userRoleRepository.findByName(userDTO.getUserRole().getName()));
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
        return mapperUtil.convert(user, new UserDTO());
    }

    @Override
    public String delete(Long id) {
        User user = userRepository.findById(id).get();
        if(isDeletingSafe(user)){
            user.setIsDeleted(true);
            userRepository.save(user);
            return "success";
        }
        return "failure";
    }

    @Override
    public UserDTO drop(Long id) {
        User user = userRepository.findById(id).get();
        user.setStudentStatus(StudentStatus.DROPPED);
        userRepository.save(user);
        return mapperUtil.convert(user, new UserDTO());
    }

    public Boolean isDeletingSafe(User user) {
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

}
