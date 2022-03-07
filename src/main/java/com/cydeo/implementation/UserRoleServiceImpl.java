package com.cydeo.implementation;

import com.cydeo.dto.UserRoleDTO;
import com.cydeo.entity.UserRole;
import com.cydeo.mapper.MapperUtil;
import com.cydeo.repository.UserRoleRepository;
import com.cydeo.service.UserRoleService;
import org.springframework.stereotype.Service;

@Service
public class UserRoleServiceImpl implements UserRoleService {

    private final MapperUtil mapperUtil;
    private final UserRoleRepository userRoleRepository;

    public UserRoleServiceImpl(MapperUtil mapperUtil, UserRoleRepository userRoleRepository) {
        this.mapperUtil = mapperUtil;
        this.userRoleRepository = userRoleRepository;
    }

    @Override
    public UserRoleDTO getUserRoleByName(String userRoleName) {
        UserRole userRole = userRoleRepository.findByName(userRoleName);
        return mapperUtil.convert(userRole, new UserRoleDTO());
    }

}
