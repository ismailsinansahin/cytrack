package com.cydeo.controller;

import com.cydeo.service.GroupService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/group")
public class GroupController {

    GroupService groupService;

    public GroupController(GroupService groupService) {
        this.groupService = groupService;
    }

    @GetMapping("/groupList")
    public String groupList(Model model){
        model.addAttribute("groups", groupService.listAllGroups());
        return "group/group-list";
    }

}
