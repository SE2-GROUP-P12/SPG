package it.polito.SE2.P12.SPG.serviceTest;

import com.icegreen.greenmail.configuration.GreenMailConfiguration;
import com.icegreen.greenmail.junit5.GreenMailExtension;
import com.icegreen.greenmail.util.ServerSetupTest;
import it.polito.SE2.P12.SPG.entity.*;
import it.polito.SE2.P12.SPG.repository.*;
import it.polito.SE2.P12.SPG.service.SpgOrderService;
import it.polito.SE2.P12.SPG.service.SpgUserService;
import it.polito.SE2.P12.SPG.service.WalletOperationService;
import it.polito.SE2.P12.SPG.utils.DBUtilsService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.mail.internet.MimeMessage;
import java.time.Instant;

@SpringBootTest
public class UserServiceTest {
    @Autowired
    private BasketRepo basketRepo;
    @Autowired
    private ProductRepo productRepo;
    @Autowired
    private UserRepo userRepo;
    @Autowired
    private OrderRepo orderRepo;
    @Autowired
    private CustomerRepo customerRepo;
    @Autowired
    private SpgOrderService orderService;
    @Autowired
    private SpgUserService userService;
    @Autowired
    private DBUtilsService dbUtilsService;
    @Autowired
    private ShopEmployeeRepo shopEmployeeRepo;
    @Autowired
    private FarmerRepo farmerRepo;
    @Autowired
    private AdminRepo adminRepo;

    /*Email mock initialization*/
    @RegisterExtension
    static GreenMailExtension greenMail = new GreenMailExtension(ServerSetupTest.SMTP) //set up mail server protocol
            .withConfiguration(GreenMailConfiguration.aConfig().withUser("duke@test.com", "password")) //remember to set-up user in test properties
            .withPerMethodLifecycle(false);//reload extension each test: false


    @BeforeEach
    public void initContext() {
        dbUtilsService.dropAll();
        //Create 2 user to issue some order
        Customer cust1 = new Customer("customer1", "surname1", "ssn_aaaaaaaaaaaa", "123456789", "customer1@foomail.com", "password1223ABC", "address1", 92.5);
        Customer cust2 = new Customer("customer2", "surname2", "ssn_bbbbbbbbbbbb", "123456789", "customer2@foomail.com", "password1223ABC", "address2", 12.82);
        dbUtilsService.saveCustomer(cust1);
        dbUtilsService.saveCustomer(cust2);
        //Create a ShopEmployee
        ShopEmployee se1 = new ShopEmployee("shopE", "Mployee", "ssn_shooooooopemp", "123456789", "shop@employee.com", "password");
        dbUtilsService.saveShopEmployee(se1);
        //Create a farmer
        Farmer farm1 = new Farmer("aaa", "bbb", "ssn_faaarmer", "42342342", "farmer@farmer.com", "password");
        dbUtilsService.saveFarmer(farm1);
        //Create an admin
        Admin admin = new Admin("admin", "aaa", "ssn-admin", "43848234892", "admin@admin.com", "password");
        dbUtilsService.saveAdmin(admin);
    }

    @Test
    public void findUserByEmailTest() {

        User user = userService.getUserByEmail("customer1@foomail.com");
        Assertions.assertNotNull(user);

        Assertions.assertEquals("customer1", user.getName());
        Assertions.assertEquals("surname1", user.getSurname());
        Assertions.assertEquals("ssn_aaaaaaaaaaaa", user.getSsn());
        Assertions.assertEquals("123456789", user.getPhoneNumber());
        Assertions.assertEquals("password1223ABC", user.getPassword());

    }

    @Test
    public void addNewCustomerTest() {
        Customer newCust = new Customer("customer3", "surname3", "ssn_ccccccccccc", "123456789", "customer3@foomail.com", "password1223ABC", "address3", 15.3);

        Assertions.assertTrue(userService.addNewCustomer(newCust));
        Assertions.assertTrue(userRepo.existsByEmail("customer3@foomail.com"));
        Customer fromRepo = customerRepo.findCustomerByEmail("customer3@foomail.com");
        Assertions.assertNotNull(fromRepo);

        Assertions.assertEquals("customer3", fromRepo.getName());
        Assertions.assertEquals("surname3", fromRepo.getSurname());
        Assertions.assertEquals("ssn_ccccccccccc", fromRepo.getSsn());
        Assertions.assertEquals("123456789", fromRepo.getPhoneNumber());
        Assertions.assertEquals("customer3@foomail.com", fromRepo.getEmail());
        Assertions.assertEquals("password1223ABC", fromRepo.getPassword());
        Assertions.assertEquals("address3", fromRepo.getAddress());
        Assertions.assertEquals(15.3, fromRepo.getWallet());
    }

    @Test
    void addDuplicateEmailCustomerTest() {
        Customer newCust = new Customer("customer3", "surname3", "ssn_ccccccccccc", "123456789", "customer3@foomail.com", "password1223ABC", "address3", 15.3);
        Customer newCustDup = new Customer("customerDup", "surnameDup", "ssn_dupdupdup", "987654321", "customer3@foomail.com", "passwordDup", "addressDup", 35.1);

        Assertions.assertTrue(userService.addNewCustomer(newCust));
        Assertions.assertTrue(userRepo.existsByEmail("customer3@foomail.com"));
        Assertions.assertNotNull(customerRepo.findCustomerByEmail("customer3@foomail.com"));

        Assertions.assertFalse(userService.addNewCustomer(newCustDup));
        Assertions.assertFalse(userRepo.existsBySsn("ssn_dupdupdup"));

        Customer fromRepo = customerRepo.findCustomerByEmail("customer3@foomail.com");
        Assertions.assertNotNull(fromRepo);

        Assertions.assertEquals("customer3", fromRepo.getName());
        Assertions.assertEquals("surname3", fromRepo.getSurname());
        Assertions.assertEquals("ssn_ccccccccccc", fromRepo.getSsn());
        Assertions.assertEquals("123456789", fromRepo.getPhoneNumber());
        Assertions.assertEquals("customer3@foomail.com", fromRepo.getEmail());
        Assertions.assertEquals("password1223ABC", fromRepo.getPassword());
        Assertions.assertEquals("address3", fromRepo.getAddress());
        Assertions.assertEquals(15.3, fromRepo.getWallet());
    }

    @Test
    void addDuplicateSsnCustomerTest() {
        Customer newCust = new Customer("customer3", "surname3", "ssn_ccccccccccc", "123456789", "customer3@foomail.com", "password1223ABC", "address3", 15.3);
        Customer newCustDup = new Customer("customerDup", "surnameDup", "ssn_ccccccccccc", "987654321", "customerDup@foomail.com", "passwordDup", "addressDup", 35.1);

        Assertions.assertTrue(userService.addNewCustomer(newCust));
        Assertions.assertTrue(userRepo.existsByEmail("customer3@foomail.com"));
        Assertions.assertNotNull(customerRepo.findCustomerByEmail("customer3@foomail.com"));

        Assertions.assertFalse(userService.addNewCustomer(newCustDup));
        Assertions.assertFalse(userRepo.existsByEmail("customerDup@foomail.com"));

        Customer fromRepo = customerRepo.findCustomerByEmail("customer3@foomail.com");
        Assertions.assertNotNull(fromRepo);


        Assertions.assertEquals("customer3", fromRepo.getName());
        Assertions.assertEquals("surname3", fromRepo.getSurname());
        Assertions.assertEquals("ssn_ccccccccccc", fromRepo.getSsn());
        Assertions.assertEquals("123456789", fromRepo.getPhoneNumber());
        Assertions.assertEquals("customer3@foomail.com", fromRepo.getEmail());
        Assertions.assertEquals("password1223ABC", fromRepo.getPassword());
        Assertions.assertEquals("address3", fromRepo.getAddress());
        Assertions.assertEquals(15.3, fromRepo.getWallet());
    }

    @Test
    void addDuplicateEmailSsnCustomerTest() {
        Customer newCust = new Customer("customer3", "surname3", "ssn_ccccccccccc", "123456789", "customer3@foomail.com", "password1223ABC", "address3", 15.3);
        Customer newCustDup = new Customer("customerDup", "surnameDup", "ssn_ccccccccccc", "987654321", "customer3@foomail.com", "passwordDup", "addressDup", 35.1);

        Assertions.assertTrue(userService.addNewCustomer(newCust));
        Assertions.assertTrue(userRepo.existsByEmail("customer3@foomail.com"));
        Assertions.assertNotNull(customerRepo.findCustomerByEmail("customer3@foomail.com"));

        Assertions.assertFalse(userService.addNewCustomer(newCustDup));
        Assertions.assertFalse(userRepo.existsByName("customerDup"));

        Customer fromRepo = customerRepo.findCustomerByEmail("customer3@foomail.com");
        Assertions.assertNotNull(fromRepo);

        Assertions.assertEquals("customer3", fromRepo.getName());
        Assertions.assertEquals("surname3", fromRepo.getSurname());
        Assertions.assertEquals("ssn_ccccccccccc", fromRepo.getSsn());
        Assertions.assertEquals("123456789", fromRepo.getPhoneNumber());
        Assertions.assertEquals("customer3@foomail.com", fromRepo.getEmail());
        Assertions.assertEquals("password1223ABC", fromRepo.getPassword());
        Assertions.assertEquals("address3", fromRepo.getAddress());
        Assertions.assertEquals(15.3, fromRepo.getWallet());
    }

    @Test //Il test tenta di convertire il basket di un employee in un ordine per un farmer
    public void farmersCannotHaveBasket() {
        Assertions.assertFalse(userService.isBasketUserType(userService.getUserByEmail("farmer@farmer.com")));
    }

    @Test //Il test tenta di convertire il basket di un employee in un ordine per un admin
    public void adminsCannotHaveBasket() {
        Assertions.assertFalse(userService.isBasketUserType(userService.getUserByEmail("admin@admin.com")));
    }


    @Test //Il test tenta di convertire il basket di un employee in un ordine per un farmer
    public void farmersCannotHaveOrders() {
        Assertions.assertFalse(userService.isOrderUserType(userService.getUserByEmail("farmer@farmer.com")));
    }

    @Test //Il test tenta di convertire il basket di un employee in un ordine per un admin
    public void adminsCannotHaveOrders() {
        Assertions.assertFalse(userService.isOrderUserType(userService.getUserByEmail("admin@admin.com")));
    }

    @Test
    public void getWalletTest() {
        Customer cust1 = customerRepo.findCustomerByEmail("customer1@foomail.com");
        Customer cust2 = customerRepo.findCustomerByEmail("customer2@foomail.com");

        Assertions.assertEquals(92.5, cust1.getWallet());
        Assertions.assertEquals(12.82, cust2.getWallet());
    }

    @Test
    public void correctTopUpTest() {

        Assertions.assertTrue(userService.topUp("customer1@foomail.com", 10.0, Instant.now()));
        Assertions.assertTrue(userService.topUp("customer2@foomail.com", 15.5, Instant.now()));

        Assertions.assertEquals(102.5, userService.getWallet("customer1@foomail.com"));
        Assertions.assertEquals(28.32, userService.getWallet("customer2@foomail.com"));
    }

    @Test
    public void zeroTopUpTest() {
        Assertions.assertFalse(userService.topUp("customer1@foomail.com", 0.0, Instant.now()));
        Assertions.assertEquals(92.5, userService.getWallet("customer1@foomail.com"));
    }

    @Test
    public void negativeTopUpTest() {
        Assertions.assertFalse(userService.topUp("customer1@foomail.com", -1.0, Instant.now()));
        Assertions.assertEquals(92.5, userService.getWallet("customer1@foomail.com"));
    }

    @Test
    public void infiniteTopUpTest() {
        Assertions.assertFalse(userService.topUp("customer1@foomail.com", Double.POSITIVE_INFINITY, Instant.now()));
        Assertions.assertFalse(userService.topUp("customer2@foomail.com", Double.NEGATIVE_INFINITY, Instant.now()));
        Assertions.assertEquals(92.5, userService.getWallet("customer1@foomail.com"));
        Assertions.assertEquals(12.82, userService.getWallet("customer2@foomail.com"));
    }

    @Test
    public void nanTopUpTest() {
        Assertions.assertFalse(userService.topUp("customer1@foomail.com", Double.NaN, Instant.now()));
        Assertions.assertEquals(92.5, userService.getWallet("customer1@foomail.com"));
    }

    @Test
    public void missedPickUpTest() {
        Customer tmp = userService.getCustomerByEmail("customer1@foomail.com");
        //Check that initial amount is 0
        Assertions.assertEquals(0, tmp.getMissedPickUpAmount());
        //Increment by one
        tmp.incrementMissedPickUp();
        Assertions.assertEquals(1, userService.incrementMissedPickUp(tmp.getEmail()));
        //Decrement by 1
        tmp.decrementMissedPickUp();
        Assertions.assertEquals(0, userService.decrementMissedPickUp(tmp.getEmail()));
        for (int i = 0; i < 5; i++) {
            userService.incrementMissedPickUp(tmp.getEmail());
        }
        //user is banned
        Assertions.assertTrue(userService.isCustomerBanned(tmp.getEmail()));
        //decrement
        userService.decrementMissedPickUp(tmp.getEmail());
        Assertions.assertFalse(userService.isCustomerBanned(tmp.getEmail()));
    }

    @Test
    public void missedPickUpTest_HandlingError() {
        //invalid user are considered as banned
        Assertions.assertTrue(userService.isCustomerBanned("invalid mail"));
        //-1 if user is not valid
        Assertions.assertEquals(-1, userService.decrementMissedPickUp("invalid mail"));
        Assertions.assertEquals(-1, userService.incrementMissedPickUp("invalid mail"));
        //Decrement on 0
        Assertions.assertEquals(0, userService.decrementMissedPickUp("customer1@foomail.com"));
        //increment on 5
        for (int i = 0; i < 10; i++) {
            userService.incrementMissedPickUp("customer1@foomail.com");
        }
        Assertions.assertEquals(5, userService.incrementMissedPickUp("customer1@foomail.com"));

    }

    @Test
    public void sentWarningPickUpAmountMailTest() {
        Assertions.assertTrue(userService.sentWarningPickUpAmountMail("customer1@foomail.com"));
        MimeMessage[] receivedMessage = greenMail.getReceivedMessages();
        Assertions.assertEquals(1, receivedMessage.length);
        //error handling (invalid email)
        Assertions.assertFalse(userService.sentWarningPickUpAmountMail("customer12344@foomail.com"));
    }


}