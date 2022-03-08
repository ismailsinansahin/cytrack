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
    private final BatchRepository batchRepository;
    private final UserRoleRepository userRoleRepository;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(MapperUtil mapperUtil, UserRepository userRepository, BatchRepository batchRepository,
                           UserRoleRepository userRoleRepository, PasswordEncoder passwordEncoder) {
        this.mapperUtil = mapperUtil;
        this.userRepository = userRepository;
        this.batchRepository = batchRepository;
        this.userRoleRepository = userRoleRepository;
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
        if(userDTO.getUserRole() == null) userDTO.setUserRole(new UserRoleDTO("Student"));
        userDTO.setUserName(userDTO.getEmail());
        userDTO.setEnabled(true);
        User user = mapperUtil.convert(userDTO, new User());
        user.setUserRole(userRoleRepository.findByName(userDTO.getUserRole().getName()));
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
