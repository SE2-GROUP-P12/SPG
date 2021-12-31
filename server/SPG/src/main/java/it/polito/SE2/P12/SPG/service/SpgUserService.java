package it.polito.SE2.P12.SPG.service;

import it.polito.SE2.P12.SPG.emailServer.EmailConfiguration;
import it.polito.SE2.P12.SPG.entity.User;
import it.polito.SE2.P12.SPG.interfaceEntity.BasketUserType;
import it.polito.SE2.P12.SPG.interfaceEntity.OrderUserType;
import it.polito.SE2.P12.SPG.interfaceEntity.WalletUserType;
import it.polito.SE2.P12.SPG.repository.UserRepo;
import it.polito.SE2.P12.SPG.utils.MailConstants;
import lombok.extern.slf4j.Slf4j;
import it.polito.SE2.P12.SPG.entity.*;
import it.polito.SE2.P12.SPG.repository.*;
import it.polito.SE2.P12.SPG.utils.UserRole;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

import static it.polito.SE2.P12.SPG.utils.OrderStatus.ORDER_STATUS_CONFIRMED;
import static it.polito.SE2.P12.SPG.utils.OrderStatus.ORDER_STATUS_OPEN;

@Service
@Slf4j
public class SpgUserService {
    private UserRepo userRepo;
    private CustomerRepo customerRepo;
    private ShopEmployeeRepo shopEmployeeRepo;
    private AdminRepo adminRepo;
    private FarmerRepo farmerRepo;
    private EmailConfiguration emailConfiguration;

    @Autowired
    public SpgUserService(UserRepo userRepo, CustomerRepo customerRepo, ShopEmployeeRepo shopEmployeeRepo, AdminRepo adminRepo, FarmerRepo farmerRepo, EmailConfiguration emailConfiguration) {
        this.userRepo = userRepo;
        this.customerRepo = customerRepo;
        this.shopEmployeeRepo = shopEmployeeRepo;
        this.adminRepo = adminRepo;
        this.farmerRepo = farmerRepo;
        this.emailConfiguration = emailConfiguration;
    }


    public Long getUserIdByEmail(String email) {
        return userRepo.findUserByEmail(email).getUserId();
    }

    public User getUserByEmail(String email) {
        return userRepo.findUserByEmail(email);
    }

    public Customer getCustomerByEmail(String email) {
        if (!userRepo.findUserByEmail(email).getRole().equals(UserRole.ROLE_CUSTOMER)) return null;
        return customerRepo.findCustomerByEmail(email);
    }

    public Farmer getFarmerById(Long farmerId) {
        if (!userRepo.findUserByUserId(farmerId).getRole().equals(UserRole.ROLE_FARMER)) return null;
        return farmerRepo.findFarmerByUserId(farmerId);
    }

    public Farmer getFarmerByName(String name) {
        if (!userRepo.findUserByName(name).getRole().equals(UserRole.ROLE_FARMER)) return null;
        return farmerRepo.findFarmerByName(name);
    }

    public Farmer getFarmerByEmail(String email) {
        return farmerRepo.findFarmerByEmail(email);
    }

    public User getUserByUserId(Long userId) {
        return userRepo.findUserByUserId(userId);
    }

    public Double getWallet(String email) {
        return customerRepo.findCustomerByEmail(email).getWallet();
    }

    public Boolean topUp(String email, double value) {
        //set up all the pending order to confirm if possible
        Customer customer = getCustomerByEmail(email);
        List<Order> orderList = customer.getOrders();
        if (orderList == null) {
            return topUp(getOrderUserTypeByEmail(email), value);
        }
        //evaluate rest wallet balance and sum to it the total top up value
        Double currentConfirmedValueRest = value;
        Double tmpConfirmedOrdersAmount = 0.00;
        for (Order order : orderList) {
            //Check if there are some opened orders and if that orders can be paid by the current top up value
            if (order.getStatus().equals(ORDER_STATUS_CONFIRMED)) {
                //Update limit that we can top up
                tmpConfirmedOrdersAmount += order.getValue();
            }
        }
        currentConfirmedValueRest += (customer.getWallet() - tmpConfirmedOrdersAmount);
        for (Order order : orderList) {
            //Check if there are some opened orders and if that orders can be paid by the current top up value
            if (order.getStatus().equals(ORDER_STATUS_OPEN) && order.getValue() <= currentConfirmedValueRest) {
                //Update limit that we can top up
                currentConfirmedValueRest -= order.getValue();
                //update oder status
                order.updateToConfirmedStatus();
            }
        }
        //perform top up
        return topUp(getOrderUserTypeByEmail(email), value);
    }

    public Boolean topUp(OrderUserType user, double value) {
        if (value <= 0 || Double.isInfinite(value) || Double.isNaN(value))
            return false;
        //adds value to the user's wallet
        if (user == null || getUserByEmail(((User) user).getEmail()) == null)
            return false;
        user.setWallet(user.getWallet() + value);
        userRepo.save((User) user);
        return true;
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

    public BasketUserType getBasketUserTypeByEmail(String email) {
        return (BasketUserType) userRepo.findByEmailAndRoleIn(email, UserRole.ROLE_BASKET_USER_TYPE);
    }

    public WalletUserType getWalletUserTypeByEmail(String email) {
        return (WalletUserType) userRepo.findByEmailAndRoleIn(email, UserRole.ROLE_WALLET_USER_TYPE);
    }

    public OrderUserType getOrderUserTypeByEmail(String email) {
        return (OrderUserType) userRepo.findByEmailAndRoleIn(email, UserRole.ROLE_ORDER_USER_TYPE);
    }

    public Boolean isBasketUserType(User user) {
        return UserRole.ROLE_BASKET_USER_TYPE.contains(user.getRole());
    }

    public Boolean isOrderUserType(User user) {
        return UserRole.ROLE_ORDER_USER_TYPE.contains(user.getRole());
    }

    public void payForProducts(Basket basket, Customer user) {
        user.pay(basket.getValue());
    }


    //Check presence of user into repo then update, return the current (updated) amount, if absent return invalid amount (-1)
    public int incrementMissedPickUp(String email) {
        Customer tmpCustomer = customerRepo.findCustomerByEmail(email);
        if (tmpCustomer == null)
            return -1;
        tmpCustomer.incrementMissedPickUp();
        customerRepo.save(tmpCustomer);
        return customerRepo.findCustomerByEmail(email).getMissedPickUpAmount();
    }

    //Check presence of user into repo then update, return the current (updated) amount, if absent return invalid amount (-1)
    public int decrementMissedPickUp(String email) {
        Customer tmpCustomer = customerRepo.findCustomerByEmail(email);
        if (tmpCustomer == null)
            return -1;
        tmpCustomer.decrementMissedPickUp();
        customerRepo.save(tmpCustomer);
        return customerRepo.findCustomerByEmail(email).getMissedPickUpAmount();
    }

    //check if missedPickUpAmount is > 5
    public boolean isCustomerBanned(String email) {
        Customer tmpCustomer = customerRepo.findCustomerByEmail(email);
        if (tmpCustomer == null)
            return true; //invalid user is considered banned
        return tmpCustomer.getMissedPickUpAmount() > 4;
    }

    //Sent automatically generated mail
    public boolean sentWarningPickUpAmountMail(String email) {
        if (customerRepo.findCustomerByEmail(email) == null)
            return false;
        //Create java mail serve starting from config
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setUsername(this.emailConfiguration.getUsername());
        mailSender.setPassword(this.emailConfiguration.getPassword());
        mailSender.setHost(this.emailConfiguration.getHost());
        mailSender.setPort(this.emailConfiguration.getPort());
        //Set parameter
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setFrom(MailConstants.NO_REPLY_EMAIL);
        mailMessage.setTo(email);
        mailMessage.setSubject(MailConstants.EMAIL_SUBJECT_WARNINGS_PICK_UP);
        mailMessage.setText(MailConstants.EMAIL_PICK_UP_WARNING(customerRepo.findCustomerByEmail(email)));
        //Sent mail
        mailSender.send(mailMessage);
        return true;
    }


}
