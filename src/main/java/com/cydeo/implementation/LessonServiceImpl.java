package com.cydeo.implementation;

import com.cydeo.dto.LessonDTO;
import com.cydeo.dto.UserDTO;
import com.cydeo.entity.InstructorLesson;
import com.cydeo.entity.Lesson;
import com.cydeo.entity.User;
import com.cydeo.mapper.MapperUtil;
import com.cydeo.repository.InstructorLessonRepository;
import com.cydeo.repository.LessonRepository;
import com.cydeo.repository.UserRepository;
import com.cydeo.service.LessonService;
import com.cydeo.service.UserService;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class LessonServiceImpl implements LessonService {

    LessonRepository lessonRepository;
    MapperUtil mapperUtil;
    UserService userService;
    InstructorLessonRepository instructorLessonRepository;
    UserRepository userRepository;

    public LessonServiceImpl(LessonRepository lessonRepository, MapperUtil mapperUtil, UserService userService,
                             InstructorLessonRepository instructorLessonRepository, UserRepository userRepository) {
        this.lessonRepository = lessonRepository;
        this.mapperUtil = mapperUtil;
        this.userService = userService;
        this.instructorLessonRepository = instructorLessonRepository;
        this.userRepository = userRepository;
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
            List<UserDTO> instructorList = userService.listAllInstructorsOfLesson(lesson.getId());
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
    public LessonDTO getLessonByLessonId(Long lessonId) {
        LessonDTO lessonDTO = mapperUtil.convert(lessonRepository.findById(lessonId).get(), new LessonDTO());
        List<UserDTO> instructorsOfLesson = userService.listAllInstructorsOfLesson(lessonId);
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
    public LessonDTO save(LessonDTO lessonDTO) {
        Lesson lesson = mapperUtil.convert(lessonDTO, new Lesson());
        lessonRepository.save(lesson);
        return mapperUtil.convert(lesson, new LessonDTO());
    }

    @Override
    public void delete(Long lessonId) {
        Lesson lesson = lessonRepository.findById(lessonId).get();
        lesson.setIsDeleted(true);
        lessonRepository.save(lesson);
    }

    @Override
    public void addInstructor(Long lessonId, UserDTO instructorDTO) {
        boolean doesExist = false;
        Lesson lesson = lessonRepository.findById(lessonId).get();
        User instructor = userRepository.findById(instructorDTO.getId()).get();
        List<InstructorLesson> instructorLessonListByLesson = instructorLessonRepository.findAllByLesson(lesson);
        for(InstructorLesson instructorLesson : instructorLessonListByLesson){
            if(instructorLesson.getInstructor().getId()==instructor.getId()){
                System.out.println("Instructor is already assigned to lesson");
                doesExist = true;
            }
        }
        if(!doesExist) instructorLessonRepository.save(new InstructorLesson(instructor,lesson));
    }

    @Override
    public void removeInstructor(Long lessonId, UserDTO instructorDTO) {
        Lesson lesson = lessonRepository.findById(lessonId).get();
        User instructor = userRepository.findById(instructorDTO.getId()).get();
        List<InstructorLesson> instructorLessonListByLesson = instructorLessonRepository.findAllByLesson(lesson);
        for(InstructorLesson instructorLesson : instructorLessonListByLesson){
            if(instructorLesson.getInstructor().getId().equals(instructor.getId())){
                InstructorLesson instructorLessonToBeDeleted = instructorLessonRepository.findByLessonAndInstructor(lesson, instructor);
                instructorLessonToBeDeleted.setIsDeleted(true);
                instructorLessonRepository.save(instructorLessonToBeDeleted);
            }
        }
        System.out.println("Instructor is not assigned to the lesson");
    }

}
