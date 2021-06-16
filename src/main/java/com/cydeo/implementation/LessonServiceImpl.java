package com.cydeo.implementation;

import com.cydeo.dto.LessonDTO;
import com.cydeo.entity.Lesson;
import com.cydeo.entity.User;
import com.cydeo.mapper.MapperUtil;
import com.cydeo.repository.LessonRepository;
import com.cydeo.repository.UserRepository;
import com.cydeo.service.LessonService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class LessonServiceImpl implements LessonService {

    LessonRepository lessonRepository;
    MapperUtil mapperUtil;
    UserRepository userRepository;

    public LessonServiceImpl(LessonRepository lessonRepository, MapperUtil mapperUtil, UserRepository userRepository) {
        this.lessonRepository = lessonRepository;
        this.mapperUtil = mapperUtil;
        this.userRepository = userRepository;
    }

    @Override
    public List<LessonDTO> listAllLessonsOfInstructor(String username) {
        User user = userRepository.findByEmail(username);
        List<Lesson> lessonList = lessonRepository.findAllByInstructorSet(user);
        return lessonList.stream().map(obj -> mapperUtil.convert(obj, new LessonDTO())).collect(Collectors.toList());
    }

}
