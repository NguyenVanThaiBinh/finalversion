package com.vaccine.service.user;



import com.vaccine.model.User;
import com.vaccine.repository.IUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService implements IUserService {
    @Autowired
    private IUserRepository userRepository;


    @Override
    public Page<User> findAll(Pageable pageable) {
        return userRepository.findAll(pageable);
    }

    @Override
    public Optional<User> findById(Long id) {
        return userRepository.findById(id);
    }

    @Override
    public User save(User user) {
        return userRepository.save(user);
    }

    @Override
    public void remove(Long id) {
        userRepository.deleteById(id);
    }

    @Override
    public List<User> getUserListIsDone(String DATE) {
        return userRepository.getUserListIsDone();
    }

    @Override
    public Integer countMaxTimeInDay() {
        return userRepository.countMaxTimeInDay();
    }

    @Override
    public String getMaxDayFromData() {
        return userRepository.getMaxDayFromData();
    }

    @Override
    public String getMaxTimeFromData() {
        return userRepository.getMaxTimeFromData();
    }

    @Override
    public int countMaxDayToNext() {
        return userRepository.countMaxDayToNext();
    }


    @Override
    public Long countCheckStatus1() {
        return null;
    }

    @Override
    public Long amountVaccine() {
        return null;
    }


    @Override
    public Iterable<User> sortById() {
        return userRepository.sortById();
    }
    @Override
    public Iterable<User> sortByIdDesc() {
        return userRepository.sortByIdDesc();

    }

    @Override
    public Page<User> findAllPageListOne(Pageable pageable, String DATE) {
        return userRepository.getUserOneDayPage(DATE,pageable);
    }

    @Override
    public Page<User> searchUsersDoctor(Pageable pageable, String search,String date) {
        return userRepository.searchUserDoctor(search,date,pageable);
    }

    @Override
    public Page<User> searchUserAdmin(Pageable pageable, String search) {
        return userRepository.searchUserAdmin(search,pageable);
    }
}
