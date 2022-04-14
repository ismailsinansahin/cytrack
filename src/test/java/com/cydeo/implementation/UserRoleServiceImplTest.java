package com.cydeo.implementation;

import com.cydeo.dto.UserRoleDTO;
import com.cydeo.entity.UserRole;
import com.cydeo.mapper.MapperUtil;
import com.cydeo.repository.UserRoleRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserRoleServiceImplTest {

    @InjectMocks
    UserRoleServiceImpl userRoleService;

    @Mock
    UserRoleRepository userRoleRepository;
    @Mock
    MapperUtil mapperUtil;

    @Test
    void getUserRoleById() {
        UserRole userRole = new UserRole();
        UserRoleDTO userRoleDTO = new UserRoleDTO();

        when(userRoleRepository.findById(anyLong())).thenReturn(Optional.of(userRole));
        when(mapperUtil.convert(any(), any())).thenReturn(userRoleDTO);
        userRoleService.getUserRoleById(anyLong());

        verify(userRoleRepository).findById(anyLong());
        verify(mapperUtil).convert(any(),any());

        Assertions.assertNotNull(userRoleService.getUserRoleById(anyLong()));

    }
}