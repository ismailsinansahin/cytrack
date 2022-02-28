package com.cydeo.controller;

import com.cydeo.service.DashboardService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/dashboards")
public class DashboardController {

    private final DashboardService dashboardService;

    public DashboardController(DashboardService dashboardService) {
        this.dashboardService = dashboardService;
    }

    @GetMapping("/studentDashboard")
    public String showStudentDashboard(Model model){
        model.addAttribute("studentTasks", dashboardService.getAllTasksOfStudent(15L));
        return "dashboard/student-dashboard";
    }

    @PostMapping(value = "/completeStudentTask/{studentId}/{taskId}",  params = {"action=completed"})
    public String completeStudentTask(@PathVariable("studentId") Long studentId, @PathVariable("taskId") Long taskId){
        dashboardService.completeStudentTask(studentId, taskId);
        return "redirect:/dashboards/studentDashboard";
    }

    @PostMapping(value = "/completeStudentTask/{studentId}/{taskId}",  params = {"action=uncompleted"})
    public String uncompleteStudentTask(@PathVariable("studentId") Long studentId, @PathVariable("taskId") Long taskId){
        dashboardService.uncompleteStudentTask(studentId, taskId);
        return "redirect:/dashboards/studentDashboard";
    }

    @GetMapping("/cydeoMentorDashboard")
    public String showCydeoMentorDashboard(Model model){
        model.addAttribute("students", dashboardService.getAllStudentsOfMentor(7L));
        return "dashboard/cydeoMentor-dashboard";
    }

    @GetMapping("/alumniMentorDashboard")
    public String showAlumniMentorDashboard(Model model){
        model.addAttribute("students", dashboardService.getAllStudentsOfMentor(7L));
        return "dashboard/alumniMentor-dashboard";
    }

}
