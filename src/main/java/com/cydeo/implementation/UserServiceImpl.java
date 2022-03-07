package com.cydeo.implementation;

import com.cydeo.dto.BatchDTO;
import com.cydeo.dto.GroupDTO;
import com.cydeo.dto.UserDTO;
import com.cydeo.dto.UserRoleDTO;
import com.cydeo.entity.User;
import com.cydeo.entity.UserRole;
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
    private final LessonRepository lessonRepository;
    private final BatchRepository batchRepository;
    private final UserRoleRepository userRoleRepository;
    private final InstructorLessonRepository instructorLessonRepository;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(MapperUtil mapperUtil, UserRepository userRepository, LessonRepository lessonRepository,
                           BatchRepository batchRepository, UserRoleRepository userRoleRepository,
                           InstructorLessonRepository instructorLessonRepository, PasswordEncoder passwordEncoder) {
        this.mapperUtil = mapperUtil;
        this.userRepository = userRepository;
        this.lessonRepository = lessonRepository;
        this.batchRepository = batchRepository;
        this.userRoleRepository = userRoleRepository;
        this.instructorLessonRepository = instructorLessonRepository;
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
        UserRole userRole = userRoleRepository.findByName("Student");
        List<UserDTO> staffsDTOList = userRepository.findAllByUserRoleNot(userRole)
                .stream()
                .map(obj -> mapperUtil.convert(obj, new UserDTO()))
                .collect(Collectors.toList());
        for(UserDTO staff : staffsDTOList){
            User user = userRepository.findById(staff.getId()).get();
            staff.setUserRoleDTO(mapperUtil.convert(user.getUserRole(), new UserRoleDTO()));
        }
        return staffsDTOList;
    }

    @Override
    public List<UserDTO> getAllStudents() {
        UserRole userRole = userRoleRepository.findByName("Student");
        System.out.println("userRole.getName() = " + userRole.getName());
        List<UserDTO> studentsDTOList =  userRepository.findAllByUserRole(userRole)
                .stream()
                .map(obj -> mapperUtil.convert(obj, new UserDTO()))
                .collect(Collectors.toList());
        for(UserDTO student : studentsDTOList){
            User user = userRepository.findById(student.getId()).get();
            student.setUserRoleDTO(mapperUtil.convert(userRole, new UserRoleDTO()));
            student.setBatchDTO(mapperUtil.convert(user.getBatch(), new BatchDTO()));
            try{
                student.setGroupDTO(mapperUtil.convert(user.getGroup(), new GroupDTO()));
            }catch (IllegalArgumentException e){
                continue;
            }
        }
        return studentsDTOList;
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
        if(userDTO.getUserRoleDTO() == null) userDTO.setUserRoleDTO(new UserRoleDTO("Student"));
        userDTO.setUserName(userDTO.getEmail());
        userDTO.setEnabled(true);
        User user = mapperUtil.convert(userDTO, new User());
        user.setUserRole(userRoleRepository.findByName(userDTO.getUserRoleDTO().getName()));
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
        userDTO = mapperUtil.convert(user, new UserDTO());
        return userDTO;
    }

    @Override
    public void delete(Long id) {
        User user = userRepository.findById(id).get();
        user.setIsDeleted(true);
        userRepository.save(user);
    }

}
