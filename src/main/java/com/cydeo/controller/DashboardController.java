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

    @GetMapping("/admin")
    public String showAdminDashboard(Model model){
        model.addAttribute("batches", dashboardService.getAllBatches());
        return "dashboard/admin-dashboard";
    }

    @GetMapping("/instructor")
    public String showInstructorDashboard(Model model){

        return "dashboard/instructor-dashboard";
    }

    @GetMapping("/cydeoMentor")
    public String showCydeoMentorDashboard(Model model){

        return "dashboard/cydeoMentor-dashboard";
    }

    @GetMapping("/alumniMentor")
    public String showAlumniMentorDashboard(Model model){

        return "dashboard/alumniMentor-dashboard";
    }

    @GetMapping("/student")
    public String showStudent(Model model){
        model.addAttribute("student", dashboardService.getCurrentUser());
        model.addAttribute("studentTasks", dashboardService.getAllTasksOfCurrentStudent());
        model.addAttribute("taskBasedNumbers", dashboardService.getTaskBasedNumbers());
        System.out.println("dashboardService.getTaskBasedNumbers() = " + dashboardService.getTaskBasedNumbers());
        model.addAttribute("weekBasedNumbers", dashboardService.getWeekBasedNumbers());
        System.out.println("dashboardService.getWeekBasedNumbers() = " + dashboardService.getWeekBasedNumbers());
        return "dashboard/student-dashboard";
    }

    @PostMapping(value = "/completeStudentTask/{taskId}",  params = {"action=completed"})
    public String completeStudentTask(@PathVariable("taskId") Long taskId){
        dashboardService.completeStudentTask(taskId);
        return "redirect:/dashboards/student";
    }

    @PostMapping(value = "/completeStudentTask/{taskId}",  params = {"action=uncompleted"})
    public String uncompleteStudentTask(@PathVariable("taskId") Long taskId){
        dashboardService.uncompleteStudentTask(taskId);
        return "redirect:/dashboards/student";
    }

}
