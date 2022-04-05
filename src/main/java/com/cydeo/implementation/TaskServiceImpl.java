package com.cydeo.implementation;

import com.cydeo.dto.BatchDTO;
import com.cydeo.dto.LessonDTO;
import com.cydeo.dto.TaskDTO;
import com.cydeo.entity.*;
import com.cydeo.enums.StudentStatus;
import com.cydeo.enums.TaskStatus;
import com.cydeo.mapper.MapperUtil;
import com.cydeo.repository.*;
import com.cydeo.service.TaskService;
import org.springframework.stereotype.Service;

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
    private final BatchGroupStudentRepository batchGroupStudentRepository;

    public TaskServiceImpl(MapperUtil mapperUtil, TaskRepository taskRepository, UserRepository userRepository,
                           BatchRepository batchRepository, LessonRepository lessonRepository,
                           StudentTaskRepository studentTaskRepository, UserRoleRepository userRoleRepository,
                           BatchGroupStudentRepository batchGroupStudentRepository) {
        this.mapperUtil = mapperUtil;
        this.taskRepository = taskRepository;
        this.userRepository = userRepository;
        this.batchRepository = batchRepository;
        this.lessonRepository = lessonRepository;
        this.studentTaskRepository = studentTaskRepository;
        this.userRoleRepository = userRoleRepository;
        this.batchGroupStudentRepository = batchGroupStudentRepository;
    }

    @Override
    public List<TaskDTO> getAllTasksOfBatch(Long batchId) {
        return taskRepository.findAllByBatch(batchRepository.findById(batchId).get())
                .stream()
                .map(obj -> mapperUtil.convert(obj, new TaskDTO()))
                .collect(Collectors.toList());
    }

    @Override
    public TaskDTO getTaskById(Long taskId) {
        return mapperUtil.convert(taskRepository.findById(taskId).get(), new TaskDTO());
    }

    @Override
    public TaskDTO create(TaskDTO taskDTO, Long batchId) {
        taskDTO.setTaskStatus(TaskStatus.PLANNED);
        Task task = mapperUtil.convert(taskDTO, new Task());
        Batch batch = batchRepository.findById(batchId).get();
        task.setBatch(batch);
        taskRepository.save(task);
        return mapperUtil.convert(task, taskDTO);
    }

    @Override
    public TaskDTO save(TaskDTO taskDTO, Long taskId, Long batchId) {
        Task task = taskRepository.findById(taskId).get();
        taskDTO.setId(taskId);
        taskDTO.setTaskStatus(task.getTaskStatus());
        task = mapperUtil.convert(taskDTO, new Task());
        task.setBatch(batchRepository.findById(batchId).get());
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
        List<User> studentList = batchGroupStudentRepository.findAllByBatch(task.getBatch())
                .stream()
                .filter(batchGroupStudent -> batchGroupStudent.getStudentStatus() == StudentStatus.ACTIVE)
                .map(BatchGroupStudent::getStudent)
                .collect(Collectors.toList());
        for (User student : studentList) {
            StudentTask studentTask = new StudentTask(false, student, task);
            studentTaskRepository.save(studentTask);
        }
        task.setTaskStatus(TaskStatus.PUBLISHED);
        taskRepository.save(task);
    }

    @Override
    public void unpublish(Long taskId) {
        Task task = taskRepository.findById(taskId).get();
        List<StudentTask> studentTaskList = studentTaskRepository.findAllByTask(task);
        for (StudentTask studentTask : studentTaskList) {
            studentTaskRepository.delete(studentTask);
        }
        task.setTaskStatus(TaskStatus.PLANNED);
        taskRepository.save(task);
    }

    @Override
    public BatchDTO getBatchById(Long batchId) {
        return mapperUtil.convert(batchRepository.findById(batchId).get(), new BatchDTO());
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
