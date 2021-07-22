package com.vaccine.service;



import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface IGeneralService<T> {


    Page<T> findAll(Pageable pageable);
    

    Optional<T> findById(Long id);

    T save(T t);

    void remove(Long id);

    List<T> getUserListIsDone(String DATE);

    Integer  countMaxTimeInDay();

    String getMaxDayFromData();

    String getMaxTimeFromData();

    int  countMaxDayToNext();

    Long countCheckStatus1();

    Long amountVaccine();


    Iterable<T> sortById();
    Iterable<T> sortByIdDesc();

    Page<T> findAllPageListOne(Pageable pageable,String DATE);

    Page<T> searchUsersDoctor(Pageable pageable, String search,String date);
    Page<T> searchUserAdmin(Pageable pageable,String search);
}