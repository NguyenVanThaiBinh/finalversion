package com.vaccine.controller;


import com.vaccine.model.*;
import com.vaccine.repository.*;
import com.vaccine.service.admindestination.IAdminDestinationService;
import com.vaccine.service.user.IUserService;
import com.vaccine.service.warehouseVaccine.IWarehouseVaccineService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;


import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(value = "/admin")
public class AdminController {

    @Autowired
    IUserService userService;

    @Autowired
    UserRoleRepository userRoleRepository;

    @Autowired
    IUserRepository userRepository;

    @Autowired
    IWarehouseVaccineService warehouseVaccineService;

    @Autowired
    IWarehouseRepository iWarehouseRepository;

    @Autowired
    IAdminDestinationRepository adminDestinationRepository;

    @Autowired
    IAdminDestinationService adminDestinationService;

    @Autowired
    IDayTimeStart iDayTimeStart;

    int countSort=0;

    @ModelAttribute("warehouses")
    public Iterable<WarehouseVaccine> warehouseVaccineResponseEntity() {
        return iWarehouseRepository.findAll();
    }

    @GetMapping
    public ModelAndView showdb(){
        ModelAndView modelAndView = new ModelAndView("/victory/dashBoar");
        return modelAndView;
    }
//  ajax user
    @GetMapping("/api-doctor")
    public ResponseEntity<Page<User>> allUser(@PageableDefault(value = 10) Pageable pageable){
        return new ResponseEntity<>(userService.findAll(pageable), HttpStatus.OK);
    }
    @GetMapping("/apiID/{id}")
    public ResponseEntity<User> getEntity(@PathVariable Long id){
        return new ResponseEntity<>(userService.findById(id).get(),HttpStatus.OK);
    }
    //ajax doctor
    @GetMapping("/api")
    public ResponseEntity<Iterable<User>> allUsers(){
        return new ResponseEntity<>(userService.getUserListIsDone("01-08-2021 "), HttpStatus.OK);
    }

    @GetMapping("/api/{search}")
    public ResponseEntity<Page<User>> searchUsers(@PageableDefault(value = 10) Pageable pageable, @PathVariable String search){
        return new ResponseEntity<>(userService.searchUserAdmin(pageable,search), HttpStatus.OK);
    }


    @PutMapping("/edit/{id}")
    public ResponseEntity<User> editEntity(@RequestBody User user,@PathVariable Long id){
        User user1 = userService.findById(id).get();
        user1.setCMND(user.getCMND());
        user1.setUserName(user.getUserName());
        user1.setEmail(user.getEmail());
        user1.setPhoneNumber(user.getPhoneNumber());
        user1.setId(id);
        return new ResponseEntity<>(userService.save(user1),HttpStatus.OK);
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<User> deleteUsers(@PathVariable("id")Long id){
        Optional<User> optional = userService.findById(id);
        userService.remove(id);
        return new ResponseEntity<>(optional.get(),HttpStatus.NO_CONTENT);
    }



    @GetMapping("/user")
    public ModelAndView listUsers(@PageableDefault(value = 10) Pageable pageable,@RequestParam("page") Optional<Integer> page){
        int currentPage = page.orElse(0);
        Page<User> userList = userService.findAll(pageable);
        ModelAndView modelAndView = new ModelAndView("/victory/user");
        List<Integer> list = new ArrayList<>();
//        System.out.println(userList.getNumberOfElements());
        for(int i=0;i<userList.getTotalPages();i++){
            list.add(i);
        }
        modelAndView.addObject("user",userList);
        modelAndView.addObject("list",list);
        modelAndView.addObject("pageActive",currentPage);
        return modelAndView;
    }

    @GetMapping("/edit-user/{id}")
    public ModelAndView showEdit(@PathVariable("id") Long id){
        User user = userService.findById(id).get();
        ModelAndView modelAndView = new ModelAndView("/editForm");
        modelAndView.addObject("user",user);
        return modelAndView;
    }
    @PostMapping("/edit-user")
    public ModelAndView editUser(@ModelAttribute("user") User user){
        userService.save(user);
        ModelAndView modelAndView = new ModelAndView("/editForm");
        modelAndView.addObject("user",new User());
        return modelAndView;
    }

    //    ---------------------------------Thời gian tiêm chủng------------------------------------------>

    @GetMapping("/timevaccine")
    public ModelAndView listTimeVaccine(){
        ModelAndView modelAndView = new ModelAndView("/victory/injectiontime");
        if(iDayTimeStart.findById(2L).isPresent()){
            modelAndView.addObject("error","Điểm tiêm đã được ấn định");
        }



        return modelAndView;
    }

    //    ---------------------------------Điểm tiêm chủng------------------------------------------>
    @GetMapping("/destination")
    public ModelAndView listDestination() {
        List<AdminDestination> adminDestinations = adminDestinationRepository.getListDestination();
        ModelAndView modelAndView = new ModelAndView("/victory/injectionPoint");
        modelAndView.addObject("destination", adminDestinations);
        return modelAndView;
    }
    @PostMapping("/create-DS")
    public ResponseEntity<AdminDestination> createDestination(@RequestBody AdminDestination adminDestination) {
        return new ResponseEntity<>(adminDestinationService.save(adminDestination), HttpStatus.CREATED);
    }
//    @DeleteMapping("/deleteDestination/{id}")
//    public ResponseEntity<AdminDestination> destinationResponseEntity(@PathVariable long id) {
//        Optional<AdminDestination> adminDestination = adminDestinationService.findById(id);
//        adminDestination.get().setDeleteStatus(1);
//        return new ResponseEntity<>(adminDestination.get(), HttpStatus.NO_CONTENT);
//    }
//    @GetMapping("/apiDs/{id}")
//    public ResponseEntity<AdminDestination> adminDestinationResponseEntity(@PathVariable Long id) {
//        return new ResponseEntity<>(adminDestinationService.findById(id).get(), HttpStatus.OK);
//    }
//    @PutMapping("/editDs/{id}")
//    public ResponseEntity<AdminDestination> editEntityDS(@RequestBody AdminDestination adminDestination, @PathVariable Long id) {
//        adminDestination.setId(id);
//        return new ResponseEntity<>(adminDestinationService.save(adminDestination), HttpStatus.OK);
//    }


    //    ---------------------------------Nhà kho------------------------------------------>

    @GetMapping("/all-W")
    public ResponseEntity<Iterable<WarehouseVaccine>> allwareHouse(){
    return new ResponseEntity<>(iWarehouseRepository.findAll(),HttpStatus.OK);
}
    @GetMapping("/warehouse")
    public ModelAndView listWareH(){
        List<WarehouseVaccine> warehouseVaccineList = iWarehouseRepository.findAll();
        ModelAndView modelAndView = new ModelAndView("/victory/warehousevaccine");
        modelAndView.addObject("warehousevaccine",warehouseVaccineList);
        return modelAndView;
    }
    @PostMapping("/create-W")
    public ResponseEntity<WarehouseVaccine> createCustomer(@RequestBody WarehouseVaccine warehouseVaccine) {
        warehouseVaccine.setAmountRegister(warehouseVaccine.getAmountVaccine());
        return new ResponseEntity<>(warehouseVaccineService.save(warehouseVaccine), HttpStatus.CREATED);
    }
    @GetMapping("/apiIdW/{id}")
    public ResponseEntity<WarehouseVaccine> getEntityById(@PathVariable Long id){
        return new ResponseEntity<>(warehouseVaccineService.findById(id).get(),HttpStatus.OK);
    }
    @PutMapping("/editW/{id}")
    public ResponseEntity<WarehouseVaccine> editEntityW(@RequestBody WarehouseVaccine warehouseVaccine,@PathVariable Long id){
        WarehouseVaccine  warehouseVaccine1 = warehouseVaccineService.findById(id).get();
        warehouseVaccine1.setId(id);
        warehouseVaccine1.setWarehouseName(warehouseVaccine.getWarehouseName());
        warehouseVaccine1.setWarehouseAddress(warehouseVaccine.getWarehouseAddress());
        warehouseVaccine1.setAmountVaccine(warehouseVaccine.getAmountVaccine());
        return new ResponseEntity<>(warehouseVaccineService.save(warehouseVaccine1),HttpStatus.OK);
    }
    @DeleteMapping("/delete-warehouse/{id}")
    public ResponseEntity<WarehouseVaccine> deleteWarehouse(@PathVariable long id) {
        Optional<WarehouseVaccine> customerOptional = warehouseVaccineService.findById(id);
        warehouseVaccineService.remove(id);
        return new ResponseEntity<>(customerOptional.get(), HttpStatus.NO_CONTENT);
    }
    //    ---------------------------------Bác sĩ------------------------------------------>
    @GetMapping("/all-H")
    public ResponseEntity<Iterable<User>> listHosp() {
        return new ResponseEntity<>(userRepository.getDoctor(), HttpStatus.OK);
    }
    @GetMapping("/hospital")
    public ModelAndView showHosp() {
        List<User> userList = userRepository.getDoctor();
        ModelAndView modelAndView = new ModelAndView("/victory/hospital");
        modelAndView.addObject("doctor", userList);
        return modelAndView;
    }
    @PostMapping("/createDoctor")
    public ResponseEntity<User> createDoctor(@RequestBody User user) {

        user.setCheckDoctor(1);

//        Ma` hoa'
        String password = user.getPassword();
        String encrytedPassword = encrytePassword(password);
        user.setPassword(encrytedPassword);
        try {
            userService.save(user);
        } catch (Exception e) {
            return new ResponseEntity<>(userService.save(user), HttpStatus.EXPECTATION_FAILED);
        }
//        Them quyen`
        //        Thêm quyền
        UserRole userRole = new UserRole();
        userRole.setAppUser(user);
        AppRole appRole = new AppRole();
        appRole.setRoleId(3L);
        userRole.setAppRole(appRole);
        userRoleRepository.save(userRole);
        return new ResponseEntity<>(userService.save(user), HttpStatus.CREATED);
    }
    @DeleteMapping("/deleteDoctor/{id}")
    public ModelAndView delete(@PathVariable Long id) {
        userService.remove(id);
        ModelAndView modelAndView = new ModelAndView("/victory/hospital");
        return modelAndView;
    }
    @PutMapping("/edit-D/{id}")
    public ResponseEntity<User> userResponseEntity(@RequestBody User user, @PathVariable Long id) {
        User user1 = userService.findById(id).get();
        user1.setId(id);
        user1.setUserName(user.getUserName());
        user1.setCMND(user.getCMND());
        return new ResponseEntity<>(userService.save(user1), HttpStatus.OK);
    }
    // Ma~ hoa mat khau~
    public static String encrytePassword(String password) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        return encoder.encode(password);
    }
}

