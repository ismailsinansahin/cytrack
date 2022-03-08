package com.cydeo.implementation;

import com.cydeo.dto.GroupDTO;
import com.cydeo.dto.UserDTO;
import com.cydeo.entity.Batch;
import com.cydeo.entity.Group;
import com.cydeo.entity.StudentTask;
import com.cydeo.entity.User;
import com.cydeo.enums.TaskType;
import com.cydeo.mapper.MapperUtil;
import com.cydeo.repository.GroupRepository;
import com.cydeo.repository.StudentTaskRepository;
import com.cydeo.repository.UserRepository;
import com.cydeo.service.GroupStatisticsService;
import com.cydeo.service.StudentStatisticsService;
import org.springframework.stereotype.Service;
import org.threeten.extra.Weeks;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class GroupStatisticsServiceImpl implements GroupStatisticsService {

    private final MapperUtil mapperUtil;
    private final UserRepository userRepository;
    private final GroupRepository groupRepository;
    private final StudentTaskRepository studentTaskRepository;

    public GroupStatisticsServiceImpl(MapperUtil mapperUtil, UserRepository userRepository,
                                      GroupRepository groupRepository,
                                      StudentTaskRepository studentTaskRepository) {
        this.mapperUtil = mapperUtil;
        this.userRepository = userRepository;
        this.groupRepository = groupRepository;
        this.studentTaskRepository = studentTaskRepository;
    }

    @Override
    public GroupDTO getGroupById(Long groupId) {
        return mapperUtil.convert(groupRepository.findById(groupId).get(), new GroupDTO());
    }

    @Override
    public List<UserDTO> getAllStudentsOfGroup(Long groupId) {
        return userRepository.findAllByGroup(groupRepository.findById(groupId).get())
                .stream()
                .map(obj -> mapperUtil.convert(obj, new UserDTO()))
                .collect(Collectors.toList());
    }

    @Override
    public Map<String, Integer> getTaskBasedNumbers(Long groupId) {
        Map<String, Integer> taskBasedNumbersMap = new HashMap<>();
        int completionRate;
        int totalCompletionRate;
        int totalOfAllTasks = 0;
        int completedOfAllTasks = 0;
        List<String> tasksTypesList = getTaskTypes();
        for(String taskType : tasksTypesList){
            int total = getTotalOfGivenTasks(groupId, taskType);
            int completed = getTotalOfCompletedTasks(groupId, taskType);
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
            totalCompletionRate = (completedOfAllTasks * 100 / totalOfAllTasks);
        }catch (ArithmeticException e){
            totalCompletionRate = 0;
        }
        taskBasedNumbersMap.put("Total", totalCompletionRate);
        return taskBasedNumbersMap;
    }

    private int getTotalOfGivenTasks(Long groupId, String taskType) {
        int total = 0;
        List<User> studentsOfGroup = userRepository.findAllByGroup(groupRepository.findById(groupId).get());
        for(User student : studentsOfGroup) {
            total += Math.toIntExact(studentTaskRepository.findAllByStudent(student)
                    .stream()
                    .filter(studentTask -> studentTask.getTask().getTaskType().getValue().equals(taskType))
                    .count());
        }
        return total;
    }

    private int getTotalOfCompletedTasks(Long groupId, String taskType) {
        int completed = 0;
        List<User> studentsOfGroup = userRepository.findAllByGroup(groupRepository.findById(groupId).get());
        for(User student : studentsOfGroup) {
            completed += Math.toIntExact(studentTaskRepository.findAllByStudent(student)
                    .stream()
                    .filter(studentTask -> studentTask.getTask().getTaskType().getValue().equals(taskType))
                    .filter(StudentTask::isCompleted)
                    .count());
        }
        return completed;
    }

    private List<String> getTaskTypes() {
        return Arrays
                .stream(TaskType.values())
                .map(TaskType::getValue)
                .collect(Collectors.toList());
    }

    @Override
    public Map<String, Integer> getWeekBasedNumbers(Long groupId) {
        Map<String, Integer> weekBasedNumbersMap = new HashMap<>();
        int numberOfWeeks = calculateNumberOfWeeks(groupRepository.findById(groupId).get().getBatch());
        for (int numberOfWeek = 1 ; numberOfWeek < numberOfWeeks ; numberOfWeek++) {
            weekBasedNumbersMap.put(("W" + numberOfWeek), getNumbersForWeekI(groupId, numberOfWeek));
        }
        return weekBasedNumbersMap;
    }

    private int calculateNumberOfWeeks(Batch studentBatch) {
        return Weeks.between(studentBatch.getBatchStartDate(), studentBatch.getBatchEndDate()).getAmount();
    }

    private Integer getNumbersForWeekI(Long groupId, int numberOfWeek) {
        int completionRate;
        int total = 0;
        int completed = 0;
        List<User> studentsOfGroup = userRepository.findAllByGroup(groupRepository.findById(groupId).get());
        for(User student : studentsOfGroup) {
            total += getTotalOfGivenTasksInGivenWeek(student, groupId, numberOfWeek);
            completed += getTotalOfCompletedTasksInGivenWeek(student, groupId, numberOfWeek);
        }
        try{
            completionRate = (completed * 100 / total);
        }catch (ArithmeticException e){
            completionRate = 0;
        }
        return completionRate;
    }

    private int getTotalOfGivenTasksInGivenWeek(User student, Long groupId, int numberOfWeek) {
        LocalDate weekStartDate = groupRepository.findById(groupId).get().getBatch().getBatchStartDate().plusDays(7 * (numberOfWeek-1));
        LocalDate weekEndDate = weekStartDate.plusDays(7);
        return (int) studentTaskRepository.findAllByStudent(student)
                .stream()
                .map(StudentTask::getTask)
                .filter(task -> task.getDueDate().isAfter(weekStartDate.minusDays(1)) & task.getDueDate().isBefore(weekEndDate))
                .count();
    }

    private int getTotalOfCompletedTasksInGivenWeek(User student, Long groupId, int numberOfWeek) {
        LocalDate weekStartDate = groupRepository.findById(groupId).get().getBatch().getBatchStartDate().plusDays(7 * (numberOfWeek-1));
        LocalDate weekEndDate = weekStartDate.plusDays(7);
        return  Math.toIntExact(studentTaskRepository.findAllByStudent(student)
                .stream()
                .filter(StudentTask::isCompleted)
                .map(StudentTask::getTask)
                .filter(task -> task.getDueDate().isAfter(weekStartDate.minusDays(1)) & task.getDueDate().isBefore(weekEndDate))
                .count());
    }

}
