package com.cydeo.controller;

import com.cydeo.service.StatisticsService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/statistics")
public class StatisticsController {

    private final StatisticsService statisticsService;

    public StatisticsController(StatisticsService statisticsService) {
        this.statisticsService = statisticsService;
    }

    @GetMapping("/batchList")
    public String showBatches(Model model){
        model.addAttribute("batches", statisticsService.getAllBatches());
        return "statistics/statistics-batchList";
    }

    @GetMapping("/groupList/{batchId}")
    public String showGroupsOfBatch(@PathVariable("batchId") Long batchId, Model model){
        model.addAttribute("groups", statisticsService.getAllGroupsOfBatch(batchId));
        return "statistics/statistics-groupList";
    }

    @PostMapping("/groupList/{batchId}")
    public String goGroupsOfBatch(@PathVariable("batchId") Long batchId){
        return "redirect:/statistics/groupList/" + batchId;
    }

    @GetMapping("/studentList/{groupId}")
    public String showStudentsOfGroup(@PathVariable("groupId") Long groupId, Model model){
        model.addAttribute("students", statisticsService.getAllStudentsOfGroup(groupId));
        return "statistics/statistics-studentList";
    }

    @PostMapping("/studentList/{groupId}")
    public String goStudentsOfGroup(@PathVariable("groupId") Long groupId){
        return "redirect:/statistics/studentList/" + groupId;
    }

    @GetMapping("/student/{studentId}")
    public String showStudent(@PathVariable("studentId") Long studentId, Model model){
        model.addAttribute("student", statisticsService.findStudentById(studentId));
        return "statistics/statistics-student";
    }

    @PostMapping("/student/{studentId}")
    public String goStudent(@PathVariable("studentId") Long studentId){
        return "redirect:/statistics/student/" + studentId;
    }

}
