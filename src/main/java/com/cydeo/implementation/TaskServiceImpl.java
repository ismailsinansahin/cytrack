package com.cydeo.implementation;

import com.cydeo.dto.BatchDTO;
import com.cydeo.dto.LessonDTO;
import com.cydeo.dto.TaskDTO;
import com.cydeo.entity.StudentTask;
import com.cydeo.entity.Task;
import com.cydeo.entity.User;
import com.cydeo.enums.TaskStatus;
import com.cydeo.enums.UserRole;
import com.cydeo.mapper.MapperUtil;
import com.cydeo.repository.BatchRepository;
import com.cydeo.repository.LessonRepository;
import com.cydeo.repository.TaskRepository;
import com.cydeo.repository.UserRepository;
import com.cydeo.service.TaskService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TaskServiceImpl implements TaskService {

    MapperUtil mapperUtil;
    TaskRepository taskRepository;
    UserRepository userRepository;
    BatchRepository batchRepository;
    LessonRepository lessonRepository;

    public TaskServiceImpl(MapperUtil mapperUtil, TaskRepository taskRepository, UserRepository userRepository,
                           BatchRepository batchRepository, LessonRepository lessonRepository) {
        this.mapperUtil = mapperUtil;
        this.taskRepository = taskRepository;
        this.userRepository = userRepository;
        this.batchRepository = batchRepository;
        this.lessonRepository = lessonRepository;
    }

    @Override
    public List<TaskDTO> getAllTasks() {
        return taskRepository.findAll()
                .stream()
                .map(obj -> mapperUtil.convert(obj, new TaskDTO()))
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
    public void publish(Long taskId) {
        Task task = taskRepository.findById(taskId).get();
        task.setTaskStatus(TaskStatus.PUBLISHED);
        taskRepository.save(task);
        List<User> allStudents = userRepository.findAllByUserRole(UserRole.STUDENT);
        for (User student : allStudents) {
            if (student.getGroup().getBatch().getId() == task.getBatch().getId()) {
                new StudentTask(task.getName(), student, task);
            }
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
