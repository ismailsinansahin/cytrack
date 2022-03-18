package com.cydeo.controller;

import com.cydeo.annotations.ExecutionTime;
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
@ExecutionTime
public class GroupController {

    private final GroupService groupService;

    public GroupController(GroupService groupService) {
        this.groupService = groupService;
    }

    @GetMapping("/groupList/{batchId}")
    public String goGroupList(@PathVariable("batchId") Long batchId, Model model){
        model.addAttribute("batch", groupService.getBatchById(batchId));
        model.addAttribute("groups", groupService.getAllGroupsOfBatch(batchId));
        return "group/group-list";
    }

    @GetMapping("/groupCreate/{batchId}")
    public String goGroupCreate(@PathVariable("batchId") Long batchId, Model model){
        model.addAttribute("batch", groupService.getBatchById(batchId));
        model.addAttribute("cydeoMentors", groupService.getAllUsersByRole("Cydeo Mentor"));
        model.addAttribute("alumniMentors", groupService.getAllUsersByRole("Alumni Mentor"));
        model.addAttribute("newGroup", new GroupDTO());
        return "group/group-create";
    }

    @PostMapping("/groupCreate/{batchId}")
    public String createGroup(@PathVariable("batchId") Long batchId, GroupDTO groupDTO){
        groupService.create(groupDTO, batchId);
        return "redirect:/groups/groupList";
    }

    @GetMapping("/groupEdit/{groupId}")
    public String goGroupEdit(@PathVariable("groupId") Long groupId, Model model) {
        model.addAttribute("group", groupService.getGroupById(groupId));
        model.addAttribute("batches", groupService.getAllNonCompletedBatches());
        model.addAttribute("cydeoMentors", groupService.getAllUsersByRole("Cydeo Mentor"));
        model.addAttribute("alumniMentors", groupService.getAllUsersByRole("Alumni Mentor"));
        return "group/group-edit";
    }

    @PostMapping("/groupUpdate/{groupId}/{batchId}")
    public String updateGroup(@PathVariable("groupId") Long groupId,  @PathVariable("batchId") Long batchId, GroupDTO groupDTO) {
        groupService.save(groupDTO, groupId, batchId);
        return "redirect:/groups/groupList/" + batchId;
    }

    @GetMapping("/groupAddRemoveStudent/{batchId}")
    public String goAddRemoveStudentPage(@PathVariable("batchId") Long batchId, Model model){
        model.addAttribute("groups", groupService.getAllGroupsOfBatch(batchId));
        model.addAttribute("students", groupService.getAllStudentsOfBatch(batchId));
        model.addAttribute("newStudent", new UserDTO());
        return "group/group-addRemoveStudent";
    }

    @PostMapping(value = "/selectedBatchStudents")
    public String selectedBatchStudents(BatchDTO batchDTO){
        return "redirect:/groups/groupAddRemoveStudent/" + batchDTO.getId();
    }

    @PostMapping(value = "/assignToGroup")
    public String assignStudentToGroup(UserDTO studentDTO){
        groupService.assignStudentToGroup(studentDTO);
        return "redirect:/groups/groupAddRemoveStudent/" + studentDTO.getGroup().getBatch().getId();
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
