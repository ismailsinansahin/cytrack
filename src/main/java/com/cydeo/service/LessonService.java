package com.cydeo.service;

import com.cydeo.dto.LessonDTO;
import com.cydeo.entity.Lesson;

import java.util.List;
import java.util.Map;

public interface LessonService {

    List<LessonDTO> listAllLessonsOfInstructor(String username);


}
