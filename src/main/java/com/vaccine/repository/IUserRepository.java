package com.vaccine.repository;


import com.vaccine.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IUserRepository extends JpaRepository<User,Long> {
    @Query("SELECT e from User e WHERE e.checkDoctor = 1")
    List<User> getDoctor();

    @Query("SELECT e FROM User e WHERE e.status = 2 or e.status = 1 order by e.id")
    List<User> getUserListIsDone();

    @Query("SELECT e FROM User e WHERE e.CMND = ?1")
    User getUserByCMND(String CMND);

    @Query(" select count(e.TimeVaccine) from User e where (e.status = 1 or  e.status = 2) and  e.TimeVaccine = (select max(TimeVaccine) from e)  and e.DateVaccine =  (select max(DateVaccine) from e)")
    Integer  countMaxTimeInDay();

    @Query(" select count(e.DateVaccine) from User e where (e.status = 1 or  e.status = 2) and  e.DateVaccine = (select max(DateVaccine) from e)")
    int  countMaxDayToNext();

    @Query("select max(e.DateVaccine) from User e where (e.status = 1 or  e.status = 2) ")
    String getMaxDayFromData();

    @Query("select max(e.TimeVaccine) from User e where (e.status = 1 or  e.status = 2) and  e.DateVaccine = (select max(DateVaccine) from e)")
    String getMaxTimeFromData();

    @Query("select e from User  e where (e.status=1 or e.status=2) and e.DateVaccine=?1")
    List<User> getUserOneDay(String date);

    @Query("select u from User u order by u.id")
    Iterable<User> sortById();

    @Query("select  u from User u order by u.id desc ")
    Iterable<User> sortByIdDesc();

    @Query("select e from User  e where (e.status=1 or e.status=2) and e.DateVaccine=?1")
    Page<User> getUserOneDayPage(String date,Pageable pageable);

    @Query("select u from User u where u.CMND LIKE %?1%")
    Page<User> searchUserAdmin(String search, Pageable pageable);
    @Query("select u from User u where u.CMND LIKE %?1% and (u.status=1 or u.status=2) and u.DateVaccine=?2")
    Page<User> searchUserDoctor(String search,String date, Pageable pageable);
}
