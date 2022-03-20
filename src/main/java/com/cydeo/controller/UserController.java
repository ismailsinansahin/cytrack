package com.cydeo.controller;

import com.cydeo.annotations.ExecutionTime;
import com.cydeo.dto.UserDTO;
import com.cydeo.enums.Country;
import com.cydeo.enums.Gender;
import com.cydeo.enums.StudentStatus;
import com.cydeo.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.*;

@Controller
@RequestMapping("/users")
@ExecutionTime
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    //**************STAFF OPERATIONS**************************

    @GetMapping("/staffList")
    public String goStaffListPage(Model model) {
        model.addAttribute("staffs", userService.getAllStaffs());
        return "user/staff-list";
    }

    @GetMapping("/staffCreate")
    public String goStaffCreate(Model model) {
        model.addAttribute("newUser", new UserDTO());
        model.addAttribute("userRoles", userService.getAllUserRoles());
        model.addAttribute("countries", Arrays.asList(Country.values()));
        model.addAttribute("genders", Arrays.asList(Gender.values()));
        return "user/staff-create";
    }

    @PostMapping("/staffCreate")
    public String createStaff(UserDTO userDTO){
        userService.saveStaff(userDTO);
        return "redirect:/users/staffList";
    }

    @PostMapping(value = "/staffEditDelete/{id}", params = {"action=edit"})
    public String editStaff(@PathVariable("id") Long id){
        return "redirect:/users/staffEdit/" + id;
    }

    @GetMapping("/staffEdit/{id}")
    public String goStaffEdit(@PathVariable("id") Long id, Model model) {
        model.addAttribute("user", userService.getUserById(id));
        model.addAttribute("userRoles", userService.getAllUserRoles());
        model.addAttribute("countries", Arrays.asList(Country.values()));
        model.addAttribute("genders", Arrays.asList(Gender.values()));
        return "user/staff-edit";
    }

    @PostMapping("/staffUpdate/{id}")
    public String updateStaff(@PathVariable("id") Long id, UserDTO userDTO) {
        userDTO.setId(id);
        userService.saveStaff(userDTO);
        return "redirect:/users/staffList";
    }

    @PostMapping(value = "/staffEditDelete/{userId}", params = {"action=delete"})
    public String goStaffDeleteConfirmation(@PathVariable("userId") Long userId, RedirectAttributes redirectAttributes){
        if(userService.isDeletionSafe(userId)) redirectAttributes.addFlashAttribute("confirmDeletion", "Delete Staff?");
        if(!userService.isDeletionSafe(userId)) redirectAttributes.addFlashAttribute("errorMessage", userService.getDeleteErrorMessage(userId));
        redirectAttributes.addFlashAttribute("userId", userId);
        return "redirect:/users/staffList/" + userId;
    }

    @GetMapping("/staffList/{userId}")
    public String goStaffList(@PathVariable("userId") Long userId, Model model){
        model.addAttribute("staffs", userService.getAllStaffs());
        model.addAttribute("userId", userId);
        return "user/staff-list";
    }

    @GetMapping("/deleteStaff/{userId}")
    public String deleteStaff(@PathVariable("userId") Long userId){
        userService.deleteStaff(userId);
        return "redirect:/users/staffList";
    }

    //**************STUDENT OPERATIONS**************************

    @GetMapping("/studentList")
    public String goStudentListPage(Model model) {
        model.addAttribute("students", userService.getAllStudents());
        return "user/student-list";
    }

    @PostMapping(value = "/studentEditDelete/{studentId}", params = {"action=edit"})
    public String editStudent(@PathVariable("studentId") Long studentId) {
        return "redirect:/users/studentEdit/" + studentId;
    }

    @GetMapping("/studentEdit/{studentId}")
    public String goStudentEdit(@PathVariable("studentId") Long studentId, Model model) {
        model.addAttribute("student", userService.getUserById(studentId));
        model.addAttribute("countries", Arrays.asList(Country.values()));
        model.addAttribute("genders", Arrays.asList(Gender.values()));
        model.addAttribute("studentStatuses", Arrays.asList(StudentStatus.values()));
        model.addAttribute("batches", userService.getStudentPossibleBatches(studentId));
        return "user/student-edit";
    }

    @PostMapping("/studentUpdate/{studentId}")
    public String updateStudent(@PathVariable("studentId") Long studentId, UserDTO userDTO) {
        userDTO.setId(studentId);
        userService.updateStudent(userDTO);
        return "redirect:/users/studentList";
    }

    @PostMapping(value = "/studentEditDelete/{studentId}", params = {"action=delete"})
    public String goStudentDeleteConfirmation(@PathVariable("studentId") Long studentId, RedirectAttributes redirectAttributes){
        redirectAttributes.addFlashAttribute("confirmDeletion", "Delete Student?");
        redirectAttributes.addFlashAttribute("studentId", studentId);
        return "redirect:/users/studentList/" + studentId;
    }

    @GetMapping("/studentList/{studentId}")
    public String goStudentList(@PathVariable("studentId") Long studentId, Model model){
        model.addAttribute("students", userService.getAllStudents());
        model.addAttribute("studentId", studentId);
        return "user/student-list";
    }

    @GetMapping("/deleteStudent/{studentId}")
    public String deleteStudent(@PathVariable("studentId") Long studentId){
        userService.deleteStudent(studentId);
        return "redirect:/users/studentList";
    }

    //**************BATCH STUDENT OPERATIONS**************************

    @GetMapping("/batchStudentList/{batchId}")
    public String goBatchStudentList(@PathVariable("batchId") Long batchId, Model model) {
        model.addAttribute("students", userService.getAllStudentsByBatch(batchId));
        model.addAttribute("groupsMap", userService.getStudentsGroupNumbersMap(batchId));
        model.addAttribute("batch", userService.getBatchById(batchId));
        return "user/batch-student-list";
    }

    @GetMapping("/groupStudentList/{batchId}/{groupId}")
    public String goGroupStudentList(@PathVariable("batchId") Long batchId, @PathVariable("groupId") Long groupId, Model model) {
        model.addAttribute("students", userService.getAllStudentsByGroup(groupId));
        model.addAttribute("batch", userService.getBatchById(batchId));
        model.addAttribute("group", userService.getGroupById(groupId));
        return "user/group-student-list";
    }

    @GetMapping("/batchStudentCreate/{batchId}")
    public String goBatchStudentCreate(@PathVariable("batchId") Long batchId, Model model) {
        model.addAttribute("newUser", new UserDTO());
        model.addAttribute("countries", Arrays.asList(Country.values()));
        model.addAttribute("genders", Arrays.asList(Gender.values()));
        model.addAttribute("groups", userService.getAllGroupsOfBatch(batchId));
        model.addAttribute("batch", userService.getBatchById(batchId));
        model.addAttribute("studentStatus", StudentStatus.NEW);
        return "user/batch-student-create";
    }

    @PostMapping("/batchStudentCreate/{batchId}")
    public String createBatchStudent(@PathVariable("batchId") Long batchId, UserDTO userDTO){
        userService.createBatchStudent(userDTO, batchId);
        return "redirect:/users/batchStudentList/" + batchId;
    }

    @PostMapping(value = "/batchStudentEditDrop/{batchId}/{studentId}", params = {"action=edit"})
    public String editBatchStudent(@PathVariable("batchId") Long batchId, @PathVariable("studentId") Long studentId){
        return "redirect:/users/batchStudentEdit/" + batchId + "/" + studentId;
    }

    @GetMapping("/batchStudentEdit/{batchId}/{studentId}")
    public String goBatchStudentEdit(@PathVariable("batchId") Long batchId, @PathVariable("studentId") Long studentId, Model model) {
        model.addAttribute("student", userService.getUserById(studentId));
        model.addAttribute("countries", Arrays.asList(Country.values()));
        model.addAttribute("genders", Arrays.asList(Gender.values()));
        model.addAttribute("studentStatuses", Arrays.asList(StudentStatus.values()));
        model.addAttribute("batch", userService.getBatchById(batchId));
        model.addAttribute("group", userService.getStudentGroup(batchId, studentId));
        return "user/batch-student-edit";
    }

    @PostMapping("/batchStudentUpdate/{batchId}/{groupId}")
    public String updateBatchStudent(@PathVariable("batchId") Long batchId, @PathVariable("groupId") Long groupId, UserDTO userDTO){
        userService.updateBatchStudent(userDTO, batchId, groupId);
        return "redirect:/users/groupStudentList/" + batchId + "/" + groupId;
    }

    @PostMapping(value = "/batchStudentEditDrop/{batchId}/{studentId}", params = {"action=drop"})
    public String dropBatchStudent(@PathVariable("batchId") Long batchId, @PathVariable("studentId") Long studentId){
        userService.drop(studentId);
        return "redirect:/users/batchStudentList/" + batchId;
    }

}
