package com.cydeo.repository;

import com.cydeo.entity.Batch;
import com.cydeo.entity.Lesson;
import com.cydeo.entity.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {

    List<Task> findAllByBatch(Batch batch);
    List<Task> findAllByLesson(Lesson lesson);

}
