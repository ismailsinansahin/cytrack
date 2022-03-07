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
    private final GroupRepository groupRepository;
    private final BatchRepository batchRepository;
    private final MapperUtil mapperUtil;

    public DashboardServiceImpl(StudentTaskRepository studentTaskRepository, UserRepository userRepository,
                                TaskRepository taskRepository, GroupRepository groupRepository,
                                BatchRepository batchRepository, MapperUtil mapperUtil) {
        this.studentTaskRepository = studentTaskRepository;
        this.userRepository = userRepository;
        this.taskRepository = taskRepository;
        this.groupRepository = groupRepository;
        this.batchRepository = batchRepository;
        this.mapperUtil = mapperUtil;
    }

    @Override
    public UserDTO getCurrentUser() {
        User student = userRepository.findById(getCurrentUserId()).get();
        Group group = groupRepository.findById(student.getGroup().getId()).get();
        User cydeoMentor = userRepository.findById(group.getCydeoMentor().getId()).get();
        User alumniMentor = userRepository.findById(group.getAlumniMentor().getId()).get();
        Batch batch = batchRepository.findById(group.getId()).get();
        GroupDTO groupDTO = mapperUtil.convert(group, new GroupDTO());
        groupDTO.setCydeoMentor(mapperUtil.convert(cydeoMentor, new UserDTO()));
        groupDTO.setAlumniMentor(mapperUtil.convert(alumniMentor, new UserDTO()));
        BatchDTO batchDTO = mapperUtil.convert(batch, new BatchDTO());
        groupDTO.setBatch(batchDTO);
        UserDTO studentDTO = mapperUtil.convert(student, new UserDTO());
//        studentDTO.setGroupDTO(groupDTO);
//        studentDTO.setBatchDTO(batchDTO);
        return studentDTO;
    }

    public Long getCurrentUserId(){
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username = (principal instanceof UserDetails) ? ((UserDetails)principal).getUsername() : principal.toString();
        return userRepository.findByUserName(username).getId();
    }

    @Override
    public List<StudentTaskDTO> getAllTasksOfCurrentStudent() {
        User student = userRepository.findById(getCurrentUserId()).get();
        log.info(student.getFirstName(),"s");
        List<StudentTaskDTO> studentTaskDTOList = new ArrayList<>();
        List<StudentTask> studentTaskList = studentTaskRepository.findAllByStudent(student);
        for(StudentTask studentTask : studentTaskList){
            TaskDTO taskDTO = mapperUtil.convert(studentTask.getTask(), new TaskDTO());
            LessonDTO lessonDTO = mapperUtil.convert(studentTask.getTask().getLesson(), new LessonDTO());
            taskDTO.setLesson(lessonDTO);
            StudentTaskDTO studentTaskDTO = mapperUtil.convert(studentTask, new StudentTaskDTO());
            studentTaskDTO.setTask(taskDTO);
            studentTaskDTOList.add(studentTaskDTO);
        }
        return studentTaskDTOList;
    }

    @Override
    public Map<String, Integer> getTaskBasedNumbers() {
        Map<String, Integer> taskBasedNumbersMap = new HashMap<>();
        List<String> tasksTypesList= Arrays
                .stream(TaskType.values())
                .map(TaskType::getValue)
                .collect(Collectors.toList());
        for(String taskType : tasksTypesList){
            int completionRate = getCompletionOfTask(taskType);
            taskBasedNumbersMap.put(taskType, completionRate);
        }
        taskBasedNumbersMap.put("Total", getTotalCompletionRate());
        return taskBasedNumbersMap;
    }

    private Integer getCompletionOfTask(String taskType) {
        int completionRate;
        User student = userRepository.findById(getCurrentUserId()).get();
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

    private Integer getTotalCompletionRate() {
        int completionRate;
        User student = userRepository.findById(getCurrentUserId()).get();
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
    public Map<String, Integer> getWeekBasedNumbers() {
        Map<String, Integer> weekBasedNumbersMap = new HashMap<>();
        User student = userRepository.findById(getCurrentUserId()).get();
        Batch studentBatch = student.getBatch();
        int numberOfWeeks = calculateNumberOfWeeks(studentBatch);
        for (int i = 1 ; i < numberOfWeeks ; i++) {
            weekBasedNumbersMap.put(("W" + i), getNumbersForWeekI(studentBatch, i));
        }
        return weekBasedNumbersMap;
    }

    private int calculateNumberOfWeeks(Batch studentBatch) {
        return Weeks.between(studentBatch.getBatchStartDate(), studentBatch.getBatchEndDate()).getAmount();
    }

    private Integer getNumbersForWeekI(Batch studentBatch, int i) {
        int completionRate;
        User student = userRepository.findById(getCurrentUserId()).get();
        LocalDate weekStartDate = studentBatch.getBatchStartDate().plusDays(7 * (i-1));
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
