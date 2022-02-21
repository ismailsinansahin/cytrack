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

import java.util.ArrayList;
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

    List<UserDTO> tempInstructorsOfLesson = new ArrayList<>();
    boolean isTempUsedBefore = false;

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
    public LessonDTO getLessonByLessonIdWithTempInstructorList(Long lessonId) {
        LessonDTO lessonDTO = mapperUtil.convert(lessonRepository.findById(lessonId).get(), new LessonDTO());
        tempInstructorsOfLesson = getTempInstructorListFromDB(lessonId);
        isTempUsedBefore = true;
        String instructors = "";
        for (UserDTO instructor : tempInstructorsOfLesson) {
            String fullName = instructor.getFirstName() + " " + instructor.getLastName();
            instructors += fullName + " / ";
        }
        instructors = (instructors.equals("")) ? "-" : instructors.substring(0,instructors.length()-2);
        lessonDTO.setInstructorList(instructors);
        return lessonDTO;
    }

    @Override
    public void addInstructor(Long lessonId, UserDTO instructorDTO) {
        getTempInstructorListFromDB(lessonId);
        List<Long> instructorListOfLessonInTemp = tempInstructorsOfLesson
                .stream()
                .map(UserDTO::getId)
                .collect(Collectors.toList());
        if(!instructorListOfLessonInTemp.contains(instructorDTO.getId())){
            instructorDTO = mapperUtil.convert(userRepository.findById(instructorDTO.getId()).get(), instructorDTO);
            tempInstructorsOfLesson.add(instructorDTO);
        }
    }

    @Override
    public void removeInstructor(Long lessonId, UserDTO instructorDTO) {
        Long instructorDTOId = instructorDTO.getId();
        getTempInstructorListFromDB(lessonId);
        List<Long> instructorListOfLessonInTemp = tempInstructorsOfLesson
                .stream()
                .map(UserDTO::getId)
                .collect(Collectors.toList());
        if(instructorListOfLessonInTemp.contains(instructorDTO.getId())){
            UserDTO instructorToBeRemoved = tempInstructorsOfLesson
                    .stream()
                    .filter(obj -> obj.getId()==instructorDTOId)
                    .findAny().get();
            tempInstructorsOfLesson.remove(instructorToBeRemoved);
        }
    }

    @Override
    public void updateInstructorList(Long lessonId) {
        Lesson lesson = lessonRepository.findById(lessonId).get();
        List<Long> instructorListOfLessonInDB = userService.listAllInstructorsOfLesson(lessonId)
                .stream()
                .map(UserDTO::getId)
                .collect(Collectors.toList());
        List<Long> instructorListOfLessonInTemp = tempInstructorsOfLesson
                .stream()
                .map(UserDTO::getId)
                .collect(Collectors.toList());
        for(Long instructorInTemp : instructorListOfLessonInTemp){
            if(!(instructorListOfLessonInDB.contains(instructorInTemp))){
                User instructor = mapperUtil.convert(userService.getUserById(instructorInTemp), new User());
                instructorLessonRepository.save(new InstructorLesson(instructor, lesson));
            }
        }
        for(Long instructorInDB : instructorListOfLessonInDB){
            if(!(instructorListOfLessonInTemp.contains(instructorInDB))){
                User instructorToBeRemoved = mapperUtil.convert(userService.getUserById(instructorInDB), new User());
                InstructorLesson instructorLesson = instructorLessonRepository.findByLessonAndInstructor(lesson, instructorToBeRemoved);
                instructorLesson.setIsDeleted(true);
                instructorLessonRepository.save(instructorLesson);
            }
        }
        tempInstructorsOfLesson.clear();
        isTempUsedBefore = false;
    }

    @Override
    public LessonDTO cancelEditingInstructorList(Long lessonId) {
        LessonDTO lessonDTO = mapperUtil.convert(lessonRepository.findById(lessonId).get(), new LessonDTO());
        tempInstructorsOfLesson = instructorLessonRepository.findAll()
                .stream()
                .filter(obj -> obj.getLesson().getId()==lessonId)
                .map(InstructorLesson::getInstructor)
                .map(obj -> mapperUtil.convert(obj, new UserDTO()))
                .collect(Collectors.toList());
        String instructors = "";
        for (UserDTO instructor : tempInstructorsOfLesson) {
            String fullName = instructor.getFirstName() + " " + instructor.getLastName();
            instructors += fullName + " / ";
        }
        instructors = (instructors.equals("")) ? "-" : instructors.substring(0,instructors.length()-2);
        lessonDTO.setInstructorList(instructors);
        tempInstructorsOfLesson = getTempInstructorListFromDB(lessonId);
        return lessonDTO;
    }

    private List<UserDTO> getTempInstructorListFromDB(Long lessonId){
        if(!isTempUsedBefore){
            tempInstructorsOfLesson = instructorLessonRepository.findAll()
                    .stream()
                    .filter(obj -> obj.getLesson().getId()==lessonId)
                    .map(InstructorLesson::getInstructor)
                    .map(obj -> mapperUtil.convert(obj, new UserDTO()))
                    .collect(Collectors.toList());
        }
        return tempInstructorsOfLesson;
    }

}
