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
@Table(name="vaccine_destinations")
public class AdminDestination {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    String nameDestination;

    @Column(name = "delete_status",columnDefinition = "integer default '0' ")
    int deleteStatus;

    public AdminDestination(String nameDestination, WarehouseVaccine warehouseVaccine) {
        this.nameDestination = nameDestination;
        this.warehouseVaccine = warehouseVaccine;
    }

    @ManyToOne
    @JoinColumn(name = "id_warehouse_vaccine")
    private WarehouseVaccine warehouseVaccine;
}
