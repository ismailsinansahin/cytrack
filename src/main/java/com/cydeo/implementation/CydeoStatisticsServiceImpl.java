package com.cydeo.implementation;

import com.cydeo.dto.BatchDTO;
import com.cydeo.entity.Batch;
import com.cydeo.entity.BatchGroupStudent;
import com.cydeo.entity.StudentTask;
import com.cydeo.entity.User;
import com.cydeo.enums.StudentStatus;
import com.cydeo.mapper.MapperUtil;
import com.cydeo.repository.BatchGroupStudentRepository;
import com.cydeo.repository.BatchRepository;
import com.cydeo.repository.StudentTaskRepository;
import com.cydeo.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.threeten.extra.Days;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CydeoStatisticsServiceImpl implements com.cydeo.service.CydeoStatisticsService {

    private final MapperUtil mapperUtil;
    private final BatchRepository batchRepository;
    private final UserRepository userRepository;
    private final StudentTaskRepository studentTaskRepository;
    private final BatchGroupStudentRepository batchGroupStudentRepository;

    public CydeoStatisticsServiceImpl(MapperUtil mapperUtil, BatchRepository batchRepository,
                                      UserRepository userRepository, StudentTaskRepository studentTaskRepository,
                                      BatchGroupStudentRepository batchGroupStudentRepository) {
        this.mapperUtil = mapperUtil;
        this.batchRepository = batchRepository;
        this.userRepository = userRepository;
        this.studentTaskRepository = studentTaskRepository;
        this.batchGroupStudentRepository = batchGroupStudentRepository;
    }

    @Override
    public List<BatchDTO> getAllBatches() {
        return batchRepository.findAllByIdIsNot(1L)
                .stream()
                .map(obj -> mapperUtil.convert(obj, new BatchDTO()))
                .peek(batchDTO -> batchDTO.setCompletion(getCompletionRate(batchDTO)))
                .peek(batchDTO -> batchDTO.setActiveStudents(getActiveStudents(batchDTO)))
                .peek(batchDTO -> batchDTO.setDroppedTransferredStudents(getDroppedTransferredStudents(batchDTO)))
                .peek(batchDTO -> batchDTO.setStudentProgress(getStudentProgress(batchDTO)))
                .collect(Collectors.toList());
    }

    private int getCompletionRate(BatchDTO batchDTO) {
        if(batchDTO.getBatchEndDate().isBefore(LocalDate.now())) return 100;
        if(batchDTO.getBatchStartDate().isAfter(LocalDate.now())) return 0;
        int daysInBatch = Days.between(batchDTO.getBatchStartDate(), batchDTO.getBatchEndDate()).getAmount();
        int daysUntilToday = Days.between(batchDTO.getBatchStartDate(), LocalDate.now()).getAmount();
        return daysUntilToday * 100 / daysInBatch;
    }

    private int getActiveStudents(BatchDTO batchDTO) {
        Batch batch = batchRepository.findById(batchDTO.getId()).get();
        List<BatchGroupStudent> batchGroupStudentList = batchGroupStudentRepository.findAllByBatch(batch);
        return (int) batchGroupStudentList
                .stream()
                .map(BatchGroupStudent::getStudentStatus)
                .filter(studentStatus -> studentStatus == StudentStatus.ALUMNI || studentStatus == StudentStatus.ACTIVE)
                .count();
    }

    private int getDroppedTransferredStudents(BatchDTO batchDTO) {
        Batch batch = batchRepository.findById(batchDTO.getId()).get();
        List<BatchGroupStudent> batchGroupStudentList = batchGroupStudentRepository.findAllByBatch(batch);
        return (int) batchGroupStudentList
                .stream()
                .filter(batchGroupStudent -> batchGroupStudent.getStudentStatus() == StudentStatus.DROPPED || batchGroupStudent.getStudentStatus() == StudentStatus.TRANSFERRED)
                .filter(batchGroupStudent -> batchGroupStudent.getStudent().getCurrentBatch() != batchGroupStudent.getBatch())
                .count();
    }

    private int getStudentProgress(BatchDTO batchDTO) {
        int studentProgress;
        int totalTask = 0;
        int totalCompleted = 0;
        List<User> studentsInBatch = batchGroupStudentRepository.findAllByBatch(batchRepository.findById(batchDTO.getId()).get())
                .stream()
                .map(BatchGroupStudent::getStudent)
                .collect(Collectors.toList());
        for (User student : studentsInBatch){
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

}
