package com.cydeo.implementation;

import com.cydeo.dto.BatchDTO;
import com.cydeo.dto.LessonDTO;
import com.cydeo.dto.TaskDTO;
import com.cydeo.entity.*;
import com.cydeo.enums.TaskStatus;
import com.cydeo.mapper.MapperUtil;
import com.cydeo.repository.*;
import com.cydeo.service.TaskService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TaskServiceImpl implements TaskService {

    private final MapperUtil mapperUtil;
    private final TaskRepository taskRepository;
    private final UserRepository userRepository;
    private final BatchRepository batchRepository;
    private final LessonRepository lessonRepository;
    private final StudentTaskRepository studentTaskRepository;
    private final UserRoleRepository userRoleRepository;

    public TaskServiceImpl(MapperUtil mapperUtil, TaskRepository taskRepository, UserRepository userRepository,
                           BatchRepository batchRepository, LessonRepository lessonRepository,
                           StudentTaskRepository studentTaskRepository, UserRoleRepository userRoleRepository) {
        this.mapperUtil = mapperUtil;
        this.taskRepository = taskRepository;
        this.userRepository = userRepository;
        this.batchRepository = batchRepository;
        this.lessonRepository = lessonRepository;
        this.studentTaskRepository = studentTaskRepository;
        this.userRoleRepository = userRoleRepository;
    }

    @Override
    public List<TaskDTO> getAllTasks() {
        List<Task> taskList = taskRepository.findAll();
//        List<TaskDTO> taskDTOList = new ArrayList<>();
//        for(Task task : taskList){
//            TaskDTO taskDTO = mapperUtil.convert(task, new TaskDTO());
//            BatchDTO batchDTO = mapperUtil.convert(task.getBatch(), new BatchDTO());
//            LessonDTO lessonDTO = mapperUtil.convert(task.getLesson(), new LessonDTO());
//            taskDTO.setBatch(batchDTO);
//            taskDTO.setLesson(lessonDTO);
//            taskDTOList.add(taskDTO);
//        }
        return taskList.stream()
                .map(task -> mapperUtil.convert(task,new TaskDTO()))
                .collect(Collectors.toList());
    }

    @Override
    public TaskDTO getTaskByTaskId(Long taskId) {
        Task task = taskRepository.findById(taskId).get();
        return mapperUtil.convert(task, new TaskDTO());
    }

    @Override
    public TaskDTO save(TaskDTO taskDTO) {
        taskDTO.setTaskStatus(TaskStatus.PLANNED);
        Task task = mapperUtil.convert(taskDTO, new Task());
        taskRepository.save(task);
        return mapperUtil.convert(task, taskDTO);
    }

    @Override
    public void delete(Long taskId) {
        Task task = taskRepository.findById(taskId).get();
        task.setIsDeleted(true);
        taskRepository.save(task);
    }

    @Override
    public void complete(Long taskId) {
        Task task = taskRepository.findById(taskId).get();
        task.setTaskStatus(TaskStatus.OUT_OF_TIME);
        taskRepository.save(task);
    }

    @Override
    public void publish(Long taskId, Long batchId) {
        Task task = taskRepository.findById(taskId).get();
        task.setTaskStatus(TaskStatus.PUBLISHED);
        taskRepository.save(task);
        Batch batch = batchRepository.findById(batchId).get();
        UserRole userRole = userRoleRepository.findByName("Student");
        List<User> allStudents = userRepository.findAllByUserRoleAndBatch(userRole, batch);
        for (User student : allStudents) {
            StudentTask studentTask = new StudentTask(task.getName(), false, student, task);
            studentTaskRepository.save(studentTask);
        }
    }

    @Override
    public List<BatchDTO> getAllBatches() {
        return batchRepository.findAll()
                .stream()
                .map(obj -> mapperUtil.convert(obj, new BatchDTO()))
                .collect(Collectors.toList());
    }

    @Override
    public List<LessonDTO> getAllLessons() {
        return lessonRepository.findAll()
                .stream()
                .map(obj -> mapperUtil.convert(obj, new LessonDTO()))
                .collect(Collectors.toList());
    }

}
