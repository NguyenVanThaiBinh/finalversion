package com.vaccine.model;

import javax.persistence.*;

@Entity
@Table(name = "User_Role", //
        uniqueConstraints = { //
                @UniqueConstraint(name = "USER_ROLE_UK", columnNames = { "User_Id", "Role_Id" }) })
public class UserRole {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Id", nullable = false)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "User_Id")
    private User appUser;

    @ManyToOne
    @JoinColumn(name = "Role_Id")
    private AppRole appRole;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getAppUser() {
        return appUser;
    }

    public void setAppUser(User appUser) {
        this.appUser = appUser;
    }

    public AppRole getAppRole() {
        return appRole;
    }

    public UserRole(User appUser, AppRole appRole) {
        this.appUser = appUser;
        this.appRole = appRole;
    }

    public UserRole() {
    }

    public void setAppRole(AppRole appRole) {
        this.appRole = appRole;
    }

}
