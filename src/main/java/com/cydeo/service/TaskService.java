package com.cydeo.service;

import com.cydeo.dto.BatchDTO;
import com.cydeo.dto.LessonDTO;
import com.cydeo.dto.TaskDTO;

import java.util.List;

public interface TaskService {

    List<TaskDTO> getAllTasks();
    TaskDTO getTaskByTaskId(Long taskId);
    TaskDTO save(TaskDTO taskDTO);
    List<BatchDTO> getAllBatches();
    List<LessonDTO> getAllLessons();
    void delete(Long taskId);
    void publish(Long taskId);
    void complete(Long taskId);

}
