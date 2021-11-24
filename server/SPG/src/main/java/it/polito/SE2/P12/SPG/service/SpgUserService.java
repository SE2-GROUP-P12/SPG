package it.polito.SE2.P12.SPG.service;

import it.polito.SE2.P12.SPG.entity.*;
import it.polito.SE2.P12.SPG.repository.CustomerRepo;
import it.polito.SE2.P12.SPG.repository.UserRepo;
import it.polito.SE2.P12.SPG.utils.UserRole;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.nio.charset.CodingErrorAction;
import java.util.HashMap;
import java.util.Map;

@Service
public class SpgUserService {
    private UserRepo userRepo;
    private CustomerRepo customerRepo;

    @Autowired
    public SpgUserService(UserRepo userRepo, CustomerRepo customerRepo) {
        this.userRepo = userRepo;
        this.customerRepo = customerRepo;
    }

    public void populateDB() {
        //ADMIN
        Admin admin = new Admin("admin", "admin", "ADMIN00000000000", "0000000000",
                "admin", "password");
        //Customers
        Customer temp1 = new Customer("Mario", "Rossi", "RSSMRA00D12N376V", "01234567892", "mario.rossi@gmail.com", "password", "Main street 1234");
        Customer temp2 = new Customer("Paolo", "Bianchi", "BNCPLA00D12N376V", "01234567892",
                "paolo.bianchi@gmail.com", "password", "Main street 1456");
        //Shop Employee
        ShopEmployee temp3 = new ShopEmployee("Francesco", "Conte", "CNTFRN00D12N376V", "01234567892",
                "francesco.conte@gmail.com", "password");
        //Farmer
        Farmer temp4 = new Farmer("Thomas", "Jefferson", "JFRTHM00D12N376V", "01234567892",
                "thomas.jefferson@gmail.com", "password");
        if (userRepo.findUserByEmail("mario.rossi@gmail.com") == null) userRepo.save(temp1);
        if (userRepo.findUserByEmail("paolo.bianchi@gmail.com") == null) userRepo.save(temp2);
        if (userRepo.findUserByEmail("francesco.conte@gmail.com") == null) userRepo.save(temp3);
        if (userRepo.findUserByEmail("admin") == null) userRepo.save(admin);
        if (userRepo.findUserByEmail("thomas.jefferson@gmail.com") == null) userRepo.save(temp4);
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

    public Double getWallet(String email) {
        return customerRepo.findCustomerByEmail(email).getWallet();
    }

    public double topUp(String email, double value) {
        //adds value to the user's wallet
        Customer tmp = customerRepo.findCustomerByEmail(email);
        tmp.setWallet(tmp.getWallet() + value);
        customerRepo.save(tmp);
        return tmp.getWallet();
    }

    public Map<String, Boolean> checkPresenceOfUser(String email, String ssn) {
        //checks if a user is present in the database
        Map<String, Boolean> response = new HashMap<>();
        //System.out.println("Here, ssn: " + ssn + ", email : " + email);
        if (checkPresenceOfMail(email) || checkPresenceOfSSN(ssn))
            response.put("exist", true);
        else
            response.put("exist", false);
        return response;
    }

    public Boolean checkPresenceOfMail(String email) {
        return userRepo.existsByEmail(email);
    }

    private Boolean checkPresenceOfSSN(String ssn) {
        return userRepo.existsBySsn(ssn);
    }

    public Map<String, String> addNewClient(User user) {
        //adds a new user to the database
        Map<String, String> response = new HashMap<>();
        if (userRepo.existsByEmail(user.getEmail()) || userRepo.existsBySsn(user.getSsn())) {
            response.put("responseMessage", "200-OK-(Customer already present)");
            return response;
        }
        userRepo.save(user);
        response.put("responseMessage", "200-OK-(Customer added successfully)");
        return response;
    }

    public boolean checkEmployeePermission(Long userId) {
        return userRepo.findUserByUserId(userId).getRole() == UserRole.ROLE_SHOP_EMPLOYEE;
    }

}
