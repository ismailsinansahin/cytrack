package com.cydeo.implementation;

import com.cydeo.dto.BatchDTO;
import com.cydeo.entity.StudentTask;
import com.cydeo.entity.User;
import com.cydeo.enums.StudentStatus;
import com.cydeo.mapper.MapperUtil;
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

    public CydeoStatisticsServiceImpl(MapperUtil mapperUtil, BatchRepository batchRepository,
                                      UserRepository userRepository, StudentTaskRepository studentTaskRepository) {
        this.mapperUtil = mapperUtil;
        this.batchRepository = batchRepository;
        this.userRepository = userRepository;
        this.studentTaskRepository = studentTaskRepository;
    }

    @Override
    public List<BatchDTO> getAllBatches() {
        return batchRepository.findAll()
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
        return (int) userRepository.findAllByBatch(batchRepository.findById(batchDTO.getId()).get())
                .stream()
                .filter(student -> student.getStudentStatus().equals(StudentStatus.NEW) ||
                                   student.getStudentStatus().equals(StudentStatus.RETURNING))
                .count();
    }

    private int getDroppedTransferredStudents(BatchDTO batchDTO) {
        return (int) userRepository.findAllByBatch(batchRepository.findById(batchDTO.getId()).get())
                .stream()
                .filter(student -> student.getStudentStatus().equals(StudentStatus.DROPPED) ||
                        student.getStudentStatus().equals(StudentStatus.TRANSFERRED))
                .count();
    }

    private int getStudentProgress(BatchDTO batchDTO) {
        int studentProgress;
        int totalTask = 0;
        int totalCompleted = 0;
        List<User> studentsInBatch = userRepository.findAllByBatch(batchRepository.findById(batchDTO.getId()).get());
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
