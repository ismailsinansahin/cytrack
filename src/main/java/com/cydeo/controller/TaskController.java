package com.cydeo.controller;

import com.cydeo.annotations.ExecutionTime;
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
@ExecutionTime
public class TaskController {

    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @GetMapping("/taskList/{batchId}")
    public String goTaskList(@PathVariable("batchId") Long batchId, Model model){
        model.addAttribute("batch", taskService.getBatchById(batchId));
        model.addAttribute("tasks", taskService.getAllTasksOfBatch(batchId));
        return "task/task-list";
    }

    @GetMapping("/taskCreate/{batchId}")
    public String goTaskCreate(@PathVariable("batchId") Long batchId, Model model){
        model.addAttribute("batch", taskService.getBatchById(batchId));
        model.addAttribute("batches", taskService.getAllBatches());
        model.addAttribute("lessons", taskService.getAllLessons());
        model.addAttribute("taskTypes", Arrays.asList(TaskType.values()));
        model.addAttribute("newTask", new TaskDTO());
        return "task/task-create";
    }

    @PostMapping("/taskCreate/{batchId}")
    public String createTask(@PathVariable("batchId") Long batchId, TaskDTO taskDTO){
        taskService.create(taskDTO, batchId);
        return "redirect:/tasks/taskList/" + taskService.getBatchById(batchId).getId();
    }

    @GetMapping("/taskEdit/{taskId}")
    public String goTaskEdit(@PathVariable("taskId") Long taskId, Model model){
        model.addAttribute("task", taskService.getTaskById(taskId));
        model.addAttribute("batches", taskService.getAllBatches());
        model.addAttribute("lessons", taskService.getAllLessons());
        model.addAttribute("taskTypes", Arrays.asList(TaskType.values()));
        return "task/task-edit";
    }

    @PostMapping("/taskUpdate/{taskId}/{batchId}")
    public String updateTask(@PathVariable("taskId") Long taskId, @PathVariable("batchId") Long batchId, TaskDTO taskDTO){
        taskService.save(taskDTO, taskId, batchId);
        return "redirect:/tasks/taskList/" + batchId;
    }

    @PostMapping(value = "/publishCompleteEditDelete/{taskId}", params = {"action=delete"})
    public String deleteTask(@PathVariable("taskId") Long taskId){
        taskService.delete(taskId);
        return "redirect:/tasks/taskList/" + taskService.getTaskById(taskId).getBatch().getId();
    }

    @PostMapping(value = "/publishCompleteEditDelete/{taskId}", params = {"action=publish"})
    public String publishTask(@PathVariable("taskId") Long taskId){
        taskService.publish(taskId);
        return "redirect:/tasks/taskList/" + taskService.getTaskById(taskId).getBatch().getId();
    }

    @PostMapping(value = "/publishCompleteEditDelete/{taskId}", params = {"action=unpublish"})
    public String unpublishTask(@PathVariable("taskId") Long taskId){
        taskService.unpublish(taskId);
        return "redirect:/tasks/taskList/" + taskService.getTaskById(taskId).getBatch().getId();
    }

    @PostMapping(value = "/publishCompleteEditDelete/{taskId}", params = {"action=complete"})
    public String completeTask(@PathVariable("taskId") Long taskId){
        taskService.complete(taskId);
        return "redirect:/tasks/taskList/" + taskService.getTaskById(taskId).getBatch().getId();
    }

    @PostMapping(value = "/publishCompleteEditDelete/{taskId}", params = {"action=edit"})
    public String goEditPage(@PathVariable("taskId") Long taskId){
        return "redirect:/tasks/taskEdit/" + taskId;
    }

}
