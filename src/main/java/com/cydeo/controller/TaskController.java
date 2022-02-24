package com.cydeo.controller;

import com.cydeo.dto.TaskDTO;
import com.cydeo.enums.TaskType;
import com.cydeo.service.TaskService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Arrays;

@Controller
@RequestMapping("/tasks")
public class TaskController {

    TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @GetMapping("/taskList")
    public String goTaskList(Model model){
        model.addAttribute("tasks", taskService.getAllTasks());
        return "task/task-list";
    }

    @GetMapping("/taskCreate")
    public String goTaskCreate(Model model){
        model.addAttribute("newTask", new TaskDTO());
        model.addAttribute("batches", taskService.getAllBatches());
        model.addAttribute("lessons", taskService.getAllLessons());
        model.addAttribute("taskTypes", Arrays.asList(TaskType.values()));
        return "task/task-create";
    }

    @GetMapping("/taskEdit/{taskId}")
    public String goTaskEdit(@PathVariable("taskId") Long taskId, Model model){
        model.addAttribute("task", taskService.getTaskByTaskId(taskId));
        model.addAttribute("batches", taskService.getAllBatches());
        model.addAttribute("lessons", taskService.getAllLessons());
        model.addAttribute("taskTypes", Arrays.asList(TaskType.values()));
        return "task/task-edit";
    }

    @PostMapping("/taskEdit/{taskId}")
    public String updateTask(@PathVariable("taskId") Long taskId, TaskDTO taskDTO){
        taskDTO.setId(taskId);
        taskService.save(taskDTO);
        return "redirect:/tasks/taskList";
    }

    @PostMapping("/taskCreate")
    public String createTask(TaskDTO taskDTO){
        taskService.save(taskDTO);
        return "redirect:/tasks/taskList";
    }

    @PostMapping(value = "/publishCompleteEditDelete/{taskId}", params = {"action=delete"})
    public String deleteTask(@PathVariable("taskId") Long taskId){
        taskService.delete(taskId);
        return "redirect:/tasks/taskList";
    }

    @PostMapping(value = "/publishCompleteEditDelete/{taskId}", params = {"action=publish"})
    public String publishTask(@PathVariable("taskId") Long taskId){
        taskService.publish(taskId);
        return "redirect:/tasks/taskList";
    }

    @PostMapping(value = "/publishCompleteEditDelete/{taskId}", params = {"action=complete"})
    public String completeTask(@PathVariable("taskId") Long taskId){
        taskService.complete(taskId);
        return "redirect:/tasks/taskList";
    }

    @PostMapping(value = "/publishCompleteEditDelete/{taskId}", params = {"action=edit"})
    public String goEditPage(@PathVariable("taskId") Long taskId){
        return "redirect:/tasks/taskEdit/" + taskId;
    }

}
