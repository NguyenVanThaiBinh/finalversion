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
@Table(name="day_time_start")
public class DayTimeStart {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    String day_start;

    int people_per_hour;
}
