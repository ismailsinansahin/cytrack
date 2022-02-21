package com.cydeo.service;

import com.cydeo.dto.LessonDTO;
import com.cydeo.dto.UserDTO;

import java.util.List;
import java.util.Map;

public interface LessonService {

    List<LessonDTO> listAllLessons();
    LessonDTO getLessonByLessonId(Long lessonId);
    LessonDTO save(LessonDTO lessonDTO);
    void delete(Long lessonId);
    LessonDTO getLessonByLessonIdWithTempInstructorList(Long lessonId);
    void addInstructor(Long lessonId, UserDTO instructor);
    void removeInstructor(Long lessonId, UserDTO instructor);
    void updateInstructorList(Long lessonId);
    LessonDTO cancelEditingInstructorList(Long lessonId);
    Map<LessonDTO, String> getLessonsAndInstructorsMap();

}
