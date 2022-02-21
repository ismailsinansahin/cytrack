package com.cydeo.controller;

import com.cydeo.dto.UserDTO;
import com.cydeo.enums.Country;
import com.cydeo.enums.Gender;
import com.cydeo.enums.StudentStatus;
import com.cydeo.enums.UserRole;
import com.cydeo.service.LessonService;
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

    UserService userService;
    LessonService lessonService;

    public UserController(UserService userService, LessonService lessonService) {
        this.userService = userService;
        this.lessonService = lessonService;
    }

    @GetMapping("/userList")
    public String goUserList(Model model) {
        model.addAttribute("users", userService.listAllUsers());
        return "user/user-list";
    }

    @GetMapping("/userCreate")
    public String goUserCreate(Model model) {
        model.addAttribute("newUser", new UserDTO());
        model.addAttribute("countries", Arrays.asList(Country.values()));
        model.addAttribute("genders", Arrays.asList(Gender.values()));
        model.addAttribute("userRoles", Arrays.asList(UserRole.values()));
        model.addAttribute("userStatuses", Arrays.asList(StudentStatus.values()));
        return "user/user-create";
    }

    @PostMapping("/userCreate")
    public String createUser(UserDTO userDTO){
        userService.save(userDTO);
        return "redirect:/users/userList";
    }

    @GetMapping("/userEdit/{id}")
    public String goUserEdit(@PathVariable("id") Long id, Model model) {
        model.addAttribute("user", userService.getUserById(id));
        model.addAttribute("countries", Arrays.asList(Country.values()));
        model.addAttribute("genders", Arrays.asList(Gender.values()));
        model.addAttribute("userRoles", Arrays.asList(UserRole.values()));
        model.addAttribute("userStatuses", Arrays.asList(StudentStatus.values()));
        return "user/user-edit";
    }

    @PostMapping("/userUpdate/{id}")
    public String updateUser(@PathVariable("id") Long id, UserDTO userDTO) {
        userDTO.setId(id);
        userService.save(userDTO);
        return "redirect:/users/userList";
    }

    @PostMapping(value = "/userEditDelete/{id}", params = {"action=edit"})
    public String editUser(@PathVariable("id") Long id){
        return "redirect:/users/userEdit/" + id;
    }

    @PostMapping(value = "/userEditDelete/{id}", params = {"action=delete"})
    public String deleteUser(@PathVariable("id") Long id){
        userService.delete(id);
        return "redirect:/users/userList";
    }

}
