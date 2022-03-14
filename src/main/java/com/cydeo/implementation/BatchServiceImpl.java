package com.cydeo.implementation;

import com.cydeo.dto.BatchDTO;
import com.cydeo.entity.Batch;
import com.cydeo.entity.StudentTask;
import com.cydeo.entity.Task;
import com.cydeo.entity.User;
import com.cydeo.enums.BatchStatus;
import com.cydeo.enums.TaskStatus;
import com.cydeo.mapper.MapperUtil;
import com.cydeo.repository.*;
import com.cydeo.service.BatchService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class BatchServiceImpl implements BatchService {

    private final MapperUtil mapperUtil;
    private final BatchRepository batchRepository;
    private final GroupRepository groupRepository;
    private final TaskRepository taskRepository;
    private final StudentTaskRepository studentTaskRepository;
    private final UserRepository userRepository;

    public BatchServiceImpl(BatchRepository batchRepository, MapperUtil mapperUtil,
                            GroupRepository groupRepository, TaskRepository taskRepository,
                            StudentTaskRepository studentTaskRepository, UserRepository userRepository) {
        this.batchRepository = batchRepository;
        this.mapperUtil = mapperUtil;
        this.groupRepository = groupRepository;
        this.taskRepository = taskRepository;
        this.studentTaskRepository = studentTaskRepository;
        this.userRepository = userRepository;
    }

    @Override
    public List<BatchDTO> listAllBatches() {
        return batchRepository.findAll()
                .stream()
                .map(obj -> mapperUtil.convert(obj, new BatchDTO()))
                .collect(Collectors.toList());
    }

    @Override
    public BatchDTO create(BatchDTO batchDTO) {
        Batch batch = mapperUtil.convert(batchDTO, new Batch());
        batch.setBatchStatus(BatchStatus.PLANNED);
        batchRepository.save(batch);
        return mapperUtil.convert(batch, new BatchDTO());
    }

    @Override
    public BatchDTO save(BatchDTO batchDTO, Long batchId) {
        batchDTO.setId(batchId);
        Batch batch = mapperUtil.convert(batchDTO, new Batch());
        batch.setBatchStatus(BatchStatus.PLANNED);
        batchRepository.save(batch);
        return mapperUtil.convert(batch, new BatchDTO());
    }

    @Override
    public String delete(Long batchId) {
        Batch batch = batchRepository.findById(batchId).get();
        if(isDeletingSafe(batch)){
            batch.setIsDeleted(true);
            batchRepository.save(batch);
            return "success";
        }
        return "failure";
    }

    public Boolean isDeletingSafe(Batch batch) {
        return (groupRepository.findAllByBatch(batch).size() < 1) || (taskRepository.findAllByBatch(batch).size() < 1);
    }

    @Override
    public void deleteBatchWithAllStudentsAndTasks(Long batchId) {
        Batch batch = batchRepository.findById(batchId).get();
        List<Task> taskList = taskRepository.findAllByBatch(batch);
        List<User> studentList = userRepository.findAllByBatch(batch);
        for (Task task : taskList) {
            List<StudentTask> studentTaskList = studentTaskRepository.findAllByTask(task);
            for (StudentTask studentTask : studentTaskList) {
                studentTaskRepository.delete(studentTask);
            }
        }
        for (Task task : taskList) {
            task.setIsDeleted(true);
            taskRepository.save(task);
        }
        for (User student : studentList) {
            student.setIsDeleted(true);
            userRepository.save(student);
        }
        batch.setIsDeleted(true);
        batchRepository.save(batch);
    }

    @Override
    public void deleteBatchWithoutStudentsAndTasks(Long batchId) {
        Batch batch = batchRepository.findById(batchId).get();
        List<Task> taskList = taskRepository.findAllByBatch(batch);
        List<User> studentList = userRepository.findAllByBatch(batch);
        for (Task task : taskList) {
            List<StudentTask> studentTaskList = studentTaskRepository.findAllByTask(task);
            for (StudentTask studentTask : studentTaskList) {
                studentTaskRepository.delete(studentTask);
            }
        }
        for (Task task : taskList) {
            task.setTaskStatus(TaskStatus.PLANNED);
            task.setBatch(null);
            taskRepository.save(task);
        }
        for (User student : studentList) {
            student.setBatch(null);
            userRepository.save(student);
        }
        batch.setIsDeleted(true);
        batchRepository.save(batch);
    }

    @Override
    public void start(Long batchId) {
        Batch batch = batchRepository.findById(batchId).get();
        batch.setBatchStatus(BatchStatus.INPROGRESS);
        batchRepository.save(batch);
    }

    @Override
    public void complete(Long batchId) {
        Batch batch = batchRepository.findById(batchId).get();
        batch.setBatchStatus(BatchStatus.COMPLETED);
        batchRepository.save(batch);
    }

    @Override
    public BatchDTO getByBatchId(Long batchId) {
        Batch batch = batchRepository.findById(batchId).get();
        return mapperUtil.convert(batch, new BatchDTO());
    }

}
