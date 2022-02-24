package com.cydeo.service;

import com.cydeo.dto.LessonDTO;
import com.cydeo.dto.UserDTO;
import com.cydeo.enums.UserRole;

import java.util.List;
import java.util.Map;

public interface LessonService {

    List<LessonDTO> listAllLessons();
    LessonDTO getLessonByLessonId(Long lessonId);
    LessonDTO create(LessonDTO lessonDTO);
    LessonDTO save(LessonDTO lessonDTO, Long lessonId);
    void delete(Long lessonId);
    void addInstructor(Long lessonId, Long instructorId);
    void removeInstructor(Long lessonId, Long instructorId);
    List<Long> getInstructorIdsOfLesson(Long lessonId);
    Map<LessonDTO, String> getLessonsAndInstructorsMap();
    List<LessonDTO> listAllLessonsOfInstructor(Long userId);
    Map<UserDTO, String> getInstructorsAndLessonsMap();
    List<UserDTO> getAllInstructors();
    List<UserDTO> listAllInstructorsOfLesson(Long lessonId);

}
