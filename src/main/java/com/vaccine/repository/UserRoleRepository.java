package com.vaccine.repository;



import com.vaccine.model.User;
import com.vaccine.model.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserRoleRepository extends JpaRepository<UserRole,Long> {

    List<UserRole> findByAppUser(User appUser);

}
