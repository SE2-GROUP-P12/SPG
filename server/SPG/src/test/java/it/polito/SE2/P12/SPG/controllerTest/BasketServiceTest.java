package it.polito.SE2.P12.SPG.controllerTest;

import it.polito.SE2.P12.SPG.controller.SpgController;
import it.polito.SE2.P12.SPG.entity.Basket;
import it.polito.SE2.P12.SPG.entity.Product;
import it.polito.SE2.P12.SPG.entity.User;
import it.polito.SE2.P12.SPG.repository.BasketRepo;
import it.polito.SE2.P12.SPG.repository.ProductRepo;
import it.polito.SE2.P12.SPG.repository.UserRepo;
import it.polito.SE2.P12.SPG.security.SecurityConfiguration;
import it.polito.SE2.P12.SPG.service.SpgBasketService;
import org.hibernate.TransientObjectException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@SpringBootTest
public class BasketServiceTest {
    @Autowired
    private SpgController spgController;
    @Autowired
    private BasketRepo basketRepo;
    @Autowired
    private ProductRepo productRepo;
    @Autowired
    private UserRepo userRepo;
    @Autowired
    private SpgBasketService basketService;

    @BeforeEach
    public void initContext() {
        SecurityConfiguration.setTestContext();
        basketRepo.deleteAll();
        productRepo.deleteAll();
        userRepo.deleteAll();
        //Create some product and then add them to the Customer Cart
        Product prod1 = new Product("Prod1", "Producer1", "KG", 1000.0, 10.50F);
        Product prod2 = new Product("Prod2", "Producer2", "KG", 100.0, 5.50F);
        Product prod3 = new Product("Prod3", "Producer3", "KG", 20.0, 8.00F);
        productRepo.save(prod1);
        productRepo.save(prod2);
        productRepo.save(prod3);
        //Create 2 user to issue some order
        User user1 = new User("customer1", "surname1", "ssn_aaaaaaaaaaaa", "", "CUSTOMER", "customer1@foomail.com", "password1223ABC");
        User user2 = new User("customer2", "surname1", "ssn_bbbbbbbbbbbb", "", "CUSTOMER", "customer2@foomail.com", "password1223ABC");
        userRepo.save(user1);
        userRepo.save(user2);
    }

    /*@Test
    public void retrieveEmptyBasket() {
        User u = userRepo.findUserByEmail("customer1@foomail.com");
        Basket b = u.getBasket();

        Assertions.assertEquals(b.getBasketId(), u.getBasket().getBasketId());
        Assertions.assertEquals(b.getProductQuantityMap().size(), 0);
    }

    @Test
    public void addCorrectlyProductToBasket() {
        Product p1 = productRepo.findAll().get(0);
        Product p2 = productRepo.findAll().get(1);
        Product p3 = productRepo.findAll().get(2);
        User u1 = userRepo.findUserByEmail("customer1@foomail.com");
        Basket b1 = u1.getBasket();

        Assertions.assertEquals(u1.getBasket(), b1);

        Assertions.assertEquals(b1.getProductQuantityMap().size(), 0);
        basketService.addProductToCart(p1, 10.0, u1);
        Assertions.assertEquals(b1.getProductQuantityMap().size(), 1);
        Assertions.assertTrue(b1.getProductQuantityMap().containsKey(p1));
        Assertions.assertEquals(b1.getProductQuantityMap().get(p1), 10.0);

        basketService.addProductToCart(p2, 12.0, u1);
        basketService.addProductToCart(p3, 15.2, u1);
        Assertions.assertEquals(b1.getProductQuantityMap().size(), 3);
        Assertions.assertTrue(b1.getProductQuantityMap().containsKey(p2));
        Assertions.assertTrue(b1.getProductQuantityMap().containsKey(p3));
        Assertions.assertEquals(b1.getProductQuantityMap().get(p2), 12.0);
        Assertions.assertEquals(b1.getProductQuantityMap().get(p3), 15.2);
    }

    @Test
    public void addNotStoredProductToBasket() {
        Product notStored = new Product("Unicorn Powder", "Unicornland", "gr", 10000.0, 10.0);
        User u1 = userRepo.findUserByEmail("customer1@foomail.com");
        Basket b = u1.getBasket();

        Assertions.assertThrows(InvalidDataAccessApiUsageException.class, () -> {
            basketService.addProductToCart(notStored, 10.0, u1);
        });
    }

    @Test
    public void addNotAvailableProductToBasket() {
        Product p = productRepo.findAll().get(0);
        User u1 = userRepo.findUserByEmail("customer1@foomail.com");
        Basket b = u1.getBasket();

        basketService.addProductToCart(p, 100000000000.0, u1);
        Assertions.assertEquals(b.getProductQuantityMap().size(), 0);
    }*/
}
