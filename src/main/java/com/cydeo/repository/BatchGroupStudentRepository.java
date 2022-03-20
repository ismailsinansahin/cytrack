package com.cydeo.repository;

import com.cydeo.entity.Batch;
import com.cydeo.entity.BatchGroupStudent;
import com.cydeo.entity.Group;
import com.cydeo.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BatchGroupStudentRepository extends JpaRepository<BatchGroupStudent, Long> {

    List<BatchGroupStudent> findAllByBatch(Batch batch);
    List<BatchGroupStudent> findAllByGroup(Group group);
    List<BatchGroupStudent> findAllByStudent(User student);
    List<BatchGroupStudent> findAllByGroupAndStudent(Group group, User student);
    BatchGroupStudent findByBatchAndStudent(Batch batch, User student);

}
