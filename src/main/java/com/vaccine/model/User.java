package com.vaccine.model;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

@Entity
@Data
@NoArgsConstructor
@ToString
@AllArgsConstructor
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    String userName;

    @Column(unique = true,length = 9)
    String CMND;

    String password;

    String phoneNumber;


    String email;

    @Column(columnDefinition = "integer default '0' ")
    int age;

    String gender;

    //    Huyen
    String district;

    //    Xa~
    String commune;

    String healthyStatus;

    //    Current day;
    String createDay;

    String DateVaccine;

    String TimeVaccine;


    @Column(columnDefinition = "integer default '3' ")
    int status;

    @Column(columnDefinition = "integer default '0' ")
    int checkDoctor;

    @Column(columnDefinition = "integer default '0' ")
    int checkStatus;

    @ManyToOne
    @JoinColumn(name = "id_destination",columnDefinition = "integer default '1' ")
    private AdminDestination adminDestination;

    public String getEncrytedPassword() {
        return password;
    }
    public User(String userName,String CMND, String password){
        this.userName = userName;
        this.CMND = CMND;
        this.password = password;
    }

}
