package com.cydeo.controller;

import com.cydeo.annotations.ExecutionTime;
import com.cydeo.dto.InstructorLessonDTO;
import com.cydeo.dto.LessonDTO;
import com.cydeo.service.LessonService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/lessons")
@ExecutionTime
public class LessonController {

    private final LessonService lessonService;

    public LessonController(LessonService lessonService) {
        this.lessonService = lessonService;
    }

    @GetMapping("/lessonList")
    public String goLessonList(Model model){
        model.addAttribute("lessonsAndInstructors", lessonService.getLessonsAndInstructorsMap());
        return "lesson/lesson-list";
    }

    @GetMapping("/lessonList/{lessonId}")
    public String goLessonList(@PathVariable("lessonId") Long lessonId, Model model){
        model.addAttribute("lessonsAndInstructors", lessonService.getLessonsAndInstructorsMap());
        model.addAttribute("lessonId", lessonId);
        return "lesson/lesson-list";
    }

    @GetMapping("/lessonCreate")
    public String goLessonCreate(Model model){
        model.addAttribute("newLesson", new LessonDTO());
        model.addAttribute("instructors", lessonService.getAllInstructors());
        model.addAttribute("instructorLesson", new InstructorLessonDTO());
        return "lesson/lesson-create";
    }

    @PostMapping("/lessonCreate")
    public String createLesson(LessonDTO lessonDTO){
        lessonService.create(lessonDTO);
        return "redirect:/lessons/lessonList";
    }

    @PostMapping(value = "/addRemoveInstructorEditDelete/{lessonId}", params = {"action=delete"})
    public String deleteLesson(@PathVariable("lessonId") Long lessonId, RedirectAttributes redirectAttributes){
        String result = lessonService.delete(lessonId);
        if(result.equals("success")) redirectAttributes.addFlashAttribute(result, "The lesson was successfully deleted.");
        if(result.equals("failure")) redirectAttributes.addFlashAttribute(result, "The lesson has tasks. Do you want to delete anyway?");
        redirectAttributes.addFlashAttribute("lessonId", lessonId);
        return "redirect:/lessons/lessonList/"+ lessonId;
    }

    @GetMapping("/deleteAll/{lessonId}")
    public String deleteAll(@PathVariable("lessonId") Long lessonId){
        lessonService.deleteLessonWithAllTasks(lessonId);
        return "redirect:/lessons/lessonList/" + lessonId;
    }

    @GetMapping("/keepTasks/{lessonId}")
    public String keepTasks(@PathVariable("lessonId") Long lessonId){
        lessonService.deleteLessonWithoutTasks(lessonId);
        return "redirect:/lessons/lessonList/" + lessonId;
    }

    @PostMapping(value = "/addRemoveInstructorEditDelete/{lessonId}", params = {"action=edit"})
    public String goEditPage(@PathVariable("lessonId") Long lessonId){
        return "redirect:/lessons/lessonEdit/" + lessonId;
    }

    @GetMapping("/lessonEdit/{lessonId}")
    public String goLessonEdit(@PathVariable("lessonId") Long lessonId, Model model){
        model.addAttribute("lesson", lessonService.getLessonByLessonId(lessonId));
        return "lesson/lesson-edit";
    }

    @PostMapping("/lessonUpdate/{lessonId}")
    public String updateLesson(@PathVariable("lessonId") Long lessonId, LessonDTO lessonDTO){
        lessonService.save(lessonDTO, lessonId);
        return "redirect:/lessons/lessonList";
    }

    @PostMapping(value = "/addRemoveInstructorEditDelete/{lessonId}", params = {"action=addInstructor"})
    public String goLessonAddRemoveInstructorPage(@PathVariable("lessonId") Long lessonId){
        return "redirect:/lessons/lessonAddRemoveInstructor/" + lessonId;
    }

    @GetMapping("/lessonAddRemoveInstructor/{lessonId}")
    public String goLessonAddRemoveInstructor(@PathVariable("lessonId") Long lessonId, Model model){
        model.addAttribute("lesson", lessonService.getLessonByLessonId(lessonId));
        model.addAttribute("instructorAndLessons", lessonService.getInstructorsAndLessonsMap());
        model.addAttribute("instructorIdsOfLesson", lessonService.getInstructorIdsOfLesson(lessonId));
        return "lesson/lesson-addRemoveInstructor";
    }

    @PostMapping(value = "/addRemoveInstructor/{instructorId}/{lessonId}",params = {"action=removeInstructor"})
    public String removeInstructor(@PathVariable("instructorId") Long instructorId, @PathVariable("lessonId") Long lessonId){
        lessonService.removeInstructor(lessonId, instructorId);
        return "redirect:/lessons/lessonAddRemoveInstructor/" + lessonId;
    }

    @PostMapping(value = "/addRemoveInstructor/{instructorId}/{lessonId}", params = {"action=addInstructor"})
    public String addInstructor(@PathVariable("instructorId") Long instructorId, @PathVariable("lessonId") Long lessonId){
        lessonService.addInstructor(lessonId, instructorId);
        return "redirect:/lessons/lessonAddRemoveInstructor/" + lessonId;
    }

}
