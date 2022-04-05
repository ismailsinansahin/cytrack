package com.cydeo.service;

import com.cydeo.dto.BatchDTO;
import com.cydeo.dto.LessonDTO;
import com.cydeo.dto.TaskDTO;

import java.util.List;

public interface TaskService {

    List<TaskDTO> getAllTasksOfBatch(Long batchId);
    TaskDTO getTaskById(Long taskId);
    BatchDTO getBatchById(Long batchId);
    List<BatchDTO> getAllBatches();
    List<LessonDTO> getAllLessons();
    TaskDTO create(TaskDTO taskDTO, Long batchId);
    TaskDTO save(TaskDTO taskDTO, Long taskId, Long batchId);
    void delete(Long taskId);
    void publish(Long taskId);
    void unpublish(Long taskId);
    void complete(Long taskId);

}
