package com.vaccine.repository;

import com.vaccine.model.WarehouseVaccine;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface IWarehouseRepository extends JpaRepository<WarehouseVaccine,Long> {
    @Query("SELECT w.amountVaccine from WarehouseVaccine w ")
    Long amountVaccine();
}
