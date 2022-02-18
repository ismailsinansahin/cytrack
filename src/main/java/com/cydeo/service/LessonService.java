package com.cydeo.service;

import com.cydeo.dto.BatchDTO;
import com.cydeo.dto.InstructorLessonDTO;
import com.cydeo.dto.LessonDTO;
import com.cydeo.dto.UserDTO;
import com.cydeo.entity.Lesson;

import java.util.List;
import java.util.Map;

public interface LessonService {

    List<LessonDTO> listAllLessons();
    LessonDTO getLessonByLessonId(Long lessonId);
    LessonDTO save(LessonDTO lessonDTO);
    void delete(Long lessonId);
    void addInstructor(Long lessonId, UserDTO instructor);
    void removeInstructor(Long lessonId, UserDTO instructor);
    Map<LessonDTO, String> getLessonsAndInstructorsMap();

}
