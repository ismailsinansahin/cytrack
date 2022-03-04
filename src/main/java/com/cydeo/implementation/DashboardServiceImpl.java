package com.cydeo.implementation;

import com.cydeo.dto.BatchDTO;
import com.cydeo.entity.Group;
import com.cydeo.entity.StudentTask;
import com.cydeo.entity.Task;
import com.cydeo.entity.User;
import com.cydeo.mapper.MapperUtil;
import com.cydeo.repository.*;
import com.cydeo.service.DashboardService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
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
    public List<StudentTask> getAllTasksOfStudent(Long currentStudentId) {
        User user = userRepository.findById(currentStudentId).get();
        return studentTaskRepository.findAllByStudent(user);
    }

    @Override
    public void completeStudentTask(Long studentId, Long taskId) {
        User student = userRepository.findById(studentId).get();
        Task task = taskRepository.findById(taskId).get();
        StudentTask studentTask = studentTaskRepository.findByStudentAndTask(student, task);
        studentTask.setCompleted(true);
        studentTaskRepository.save(studentTask);
    }

    @Override
    public void uncompleteStudentTask(Long studentId, Long taskId) {
        User student = userRepository.findById(studentId).get();
        Task task = taskRepository.findById(taskId).get();
        StudentTask studentTask = studentTaskRepository.findByStudentAndTask(student, task);
        studentTask.setCompleted(false);
        studentTaskRepository.save(studentTask);
    }

    @Override
    public List<User> getAllStudentsOfMentor(Long mentorId) {
        List<User> allStudentsOfCydeoMentor = new ArrayList<>();
        User cydeoMentor = userRepository.findById(mentorId).get();
        List<Group> groupsOfCydeoMentor = groupRepository.findAllByCydeoMentor(cydeoMentor);
        for(Group group : groupsOfCydeoMentor){
            List<User> students = userRepository.findAllByGroup(group);
            allStudentsOfCydeoMentor.addAll(students);
        }
        return allStudentsOfCydeoMentor;
    }

    @Override
    public List<BatchDTO> getAllBatches() {
        return batchRepository.findAll()
                .stream()
                .map(obj -> mapperUtil.convert(obj, new BatchDTO()))
                .collect(Collectors.toList());
    }

}
