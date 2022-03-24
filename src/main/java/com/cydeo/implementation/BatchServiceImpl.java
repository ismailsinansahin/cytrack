package com.cydeo.implementation;

import com.cydeo.dto.BatchDTO;
import com.cydeo.dto.BatchGroupStudentDTO;
import com.cydeo.entity.*;
import com.cydeo.enums.BatchStatus;
import com.cydeo.enums.StudentStatus;
import com.cydeo.mapper.MapperUtil;
import com.cydeo.repository.*;
import com.cydeo.service.BatchService;
import org.springframework.stereotype.Service;

import java.util.*;
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
        return batchRepository.findAllByIdIsNot(1L)
                .stream()
                .map(obj -> mapperUtil.convert(obj, new BatchDTO()))
                .collect(Collectors.toList());
    }

    @Override
    public Map<BatchDTO, List<Integer>> getBatchesWithNumberOfStudentsMap() {
        Map<BatchDTO, List<Integer>> batchesWithNumberOfStudentsMap = new HashMap<>();
        List<Batch> allBatches = batchRepository.findAllByIdIsNot(1L);
        for (Batch batch : allBatches) {
            int activeStudents = getActiveStudents(batch);
            int droppedTransferredStudents = getDroppedTransferredStudents(batch);
            List<Integer> numberOfStudentsList = Arrays.asList(activeStudents, droppedTransferredStudents);
            BatchDTO batchDTO = mapperUtil.convert(batch, new BatchDTO());
            batchesWithNumberOfStudentsMap.put(batchDTO, numberOfStudentsList);
        }
        return batchesWithNumberOfStudentsMap;
    }

    private int getActiveStudents(Batch batch) {
        List<BatchGroupStudent> batchGroupStudentList = batchGroupStudentRepository.findAllByBatch(batch);
        return (int) batchGroupStudentList
                .stream()
                .map(BatchGroupStudent::getStudentStatus)
                .filter(studentStatus -> studentStatus == StudentStatus.ALUMNI || studentStatus == StudentStatus.ACTIVE)
                .count();
    }

    private int getDroppedTransferredStudents(Batch batch) {
        List<BatchGroupStudent> batchGroupStudentList = batchGroupStudentRepository.findAllByBatch(batch);
        return (int) batchGroupStudentList
                .stream()
                .filter(batchGroupStudent -> batchGroupStudent.getStudentStatus() == StudentStatus.DROPPED || batchGroupStudent.getStudentStatus() == StudentStatus.TRANSFERRED)
                .filter(batchGroupStudent -> batchGroupStudent.getStudent().getCurrentBatch() != batchGroupStudent.getBatch())
                .count();
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
            batchGroupStudentRepository.save(new BatchGroupStudent(batch, group, null, null));
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
            student.setCurrentBatch(batchRepository.findById(1L).get());
            student.setCurrentGroup(groupRepository.findById(1L).get());
            userRepository.save(student);
        }
        List<Group> groupList = batchGroupStudentRepository.findAllByBatch(batch)
                .stream()
                .map(BatchGroupStudent::getGroup)
                .filter(Objects::nonNull)
                .distinct()
                .collect(Collectors.toList());
        for (Group group : groupList) {
            group.setAlumniMentor(null);
            group.setCydeoMentor(null);
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
            if(batchGroupStudent.getStudent()==null) batchGroupStudent.setStudentStatus(null);
            else batchGroupStudent.setStudentStatus(StudentStatus.TRANSFERRED);
            batchGroupStudent.setBatch(batchRepository.findById(1L).get());
            batchGroupStudent.setGroup(groupRepository.findById(1L).get());
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
        List<BatchGroupStudent> actives = new ArrayList<>(batchGroupStudentRepository.findAllByBatchAndStudentStatus(batch, StudentStatus.ACTIVE));
        for (BatchGroupStudent batchGroupStudent : actives) {
            batchGroupStudent.setStudentStatus(StudentStatus.ALUMNI);
            batchGroupStudentRepository.save(batchGroupStudent);
        }
        for (BatchGroupStudent batchGroupStudent : actives) {
            User student = batchGroupStudent.getStudent();
            student.setCurrentBatch(batchRepository.findById(1L).get());
            student.setCurrentGroup(groupRepository.findById(1L).get());
            userRepository.save(student);
        }
        batch.setBatchStatus(BatchStatus.COMPLETED);
        batchRepository.save(batch);
    }

    @Override
    public BatchDTO getByBatchId(Long batchId) {
        Batch batch = batchRepository.findById(batchId).get();
        return mapperUtil.convert(batch, new BatchDTO());
    }

}
