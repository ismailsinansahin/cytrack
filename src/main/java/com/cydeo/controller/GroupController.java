package com.cydeo.controller;

import com.cydeo.dto.GroupDTO;
import com.cydeo.enums.UserRole;
import com.cydeo.service.BatchService;
import com.cydeo.service.GroupService;
import com.cydeo.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/groups")
public class GroupController {

    GroupService groupService;
    BatchService batchService;
    UserService userService;

    public GroupController(GroupService groupService, BatchService batchService, UserService userService) {
        this.groupService = groupService;
        this.batchService = batchService;
        this.userService = userService;
    }

    @GetMapping("/groupList")
    public String groupList(Model model){
        List<GroupDTO> groups = groupService.listAllGroups();
        model.addAttribute("groups", groups);
        return "group/group-list";
    }

    @GetMapping("/groupCreate")
    public String groupCreate(Model model){
        model.addAttribute("newGroup", new GroupDTO());
        model.addAttribute("batches", batchService.listAllNonCompletedBatches());
        model.addAttribute("cydeoMentors", userService.listAllUsersByRole(UserRole.CYDEO_MENTOR));
        model.addAttribute("alumniMentors", userService.listAllUsersByRole(UserRole.ALUMNI_MENTOR));
        return "group/group-create";
    }

    @PostMapping("/groupCreate")
    public String groupSave(GroupDTO groupDTO){
        groupService.save(groupDTO);
        return "redirect:/groups/groupList";
    }

    @GetMapping("/groupEdit/{id}")
    public String goGroupEdit(@PathVariable("id") Long id, Model model) {
        GroupDTO dto = groupService.getGroupById(id);
        System.out.println(dto.getAlumniMentor().getFirstName());
        model.addAttribute("group", groupService.getGroupById(id));

        model.addAttribute("batches", batchService.listAllNonCompletedBatches());
        model.addAttribute("cydeoMentors", userService.listAllUsersByRole(UserRole.CYDEO_MENTOR));
        model.addAttribute("alumniMentors", userService.listAllUsersByRole(UserRole.ALUMNI_MENTOR));
        return "group/group-edit";
    }

    @PostMapping("/groupUpdate/{id}")
    public String updateGroup(@PathVariable("id") Long id, GroupDTO groupDTO) {
        groupDTO.setId(id);
        groupService.save(groupDTO);
        return "redirect:/groups/groupList";
    }

    @PostMapping(value = "/groupEditDelete/{id}", params = {"action=edit"})
    public String editGroup(@PathVariable("id") Long id){
        return "redirect:/groups/groupEdit/" + id;
    }

    @PostMapping(value = "/groupEditDelete/{id}", params = {"action=delete"})
    public String deleteGroup(@PathVariable("id") Long id){
        groupService.delete(id);
        return "redirect:/groups/groupList";
    }

}
