package com.cydeo.controller;

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

import java.util.*;

@Controller
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/staffList")
    public String goStaffList(Model model) {
        model.addAttribute("staffs", userService.getAllStaffs());
        return "user/staff-list";
    }

    @GetMapping("/studentList")
    public String goStudentList(Model model) {
        model.addAttribute("students", userService.getAllStudents());
        return "user/student-list";
    }

    @GetMapping("/staffCreate")
    public String goStaffCreate(Model model) {
        model.addAttribute("newUser", new UserDTO());
        model.addAttribute("userRoles", userService.getAllUserRoles());
        model.addAttribute("countries", Arrays.asList(Country.values()));
        model.addAttribute("genders", Arrays.asList(Gender.values()));
        return "user/staff-create";
    }

    @GetMapping("/studentCreate")
    public String goStudentCreate(Model model) {
        model.addAttribute("newUser", new UserDTO());
        model.addAttribute("countries", Arrays.asList(Country.values()));
        model.addAttribute("genders", Arrays.asList(Gender.values()));
        model.addAttribute("studentStatuses", Arrays.asList(StudentStatus.values()));
        model.addAttribute("batches", userService.getAllBatches());
        return "user/student-create";
    }

    @PostMapping("/staffCreate")
    public String createStaff(UserDTO userDTO){
        userService.save(userDTO);
        return "redirect:/users/staffList";
    }

    @PostMapping("/studentCreate")
    public String createStudent(UserDTO userDTO){
        userService.save(userDTO);
        return "redirect:/users/studentList";
    }

    @GetMapping("/staffEdit/{id}")
    public String goStaffEdit(@PathVariable("id") Long id, Model model) {
        model.addAttribute("user", userService.getUserById(id));
        model.addAttribute("userRoles", userService.getAllUserRoles());
        model.addAttribute("countries", Arrays.asList(Country.values()));
        model.addAttribute("genders", Arrays.asList(Gender.values()));
        return "user/staff-edit";
    }

    @GetMapping("/studentEdit/{id}")
    public String goStudentEdit(@PathVariable("id") Long id, Model model) {
        model.addAttribute("user", userService.getUserById(id));
        model.addAttribute("countries", Arrays.asList(Country.values()));
        model.addAttribute("genders", Arrays.asList(Gender.values()));
        model.addAttribute("studentStatuses", Arrays.asList(StudentStatus.values()));
        model.addAttribute("batches", userService.getAllBatches());
        return "user/student-edit";
    }

    @PostMapping("/staffUpdate/{id}")
    public String updateStaff(@PathVariable("id") Long id, UserDTO userDTO) {
        userDTO.setId(id);
        userService.save(userDTO);
        return "redirect:/users/staffList";
    }

    @PostMapping("/studentUpdate/{id}")
    public String updateStudent(@PathVariable("id") Long id, UserDTO userDTO) {
        userDTO.setId(id);
        userService.save(userDTO);
        return "redirect:/users/studentList";
    }

    @PostMapping(value = "/staffEditDelete/{id}", params = {"action=edit"})
    public String editStaff(@PathVariable("id") Long id){
        return "redirect:/users/staffEdit/" + id;
    }

    @PostMapping(value = "/staffEditDelete/{id}", params = {"action=delete"})
    public String deleteStaff(@PathVariable("id") Long id){
        userService.delete(id);
        return "redirect:/users/staffList";
    }
    @PostMapping(value = "/studentEditDropDelete/{id}", params = {"action=edit"})
    public String editStudent(@PathVariable("id") Long id){
        return "redirect:/users/studentEdit/" + id;
    }

    @PostMapping(value = "/studentEditDropDelete/{id}", params = {"action=drop"})
    public String dropStudent(@PathVariable("id") Long id){
        userService.drop(id);
        return "redirect:/users/studentList";
    }

    @PostMapping(value = "/studentEditDropDelete/{id}", params = {"action=delete"})
    public String deleteStudent(@PathVariable("id") Long id){
        userService.delete(id);
        return "redirect:/users/studentList";
    }

}
