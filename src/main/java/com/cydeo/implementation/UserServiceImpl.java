package com.cydeo.implementation;

import com.cydeo.dto.BatchDTO;
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
        return userRepository.findAllByUserRoleNot(userRole)
                .stream()
                .map(obj -> mapperUtil.convert(obj, new UserDTO()))
                .collect(Collectors.toList());
    }

    @Override
    public List<UserDTO> getAllStudents() {
        UserRole userRole = userRoleRepository.findByName("Student");
        return userRepository.findAllByUserRole(userRole)
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
    public UserDTO getUserByEmail(String email) {
        return mapperUtil.convert(userRepository.findByEmail(email), new UserDTO());
    }

    @Override
    public UserDTO getUserById(Long id) {
        return mapperUtil.convert(userRepository.findById(id).get(), new UserDTO());
    }

    @Override
    public UserDTO save(UserDTO userDTO) {
        UserRoleDTO userRoleDTO = mapperUtil.convert(userRoleRepository.findByName("Student"), new UserRoleDTO());
        if (userDTO.getUserRole() == null) userDTO.setUserRole(userRoleDTO);
        userDTO.setEnabled(true);
        User user = mapperUtil.convert(userDTO, new User());
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
        return mapperUtil.convert(user, new UserDTO());
    }

    @Override
    public void delete(Long id) {
        User user = userRepository.findById(id).get();
        user.setIsDeleted(true);
        userRepository.save(user);
    }

}
