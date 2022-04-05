package com.cydeo.implementation;

import com.cydeo.dto.LessonDTO;
import com.cydeo.dto.UserDTO;
import com.cydeo.entity.*;
import com.cydeo.enums.TaskStatus;
import com.cydeo.mapper.MapperUtil;
import com.cydeo.repository.*;
import com.cydeo.service.LessonService;
import com.cydeo.service.UserService;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class LessonServiceImpl implements LessonService {

    private final LessonRepository lessonRepository;
    private final MapperUtil mapperUtil;
    private final UserService userService;
    private final InstructorLessonRepository instructorLessonRepository;
    private final UserRepository userRepository;
    private final UserRoleRepository userRoleRepository;
    private final TaskRepository taskRepository;
    private final StudentTaskRepository studentTaskRepository;

    public LessonServiceImpl(LessonRepository lessonRepository, MapperUtil mapperUtil, UserService userService,
                             InstructorLessonRepository instructorLessonRepository, UserRepository userRepository,
                             UserRoleRepository userRoleRepository, TaskRepository taskRepository,
                             StudentTaskRepository studentTaskRepository) {
        this.lessonRepository = lessonRepository;
        this.mapperUtil = mapperUtil;
        this.userService = userService;
        this.instructorLessonRepository = instructorLessonRepository;
        this.userRepository = userRepository;
        this.userRoleRepository = userRoleRepository;
        this.taskRepository = taskRepository;
        this.studentTaskRepository = studentTaskRepository;
    }

    @Override
    public List<LessonDTO> listAllLessons() {
        List<Lesson> lessons = lessonRepository.findAll();
        return lessons.stream().map(obj -> mapperUtil.convert(obj, new LessonDTO())).collect(Collectors.toList());
    }

    @Override
    public Map<LessonDTO, String> getLessonsAndInstructorsMap() {
        Map<LessonDTO,String> lessonsInstructorsMap = new HashMap<>();
        List<LessonDTO> lessons = listAllLessons();
        for (LessonDTO lesson : lessons) {
            List<UserDTO> instructorList = listAllInstructorsOfLesson(lesson.getId());
            String instructors = instructorList
                    .stream()
                    .map(obj -> obj.getFirstName() + " " + obj.getLastName())
                    .reduce("",(x,y)-> x + y + " / ");
            instructors = (instructors.equals("")) ? "-" : instructors.substring(0,instructors.length()-2);
            lessonsInstructorsMap.put(lesson,instructors);
        }
        return lessonsInstructorsMap;
    }

    @Override
    public Map<UserDTO, String> getInstructorsAndLessonsMap() {
        Map<UserDTO,String> instructorsLessonsMap = new HashMap<>();
        UserRole userRole = userRoleRepository.findByName("Instructor");
        List<UserDTO> instructors = userRepository.findAllByUserRole(userRole)
                .stream()
                .map(obj -> mapperUtil.convert(obj, new UserDTO()))
                .collect(Collectors.toList());
        for (UserDTO instructor : instructors) {
            List<LessonDTO> lessonsList = listAllLessonsOfInstructor(instructor.getId());
            String lessons = lessonsList
                    .stream()
                    .map(LessonDTO::getName)
                    .reduce("",(x,y)-> x + y + " / ");
            lessons = (lessons.equals("")) ? "-" : lessons.substring(0,lessons.length()-2);
            instructorsLessonsMap.put(instructor,lessons);
        }
        return instructorsLessonsMap;
    }

    @Override
    public List<UserDTO> getAllInstructors() {
        UserRole userRole = userRoleRepository.findByName("instructor");
        return userRepository.findAllByUserRole(userRole)
                .stream()
                .map(obj -> mapperUtil.convert(obj, new UserDTO()))
                .collect(Collectors.toList());
    }

    @Override
    public LessonDTO getLessonByLessonId(Long lessonId) {
        LessonDTO lessonDTO = mapperUtil.convert(lessonRepository.findById(lessonId).get(), new LessonDTO());
        List<UserDTO> instructorsOfLesson = listAllInstructorsOfLesson(lessonId);
        String instructors = "";
        for (UserDTO instructor : instructorsOfLesson) {
            String fullName = instructor.getFirstName() + " " + instructor.getLastName();
            instructors += fullName + " / ";
        }
        instructors = (instructors.equals("")) ? "-" : instructors.substring(0,instructors.length()-2);
        lessonDTO.setInstructorList(instructors);
        return lessonDTO;
    }

    @Override
    public LessonDTO create(LessonDTO lessonDTO) {
        Lesson lesson = mapperUtil.convert(lessonDTO, new Lesson());
        lessonRepository.save(lesson);
        return mapperUtil.convert(lesson, new LessonDTO());
    }

    @Override
    public LessonDTO save(LessonDTO lessonDTO, Long lessonId) {
        lessonDTO.setId(lessonId);
        Lesson lesson = mapperUtil.convert(lessonDTO, new Lesson());
        lessonRepository.save(lesson);
        return mapperUtil.convert(lesson, new LessonDTO());
    }

    @Override
    public String delete(Long lessonId) {
        Lesson lesson = lessonRepository.findById(lessonId).get();
        if(isDeletingSafe(lesson)){
            lesson.setIsDeleted(true);
            lessonRepository.save(lesson);
            return "success";
        }
        return "failure";
    }

    public Boolean isDeletingSafe(Lesson lesson) {
        return (taskRepository.findAllByLesson(lesson).size() < 1);
    }

    @Override
    public void deleteLessonWithAllTasks(Long lessonId) {
        Lesson lesson = lessonRepository.findById(lessonId).get();
        List<Task> taskList = taskRepository.findAllByLesson(lesson);
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
        lesson.setIsDeleted(true);
        lessonRepository.save(lesson);
    }

    @Override
    public void deleteLessonWithoutTasks(Long lessonId) {
        Lesson lesson = lessonRepository.findById(lessonId).get();
        List<Task> taskList = taskRepository.findAllByLesson(lesson);
        for (Task task : taskList) {
            List<StudentTask> studentTaskList = studentTaskRepository.findAllByTask(task);
            for (StudentTask studentTask : studentTaskList) {
                studentTaskRepository.delete(studentTask);
            }
        }
        for (Task task : taskList) {
            task.setLesson(null);
            taskRepository.save(task);
        }
        lesson.setIsDeleted(true);
        lessonRepository.save(lesson);
    }

    @Override
    public void addInstructor(Long lessonId, Long instructorId) {
        if(!getInstructorIdsOfLesson(lessonId).contains(instructorId)){
            User instructor = userRepository.findById(instructorId).get();
            Lesson lesson = lessonRepository.findById(lessonId).get();
            instructorLessonRepository.save(new InstructorLesson(instructor, lesson));
        }
    }

    @Override
    public void removeInstructor(Long lessonId, Long instructorId) {
        if(getInstructorIdsOfLesson(lessonId).contains(instructorId)) {
            User instructor = userRepository.findById(instructorId).get();
            Lesson lesson = lessonRepository.findById(lessonId).get();
            InstructorLesson instructorLessonToBeDeleted = instructorLessonRepository.findByLessonAndInstructor(lesson, instructor);
            instructorLessonToBeDeleted.setIsDeleted(true);
            instructorLessonRepository.save(instructorLessonToBeDeleted);
        }
    }

    @Override
    public List<LessonDTO> listAllLessonsOfInstructor(Long userId) {
        User user = userRepository.findById(userId).get();
        List<InstructorLesson> instructorLessonListByInstructor = instructorLessonRepository.findAllByInstructor(user);
        return instructorLessonListByInstructor
                .stream()
                .map(obj -> mapperUtil.convert(obj.getLesson(), new LessonDTO()))
                .collect(Collectors.toList());
    }

    @Override
    public List<Long> getInstructorIdsOfLesson(Long lessonId) {
        List<InstructorLesson> instructorsOfLessonList = instructorLessonRepository.findAllByLesson(lessonRepository.findById(lessonId).get());
        List<Long> instructorIdsOfLesson = instructorsOfLessonList.stream().map(obj -> obj.getInstructor().getId()).collect(Collectors.toList());
        return instructorIdsOfLesson;
    }

    @Override
    public List<UserDTO> listAllInstructorsOfLesson(Long lessonId) {
        Lesson lesson = lessonRepository.findById(lessonId).get();
        List<InstructorLesson> instructorLessonListByLesson = instructorLessonRepository.findAllByLesson(lesson);
        return instructorLessonListByLesson
                .stream()
                .map(obj -> mapperUtil.convert(obj.getInstructor(), new UserDTO()))
                .collect(Collectors.toList());
    }

}
