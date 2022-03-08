package com.cydeo.implementation;

import com.cydeo.dto.*;
import com.cydeo.entity.Batch;
import com.cydeo.entity.StudentTask;
import com.cydeo.entity.Task;
import com.cydeo.entity.User;
import com.cydeo.enums.TaskType;
import com.cydeo.mapper.MapperUtil;
import com.cydeo.repository.*;
import com.cydeo.service.StudentStatisticsService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.threeten.extra.Weeks;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class StudentStatisticsServiceImpl implements StudentStatisticsService {

    private final MapperUtil mapperUtil;
    private final UserRepository userRepository;
    private final StudentTaskRepository studentTaskRepository;

    public StudentStatisticsServiceImpl(MapperUtil mapperUtil, UserRepository userRepository,
                                        StudentTaskRepository studentTaskRepository) {
        this.mapperUtil = mapperUtil;
        this.userRepository = userRepository;
        this.studentTaskRepository = studentTaskRepository;
    }

    @Override
    public UserDTO findStudentById(Long studentId) {
        return mapperUtil.convert(userRepository.findById(getCurrentUserId()).get(), new UserDTO());
    }

    public Long getCurrentUserId(){
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username = (principal instanceof UserDetails) ? ((UserDetails)principal).getUsername() : principal.toString();
        return userRepository.findByUserName(username).getId();
    }

    @Override
    public List<StudentTaskDTO> getAllTasksOfStudent(Long studentId) {
        return studentTaskRepository.findAllByStudent(userRepository.findById(getCurrentUserId()).get())
                .stream()
                .map(obj -> mapperUtil.convert(obj, new StudentTaskDTO()))
                .collect(Collectors.toList());
    }

    @Override
    public Map<String, Integer> getTaskBasedNumbers(Long studentId) {
        Map<String, Integer> taskBasedNumbersMap = new HashMap<>();
        List<String> tasksTypesList= Arrays
                .stream(TaskType.values())
                .map(TaskType::getValue)
                .collect(Collectors.toList());
        for(String taskType : tasksTypesList){
            int completionRate = getCompletionOfTask(studentId, taskType);
            taskBasedNumbersMap.put(taskType, completionRate);
        }
        taskBasedNumbersMap.put("Total", getTotalCompletionRate(studentId));
        return taskBasedNumbersMap;
    }

    private Integer getCompletionOfTask(Long studentId, String taskType) {
        int completionRate;
        User student = userRepository.findById(studentId).get();
        int total = Math.toIntExact(studentTaskRepository.findAllByStudent(student)
                .stream()
                .filter(studentTask -> studentTask.getTask().getTaskType().getValue().equals(taskType))
                .count());
        int completed = Math.toIntExact(studentTaskRepository.findAllByStudent(student)
                .stream()
                .filter(studentTask -> studentTask.getTask().getTaskType().getValue().equals(taskType))
                .filter(StudentTask::isCompleted)
                .count());
        try{
            completionRate = (completed * 100 / total);
        }catch (ArithmeticException e){
            completionRate = 0;
        }
        return completionRate;
    }

    private Integer getTotalCompletionRate(Long studentId) {
        int completionRate;
        User student = userRepository.findById(studentId).get();
        int total = (int) studentTaskRepository.findAllByStudent(student).stream().count();
        int completed = Math.toIntExact(studentTaskRepository.findAllByStudent(student)
                .stream()
                .filter(StudentTask::isCompleted)
                .count());
        try{
            completionRate = (completed * 100 / total);
        }catch (ArithmeticException e){
            completionRate = 0;
        }
        return completionRate;
    }

    @Override
    public Map<String, Integer> getWeekBasedNumbers(Long studentId) {
        Map<String, Integer> weekBasedNumbersMap = new HashMap<>();
        User student = userRepository.findById(studentId).get();
        Batch studentBatch = student.getBatch();
        int numberOfWeeks = calculateNumberOfWeeks(studentBatch);
        for (int i = 1 ; i < numberOfWeeks ; i++) {
            weekBasedNumbersMap.put(("W" + i), getNumbersForWeekI(studentId, i));
        }
        return weekBasedNumbersMap;
    }

    private int calculateNumberOfWeeks(Batch studentBatch) {
        return Weeks.between(studentBatch.getBatchStartDate(), studentBatch.getBatchEndDate()).getAmount();
    }

    private Integer getNumbersForWeekI(Long studentId, int i) {
        int completionRate;
        User student = userRepository.findById(studentId).get();
        LocalDate weekStartDate = student.getBatch().getBatchStartDate().plusDays(7 * (i-1));
        LocalDate weekEndDate = weekStartDate.plusDays(7);
        int total = (int) studentTaskRepository.findAllByStudent(student)
                .stream()
                .map(StudentTask::getTask)
                .filter(task -> task.getDueDate().isAfter(weekStartDate.minusDays(1)) & task.getDueDate().isBefore(weekEndDate))
                .count();
        int completed = Math.toIntExact(studentTaskRepository.findAllByStudent(student)
                .stream()
                .filter(StudentTask::isCompleted)
                .map(StudentTask::getTask)
                .filter(task -> task.getDueDate().isAfter(weekStartDate.minusDays(1)) & task.getDueDate().isBefore(weekEndDate))
                .count());
        try{
            completionRate = (completed * 100 / total);
        }catch (ArithmeticException e){
            completionRate = 0;
        }
        return completionRate;
    }

}
