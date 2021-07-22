package com.vaccine.model;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import javax.persistence.*;
@Entity
@Data
@NoArgsConstructor
@ToString
@AllArgsConstructor
@Table(name = "day_vaccine_register")
public class AmountVaccineRegisterInDay {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    String date;
    Long amount;
    Long countSubmit;
    public AmountVaccineRegisterInDay(String date, Long amount, Long countSubmit,WarehouseVaccine warehouseVaccine){
        this.amount = amount;
        this.countSubmit = countSubmit;
        this.date = date;
        this.warehouseVaccine = warehouseVaccine;
    }
    @ManyToOne
    @JoinColumn(name = "id_warehourse")
    WarehouseVaccine warehouseVaccine;
}