package com.vaccine.service.warehouseVaccine;

import com.vaccine.model.User;
import com.vaccine.model.WarehouseVaccine;
import com.vaccine.repository.IWarehouseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public class WarehouseVaccineService implements IWarehouseVaccineService{
    @Autowired
    private IWarehouseRepository warehouseRepository;



    @Override
    public Page<WarehouseVaccine> findAll(Pageable pageable) {
        return warehouseRepository.findAll(pageable);
    }

    @Override
    public Optional<WarehouseVaccine> findById(Long id) {
        return warehouseRepository.findById(id);
    }

    @Override
    public WarehouseVaccine save(WarehouseVaccine warehouseVaccine) {
        return warehouseRepository.save(warehouseVaccine);
    }

    @Override
    public void remove(Long id) {
        warehouseRepository.deleteById(id);
    }

    @Override
    public List<WarehouseVaccine> getUserListIsDone(String DATE) {
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
        return null;
    }

    @Override
    public Long amountVaccine() {
        return warehouseRepository.amountVaccine();
    }


    @Override
    public Iterable<WarehouseVaccine> sortById() {
        return null;
    }

    @Override
    public Iterable<WarehouseVaccine> sortByIdDesc() {
        return null;
    }

    @Override
    public Page<WarehouseVaccine> findAllPageListOne(Pageable pageable, String s) {
        return null;
    }

    @Override
    public Page<WarehouseVaccine> searchUsersDoctor(Pageable pageable, String search, String date) {
        return null;
    }

    @Override
    public Page<WarehouseVaccine> searchUserAdmin(Pageable pageable, String search) {
        return null;
    }
}