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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/groups")
public class GroupController {

    private final GroupService groupService;

    public GroupController(GroupService groupService) {
        this.groupService = groupService;
    }

    @GetMapping("/batchGroupList/{batchId}")
    public String goGroupList(@PathVariable("batchId") Long batchId, Model model){
        model.addAttribute("batch", groupService.getBatchById(batchId));
        model.addAttribute("groups", groupService.getAllGroupsOfBatch(batchId));
        return "group/batch-group-list";
    }

    @GetMapping("/groupCreate/{batchId}")
    public String goGroupCreate(@PathVariable("batchId") Long batchId, Model model){
        model.addAttribute("batch", groupService.getBatchById(batchId));
        model.addAttribute("cydeoMentors", groupService.getAllUsersByRole("Cydeo Mentor"));
        model.addAttribute("alumniMentors", groupService.getAllUsersByRole("Alumni Mentor"));
        model.addAttribute("newGroup", new GroupDTO());
        return "group/batch-group-create";
    }

    @PostMapping("/groupCreate/{batchId}")
    public String createGroup(@PathVariable("batchId") Long batchId, GroupDTO groupDTO){
        groupService.create(groupDTO, batchId);
        return "redirect:/groups/batchGroupList/" + batchId;
    }

    @GetMapping("/groupEdit/{batchId}/{groupId}")
    public String goGroupEdit(@PathVariable("batchId") Long batchId, @PathVariable("groupId") Long groupId, Model model) {
        model.addAttribute("batch", groupService.getBatchById(batchId));
        model.addAttribute("group", groupService.getGroupById(groupId));
        model.addAttribute("cydeoMentors", groupService.getAllUsersByRole("Cydeo Mentor"));
        model.addAttribute("alumniMentors", groupService.getAllUsersByRole("Alumni Mentor"));
        return "group/batch-group-edit";
    }

    @PostMapping("/groupUpdate/{batchId}/{groupId}")
    public String updateGroup( @PathVariable("batchId") Long batchId, @PathVariable("groupId") Long groupId, GroupDTO groupDTO) {
        groupService.save(groupDTO, groupId, batchId);
        return "redirect:/groups/batchGroupList/" + batchId;
    }

    @PostMapping(value = "/groupGoEditDelete/{batchId}/{groupId}", params = {"action=go"})
    public String goGroup(@PathVariable("batchId") Long batchId, @PathVariable("groupId") Long groupId){
        return "redirect:/users/groupStudentList/" + batchId + "/" + groupId;
    }

    @PostMapping(value = "/groupGoEditDelete/{batchId}/{groupId}", params = {"action=edit"})
    public String editGroup(@PathVariable("batchId") Long batchId, @PathVariable("groupId") Long groupId){
        return "redirect:/groups/groupEdit/" + batchId + "/" + groupId;
    }

    @PostMapping(value = "/groupGoEditDelete/{batchId}/{groupId}", params = {"action=delete"})
    public String goGroupDeleteConfirmation(@PathVariable("batchId") Long batchId, @PathVariable("groupId") Long groupId, RedirectAttributes redirectAttributes){
        if(groupService.isDeletionSafe(groupId)) redirectAttributes.addFlashAttribute("confirmDeletion", "Delete Group");
        if(!groupService.isDeletionSafe(groupId)) redirectAttributes.addFlashAttribute("errorMessage", "The group cannot be deleted. It has active students.");
        redirectAttributes.addFlashAttribute("groupId", groupId);
        return "redirect:/groups/batchGroupList/" + batchId + "/" + groupId;
    }

    @GetMapping("/batchGroupList/{batchId}/{groupId}")
    public String goGroupList(@PathVariable("batchId") Long batchId, @PathVariable("groupId") Long groupId, Model model){
        model.addAttribute("batch", groupService.getBatchById(batchId));
        model.addAttribute("groups", groupService.getAllGroupsOfBatch(batchId));
        model.addAttribute("groupId", groupId);
        return "group/batch-group-list";
    }

    @GetMapping("/deleteGroup/{batchId}/{groupId}")
    public String deleteGroup(@PathVariable("batchId") Long batchId, @PathVariable("groupId") Long groupId){
        groupService.delete(groupId);
        return "redirect:/groups/batchGroupList/" + batchId;
    }

    @GetMapping("/groupAddRemoveStudent/{batchId}")
    public String goAddRemoveStudentPage(@PathVariable("batchId") Long batchId, Model model){
        model.addAttribute("batch", groupService.getBatchById(batchId));
        model.addAttribute("groups", groupService.getAllGroupsOfBatch(batchId));
        model.addAttribute("students", groupService.getAllStudentsOfBatch(batchId));
        model.addAttribute("studentGroupMap", groupService.getBatchGroupStudentMap(batchId));
        model.addAttribute("newStudent", new UserDTO());
        return "group/group-addRemoveStudent";
    }

    @PostMapping(value = "/assignToGroup/{batchId}")
    public String assignStudentToGroup(@PathVariable("batchId") Long batchId, UserDTO studentDTO){
        groupService.assignStudentToGroup(studentDTO, batchId);
        return "redirect:/groups/groupAddRemoveStudent/" + batchId;
    }














    @PostMapping(value = "/selectedBatchStudents")
    public String selectedBatchStudents(BatchDTO batchDTO){
        return "redirect:/groups/groupAddRemoveStudent/" + batchDTO.getId();
    }

}
