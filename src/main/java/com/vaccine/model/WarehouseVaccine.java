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
@Table(name = "warehouse_vaccine")
public class WarehouseVaccine {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    Long amountVaccine;
    String WarehouseName;
    String warehouseAddress;
    Long amountRegister;
}
