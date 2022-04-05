package com.cydeo.implementation;

import com.cydeo.dto.*;
import com.cydeo.entity.*;
import com.cydeo.enums.BatchStatus;
import com.cydeo.enums.StudentStatus;
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
    private final BatchRepository batchRepository;

    public StudentStatisticsServiceImpl(MapperUtil mapperUtil, UserRepository userRepository,
                                        StudentTaskRepository studentTaskRepository,
                                        BatchGroupStudentRepository batchGroupStudentRepository,
                                        BatchRepository batchRepository) {
        this.mapperUtil = mapperUtil;
        this.userRepository = userRepository;
        this.studentTaskRepository = studentTaskRepository;
        this.batchGroupStudentRepository = batchGroupStudentRepository;
        this.batchRepository = batchRepository;
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

    @Override
    public Map<UserDTO, List<Object>> getStudentsWithGroupNumbersAndStudentStatusMap(Long batchId) {
        Map<UserDTO, List<Object>> studentsWithGroupNumbersAndStudentStatusMap = new HashMap<>();
        Batch batch = batchRepository.findById(batchId).get();
        List<User> allStudentsOfBatch = batchGroupStudentRepository.findAllByBatch(batch)
                .stream()
                .map(BatchGroupStudent::getStudent)
                .filter(Objects::nonNull)
                .distinct()
                .collect(Collectors.toList());
        for (User student : allStudentsOfBatch) {
            UserDTO studentDTO = mapperUtil.convert(student, new UserDTO());
            studentDTO.setStudentProgress(getStudentProgress(studentDTO));
            StudentStatus studentStatus = null;
            try{
                studentStatus = batchGroupStudentRepository.findAllByBatchAndStudent(batch, student)
                        .stream()
                        .max(Comparator.comparing(BatchGroupStudent::getLastUpdateDateTime)).get()
                        .getStudentStatus();
                Group group = batchGroupStudentRepository.findAllByBatchAndStudent(batch, student)
                        .stream()
                        .max(Comparator.comparing(BatchGroupStudent::getLastUpdateDateTime)).get()
                        .getGroup();
                GroupDTO groupDTO = mapperUtil.convert(group, new GroupDTO());
                List<Object> groupAndStudentStatusList = Arrays.asList(groupDTO, studentStatus);
                studentsWithGroupNumbersAndStudentStatusMap.put(studentDTO, groupAndStudentStatusList);
            }catch (IllegalArgumentException e){
                List<Object> groupAndStudentStatusList = Arrays.asList(null, studentStatus);
                studentsWithGroupNumbersAndStudentStatusMap.put(studentDTO, groupAndStudentStatusList);
            }
        }
        return studentsWithGroupNumbersAndStudentStatusMap;
    }

    private int getStudentProgress(UserDTO studentDTO) {
        int studentProgress;
        int totalTask = 0;
        int totalCompleted = 0;
        User student = userRepository.findById(studentDTO.getId()).get();
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
        try{
            studentProgress = totalCompleted * 100 / totalTask;
        }catch (ArithmeticException e){
            studentProgress = 0;
        }
        return studentProgress;
    }

    @Override
    public List<Object> getStudentGroupStudentStatusList(Long batchId, Long studentId) {
        List<Object> studentGroupStudentStatusList;
        Batch batch = batchRepository.findById(batchId).get();
        User student = userRepository.findById(studentId).get();
        BatchGroupStudent batchGroupStudent = batchGroupStudentRepository.findByBatchAndStudent(batch, student);
        UserDTO studentDTO = mapperUtil.convert(student, new UserDTO());
        studentDTO.setStudentProgress(getStudentProgress(studentDTO));
        StudentStatus studentStatus = null;
        try{
            studentStatus = batchGroupStudent.getStudentStatus();
            Group group = batchGroupStudent.getGroup();
            GroupDTO groupDTO = mapperUtil.convert(group, new GroupDTO());
            studentGroupStudentStatusList = Arrays.asList(student, groupDTO, studentStatus);
        }catch (IllegalArgumentException e){
            studentGroupStudentStatusList = Arrays.asList(student, null, studentStatus);
        }
        return studentGroupStudentStatusList;
    }

}
