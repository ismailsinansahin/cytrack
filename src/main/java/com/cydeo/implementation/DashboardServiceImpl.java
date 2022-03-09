package com.cydeo.implementation;

import com.cydeo.dto.*;
import com.cydeo.entity.*;
import com.cydeo.enums.TaskType;
import com.cydeo.mapper.MapperUtil;
import com.cydeo.repository.*;
import com.cydeo.service.DashboardService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.threeten.extra.Weeks;


import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class DashboardServiceImpl implements DashboardService {

    private final StudentTaskRepository studentTaskRepository;
    private final UserRepository userRepository;
    private final TaskRepository taskRepository;
    private final BatchRepository batchRepository;
    private final MapperUtil mapperUtil;

    public DashboardServiceImpl(StudentTaskRepository studentTaskRepository, UserRepository userRepository,
                                TaskRepository taskRepository, BatchRepository batchRepository, MapperUtil mapperUtil) {
        this.studentTaskRepository = studentTaskRepository;
        this.userRepository = userRepository;
        this.taskRepository = taskRepository;
        this.batchRepository = batchRepository;
        this.mapperUtil = mapperUtil;
    }

    @Override
    public UserDTO getCurrentUser() {
        return mapperUtil.convert(userRepository.findById(getCurrentUserId()).get(), new UserDTO());

    }

    public Long getCurrentUserId(){
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username = (principal instanceof UserDetails) ? ((UserDetails)principal).getUsername() : principal.toString();
        return userRepository.findByUserName(username).getId();
    }

    @Override
    public List<StudentTaskDTO> getAllTasksOfCurrentStudent() {
        return studentTaskRepository.findAllByStudent(userRepository.findById(getCurrentUserId()).get())
                .stream()
                .map(obj -> mapperUtil.convert(obj, new StudentTaskDTO()))
                .collect(Collectors.toList());
    }

    @Override
    public Map<String, Integer> getTaskBasedNumbers() {
        Map<String, Integer> taskBasedNumbersMap = new HashMap<>();
        int completionRate;
        int totalcompletionRate;
        int totalOfAllTasks = 0;
        int completedOfAllTasks = 0;
        Long studentId = getCurrentUserId();
        List<String> tasksTypesList = getTaskTypes();
        for(String taskType : tasksTypesList){
            int total = getTotalOfGivenTasks(studentId, taskType);
            int completed = getTotalOfCompletedTasks(studentId, taskType);
            try{
                completionRate = (completed * 100 / total);
            }catch (ArithmeticException e){
                completionRate = 0;
            }
            taskBasedNumbersMap.put(taskType, completionRate);
            totalOfAllTasks += total;
            completedOfAllTasks += completed;
        }
        try{
            totalcompletionRate = (completedOfAllTasks * 100 / totalOfAllTasks);
        }catch (ArithmeticException e){
            totalcompletionRate = 0;
        }
        taskBasedNumbersMap.put("Total", totalcompletionRate);
        return taskBasedNumbersMap;
    }

    private List<String> getTaskTypes() {
        return Arrays
                .stream(TaskType.values())
                .map(TaskType::getValue)
                .collect(Collectors.toList());
    }

    private int getTotalOfGivenTasks(Long studentId, String taskType) {
        return Math.toIntExact(studentTaskRepository.findAllByStudent(userRepository.findById(studentId).get())
                .stream()
                .filter(studentTask -> studentTask.getTask().getTaskType().getValue().equals(taskType))
                .count());
    }

    private int getTotalOfCompletedTasks(Long studentId, String taskType) {
        return Math.toIntExact(studentTaskRepository.findAllByStudent(userRepository.findById(studentId).get())
                .stream()
                .filter(studentTask -> studentTask.getTask().getTaskType().getValue().equals(taskType))
                .filter(StudentTask::isCompleted)
                .count());
    }

    @Override
    public Map<String, Integer> getWeekBasedNumbers() {
        Long studentId = getCurrentUserId();
        Map<String, Integer> weekBasedNumbersMap = new HashMap<>();
        int numberOfWeeks = calculateNumberOfWeeks(userRepository.findById(studentId).get().getBatch());
        for (int numberOfWeek = 1 ; numberOfWeek < numberOfWeeks ; numberOfWeek++) {
            weekBasedNumbersMap.put(("W" + numberOfWeek), getNumbersForWeek(studentId, numberOfWeek));
        }
        return weekBasedNumbersMap;
    }

    private int calculateNumberOfWeeks(Batch studentBatch) {
        return Weeks.between(studentBatch.getBatchStartDate(), studentBatch.getBatchEndDate()).getAmount();
    }

    private Integer getNumbersForWeek(Long studentId, int numberOfWeek) {
        int completionRate;
        User student = userRepository.findById(studentId).get();
        int total = getTotalOfGivenTasksInGivenWeek(student, numberOfWeek);
        int completed = getTotalOfCompletedTasksInGivenWeek(student, numberOfWeek);
        try{
            completionRate = (completed * 100 / total);
        }catch (ArithmeticException e){
            completionRate = 0;
        }
        return completionRate;
    }

    private int getTotalOfGivenTasksInGivenWeek(User student, int numberOfWeek) {
        LocalDate weekStartDate = student.getBatch().getBatchStartDate().plusDays(7L * (numberOfWeek-1));
        LocalDate weekEndDate = weekStartDate.plusDays(7);
        return (int) studentTaskRepository.findAllByStudent(student)
                .stream()
                .map(StudentTask::getTask)
                .filter(task -> task.getDueDate().isAfter(weekStartDate.minusDays(1)) & task.getDueDate().isBefore(weekEndDate))
                .count();
    }

    private int getTotalOfCompletedTasksInGivenWeek(User student, int numberOfWeek) {
        LocalDate weekStartDate = student.getBatch().getBatchStartDate().plusDays(7L * (numberOfWeek-1));
        LocalDate weekEndDate = weekStartDate.plusDays(7);
        return Math.toIntExact(studentTaskRepository.findAllByStudent(student)
                .stream()
                .filter(StudentTask::isCompleted)
                .map(StudentTask::getTask)
                .filter(task -> task.getDueDate().isAfter(weekStartDate.minusDays(1)) & task.getDueDate().isBefore(weekEndDate))
                .count());
    }

    @Override
    public void completeStudentTask(Long taskId) {
        User student = userRepository.findById(getCurrentUserId()).get();
        Task task = taskRepository.findById(taskId).get();
        StudentTask studentTask = studentTaskRepository.findByStudentAndTask(student, task);
        studentTask.setCompleted(true);
        studentTaskRepository.save(studentTask);
    }

    @Override
    public void uncompleteStudentTask(Long taskId) {
        User student = userRepository.findById(getCurrentUserId()).get();
        Task task = taskRepository.findById(taskId).get();
        StudentTask studentTask = studentTaskRepository.findByStudentAndTask(student, task);
        studentTask.setCompleted(false);
        studentTaskRepository.save(studentTask);
    }

    @Override
    public List<BatchDTO> getAllBatches() {
        return batchRepository.findAll()
                .stream()
                .map(obj -> mapperUtil.convert(obj, new BatchDTO()))
                .collect(Collectors.toList());
    }

}
