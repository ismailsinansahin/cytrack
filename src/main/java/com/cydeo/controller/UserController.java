package com.cydeo.controller;

import com.cydeo.dto.LessonDTO;
import com.cydeo.dto.UserDTO;
import com.cydeo.entity.User;
import com.cydeo.service.LessonService;
import com.cydeo.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.*;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/user")
public class UserController {

    UserService userService;
    LessonService lessonService;

    public UserController(UserService userService, LessonService lessonService) {
        this.userService = userService;
        this.lessonService = lessonService;
    }

    @GetMapping("/adminList")
    public String adminList(Model model) {
        model.addAttribute("admins", userService.listAllUsersByRole("Admin"));
        return "user/admin-list";
    }

    @GetMapping("/instructorList")
    public String instructorList(Model model) {
        model.addAttribute("instructors", userService.getInstructorsAndLessonsMap());
        return "user/instructor-list";
    }

    @GetMapping("/cybertekMentorList")
    public String cybertekMentorList(Model model) {
        model.addAttribute("cybertekMentors", userService.getCybertekMentorsAndGroupsMap());
        return "user/cybertekmentor-list";
    }

    @GetMapping("/alumniMentorList")
    public String alumniMentorList(Model model) {
        model.addAttribute("alumniMentors", userService.getAlumniMentorsAndGroupsMap());
        return "user/alumnimentor-list";
    }

    @GetMapping("/studentList")
    public String studentList(Model model) {
        model.addAttribute("students", userService.listAllUsersByRole("Student"));
        return "user/student-list";
    }




}
