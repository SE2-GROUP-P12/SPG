package it.polito.SE2.P12.SPG.service;

import it.polito.SE2.P12.SPG.entity.User;
import it.polito.SE2.P12.SPG.repository.UserRepo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

@Service
@Slf4j
public class SpgUserService {
    private final UserRepo userRepo;

    @Autowired
    public SpgUserService(UserRepo userRepo) {
        this.userRepo = userRepo;
    }

    public void populateDB() {
        //ADMIN
        String adminString = "admin";
        String password = "password";
        User admin = new User(adminString, adminString, "ADMIN00000012345", "0000000000",
                "ADMIN", "admin@foomail.com", "password");
        //Other users
        User temp1 = new User("Mario", "Rossi", "RSSMRA00D12N376V", "01234567292",
                "CUSTOMER", "mario.rossi@gmail.com", password);
        User temp2 = new User("Paolo", "Bianchi", "BNCPLA00D12N376V", "01264567892",
                "CUSTOMER", "paolo.bianchi@gmail.com", password);
        User temp3 = new User("Francesco", "Conte", "CNTFRN00D12N376V", "01234567862",
                "EMPLOYEE", "francesco.conte@gmail.com", password);
        if (userRepo.findUserByEmail("mario.rossi@gmail.com") == null) userRepo.save(temp1);
        if (userRepo.findUserByEmail("paolo.bianchi@gmail.com") == null) userRepo.save(temp2);
        if (userRepo.findUserByEmail("francesco.conte@gmail.com") == null) userRepo.save(temp3);
        if (userRepo.findUserByEmail("admin@foomail.com") == null) userRepo.save(admin);
    }

    public Long getUserIdByEmail(String email) {
        return userRepo.findUserByEmail(email).getUserId();
    }

    public User getUserByEmail(String email) {
        return userRepo.findUserByEmail(email);
    }

    public User getUserByUserId(Long userId) {
        return userRepo.findUserByUserId(userId);
    }

    public double getWallet(String email) {
        return userRepo.findUserByEmail(email).getWallet();
    }

    public double topUp(String email, double value) {
        User tmp = userRepo.findUserByEmail(email);
        tmp.setWallet(tmp.getWallet() + value);
        userRepo.save(tmp);
        return tmp.getWallet();
    }

    public Map<String, Boolean> checkPresenceOfUser(String email, String ssn) {
        Map<String, Boolean> response = new HashMap<>();
        if (checkPresenceOfMail(email) || checkPresenceOfSSN(ssn))
            response.put("exist", true);
        else
            response.put("exist", false);
        return response;
    }

    public Boolean checkPresenceOfMail(String email) {
        return userRepo.existsByEmail(email);
    }

    public Boolean checkPresenceOfSSN(String ssn) {
        return userRepo.existsBySsn(ssn);
    }

    public Map<String, String> addNewClient(User user) {
        Map<String, String> response = new HashMap<>();
        if (userRepo.existsByEmail(user.getEmail()) || userRepo.existsBySsn(user.getSsn())) {
            response.put("responseMessage", "200-OK-(Customer already present)");
            return response;
        }
        log.info(String.format("User (%s, %s) created, %s", user.getEmail(), user.getSsn(), LocalDate.now()));
        userRepo.save(user);
        response.put("responseMessage", "200-OK-(Customer added successfully)");
        return response;
    }

}
