package com.vaccine.service.admindestination;

import com.vaccine.model.AdminDestination;
import com.vaccine.model.User;
import com.vaccine.repository.IAdminDestinationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;


public class AdminDestinationService implements IAdminDestinationService{

    @Autowired
    IAdminDestinationRepository adminDestinationRepository;


    @Override
    public Page<AdminDestination> findAll(Pageable pageable) {
        return adminDestinationRepository.findAll(pageable);
    }

    @Override
    public Optional<AdminDestination> findById(Long id) {
        return adminDestinationRepository.findById(id);
    }

    @Override
    public AdminDestination save(AdminDestination adminDestination) {
        return adminDestinationRepository.save(adminDestination);
    }

    @Override
    public void remove(Long id) {
        adminDestinationRepository.deleteById(id);
    }

    @Override
    public List<AdminDestination> getUserListIsDone(String DATE) {
        return null;
    }

    @Override
    public Integer countMaxTimeInDay() {
        return null;
    }

    @Override
    public String getMaxDayFromData() {
        return null;
    }

    @Override
    public String getMaxTimeFromData() {
        return null;
    }

    @Override
    public int countMaxDayToNext() {
        return 0;
    }


    @Override
    public Long countCheckStatus1() {
        return adminDestinationRepository.countCheckStatus1();
    }

    @Override
    public Long amountVaccine() {
        return null;
    }


    @Override
    public Iterable<AdminDestination> sortById() {
        return null;
    }

    @Override
    public Iterable<AdminDestination> sortByIdDesc() {
        return null;
    }

    @Override
    public Page<AdminDestination> findAllPageListOne(Pageable pageable, String s) {
        return null;
    }

    @Override
    public Page<AdminDestination> searchUsersDoctor(Pageable pageable, String search, String date) {
        return null;
    }

    @Override
    public Page<AdminDestination> searchUserAdmin(Pageable pageable, String search) {
        return null;
    }
}
