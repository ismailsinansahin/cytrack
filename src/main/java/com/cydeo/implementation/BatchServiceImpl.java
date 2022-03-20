package com.cydeo.implementation;

import com.cydeo.dto.BatchDTO;
import com.cydeo.entity.*;
import com.cydeo.enums.BatchStatus;
import com.cydeo.mapper.MapperUtil;
import com.cydeo.repository.*;
import com.cydeo.service.BatchService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class BatchServiceImpl implements BatchService {

    private final MapperUtil mapperUtil;
    private final BatchRepository batchRepository;
    private final GroupRepository groupRepository;
    private final TaskRepository taskRepository;
    private final StudentTaskRepository studentTaskRepository;
    private final UserRepository userRepository;
    private final BatchGroupStudentRepository batchGroupStudentRepository;

    public BatchServiceImpl(BatchRepository batchRepository, MapperUtil mapperUtil,
                            GroupRepository groupRepository, TaskRepository taskRepository,
                            StudentTaskRepository studentTaskRepository, UserRepository userRepository,
                            BatchGroupStudentRepository batchGroupStudentRepository) {
        this.batchRepository = batchRepository;
        this.mapperUtil = mapperUtil;
        this.groupRepository = groupRepository;
        this.taskRepository = taskRepository;
        this.studentTaskRepository = studentTaskRepository;
        this.userRepository = userRepository;
        this.batchGroupStudentRepository = batchGroupStudentRepository;
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
        int numberOfGroups = batchDTO.getNumberOfGroups();
        groupRepository.save(new Group("-", "", null, null));
        for (int i=1 ; i<=numberOfGroups ; i++) {
            User cydeoMentor = userRepository.findById(6L).get();
            User alumniMentor = userRepository.findById(8L).get();
            Group group = new Group("Group-" + i, "", cydeoMentor, alumniMentor);
            groupRepository.save(group);
            batchGroupStudentRepository.save(new BatchGroupStudent(batch, group, null));
        }
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
    public void delete(Long batchId) {
        Batch batch = batchRepository.findById(batchId).get();
        List<BatchGroupStudent> batchGroupStudentsToBeDeleted = new ArrayList<>(batchGroupStudentRepository.findAllByBatch(batch));
        List<User> studentList = batchGroupStudentsToBeDeleted
                .stream()
                .map(BatchGroupStudent::getStudent)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
        for (User student : studentList) {
            student.setCurrentBatch(null);
            student.setCurrentGroup(null);
            userRepository.save(student);
        }
        List<Group> groupList = batchGroupStudentRepository.findAllByBatch(batch)
                .stream()
                .map(BatchGroupStudent::getGroup)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
        for (Group group : groupList) {
            group.setIsDeleted(true);
            groupRepository.save(group);
        }
        List<Task> taskList = taskRepository.findAllByBatch(batch);
        for (Task task : taskList) {
            task.setIsDeleted(true);
            taskRepository.save(task);
        }
        for (BatchGroupStudent batchGroupStudent : batchGroupStudentsToBeDeleted){
            batchGroupStudent.setIsDeleted(true);
            batchGroupStudentRepository.save(batchGroupStudent);
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
        List<User> studentList = batchGroupStudentRepository.findAllByBatch(batch)
                .stream()
                .map(BatchGroupStudent::getStudent)
                .collect(Collectors.toList());
        for (User student : studentList) {
            batchGroupStudentRepository.save(new BatchGroupStudent(batch, getCurrentGroup(student), student));
        }
        batch.setBatchStatus(BatchStatus.COMPLETED);
        batchRepository.save(batch);
    }

    @Override
    public BatchDTO getByBatchId(Long batchId) {
        Batch batch = batchRepository.findById(batchId).get();
        return mapperUtil.convert(batch, new BatchDTO());
    }

    Group getCurrentGroup(User student){
        return batchGroupStudentRepository.findAllByStudent(student)
                .stream()
                .filter(obj -> obj.getBatch().getBatchStatus().equals(BatchStatus.INPROGRESS))
                .map(BatchGroupStudent::getGroup)
                .findFirst().get();
    }

}
