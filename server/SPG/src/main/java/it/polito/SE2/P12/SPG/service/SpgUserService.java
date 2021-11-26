package it.polito.SE2.P12.SPG.service;

import it.polito.SE2.P12.SPG.entity.User;
import it.polito.SE2.P12.SPG.interfaceEntity.BasketUser;
import it.polito.SE2.P12.SPG.repository.UserRepo;
import lombok.extern.slf4j.Slf4j;
import it.polito.SE2.P12.SPG.entity.*;
import it.polito.SE2.P12.SPG.repository.*;
import it.polito.SE2.P12.SPG.utils.UserRole;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class SpgUserService {
    private UserRepo userRepo;
    private CustomerRepo customerRepo;
    private ShopEmployeeRepo shopEmployeeRepo;
    private AdminRepo adminRepo;
    private FarmerRepo farmerRepo;

    @Autowired
    public SpgUserService(UserRepo userRepo, CustomerRepo customerRepo, ShopEmployeeRepo shopEmployeeRepo, AdminRepo adminRepo, FarmerRepo farmerRepo) {
        this.userRepo = userRepo;
        this.customerRepo = customerRepo;
        this.shopEmployeeRepo = shopEmployeeRepo;
        this.adminRepo = adminRepo;
        this.farmerRepo = farmerRepo;
    }

    public void populateDB() {
        //ADMIN
        Admin admin = new Admin("admin", "admin", "ADMIN00000000000", "0000000000",
                "admin@foomail.com", "password");
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
        if (userRepo.findUserByEmail("mario.rossi@gmail.com") == null) customerRepo.save(temp1);
        if (userRepo.findUserByEmail("paolo.bianchi@gmail.com") == null) customerRepo.save(temp2);
        if (userRepo.findUserByEmail("francesco.conte@gmail.com") == null) shopEmployeeRepo.save(temp3);
        if (userRepo.findUserByEmail("admin@foomail.com") == null) adminRepo.save(admin);
        if (userRepo.findUserByEmail("thomas.jefferson@gmail.com") == null) farmerRepo.save(temp4);
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

    public Boolean checkPresenceOfUser(String email, String ssn) {
        if (checkPresenceOfMail(email) || checkPresenceOfSSN(ssn))
            return true;
        return false;
    }

    public Boolean checkPresenceOfMail(String email) {
        return userRepo.existsByEmail(email);
    }

    public Boolean checkPresenceOfSSN(String ssn) {
        return userRepo.existsBySsn(ssn);
    }

    public Boolean addNewCustomer(Customer customer) {
        if (userRepo.existsByEmail(customer.getEmail()) || userRepo.existsBySsn(customer.getSsn())) {
            return false;
        }
        customerRepo.save(customer);
        return true;
    }

    public boolean checkEmployeePermission(Long userId) {
        return userRepo.findUserByUserId(userId).getRole() == UserRole.ROLE_SHOP_EMPLOYEE;
    }

    public BasketUser getBasketUserByEmail(String email){
        return (BasketUser) userRepo.findByEmailAndRoleIn(email, UserRole.ROLE_BASKETUSER);
    }

}
