package com.vaccine.controller;
import com.vaccine.model.AmountVaccineRegisterInDay;
import com.vaccine.model.DayTimeStart;
import com.vaccine.model.User;
import com.vaccine.model.WarehouseVaccine;
import com.vaccine.repository.IDayTimeStart;
import com.vaccine.repository.IDayVaccineRegisterRepository;
import com.vaccine.repository.IUserRepository;
import com.vaccine.service.admindestination.IAdminDestinationService;
import com.vaccine.service.user.IUserService;
import com.vaccine.service.warehouseVaccine.IWarehouseVaccineService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
@RestController
@RequestMapping(value = "/doctor")
public class DoctorController {
    int count=0;
    int countSort=0;
    static int countCheckBefore =0;
    static int countCheckAfter =0;
    static int checkSetAll = 0;
    static int checkAmountRegister=0;
    List<User> listTest = new ArrayList<>();
    String[] str = LocalDate.now().toString().split("-");
    String date = str[2]+"-"+str[1]+"-"+str[0]+" ";
    @Autowired
    IUserService userService;
    @Autowired
    IUserRepository iUserRepository;
    @Autowired
    IAdminDestinationService adminDestinationService;
    @Autowired
    IWarehouseVaccineService warehouseVaccineService;
    @Autowired
    IDayVaccineRegisterRepository dayVaccineService;
    @Autowired
    IDayTimeStart dayTimeStart;
    @GetMapping("/list")
    public ResponseEntity<Page<User>> allUsers(@PageableDefault(value = 15) Pageable pageable){
        return new ResponseEntity<>(userService.findAllPageListOne(pageable,date), HttpStatus.OK);
    }
    @GetMapping("/list/{date}")
    public ResponseEntity<Page<User>> allUsersDate(@PageableDefault(value = 15) Pageable pageable, @PathVariable String date){
        date+=" ";
        return new ResponseEntity<>(userService.findAllPageListOne(pageable,date),HttpStatus.OK);
    }
    @GetMapping("/api/{date}/{search}")
    public ResponseEntity<Page<User>> searchUsers(@PageableDefault(value = 15) Pageable pageable,@PathVariable String date, @PathVariable String search){
        date+=" ";
        return new ResponseEntity<>(userService.searchUsersDoctor(pageable,search,date), HttpStatus.OK);
    }
    @GetMapping("/apiRegister")
    public ResponseEntity<WarehouseVaccine> getAmount(){
        return new ResponseEntity<>(warehouseVaccineService.findById(1L).get(),HttpStatus.OK);
    }
    @GetMapping("/apiDateTime")
    public ResponseEntity<DayTimeStart> getDayTime(){
        return new ResponseEntity<>(dayTimeStart.findById(1L).get(),HttpStatus.OK);
    }
    @GetMapping("/fullApiUser")
    public ResponseEntity<Iterable<User>> getFullUser(){
        return new ResponseEntity<>(iUserRepository.findAll(),HttpStatus.OK);
    }
    @GetMapping("/{date}")
    public ModelAndView showFormDate(@PageableDefault(value = 15) Pageable pageable, @RequestParam("page") Optional<Integer> page,@PathVariable String date) {
        date +=" ";
        String[] str = date.trim().split("-");
        String dateAfter = str[2]+"-"+str[1]+"-"+str[0];
        int currentPage = page.orElse(0);
        ModelAndView modelAndView = new ModelAndView("/admin/ListUserIsDone");
        Page<User> userList = userService.findAllPageListOne(pageable,date);
        modelAndView.addObject("userList", userList);
        List<Integer> list = new ArrayList<>();
        for(int i=0;i<userList.getTotalPages();i++){
            list.add(i);
        }
        if(dateAfter.compareTo(LocalDate.now().toString())!=0) {
            modelAndView.addObject("check", 1);
        }
        modelAndView.addObject("list",list);
        modelAndView.addObject("pageActive",currentPage);
        modelAndView.addObject("dateUrl",date);
        return modelAndView;
    }
    @GetMapping
    public ModelAndView showForm(@PageableDefault(value = 15) Pageable pageable, @RequestParam("page") Optional<Integer> page) {
        if(checkSetAll==0){
            listTest = iUserRepository.getUserOneDay(date);
        }
        for(int i=0;i<listTest.size();i++){
            userService.save(listTest.get(i));
        }
        count=0;
        countCheckBefore=0;
        countCheckAfter=0;
        int currentPage = page.orElse(0);
        ModelAndView modelAndView = new ModelAndView("/admin/ListUserIsDone");
        Page<User> userList = userService.findAllPageListOne(pageable,date);
        modelAndView.addObject("userList", listTest);
        List<Integer> list = new ArrayList<>();
        for(int i=0;i<userList.getTotalPages();i++){
            list.add(i);
        }
        for(User item:userList){
            if(item.getCheckStatus()==0){
                countCheckBefore++;
            }
        }
        int hours = LocalDateTime.now().getHour();
        // set hours check
        if(hours<18){
            modelAndView.addObject("checkTime",0);
        }
        else{
            modelAndView.addObject("checkTime",1);
        }
        modelAndView.addObject("check",0);
        modelAndView.addObject("list",list);
        modelAndView.addObject("pageActive",currentPage);
        return modelAndView;
    }
    @GetMapping("/check/{id}")
    public void setCheck(@PathVariable Long id,@PageableDefault(value = 15) Pageable pageable){
        User user = userService.findById(id).get();
        user.setCheckStatus(1);
        userService.save(user);
        checkSetAll++;
        count++;
    }
    @GetMapping("/unchecked/{id}")
    public void setUnchecked(@PathVariable Long id,@PageableDefault(value = 15) Pageable pageable){
        User user = userService.findById(id).get();
        user.setCheckStatus(0);
        userService.save(user);
        checkSetAll++;
        count--;
    }
    @GetMapping("/setAll")
    public ModelAndView setAll(@PageableDefault(value = 15) Pageable pageable,@RequestParam("page") Optional<Integer> page){
        AmountVaccineRegisterInDay amountVaccineRegisterInDay = dayVaccineService.findByDate(date).get();
        Page<User> list = userService.findAllPageListOne(pageable,date);
        listTest = iUserRepository.getUserOneDay(date);
        for(User item:list){
            if(item.getCheckStatus()==0){
                countCheckAfter++;
            }
        }
        WarehouseVaccine warehouseVaccine = warehouseVaccineService.findById(1L).get();
        if(amountVaccineRegisterInDay.getCountSubmit()==0){
            warehouseVaccine.setAmountRegister(warehouseVaccine.getAmountRegister()-countCheckBefore+countCheckAfter+list.getTotalElements());
        }
        else{
            if(countCheckAfter!=countCheckBefore){
                warehouseVaccine.setAmountRegister(warehouseVaccine.getAmountRegister()-countCheckBefore+countCheckAfter);
            }
        }
        amountVaccineRegisterInDay.setCountSubmit(amountVaccineRegisterInDay.getCountSubmit()+1);
        dayVaccineService.save(amountVaccineRegisterInDay);
        warehouseVaccine.setAmountVaccine(warehouseVaccine.getAmountVaccine()-count);
        warehouseVaccineService.save(warehouseVaccine);
        countCheckBefore=0;
        countCheckAfter=0;
        count=0;
        checkSetAll=0;
        return showForm(pageable,page);
    }
    @GetMapping("/sort")
    public ModelAndView sortByName(){
        ModelAndView modelAndView = new ModelAndView("/admin/ListUserIsDone");
        Iterable<User> userList;
        if(countSort%2==0){
            userList = userService.sortById();
        }
        else{
            userList = userService.sortByIdDesc();
        }
        modelAndView.addObject("userList",userList);
        countSort++;
        return modelAndView;
    }
}