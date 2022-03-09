package com.cydeo.converter;

import com.cydeo.dto.UserRoleDTO;
import com.cydeo.service.UserRoleService;
import org.springframework.boot.context.properties.ConfigurationPropertiesBinding;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
@ConfigurationPropertiesBinding
public class UserRoleDTOConverter implements Converter<String, UserRoleDTO> {

    private final UserRoleService userRoleService;

    public UserRoleDTOConverter(@Lazy UserRoleService userRoleService) {
        this.userRoleService = userRoleService;
    }

    @Override
    public UserRoleDTO convert(String s) {
        return userRoleService.getUserRoleById(Long.parseLong(s));
    }

}
