package com.vaccine.controller;


import com.vaccine.model.*;
import com.vaccine.repository.*;
import com.vaccine.service.admindestination.IAdminDestinationService;
import com.vaccine.service.user.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;


import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.security.Principal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;


@RestController
@RequestMapping(value = {"/"}, method = RequestMethod.GET, produces = "application/x-www-form-urlencoded;charset=UTF-8")
public class UserController {
    @Autowired
    IUserService userService;

    @Autowired
    UserRoleRepository userRoleRepository;

    @Autowired
    IUserRepository iUserRepository;

    @Autowired
    JavaMailSender mailSender2;

    @Autowired
    IAdminDestinationService adminDestinationService;

    @Autowired
    IDayTimeStart iDayTimeStart;

    @Autowired
    IDayVaccineRegisterRepository dayVaccineRegisterRepository;

    @Autowired
    IWarehouseRepository warehouseRepository;


    static int countTime = 0;
    static int oneDayDone = 0;

    static int setPeoplePerHour = 1;
    static int setToChangeDay = setPeoplePerHour * 4;

    static String timeStart = "";

    //    ---------------------------------Thời gian tiêm chủng------------------------------------------>

    @PostMapping("/setPeoplePerHour")
    public ModelAndView setPeoplePerHour(@RequestParam(value = "number1", required = false) int number1,
                                         @RequestParam(value = "day_time", required = false) String day_time) throws ParseException {

        setPeoplePerHour = number1;
        String[] arr = day_time.split("-");
        String str_day = arr[2] + "-" + arr[1] + "-" + arr[0];
        timeStart = str_day + " ";
        DayTimeStart dayTimeStart = new DayTimeStart();
        dayTimeStart.setDay_start(timeStart);
        dayTimeStart.setPeople_per_hour(setPeoplePerHour);
        iDayTimeStart.save(dayTimeStart);
       ModelAndView modelAndView= new ModelAndView("/victory/injectiontime");
        modelAndView.addObject("msg","Thiết lập thành công!");

        return modelAndView;
    }

    public void setDayTimeStart() {
        Optional<DayTimeStart> dayTimeStart = iDayTimeStart.findById(1L);

        setPeoplePerHour = dayTimeStart.get().getPeople_per_hour();
        timeStart = dayTimeStart.get().getDay_start();
        setToChangeDay = setPeoplePerHour * 4;

    }


    @ModelAttribute("destinations")
    public Page<AdminDestination> destinations(@PageableDefault(value = 15) Pageable pageable) {
        return adminDestinationService.findAll(pageable);
    }


    @GetMapping
    public ModelAndView home(HttpServletRequest request, Principal principal) {
        ModelAndView modelAndView = new ModelAndView("/home");
        if (request.isUserInRole("ROLE_ADMIN") || request.isUserInRole("ROLE_USER") || request.isUserInRole("ROLE_DOCTOR")) {
            String userName = principal.getName();
            User user = iUserRepository.getUserByCMND(userName);
            modelAndView.addObject("userInfo", user.getUserName());
        }
        modelAndView.addObject("user", new User());
        return modelAndView;
    }


    @GetMapping("/form")
    public ModelAndView showForm() {
        WarehouseVaccine warehouseVaccine = warehouseRepository.findById(1L).get();
        if(warehouseVaccine.getAmountRegister()>0){
            ModelAndView modelAndView = new ModelAndView("/form");
            modelAndView.addObject("user", new User());
            return modelAndView;
        }
        else{
            ModelAndView modelAndView = new ModelAndView("/security/regisFound");
            return modelAndView;
        }
    }

    @GetMapping("/user")
    public ModelAndView showUserPage(Principal principal) {
//      userName mean CMND
        String userName = principal.getName();
        User user = new User();
        user = iUserRepository.getUserByCMND(userName);

        ModelAndView modelAndView = new ModelAndView("/user/userPage");
        modelAndView.addObject("userInfo", user);
        return modelAndView;
    }

    @GetMapping("/403")
    public ModelAndView errorPage() {
        ModelAndView modelAndView = new ModelAndView("/security/403");
        modelAndView.addObject("user", new User());
        return modelAndView;
    }

    //    Lấy lại mật khẩu!!!
    @GetMapping("/forgot-password")
    public ModelAndView forgotPassword() {
        ModelAndView modelAndView = new ModelAndView("/security/forgotPassword");

        return modelAndView;
    }
    @PostMapping("/get-password")
    public ModelAndView getPassword(@RequestParam(value = "CMND", required = false) String CMND,
                                    @RequestParam(value = "email", required = false) String email) {
        ModelAndView modelAndView = new ModelAndView("/security/forgotPassword");
        User user =iUserRepository.getUserByCMND(CMND);
        if ( user != null && user.getEmail().equals(email)){
            modelAndView.addObject("msg","Kiểm tra mail để lấy lại mật khẩu!");

//            Tuỳ trường hợp
            String link1 = "https://vaccinevietnam.herokuapp.com/set-password/"+user.getCMND();
//            String link = "http://localhost:8080/set-password/"+user.getCMND();
            getPasswordByMail(user.getUserName(), user.getEmail(), link1);
            return modelAndView;
        }
        else{
            modelAndView.addObject("msg","Tài khoản không tồn tại!!!");
            return modelAndView;
        }
    }

    @GetMapping("/set-password/{token}")
    public ModelAndView setNewPassword(@PathVariable String token) {

        ModelAndView modelAndView = new ModelAndView("/security/setNewPassword");
        modelAndView.addObject("userInfo",token);
        return modelAndView;
    }

    @PostMapping("/set-new-password")
    public ModelAndView setNewPassword2(@RequestParam("CMND") String CMND,
                                        @RequestParam("password") String newPassword) {
        ModelAndView modelAndView = new ModelAndView("/security/setNewPassword");
        User user = iUserRepository.getUserByCMND(CMND);
        String lastPassword = user.getPassword();
        if(user.getPassword().equals(lastPassword)){
            user.setPassword(encrytePassword(newPassword));
            iUserRepository.save(user);
            modelAndView.addObject("msg","Mật khẩu đã thay đổi thành công!");
        }else{
            modelAndView.addObject("msg","Oh no! Đã có lỗi xảy ra!");
        }



        return modelAndView;
    }

    @GetMapping(value = {"/login"})
    public ModelAndView loginPage(HttpServletRequest request, Principal principal) {
        if (request.isUserInRole("ROLE_ADMIN") || request.isUserInRole("ROLE_USER") || request.isUserInRole("ROLE_DOCTOR")) {
//            return về trang user lại
            String userName = principal.getName();
            User user = new User();
            user = iUserRepository.getUserByCMND(userName);
            ModelAndView modelAndView = new ModelAndView("/user/userPage");
            modelAndView.addObject("userInfo", user);
            return modelAndView;
        } else {
            ModelAndView modelAndView = new ModelAndView("/security/login");

            return modelAndView;
        }
    }

    @PostMapping("/create")
    public ModelAndView createUser(User user) {
//       set age and status
        user.setAge(java.time.LocalDate.now().getYear() - user.getAge());
        user.setStatus(setStatus(user));
//      get currently date
        LocalDateTime a = LocalDateTime.now();
        DateTimeFormatter formatterDay = DateTimeFormatter.ofPattern("dd-MM-yyyy ");
        String formattedDate = a.format(formatterDay);
        user.setCreateDay(formattedDate);
//      get maxDay, maxTime from db
        String str = userService.getMaxDayFromData() + userService.getMaxTimeFromData();
        int countMaxTime = userService.countMaxTimeInDay();
        int countMaxDay = userService.countMaxDayToNext();
//      set day start and people per hour
        if (user.getStatus() == 1 || user.getStatus() == 2) {
            try {
                setDayTimeStart();
            } catch (Exception e) {
                ModelAndView modelAndView = new ModelAndView("/form");
                modelAndView.addObject("user", new User());
                modelAndView.addObject("fail", "Chiến dịch chưa bắt đầu, vui lòng quay lại sau!");
                return modelAndView;
            }
            setDayTimeVaccine(user, str, countMaxTime, countMaxDay);
//            Khanh' lam` kho bai~ vaccine
            WarehouseVaccine warehouseVaccine = warehouseRepository.findById(1L).get();
            if(warehouseVaccine.getAmountRegister()<=0){
                return new ModelAndView("/security/regisFound");
            }
            warehouseVaccine.setAmountRegister(warehouseVaccine.getAmountRegister()-1);
            warehouseRepository.save(warehouseVaccine);
            if(user.getDateVaccine()!=null){
                AmountVaccineRegisterInDay amountVaccineRegisterInDay1 = new AmountVaccineRegisterInDay(user.getDateVaccine(),0L,0L,warehouseRepository.findById(1L).get());
                AmountVaccineRegisterInDay amountVaccineRegisterInDay = dayVaccineRegisterRepository.findByDate(user.getDateVaccine()).orElse(amountVaccineRegisterInDay1);
                amountVaccineRegisterInDay.setAmount(amountVaccineRegisterInDay.getAmount()+1);
                dayVaccineRegisterRepository.save(amountVaccineRegisterInDay);
            }
        }
//        Set encrytedPassword
        String password = user.getEncrytedPassword();
        String encrytedPassword = encrytePassword(password);
        user.setPassword(encrytedPassword);
        try {
            userService.save(user);
        } catch (Exception e) {
            ModelAndView modelAndView = new ModelAndView("/form");
            modelAndView.addObject("user", new User());
            modelAndView.addObject("fail", "Số chứng minh nhân dân đã tồn tại!!!");
            return modelAndView;
        }
        try {
            //        Thêm quyền
            UserRole userRole = new UserRole();
            userRole.setAppUser(user);
            AppRole appRole = new AppRole();
            appRole.setRoleId(1L);
            userRole.setAppRole(appRole);
            userRoleRepository.save(userRole);
            //        Thêm một quyền nữa
//            appRole.setRoleId(2L);
//            userRoleRepository.save(new UserRole(user, appRole));
            //        Thêm một quyền nữa~~~
//            appRole.setRoleId(3L);
//            userRoleRepository.save(new UserRole(user, appRole));
        } catch (Exception e) {
            ModelAndView modelAndView = new ModelAndView("/form");
            modelAndView.addObject("user", new User());
            modelAndView.addObject("fail", "Oh no! Có vấn đề về cơ sở dữ liệu!");
            return modelAndView;
        }
//        Gửi mail
        if (user.getEmail() != null) {
            sendEmail("boyte.vaccine.covid@gmail.com", user.getEmail(), user.getUserName(), user.getCMND()
                    , user.getAge(), user.getDateVaccine(), user.getTimeVaccine()
                    , user.getAdminDestination().getNameDestination(), user.getStatus());
        }
        ModelAndView modelAndView = new ModelAndView("/form");
        modelAndView.addObject("user", new User());
        modelAndView.addObject("createDone", "Chúc mừng bạn đã đăng ký thành công!");
        return modelAndView;
    }

    public void sendEmail(String from, String to, String fullName, String CMND
            , int age, String day, String time, String address, int status) {

        MimeMessage msg = mailSender2.createMimeMessage();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(msg, true, "UTF-8");
            helper.setSubject("Kết quả đăng ký tiêm chủng");
            helper.setFrom(from);
            helper.setTo(to);
            if (day == null) {
                day = "---";
                time = "---";
            }
            MailText mailText = new MailText(fullName, CMND, age, day, time, address, status);


            helper.setText(mailText.getTextMail(), true);
            mailSender2.send(msg);
        } catch (Exception e) {
            System.err.println("Loi roi` part 2~~~~~~~~~~~~~~~~");
        }

    }

    public void getPasswordByMail(String name, String email, String link){
        MimeMessage msg = mailSender2.createMimeMessage();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(msg, true, "UTF-8");
            helper.setSubject("Tạo mật khẩu mới");
            helper.setFrom("boyte.vaccine.covid@gmail.com");
            helper.setTo(email);

            MailText mailText = new MailText(name, link);

            helper.setText(mailText.getPassword(), true);
            mailSender2.send(msg);
        } catch (Exception e) {
            System.err.println("Loi roi` part 2~~~~~~~~~~~~~~~~");
        }
    }

    public int setStatus(User user) {
//        1,2 ok
        int status;

        if (user.getHealthyStatus() != null) {
            String[] arr = user.getHealthyStatus().split(",");
            if (arr.length > 1 || user.getAge() < 16 || user.getAge() > 65) {
                status = 3;
            } else if (arr.length == 1 && user.getAge() >= 16 && user.getAge() <= 65) {
                status = 1;
            } else {
                status = 2;
            }
        } else {
            if (user.getAge() >= 16 && user.getAge() <= 65) {
                status = 2;
            } else {
                status = 3;
            }
        }
        return status;
    }

    public static void setDayTimeVaccine(User user, String str, Integer countMaxTime, int coutMaxDay) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");
//       Set day to start
        if (str.equals("nullnull")) {
            str = timeStart + "08:00";
        }
        LocalDateTime currentDateTime = LocalDateTime.parse(str, formatter);
//      Divide date to time
        DateTimeFormatter formatterDay = DateTimeFormatter.ofPattern("dd-MM-yyyy ");
        DateTimeFormatter formatterTime = DateTimeFormatter.ofPattern("HH:mm");
        //        Set 1 day with 8h,10h,14h,16h
        if (countMaxTime != null) {
//            lấy từ db, đếm số giờ để chuyển
            countTime = countMaxTime;
        }
        if (countMaxTime != null && countMaxTime == setPeoplePerHour) {
//            lấy từ db, chuyển giờ liền
            countTime = setPeoplePerHour;
        }
        if (coutMaxDay == setToChangeDay) {
//           lấy từ db, đếm số lần để chuyển ngày
            oneDayDone = setToChangeDay;
        }
        //        Số mũi tiêm trong một ngày, cứ thế nhân lên
        if (oneDayDone == setToChangeDay) {
            currentDateTime = currentDateTime.plusDays(1);
            currentDateTime = currentDateTime.minusHours(8);
            oneDayDone = 0;
            countTime = 0;
        }
        //  Mấy tk một giờ, ở đây là 2, 2 tk tăng lên 6h
        if (countTime == setPeoplePerHour) {
            currentDateTime = currentDateTime.plusHours(2);
            if (currentDateTime.getHour() == 12) {
                currentDateTime = currentDateTime.plusHours(2);
            }
            countTime = 0;
//  Set giá trị cho từng tk
        }
        while (countTime < setPeoplePerHour) {
            String formattedDate = currentDateTime.format(formatterDay);
            String formattedTime = currentDateTime.format(formatterTime);
            user.setTimeVaccine(formattedTime);
            user.setDateVaccine(formattedDate);
            countTime++;
            oneDayDone++;
            return;
        }
    }

    // Ma~ hoa mat khau~
    public static String encrytePassword(String password) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        return encoder.encode(password);
    }
}
