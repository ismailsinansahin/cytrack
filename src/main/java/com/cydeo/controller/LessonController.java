package com.cydeo.controller;

import com.cydeo.service.LessonService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/lesson")
public class LessonController {

    LessonService lessonService;

    public LessonController(LessonService lessonService) {
        this.lessonService = lessonService;
    }

//    @GetMapping("/lessonList")
//    public String lessonList(Model model){
//
//        model.addAttribute("lessons", lessonService.getLessonsAndInstructorsMap());
//
//        return "lesson/lesson-list";
//    }

}
