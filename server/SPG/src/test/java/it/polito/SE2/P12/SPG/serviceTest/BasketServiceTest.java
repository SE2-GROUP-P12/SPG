package it.polito.SE2.P12.SPG.serviceTest;

import it.polito.SE2.P12.SPG.entity.*;
import it.polito.SE2.P12.SPG.interfaceEntity.BasketUserType;
import it.polito.SE2.P12.SPG.repository.*;
import it.polito.SE2.P12.SPG.entity.Basket;
import it.polito.SE2.P12.SPG.entity.Product;
import it.polito.SE2.P12.SPG.repository.BasketRepo;
import it.polito.SE2.P12.SPG.repository.ProductRepo;
import it.polito.SE2.P12.SPG.service.SpgBasketService;
import it.polito.SE2.P12.SPG.service.SpgUserService;
import it.polito.SE2.P12.SPG.utils.DBUtilsService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.InvalidDataAccessApiUsageException;

@SpringBootTest
public class BasketServiceTest {
    @Autowired
    private ProductRepo productRepo;
    @Autowired
    private CustomerRepo customerRepo;
    @Autowired
    private SpgBasketService basketService;
    @Autowired
    private SpgUserService userService;
    @Autowired
    DBUtilsService dbUtilsService;
    @Autowired
    BasketRepo basketRepo;
    @Autowired
    private ShopEmployeeRepo shopEmployeeRepo;
    @Autowired
    private FarmerRepo farmerRepo;
    @Autowired
    private AdminRepo adminRepo;

    @BeforeEach
    public void initContext() {
        dbUtilsService.dropAll();
        //Create some product and then add them to the Customer Cart
        Product prod1 = new Product("Prod1", "KG", 1000.0, 10.50F);
        Product prod2 = new Product("Prod2", "KG", 100.0, 5.50F);
        Product prod3 = new Product("Prod3", "KG", 20.0, 8.00F);
        productRepo.save(prod1);
        productRepo.save(prod2);
        productRepo.save(prod3);
        //Create 2 user to issue some order
        Customer cust1 = new Customer("customer1", "surname1", "ssn_aaaaaaaaaaaa", "123456789", "customer1@foomail.com", "password1223ABC", "address1");
        Customer cust2 = new Customer("customer2", "surname2", "ssn_bbbbbbbbbbbb", "123456789", "customer2@foomail.com", "password1223ABC", "address2");
        customerRepo.save(cust1);
        customerRepo.save(cust2);
        //Create a ShopEmployee
        ShopEmployee se1 = new ShopEmployee("shopE", "Mployee", "ssn_shooooooopemp", "123456789", "shop@employee.com", "password");
        shopEmployeeRepo.save(se1);
        //Create a farmer
        Farmer farm1 = new Farmer("aaa", "bbb", "ssn_faaarmer", "42342342", "farmer@farmer.com", "password");
        farmerRepo.save(farm1);
        //Create an admin
        Admin admin = new Admin("admin", "aaa", "ssn-admin", "43848234892", "admin@admin.com", "password");
        adminRepo.save(admin);
    }

    @Test
    public void deleteBasketTest() {
        BasketUserType u = userService.getBasketUserTypeByEmail("customer1@foomail.com");
        Basket b = u.getBasket();
        Product p = productRepo.findProductByName("Prod1");
        basketService.addProductToBasket(p, 10.0, u);
        basketRepo.save(b);

        Assertions.assertNotNull(basketRepo.getById(b.getBasketId()));
        Assertions.assertEquals(10.0,b.getProductQuantityMap().get(p));
        Assertions.assertEquals(990.0,p.getQuantityAvailable());
        Assertions.assertEquals(10.0,p.getQuantityBaskets());
        basketService.dropBasket(b);
        Assertions.assertEquals(1000.0,p.getQuantityAvailable());
        Assertions.assertNotNull(basketRepo.getById(b.getBasketId()));
    }

    @Test
    public void deleteBasketFromBasketUserTypeTest() {
        BasketUserType u = userService.getBasketUserTypeByEmail("customer1@foomail.com");
        Basket b = u.getBasket();
        Product p = productRepo.findProductByName("Prod1");
        basketService.addProductToBasket(p, 10.0, u);
        basketRepo.save(b);

        Assertions.assertNotNull(basketRepo.getById(b.getBasketId()));
        Assertions.assertEquals(10.0,b.getProductQuantityMap().get(p));
        Assertions.assertEquals(990.0,p.getQuantityAvailable());
        Assertions.assertEquals(10.0,p.getQuantityBaskets());
        basketService.dropBasket(u);
        Assertions.assertEquals(1000.0,p.getQuantityAvailable());
        Assertions.assertNotNull(basketRepo.getById(b.getBasketId()));
    }

    @Test
    public void retrieveEmptyBasketTest() {
        BasketUserType u = userService.getBasketUserTypeByEmail("customer1@foomail.com");
        Basket b = u.getBasket();
        basketRepo.save(b);

        Assertions.assertEquals(b.getBasketId(), u.getBasket().getBasketId());
        Assertions.assertEquals(0,b.getProductQuantityMap().size());
    }

    @Test
    public void addCorrectlyProductToBasketShopEmployeeTest() {
        Product p1 = productRepo.findAll().get(0);
        Product p2 = productRepo.findAll().get(1);
        Product p3 = productRepo.findAll().get(2);
        BasketUserType u1 = userService.getBasketUserTypeByEmail("shop@employee.com");
        Basket b1 = u1.getBasket();
        basketRepo.save(b1);

        Assertions.assertEquals(b1,u1.getBasket());

        Assertions.assertEquals(0,b1.getProductQuantityMap().size());
        basketService.addProductToBasket(p1, 10.0, u1);
        System.out.println(basketRepo.findAll());
        System.out.println(b1.getProds());
        Assertions.assertEquals(1,b1.getProductQuantityMap().size());
        Assertions.assertTrue(b1.getProductQuantityMap().containsKey(p1));
        Assertions.assertEquals(10.0,b1.getProductQuantityMap().get(p1));
        basketService.addProductToBasket(p2, 12.0, u1);
        basketService.addProductToBasket(p3, 15.0, u1);
        Assertions.assertEquals(3,b1.getProductQuantityMap().size());
        Assertions.assertTrue(b1.getProductQuantityMap().containsKey(p2));
        Assertions.assertTrue(b1.getProductQuantityMap().containsKey(p3));
        Assertions.assertEquals(12.0,b1.getProductQuantityMap().get(p2));
        Assertions.assertEquals(15,b1.getProductQuantityMap().get(p3));

        Assertions.assertEquals(p1.getQuantityAvailable(), 990.0);
        Assertions.assertEquals(p2.getQuantityAvailable(), 88.0);
        Assertions.assertEquals(p3.getQuantityAvailable(), 5.0);

        Assertions.assertEquals(p1.getQuantityBaskets(), 10.0);
        Assertions.assertEquals(p2.getQuantityBaskets(), 12.0);
        Assertions.assertEquals(p3.getQuantityBaskets(), 15.0);
    }

    @Test
    public void addCorrectlyProductToBasketCustomerTest() {
        Product p1 = productRepo.findAll().get(0);
        Product p2 = productRepo.findAll().get(1);
        Product p3 = productRepo.findAll().get(2);
        BasketUserType u1 = userService.getBasketUserTypeByEmail("customer1@foomail.com");
        Basket b1 = u1.getBasket();
        basketRepo.save(b1);

        Assertions.assertEquals(b1,u1.getBasket());

        Assertions.assertEquals(0,b1.getProductQuantityMap().size());
        basketService.addProductToBasket(p1, 10.0, u1);
        System.out.println(basketRepo.findAll());
        System.out.println(b1.getProds());
        Assertions.assertEquals(1,b1.getProductQuantityMap().size());
        Assertions.assertTrue(b1.getProductQuantityMap().containsKey(p1));
        Assertions.assertEquals(10.0,b1.getProductQuantityMap().get(p1));
        basketService.addProductToBasket(p2, 12.0, u1);
        basketService.addProductToBasket(p3, 15.0, u1);
        Assertions.assertEquals(3,b1.getProductQuantityMap().size());
        Assertions.assertTrue(b1.getProductQuantityMap().containsKey(p2));
        Assertions.assertTrue(b1.getProductQuantityMap().containsKey(p3));
        Assertions.assertEquals(12.0,b1.getProductQuantityMap().get(p2));
        Assertions.assertEquals(15,b1.getProductQuantityMap().get(p3));

        Assertions.assertEquals(990.0,p1.getQuantityAvailable());
        Assertions.assertEquals(88.0,p2.getQuantityAvailable());
        Assertions.assertEquals(5.0,p3.getQuantityAvailable());

        Assertions.assertEquals(10.0,p1.getQuantityBaskets());
        Assertions.assertEquals(12.0,p2.getQuantityBaskets());
        Assertions.assertEquals(15.0,p3.getQuantityBaskets());
    }

    @Test
    public void addNotStoredProductToBasketCustomerTest() {
        Product notStored = new Product("Unicorn Powder", "gr", 10000.0, 10.0);
        BasketUserType u1 = userService.getBasketUserTypeByEmail("customer1@foomail.com");
        Basket b = u1.getBasket();

        Assertions.assertThrows(InvalidDataAccessApiUsageException.class, () -> {
            basketService.addProductToBasket(notStored, 10.0, u1);
        });
    }

    @Test
    public void addNotAvailableProductToBasketCustomerTest() {
        Product p = productRepo.findAll().get(0);
        BasketUserType u1 = userService.getBasketUserTypeByEmail("customer1@foomail.com");
        Basket b = u1.getBasket();

        Assertions.assertFalse(basketService.addProductToBasket(p, 100000000000.0, u1));
        Assertions.assertEquals(0,b.getProductQuantityMap().size());
    }

    @Test
    public void addNegativeProductToBasketCustomerTest() {
        Product p = productRepo.findAll().get(0);
        BasketUserType u1 = userService.getBasketUserTypeByEmail("customer1@foomail.com");
        Basket b = u1.getBasket();

        Assertions.assertFalse(basketService.addProductToBasket(p, -1.0, u1));
        Assertions.assertEquals(0,b.getProductQuantityMap().size());
    }

    @Test
    public void addInfiniteProductToBasketCustomerTest() {
        Product p = productRepo.findAll().get(0);
        BasketUserType u1 = userService.getBasketUserTypeByEmail("customer1@foomail.com");
        Basket b = u1.getBasket();

        Assertions.assertFalse(basketService.addProductToBasket(p, Double.POSITIVE_INFINITY, u1));
        Assertions.assertEquals(b.getProductQuantityMap().size(), 0);
    }

    @Test
    public void addNaNProductToBasketCustomerTest() {
        Product p = productRepo.findAll().get(0);
        BasketUserType u1 = userService.getBasketUserTypeByEmail("customer1@foomail.com");
        Basket b = u1.getBasket();

        Assertions.assertFalse(basketService.addProductToBasket(p, Double.NaN, u1));
        Assertions.assertEquals(0,b.getProductQuantityMap().size());
    }

    @Test
    public void addNotStoredProductToBasketShopEmployeeTest() {
        Product notStored = new Product("Unicorn Powder", "gr", 10000.0, 10.0);
        BasketUserType u1 = userService.getBasketUserTypeByEmail("shop@employee.com");
        Basket b = u1.getBasket();

        Assertions.assertThrows(InvalidDataAccessApiUsageException.class, () -> {
            basketService.addProductToBasket(notStored, 10.0, u1);
        });
    }

    @Test
    public void addNotAvailableProductToBasketShopEmployeeTest() {
        Product p = productRepo.findAll().get(0);
        BasketUserType u1 = userService.getBasketUserTypeByEmail("shop@employee.com");
        Basket b = u1.getBasket();

        Assertions.assertFalse(basketService.addProductToBasket(p, 100000000000.0, u1));
        Assertions.assertEquals(0,b.getProductQuantityMap().size());
    }

    @Test
    public void addNegativeProductToBasketShopEmployeeTest() {
        Product p = productRepo.findAll().get(0);
        BasketUserType u1 = userService.getBasketUserTypeByEmail("shop@employee.com");
        Basket b = u1.getBasket();

        Assertions.assertFalse(basketService.addProductToBasket(p, -1.0, u1));
        Assertions.assertEquals(0,b.getProductQuantityMap().size());
    }

    @Test
    public void addInfiniteProductToBasketShopEmployeeTest() {
        Product p = productRepo.findAll().get(0);
        BasketUserType u1 = userService.getBasketUserTypeByEmail("shop@employee.com");
        Basket b = u1.getBasket();

        Assertions.assertFalse(basketService.addProductToBasket(p, Double.POSITIVE_INFINITY, u1));
        Assertions.assertEquals(0,b.getProductQuantityMap().size());
    }

    @Test
    public void addNaNProductToBasketShopEmployeeTest() {
        Product p = productRepo.findAll().get(0);
        BasketUserType u1 = userService.getBasketUserTypeByEmail("shop@employee.com");
        Basket b = u1.getBasket();

        Assertions.assertFalse(basketService.addProductToBasket(p, Double.NaN, u1));
        Assertions.assertEquals(0,b.getProductQuantityMap().size());
    }


}
