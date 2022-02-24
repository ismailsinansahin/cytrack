package com.cydeo.controller;

import com.cydeo.dto.GroupDTO;
import com.cydeo.dto.UserDTO;
import com.cydeo.enums.UserRole;
import com.cydeo.service.GroupService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/groups")
public class GroupController {

    GroupService groupService;

    public GroupController(GroupService groupService) {
        this.groupService = groupService;
    }

    @GetMapping("/groupList")
    public String goGroupList(Model model){
        model.addAttribute("groups", groupService.getAllGroups());
        return "group/group-list";
    }

    @GetMapping("/groupCreate")
    public String goGroupCreate(Model model){
        model.addAttribute("newGroup", new GroupDTO());
        model.addAttribute("batches", groupService.getAllNonCompletedBatches());
        model.addAttribute("cydeoMentors", groupService.getAllUsersByRole(UserRole.CYDEO_MENTOR));
        model.addAttribute("alumniMentors", groupService.getAllUsersByRole(UserRole.ALUMNI_MENTOR));
        return "group/group-create";
    }

    @PostMapping("/groupCreate")
    public String createGroup(GroupDTO groupDTO){
        groupService.create(groupDTO);
        return "redirect:/groups/groupList";
    }

    @GetMapping("/groupEdit/{id}")
    public String goGroupEdit(@PathVariable("id") Long groupId, Model model) {
        model.addAttribute("group", groupService.getGroupById(groupId));
        model.addAttribute("batches", groupService.getAllNonCompletedBatches());
        model.addAttribute("cydeoMentors", groupService.getAllUsersByRole(UserRole.CYDEO_MENTOR));
        model.addAttribute("alumniMentors", groupService.getAllUsersByRole(UserRole.ALUMNI_MENTOR));
        return "group/group-edit";
    }

    @PostMapping("/groupUpdate/{id}")
    public String updateGroup(@PathVariable("id") Long groupId, GroupDTO groupDTO) {
        groupService.save(groupDTO, groupId);
        return "redirect:/groups/groupList";
    }

    @GetMapping("/addRemoveStudent")
    public String goAddRemoveStudentPage(Model model) {
        model.addAttribute("groups", groupService.getAllGroups());
        model.addAttribute("students", groupService.getAllUsersByRole(UserRole.STUDENT));
        model.addAttribute("newStudent", new UserDTO());
        return "group/group-addRemoveStudent";
    }

    @PostMapping(value = "/addRemoveStudent")
    public String addRemoveStudent(){





        return "redirect:/groups/groupList";
    }

    @PostMapping(value = "/groupEditDelete/{id}", params = {"action=edit"})
    public String editGroup(@PathVariable("id") Long groupId){
        return "redirect:/groups/groupEdit/" + groupId;
    }

    @PostMapping(value = "/groupEditDelete/{id}", params = {"action=delete"})
    public String deleteGroup(@PathVariable("id") Long groupId){
        groupService.delete(groupId);
        return "redirect:/groups/groupList";
    }

}
