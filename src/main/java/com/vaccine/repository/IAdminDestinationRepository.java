package com.vaccine.repository;

import com.vaccine.model.AdminDestination;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface IAdminDestinationRepository extends JpaRepository<AdminDestination, Long> {
    @Query("SELECT count (u) from User u where u.checkStatus=1")
    Long countCheckStatus1();

    @Query("select u from AdminDestination u where  u.deleteStatus = 0")
    List<AdminDestination> getListDestination();
}
