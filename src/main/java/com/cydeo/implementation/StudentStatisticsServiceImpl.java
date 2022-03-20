package com.cydeo.implementation;

import com.cydeo.dto.*;
import com.cydeo.entity.Batch;
import com.cydeo.entity.BatchGroupStudent;
import com.cydeo.entity.StudentTask;
import com.cydeo.entity.User;
import com.cydeo.enums.BatchStatus;
import com.cydeo.enums.TaskType;
import com.cydeo.mapper.MapperUtil;
import com.cydeo.repository.*;
import com.cydeo.service.StudentStatisticsService;
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
    private final BatchGroupStudentRepository batchGroupStudentRepository;

    public StudentStatisticsServiceImpl(MapperUtil mapperUtil, UserRepository userRepository,
                                        StudentTaskRepository studentTaskRepository,
                                        BatchGroupStudentRepository batchGroupStudentRepository) {
        this.mapperUtil = mapperUtil;
        this.userRepository = userRepository;
        this.studentTaskRepository = studentTaskRepository;
        this.batchGroupStudentRepository = batchGroupStudentRepository;
    }

    @Override
    public UserDTO findStudentById(Long studentId) {
        return mapperUtil.convert(userRepository.findById(studentId).get(), new UserDTO());
    }

    @Override
    public List<StudentTaskDTO> getAllTasksOfStudent(Long studentId) {
        return studentTaskRepository.findAllByStudent(userRepository.findById(studentId).get())
                .stream()
                .map(obj -> mapperUtil.convert(obj, new StudentTaskDTO()))
                .collect(Collectors.toList());
    }

    @Override
    public Map<String, Integer> getTaskBasedNumbers(Long studentId) {
        Map<String, Integer> taskBasedNumbersMap = new HashMap<>();
        int completionRate;
        int totalcompletionRate;
        int totalOfAllTasks = 0;
        int completedOfAllTasks = 0;
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
    public Map<String, Integer> getWeekBasedNumbers(Long studentId) {
        Map<String, Integer> weekBasedNumbersMap = new HashMap<>();
        Batch currentBatch = getCurrentBatch(userRepository.findById(studentId).get());
        int numberOfWeeks = calculateNumberOfWeeks(currentBatch);
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
        Batch currentBatch = getCurrentBatch(student);
        LocalDate weekStartDate = currentBatch.getBatchStartDate().plusDays(7L * (numberOfWeek-1));
        LocalDate weekEndDate = weekStartDate.plusDays(7);
        return (int) studentTaskRepository.findAllByStudent(student)
                .stream()
                .map(StudentTask::getTask)
                .filter(task -> task.getDueDate().isAfter(weekStartDate.minusDays(1)) & task.getDueDate().isBefore(weekEndDate))
                .count();
    }

    private int getTotalOfCompletedTasksInGivenWeek(User student, int numberOfWeek) {
        Batch currentBatch = getCurrentBatch(student);
        LocalDate weekStartDate = currentBatch.getBatchStartDate().plusDays(7L * (numberOfWeek-1));
        LocalDate weekEndDate = weekStartDate.plusDays(7);
        return Math.toIntExact(studentTaskRepository.findAllByStudent(student)
                .stream()
                .filter(StudentTask::isCompleted)
                .map(StudentTask::getTask)
                .filter(task -> task.getDueDate().isAfter(weekStartDate.minusDays(1)) & task.getDueDate().isBefore(weekEndDate))
                .count());
    }

    Batch getCurrentBatch(User student){
        return batchGroupStudentRepository.findAllByStudent(student)
                .stream()
                .map(BatchGroupStudent::getBatch)
                .filter(batch -> batch.getBatchStatus().equals(BatchStatus.INPROGRESS))
                .findFirst().get();
    }

}
