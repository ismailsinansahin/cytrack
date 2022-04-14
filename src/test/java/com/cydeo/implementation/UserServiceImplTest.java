package com.cydeo.implementation;

import com.cydeo.dto.BatchDTO;
import com.cydeo.dto.UserDTO;
import com.cydeo.dto.UserRoleDTO;
import com.cydeo.entity.Batch;
import com.cydeo.entity.User;
import com.cydeo.entity.UserRole;
import com.cydeo.mapper.MapperUtil;
import com.cydeo.repository.BatchRepository;
import com.cydeo.repository.UserRepository;
import com.cydeo.repository.UserRoleRepository;
import org.junit.jupiter.api.Assertions;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @InjectMocks
    UserServiceImpl userService;
    @Mock
    UserRepository userRepository;
    @Mock
    MapperUtil mapperUtil;
    @Mock
    BatchRepository batchRepository;
    @Mock
    UserRoleRepository userRoleRepository;
    @Mock
    PasswordEncoder passwordEncoder;


    @Test
    void listAllUsers() {
        when(userRepository.findAll()).thenReturn(List.of(new User(), new User()));
        when(mapperUtil.convert(any(), any())).thenReturn(new UserDTO());
        userService.listAllUsers();

        verify(userRepository, times(1)).findAll();
        verify(mapperUtil, times(2)).convert(any(), any());
        Assertions.assertNotNull(userService.listAllUsers());
    }

    @Test
    void getAllUserRoles() {
        List<UserRole> userRoles = new ArrayList<>();
        when(userRoleRepository.findAll()).thenReturn(List.of(new UserRole(), new UserRole()));
        when(mapperUtil.convert(any(), any())).thenReturn(new UserRoleDTO());
        userService.getAllUserRoles();

        verify(userRoleRepository).findAll();
        verify(mapperUtil, times(2)).convert(any(), any());
        Assertions.assertNotNull(userService.listAllUsers());
    }

    @Test
    void getAllStaffs() {
        UserRole userRole = new UserRole();
        when(userRoleRepository.findByName(anyString())).thenReturn(userRole);
        when(userRepository.findAllByUserRoleNot(userRole)).thenReturn(List.of(new User()));
        when(mapperUtil.convert(any(), any())).thenReturn(new UserDTO());
        userService.getAllStaffs();

        verify(userRoleRepository).findByName(anyString());
        verify(userRepository).findAllByUserRoleNot(userRole);
        verify(mapperUtil).convert(any(), any());
        Assertions.assertNotNull(userService.getAllStaffs());

    }

    @Test
    void getAllStudents() {
        UserRole userRole = new UserRole();
        when(userRoleRepository.findByName(anyString())).thenReturn(userRole);
        when(userRepository.findAllByUserRole(userRole)).thenReturn(List.of(new User()));
        when(mapperUtil.convert(any(), any())).thenReturn(new UserDTO());
        userService.getAllStudents();

        verify(userRoleRepository).findByName(anyString());
        verify(userRepository).findAllByUserRole(userRole);
        verify(mapperUtil).convert(any(), any());
        Assertions.assertNotNull(userService.getAllStudents());
    }

    @Test
    void getAllBatches() {
        BatchDTO batch = new BatchDTO();
        given(batchRepository.findAll()).willReturn(List.of(new Batch(), new Batch()));
        /*
          we are getting UnnecessaryStubbingException: when execute the following
          line. You can read here. https://www.baeldung.com/mockito-unnecessary-stubbing-exception
         */
        given(mapperUtil.convert(any(), any())).willReturn(batch);

        userService.getAllBatches();

        then(batchRepository).should().findAll();
        then(mapperUtil).should(times(2)).convert(any(), any());
        Assertions.assertNotNull(userService.getAllBatches());
    }

    @Test
    void getUserById() {
        User user = new User();
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
        when(mapperUtil.convert(any(), any())).thenReturn(new UserDTO());
        userService.getUserById(anyLong());

        verify(userRepository).findById(anyLong());
        verify(mapperUtil).convert(any(), any());
        Assertions.assertNotNull(userService.getUserById(anyLong()));
    }

    @Test
    void save() {
        UserDTO userDTO = new UserDTO();
        UserRole userRole = new UserRole();
        String password="Abc123";
        userDTO.setId(1L);
        User user = new User();
        userDTO.setUserRole(new UserRoleDTO("Student"));
        when(userRepository.findById(userDTO.getId())).thenReturn(Optional.of(user));
        when(mapperUtil.convert(any(UserDTO.class), any(User.class))).thenReturn(user);
        when(userRoleRepository.findByName(userDTO.getUserRole().getName())).thenReturn(userRole);
        user.setUserRole(userRole);
        when(passwordEncoder.encode(password)).thenReturn(password);
        user.setPassword(password);
        when(userRepository.save(any(User.class))).thenReturn(user);
        when(mapperUtil.convert(user, new UserDTO())).thenReturn(userDTO);

        UserDTO result = userService.save(userDTO);

        verify(userRepository).findById(userDTO.getId());
        verify(mapperUtil).convert(userDTO, user);
        verify(userRoleRepository).findByName(userDTO.getUserRole().getName());
        verify(userRepository).save(user);
        verify(mapperUtil).convert(user, new UserDTO());
        Assertions.assertNotNull(result);
    }

    @ParameterizedTest
    @ValueSource(longs = {1, 2, 3})
    void delete(long args) {
        User user = new User();
        user.setIsDeleted(true);
        when(userRepository.findById(args)).thenReturn(Optional.of(user));

        userService.delete(args);

        verify(userRepository).findById(args);

        assertTrue(user.getIsDeleted());

    }

    @Test
    void drop() {
        User user = new User();
        UserDTO userDTO = new UserDTO();

        when(userRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(user));
        when(mapperUtil.convert(user, new UserDTO())).thenReturn(userDTO);

        UserDTO drop = userService.drop(Mockito.anyLong());

        verify(userRepository).findById(Mockito.anyLong());
        verify(mapperUtil, times(1)).convert(user, new UserDTO());

        Assertions.assertNotNull(drop);

    }
}