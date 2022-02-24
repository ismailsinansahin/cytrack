package com.cydeo.converter;

import com.cydeo.dto.GroupDTO;
import com.cydeo.service.GroupService;
import org.springframework.boot.context.properties.ConfigurationPropertiesBinding;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
@ConfigurationPropertiesBinding
public class GroupDTOConverter implements Converter<String, GroupDTO> {

    GroupService groupService;

    public GroupDTOConverter(@Lazy GroupService groupService) {
        this.groupService = groupService;
    }

    @Override
    public GroupDTO convert(String s) {
        return groupService.getGroupById(Long.parseLong(s));
    }

}
