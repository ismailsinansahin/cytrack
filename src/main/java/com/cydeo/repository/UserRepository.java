package com.cydeo.repository;

import com.cydeo.entity.Batch;
import com.cydeo.entity.Group;
import com.cydeo.entity.User;
import com.cydeo.entity.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository <User,Long>{

    List<User> findAllByUserRole(UserRole userRole);
    List<User> findAllByUserRoleNot(UserRole userRole);

    List<User> findAllByCurrentBatch(Batch batch);

//    List<User> findAllByGroup(Group group);
//    List<User> findAllByBatch(Batch batch);
    User findByUserName(String userName);

}
