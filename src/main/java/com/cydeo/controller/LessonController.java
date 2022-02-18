package com.cydeo.controller;

import com.cydeo.dto.InstructorLessonDTO;
import com.cydeo.dto.LessonDTO;
import com.cydeo.dto.UserDTO;
import com.cydeo.enums.UserRole;
import com.cydeo.service.LessonService;
import com.cydeo.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Map;

@Controller
@RequestMapping("/lessons")
public class LessonController {

    LessonService lessonService;
    UserService userService;

    public LessonController(LessonService lessonService, UserService userService) {
        this.lessonService = lessonService;
        this.userService = userService;
    }

    @GetMapping("/lessonList")
    public String lessonList(Model model){
        Map<LessonDTO, String> lessonAndInstructorMap = lessonService.getLessonsAndInstructorsMap();
        model.addAttribute("lessonsAndInstructors", lessonAndInstructorMap);
        return "lesson/lesson-list";
    }

    @GetMapping("/lessonCreate")
    public String lessonCreate(Model model){
        model.addAttribute("newLesson", new LessonDTO());
        model.addAttribute("instructors", userService.listAllUsersByRole(UserRole.INSTRUCTOR));
        model.addAttribute("instructorLesson", new InstructorLessonDTO());
        return "lesson/lesson-create";
    }

    @PostMapping("/lessonCreate")
    public String saveLesson(LessonDTO lessonDTO){
        lessonService.save(lessonDTO);
        return "redirect:/lessons/lessonList";
    }

    @GetMapping("/lessonEdit/{lessonId}")
    public String lessonEdit(@PathVariable("lessonId") Long lessonId, Model model){
        model.addAttribute("lesson", lessonService.getLessonByLessonId(lessonId));
        return "lesson/lesson-edit";
    }

    @PostMapping("/lessonUpdate/{lessonId}")
    public String updateLesson(@PathVariable("lessonId") Long lessonId, LessonDTO lessonDTO){
        lessonDTO.setId(lessonId);
        lessonService.save(lessonDTO);
        return "redirect:/lessons/lessonList";
    }

    @GetMapping("/lessonAddRemoveInstructor/{lessonId}")
    public String lessonAddRemoveInstructor(@PathVariable("lessonId") Long lessonId, Model model){
        model.addAttribute("lesson", lessonService.getLessonByLessonId(lessonId));
        model.addAttribute("instructors", userService.listAllUsersByRole(UserRole.INSTRUCTOR));
        model.addAttribute("newInstructor", new UserDTO());
        return "lesson/lesson-addRemoveInstructor";
    }

    @PostMapping(value = "/addRemoveInstructor/{lessonId}", params = {"action=addInstructor"})
    public String addInstructor(@PathVariable("lessonId") Long lessonId, UserDTO instructor){
        lessonService.addInstructor(lessonId, instructor);
        return "redirect:/lessons/lessonAddRemoveInstructor/" + lessonId;
    }

    @PostMapping(value = "/addRemoveInstructor/{lessonId}", params = {"action=removeInstructor"})
    public String removeInstructor(@PathVariable("lessonId") Long lessonId, UserDTO instructor){
        lessonService.removeInstructor(lessonId, instructor);
        return "redirect:/lessons/lessonAddRemoveInstructor/" + lessonId;
    }


    @PostMapping(value = "/addRemoveInstructorEditDelete/{lessonId}", params = {"action=addInstructor"})
    public String goAddInstructorPage(@PathVariable("lessonId") Long lessonId){
        return "redirect:/lessons/lessonAddRemoveInstructor/" + lessonId;
    }

    @PostMapping(value = "/addRemoveInstructorEditDelete/{lessonId}", params = {"action=edit"})
    public String goEditPage(@PathVariable("lessonId") Long lessonId){
        return "redirect:/lessons/lessonEdit/" + lessonId;
    }

    @PostMapping(value = "/addRemoveInstructorEditDelete/{lessonId}", params = {"action=delete"})
    public String deleteLesson(@PathVariable("lessonId") Long lessonId){
        lessonService.delete(lessonId);
        return "redirect:/lessons/lessonList";
    }



}
