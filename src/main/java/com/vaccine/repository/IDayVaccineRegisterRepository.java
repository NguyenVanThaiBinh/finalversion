package com.vaccine.repository;
import com.vaccine.model.AmountVaccineRegisterInDay;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.Optional;
@Repository
public interface IDayVaccineRegisterRepository extends JpaRepository<AmountVaccineRegisterInDay,Long> {
    @Query("SELECT e FROM AmountVaccineRegisterInDay e WHERE e.date=?1")
    Optional<AmountVaccineRegisterInDay> findByDate(String date);
}