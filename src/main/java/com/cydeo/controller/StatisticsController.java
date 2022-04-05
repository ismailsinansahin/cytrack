package com.cydeo.controller;

import com.cydeo.annotations.ExecutionTime;
import com.cydeo.service.BatchStatisticsService;
import com.cydeo.service.CydeoStatisticsService;
import com.cydeo.service.GroupStatisticsService;
import com.cydeo.service.StudentStatisticsService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/statistics")
@ExecutionTime
public class StatisticsController {

    private final StudentStatisticsService studentStatisticsService;
    private final GroupStatisticsService groupStatisticsService;
    private final BatchStatisticsService batchStatisticsService;
    private final CydeoStatisticsService cydeoStatisticsService;

    public StatisticsController(StudentStatisticsService studentStatisticsService,
                                GroupStatisticsService groupStatisticsService,
                                BatchStatisticsService batchStatisticsService,
                                CydeoStatisticsService cydeoStatisticsService) {
        this.studentStatisticsService = studentStatisticsService;
        this.groupStatisticsService = groupStatisticsService;
        this.batchStatisticsService = batchStatisticsService;
        this.cydeoStatisticsService = cydeoStatisticsService;
    }

    @GetMapping("/batchList")
    public String showBatches(Model model){
        model.addAttribute("batches", cydeoStatisticsService.getAllBatches());
        return "statistics/statistics-batchList";
    }

    @GetMapping("/groupList/{batchId}")
    public String showGroupsOfBatch(@PathVariable("batchId") Long batchId, Model model){
        model.addAttribute("batch", batchStatisticsService.getBatchWithNumberOfStudents(batchId));
        model.addAttribute("groups", batchStatisticsService.getAllGroupsOfBatch(batchId));
        model.addAttribute("taskBasedNumbers", batchStatisticsService.getTaskBasedNumbers(batchId));
        model.addAttribute("weekBasedNumbers", batchStatisticsService.getWeekBasedNumbers(batchId));
        return "statistics/statistics-groupList";
    }

    @PostMapping("/groupList/{batchId}")
    public String goGroupsOfBatch(@PathVariable("batchId") Long batchId){
        return "redirect:/statistics/groupList/" + batchId;
    }

    @GetMapping("/studentList/{batchId}/{groupId}")
    public String showStudentsOfGroup(@PathVariable("batchId") Long batchId, @PathVariable("groupId") Long groupId, Model model){
        model.addAttribute("batch", batchStatisticsService.getBatchById(batchId));
        model.addAttribute("group", groupStatisticsService.getGroupWithNumberOfStudents(groupId));
        model.addAttribute("studentsMap", groupStatisticsService.getStudentsWithStudentStatusMap(batchId, groupId));
        model.addAttribute("taskBasedNumbers", groupStatisticsService.getTaskBasedNumbers(groupId));
        model.addAttribute("weekBasedNumbers", groupStatisticsService.getWeekBasedNumbers(groupId));
        return "statistics/statistics-studentList";
    }

    @GetMapping("/batchStudentList/{batchId}")
    public String showStudentsOfBatch(@PathVariable("batchId") Long batchId, Model model){
        model.addAttribute("batch", batchStatisticsService.getBatchWithNumberOfStudents(batchId));
        model.addAttribute("studentsMap", studentStatisticsService.getStudentsWithGroupNumbersAndStudentStatusMap(batchId));
        model.addAttribute("taskBasedNumbers", batchStatisticsService.getTaskBasedNumbers(batchId));
        model.addAttribute("weekBasedNumbers", batchStatisticsService.getWeekBasedNumbers(batchId));
        return "statistics/statistics-batch-studentList";
    }

    @PostMapping("/studentList/{batchId}/{groupId}")
    public String goStudentsOfGroup(@PathVariable("batchId") Long batchId, @PathVariable("groupId") Long groupId){
        return "redirect:/statistics/studentList/" + batchId + "/" + groupId;
    }

    @GetMapping("/student/{batchId}/{studentId}")
    public String showStudent(@PathVariable("batchId") Long batchId, @PathVariable("studentId") Long studentId, Model model){
        model.addAttribute("batch", batchStatisticsService.getBatchById(batchId));
        model.addAttribute("student", studentStatisticsService.getStudentGroupStudentStatusList(batchId, studentId));
        model.addAttribute("studentTasks", studentStatisticsService.getAllTasksOfStudent(studentId));
        model.addAttribute("taskBasedNumbers", studentStatisticsService.getTaskBasedNumbers(studentId));
        model.addAttribute("weekBasedNumbers", studentStatisticsService.getWeekBasedNumbers(studentId));
        return "statistics/statistics-student";
    }

    @PostMapping("/student/{batchId}/{studentId}")
    public String goStudent(@PathVariable("batchId") Long batchId, @PathVariable("studentId") Long studentId){
        return "redirect:/statistics/student/" + batchId + "/" + studentId;
    }

}
