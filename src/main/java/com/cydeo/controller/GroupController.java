package com.cydeo.controller;

import com.cydeo.dto.BatchDTO;
import com.cydeo.dto.GroupDTO;
import com.cydeo.dto.UserDTO;
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

    private final GroupService groupService;

    public GroupController(GroupService groupService) {
        this.groupService = groupService;
    }

    @GetMapping("/groupList")
    public String goGroupList(Model model){
        model.addAttribute("groups", groupService.getAllGroups());
        model.addAttribute("lastOngoingBatch", groupService.getLastOngoingBatch());
        return "group/group-list";
    }

    @GetMapping("/groupCreate")
    public String goGroupCreate(Model model){
        model.addAttribute("newGroup", new GroupDTO());
        model.addAttribute("batches", groupService.getAllNonCompletedBatches());
        model.addAttribute("cydeoMentors", groupService.getAllUsersByRole("Cydeo Mentor"));
        model.addAttribute("alumniMentors", groupService.getAllUsersByRole("Alumni Mentor"));
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
        model.addAttribute("cydeoMentors", groupService.getAllUsersByRole("Cydeo Mentor"));
        model.addAttribute("alumniMentors", groupService.getAllUsersByRole("Alumni Mentor"));
        return "group/group-edit";
    }

    @PostMapping("/groupUpdate/{id}")
    public String updateGroup(@PathVariable("id") Long groupId, GroupDTO groupDTO) {
        groupService.save(groupDTO, groupId);
        return "redirect:/groups/groupList";
    }

    @GetMapping("/groupAddRemoveStudent/{batchId}")
    public String goAddRemoveStudentPage(@PathVariable("batchId") Long batchId, Model model){
        model.addAttribute("batches", groupService.getAllBatches());
        model.addAttribute("groups", groupService.getAllGroupsOfBatch(batchId));
        model.addAttribute("students", groupService.getAllStudentsOfBatch(batchId));
        model.addAttribute("batch", new BatchDTO());
        model.addAttribute("newStudent", new UserDTO());
        model.addAttribute("group", new GroupDTO());
        return "group/group-addRemoveStudent";
    }

    @PostMapping(value = "/selectedBatchStudents")
    public String selectedBatchStudents(BatchDTO batchDTO){
        return "redirect:/groups/groupAddRemoveStudent/" + batchDTO.getId();
    }

    @PostMapping(value = "/assignToGroup")
    public String assignStudentToGroup(UserDTO studentDTO){
        groupService.assignStudentToGroup(studentDTO);
        return "redirect:/groups/groupAddRemoveStudent/3";
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
