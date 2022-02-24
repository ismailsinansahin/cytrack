package com.cydeo.repository;

import com.cydeo.entity.Batch;
import com.cydeo.entity.User;
import com.cydeo.enums.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository <User,Long>{

    List<User> findAllByUserRole(UserRole userRole);
    List<User> findAllByUserRoleNot(UserRole userRole);
    List<User> findAllByBatch(Batch batch);

}
