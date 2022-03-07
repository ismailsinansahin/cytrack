package com.cydeo.converter;

import com.cydeo.dto.BatchDTO;
import com.cydeo.dto.LessonDTO;
import com.cydeo.service.BatchService;
import com.cydeo.service.LessonService;
import org.springframework.boot.context.properties.ConfigurationPropertiesBinding;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
@ConfigurationPropertiesBinding
public class LessonDTOConverter implements Converter<String, LessonDTO> {

    private final LessonService lessonService;

    public LessonDTOConverter(@Lazy LessonService lessonService) {
        this.lessonService = lessonService;
    }

    @Override
    public LessonDTO convert(String s) {
        return lessonService.getLessonByLessonId(Long.parseLong(s));
    }

}
