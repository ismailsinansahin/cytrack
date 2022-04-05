package com.cydeo.implementation;

import com.cydeo.dto.GroupDTO;
import com.cydeo.dto.UserDTO;
import com.cydeo.entity.*;
import com.cydeo.enums.StudentStatus;
import com.cydeo.enums.TaskType;
import com.cydeo.mapper.MapperUtil;
import com.cydeo.repository.*;
import com.cydeo.service.GroupStatisticsService;
import org.springframework.stereotype.Service;
import org.threeten.extra.Weeks;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class GroupStatisticsServiceImpl implements GroupStatisticsService {

    private final MapperUtil mapperUtil;
    private final UserRepository userRepository;
    private final GroupRepository groupRepository;
    private final BatchRepository batchRepository;
    private final StudentTaskRepository studentTaskRepository;
    private final BatchGroupStudentRepository batchGroupStudentRepository;

    public GroupStatisticsServiceImpl(MapperUtil mapperUtil, UserRepository userRepository,
                                      GroupRepository groupRepository,
                                      BatchRepository batchRepository, StudentTaskRepository studentTaskRepository,
                                      BatchGroupStudentRepository batchGroupStudentRepository) {
        this.mapperUtil = mapperUtil;
        this.userRepository = userRepository;
        this.groupRepository = groupRepository;
        this.batchRepository = batchRepository;
        this.studentTaskRepository = studentTaskRepository;
        this.batchGroupStudentRepository = batchGroupStudentRepository;
    }

    @Override
    public GroupDTO getGroupById(Long groupId) {
        return mapperUtil.convert(groupRepository.findById(groupId).get(), new GroupDTO());
    }

    @Override
    public List<UserDTO> getAllStudentsOfGroup(Long groupId) {
        return batchGroupStudentRepository.findAllByGroup(groupRepository.findById(groupId).get())
                .stream()
                .map(BatchGroupStudent::getStudent)
                .map(obj -> mapperUtil.convert(obj, new UserDTO()))
                .peek(studentDTO -> studentDTO.setStudentProgress(getStudentProgress(studentDTO)))
                .collect(Collectors.toList());
    }

    @Override
    public Map<UserDTO, StudentStatus> getStudentsWithStudentStatusMap(Long batchId, Long groupId) {
        Map<UserDTO, StudentStatus> studentsWithStudentStatusMap = new HashMap<>();
        Batch batch = batchRepository.findById(batchId).get();
        Group group = groupRepository.findById(groupId).get();
        List<BatchGroupStudent> batchGroupStudentList = batchGroupStudentRepository.findAllByBatchAndGroup(batch, group)
                .stream()
                .filter(batchGroupStudent -> batchGroupStudent.getStudent() != null)
                .collect(Collectors.toList());
        for (BatchGroupStudent batchGroupStudent : batchGroupStudentList) {
            UserDTO studentDTO = mapperUtil.convert(batchGroupStudent.getStudent(), new UserDTO());
            studentDTO.setStudentProgress(getStudentProgress(studentDTO));
            studentsWithStudentStatusMap.put(studentDTO, batchGroupStudent.getStudentStatus());
        }
        return studentsWithStudentStatusMap;
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
        List<User> studentsOfGroup = batchGroupStudentRepository
                .findAllByGroup(groupRepository.findById(groupId).get())
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

    private int getTotalOfCompletedTasks(Long groupId, String taskType) {
        int completed = 0;
        List<User> studentsOfGroup = batchGroupStudentRepository
                .findAllByGroup(groupRepository.findById(groupId).get())
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
    public Map<String, Integer> getWeekBasedNumbers(Long groupId) {
        Map<String, Integer> weekBasedNumbersMap = new LinkedHashMap<>();
        Batch batch = batchGroupStudentRepository.findAllByGroup(groupRepository.findById(groupId).get())
                .stream()
                .map(BatchGroupStudent::getBatch)
                .findFirst().get();
        int numberOfWeeks = calculateNumberOfWeeks(batch);
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
        List<User> studentsOfGroup = batchGroupStudentRepository
                .findAllByGroup(groupRepository.findById(groupId).get())
                .stream()
                .map(BatchGroupStudent::getStudent)
                .collect(Collectors.toList());
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
        Batch batch = batchGroupStudentRepository.findAllByGroup(groupRepository.findById(groupId).get())
                .stream()
                .map(BatchGroupStudent::getBatch)
                .findFirst().get();
        LocalDate weekStartDate = batch.getBatchStartDate().plusDays(7 * (numberOfWeek-1));
        LocalDate weekEndDate = weekStartDate.plusDays(7);
        return (int) studentTaskRepository.findAllByStudent(student)
                .stream()
                .map(StudentTask::getTask)
                .filter(task -> task.getDueDate().isAfter(weekStartDate.minusDays(1)) & task.getDueDate().isBefore(weekEndDate))
                .count();
    }

    private int getTotalOfCompletedTasksInGivenWeek(User student, Long groupId, int numberOfWeek) {
        Batch batch = batchGroupStudentRepository.findAllByGroup(groupRepository.findById(groupId).get())
                .stream()
                .map(BatchGroupStudent::getBatch)
                .findFirst().get();
        LocalDate weekStartDate = batch.getBatchStartDate().plusDays(7 * (numberOfWeek-1));
        LocalDate weekEndDate = weekStartDate.plusDays(7);
        return  Math.toIntExact(studentTaskRepository.findAllByStudent(student)
                .stream()
                .filter(StudentTask::isCompleted)
                .map(StudentTask::getTask)
                .filter(task -> task.getDueDate().isAfter(weekStartDate.minusDays(1)) & task.getDueDate().isBefore(weekEndDate))
                .count());
    }

    @Override
    public GroupDTO getGroupWithNumberOfStudents(Long groupId) {
        Group group = groupRepository.findById(groupId).get();
        int activeStudents = getActiveStudents(group);
        int droppedTransferredStudents = getDroppedTransferredStudents(group);
        GroupDTO groupDTO = mapperUtil.convert(group, new GroupDTO());
        groupDTO.setActiveStudents(activeStudents);
        groupDTO.setDroppedTransferredStudents(droppedTransferredStudents);
        return groupDTO;
    }

    private int getActiveStudents(Group group) {
        List<BatchGroupStudent> batchGroupStudentList = batchGroupStudentRepository.findAllByGroup(group);
        return (int) batchGroupStudentList
                .stream()
                .filter(batchGroupStudent -> batchGroupStudent.getStudentStatus()  == StudentStatus.ACTIVE || batchGroupStudent.getStudentStatus() == StudentStatus.ALUMNI)
                .count();
    }

    private int getDroppedTransferredStudents(Group group) {
        List<BatchGroupStudent> batchGroupStudentList = batchGroupStudentRepository.findAllByGroup(group);
        return (int) batchGroupStudentList
                .stream()
                .filter(batchGroupStudent -> batchGroupStudent.getStudentStatus() == StudentStatus.DROPPED || batchGroupStudent.getStudentStatus() == StudentStatus.TRANSFERRED)
                .count();
    }

}
