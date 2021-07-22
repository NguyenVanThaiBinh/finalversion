package com.vaccine.repository;


import com.vaccine.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface AppUserRepository extends JpaRepository<User, Long> {

    @Query("SELECT e FROM User e WHERE e.CMND = ?1")
    User findByUserName(String name);


}
