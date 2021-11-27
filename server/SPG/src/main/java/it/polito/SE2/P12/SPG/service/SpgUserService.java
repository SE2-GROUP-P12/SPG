package it.polito.SE2.P12.SPG.service;

import it.polito.SE2.P12.SPG.entity.User;
import it.polito.SE2.P12.SPG.interfaceEntity.BasketUserType;
import it.polito.SE2.P12.SPG.interfaceEntity.OrderUserType;
import it.polito.SE2.P12.SPG.repository.UserRepo;
import lombok.extern.slf4j.Slf4j;
import it.polito.SE2.P12.SPG.entity.*;
import it.polito.SE2.P12.SPG.repository.*;
import it.polito.SE2.P12.SPG.utils.UserRole;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Objects;

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



    public Long getUserIdByEmail(String email) {
        return userRepo.findUserByEmail(email).getUserId();
    }

    public User getUserByEmail(String email) {
        return userRepo.findUserByEmail(email);
    }
    public Customer getCustomerByEmail(String email) {
        if(userRepo.findUserByEmail(email).getRole()!=UserRole.ROLE_CUSTOMER) return null;
        return customerRepo.findCustomerByEmail(email);
    }

    public Farmer getFarmerById(Long farmerId) {
        if(userRepo.findUserByUserId(farmerId).getRole()!=UserRole.ROLE_FARMER) return null;
        return farmerRepo.findFarmerByUserId(farmerId);
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
        return Objects.equals(userRepo.findUserByUserId(userId).getRole(), UserRole.ROLE_SHOP_EMPLOYEE);
    }

    public BasketUserType getBasketUserTypeByEmail(String email){
        return (BasketUserType) userRepo.findByEmailAndRoleIn(email, UserRole.ROLE_BASKET_USER_TYPE);
    }

    public OrderUserType getOrderUserTypeByEmail(String email){
        return (OrderUserType) userRepo.findByEmailAndRoleIn(email, UserRole.ROLE_ORDER_USER_TYPE);
    }

}
