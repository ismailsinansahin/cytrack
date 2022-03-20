package com.cydeo.implementation;

import com.cydeo.dto.BatchDTO;
import com.cydeo.dto.GroupDTO;
import com.cydeo.entity.*;
import com.cydeo.enums.TaskType;
import com.cydeo.mapper.MapperUtil;
import com.cydeo.repository.*;
import com.cydeo.service.BatchStatisticsService;
import org.springframework.stereotype.Service;
import org.threeten.extra.Weeks;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class BatchStatisticsServiceImpl implements BatchStatisticsService {

    private final MapperUtil mapperUtil;
    private final BatchRepository batchRepository;
    private final GroupRepository groupRepository;
    private final UserRepository userRepository;
    private final StudentTaskRepository studentTaskRepository;
    private final BatchGroupStudentRepository  batchGroupStudentRepository;

    public BatchStatisticsServiceImpl(MapperUtil mapperUtil, BatchRepository batchRepository,
                                      GroupRepository groupRepository, UserRepository userRepository,
                                      StudentTaskRepository studentTaskRepository,
                                      BatchGroupStudentRepository batchGroupStudentRepository) {
        this.mapperUtil = mapperUtil;
        this.batchRepository = batchRepository;
        this.groupRepository = groupRepository;
        this.userRepository = userRepository;
        this.studentTaskRepository = studentTaskRepository;
        this.batchGroupStudentRepository = batchGroupStudentRepository;
    }

    @Override
    public BatchDTO getBatchById(Long batchId) {
        return mapperUtil.convert(batchRepository.findById(batchId).get(), new BatchDTO());
    }

    @Override
    public List<GroupDTO> getAllGroupsOfBatch(Long batchId) {
        return batchGroupStudentRepository.findAllByBatch(batchRepository.findById(batchId).get())
                .stream()
                .map(BatchGroupStudent::getGroup)
                .map(obj -> mapperUtil.convert(obj, new GroupDTO()))
                .peek(groupDTO -> groupDTO.setNumberOfStudents(getNumberOfStudents(groupDTO.getId())))
                .peek(groupDTO -> groupDTO.setStudentProgress(getStudentProgress(groupDTO)))
                .collect(Collectors.toList());
    }

    private int getNumberOfStudents(Long groupId) {
        Group group = groupRepository.findById(groupId).get();
        return (int)batchGroupStudentRepository.findAllByGroup(group)
                .stream()
                .filter(obj -> obj.getStudent() != null)
                .count();
    }

    private int getStudentProgress(GroupDTO groupDTO) {
        int studentProgress;
        int totalTask = 0;
        int totalCompleted = 0;
        List<User> studentsInGroup = batchGroupStudentRepository.findAllByGroup(groupRepository.findById(groupDTO.getId()).get())
                .stream()
                .map(BatchGroupStudent::getStudent)
                .collect(Collectors.toList());
        for (User student : studentsInGroup){
            int totalTaskOfStudent = (int) studentTaskRepository.findAllByStudent(student)
                    .stream()
                    .filter(studentTask -> studentTask.getStudent().equals(student))
                    .count();
            int totalCompletedTaskOfStudent = (int) studentTaskRepository.findAllByStudent(student)
                    .stream()
                    .filter(studentTask -> studentTask.getStudent().equals(student))
                    .filter(StudentTask::isCompleted)
                    .count();
            totalTask += totalTaskOfStudent;
            totalCompleted += totalCompletedTaskOfStudent;
        }
        try{
            studentProgress = totalCompleted * 100 / totalTask;
        }catch (ArithmeticException e){
            studentProgress = 0;
        }
        return studentProgress;
    }

    @Override
    public Map<String, Integer> getTaskBasedNumbers(Long batchId) {
        Map<String, Integer> taskBasedNumbersMap = new HashMap<>();
        int completionRate;
        int totalCompletionRate;
        int totalOfAllTasks = 0;
        int completedOfAllTasks = 0;
        List<String> tasksTypesList = getTaskTypes();
        for(String taskType : tasksTypesList){
            int total = getTotalOfGivenTasks(batchId, taskType);
            int completed = getTotalOfCompletedTasks(batchId, taskType);
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

    private int getTotalOfGivenTasks(Long batchId, String taskType) {
        int total = 0;
        List<User> studentsOfGroup = batchGroupStudentRepository.findAllByBatch(batchRepository.findById(batchId).get())
                .stream()
                .map(BatchGroupStudent::getStudent)
                .collect(Collectors.toList());
        for(User student : studentsOfGroup) {
            total += Math.toIntExact(studentTaskRepository.findAllByStudent(student)
                    .stream()
                    .filter(studentTask -> studentTask.getTask().getTaskType().getValue().equals(taskType))
                    .count());
        }
        return total;
    }

    private int getTotalOfCompletedTasks(Long batchId, String taskType) {
        int completed = 0;
        List<User> studentsOfGroup = batchGroupStudentRepository.findAllByBatch(batchRepository.findById(batchId).get())
                .stream()
                .map(BatchGroupStudent::getStudent)
                .collect(Collectors.toList());
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
    public Map<String, Integer> getWeekBasedNumbers(Long batchId) {
        Map<String, Integer> weekBasedNumbersMap = new HashMap<>();
        int numberOfWeeks = calculateNumberOfWeeks(batchRepository.findById(batchId).get());
        for (int numberOfWeek = 1 ; numberOfWeek < numberOfWeeks ; numberOfWeek++) {
            weekBasedNumbersMap.put(("W" + numberOfWeek), getNumbersForWeekI(batchId, numberOfWeek));
        }
        return weekBasedNumbersMap;
    }

    private int calculateNumberOfWeeks(Batch batch) {
        return Weeks.between(batch.getBatchStartDate(), batch.getBatchEndDate()).getAmount();
    }

    private Integer getNumbersForWeekI(Long batchId, int numberOfWeek) {
        int completionRate;
        int total = 0;
        int completed = 0;
        List<User> studentsOfGroup = batchGroupStudentRepository.findAllByBatch(batchRepository.findById(batchId).get())
                .stream()
                .map(BatchGroupStudent::getStudent)
                .collect(Collectors.toList());
        for(User student : studentsOfGroup) {
            total += getTotalOfGivenTasksInGivenWeek(student, batchId, numberOfWeek);
            completed += getTotalOfCompletedTasksInGivenWeek(student, batchId, numberOfWeek);
        }
        try{
            completionRate = (completed * 100 / total);
        }catch (ArithmeticException e){
            completionRate = 0;
        }
        return completionRate;
    }

    private int getTotalOfGivenTasksInGivenWeek(User student, Long batchId, int numberOfWeek) {
        LocalDate weekStartDate = batchRepository.findById(batchId).get().getBatchStartDate().plusDays(7 * (numberOfWeek-1));
        LocalDate weekEndDate = weekStartDate.plusDays(7);
        return (int) studentTaskRepository.findAllByStudent(student)
                .stream()
                .map(StudentTask::getTask)
                .filter(task -> task.getDueDate().isAfter(weekStartDate.minusDays(1)) & task.getDueDate().isBefore(weekEndDate))
                .count();
    }

    private int getTotalOfCompletedTasksInGivenWeek(User student, Long batchId, int numberOfWeek) {
        LocalDate weekStartDate = batchRepository.findById(batchId).get().getBatchStartDate().plusDays(7 * (numberOfWeek-1));
        LocalDate weekEndDate = weekStartDate.plusDays(7);
        return  Math.toIntExact(studentTaskRepository.findAllByStudent(student)
                .stream()
                .filter(StudentTask::isCompleted)
                .map(StudentTask::getTask)
                .filter(task -> task.getDueDate().isAfter(weekStartDate.minusDays(1)) & task.getDueDate().isBefore(weekEndDate))
                .count());
    }

}
