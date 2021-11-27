package it.polito.SE2.P12.SPG.serviceTest;

import it.polito.SE2.P12.SPG.entity.*;
import it.polito.SE2.P12.SPG.interfaceEntity.BasketUserType;
import it.polito.SE2.P12.SPG.repository.*;
import it.polito.SE2.P12.SPG.entity.Basket;
import it.polito.SE2.P12.SPG.entity.Product;
import it.polito.SE2.P12.SPG.repository.BasketRepo;
import it.polito.SE2.P12.SPG.repository.ProductRepo;
import it.polito.SE2.P12.SPG.repository.UserRepo;
import it.polito.SE2.P12.SPG.service.SpgBasketService;
import it.polito.SE2.P12.SPG.service.SpgUserService;
import it.polito.SE2.P12.SPG.utils.DBUtilsService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.*;
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

    @BeforeEach
    public void initContext() {
        dbUtilsService.dropAll();
        //Create some product and then add them to the Customer Cart
        Product prod1 = new Product("Prod1", "Producer1", "KG", 1000.0, 10.50F);
        Product prod2 = new Product("Prod2", "Producer2", "KG", 100.0, 5.50F);
        Product prod3 = new Product("Prod3", "Producer3", "KG", 20.0, 8.00F);
        productRepo.save(prod1);
        productRepo.save(prod2);
        productRepo.save(prod3);
        //Create 2 user to issue some order
        Customer cust1 = new Customer("customer1", "surname1", "ssn_aaaaaaaaaaaa", "123456789", "customer1@foomail.com", "password1223ABC", "address1");
        Customer cust2 = new Customer("customer2", "surname2", "ssn_bbbbbbbbbbbb", "123456789", "customer2@foomail.com", "password1223ABC", "address2");
        customerRepo.save(cust1);
        customerRepo.save(cust2);
    }

    @Test
    public void retrieveEmptyBasket() {
        BasketUserType u = userService.getBasketUserTypeByEmail("customer1@foomail.com");
        Basket b = u.getBasket();

        Assertions.assertEquals(b.getBasketId(), u.getBasket().getBasketId());
        Assertions.assertEquals(b.getProductQuantityMap().size(), 0);
    }

    @Test
    public void addCorrectlyProductToBasket() {
        Product p1 = productRepo.findAll().get(0);
        Product p2 = productRepo.findAll().get(1);
        Product p3 = productRepo.findAll().get(2);
        Customer u1 = (Customer) userService.getUserByEmail("customer1@foomail.com");
        Basket b1 = u1.getBasket();

        Assertions.assertEquals(u1.getBasket(), b1);

        Assertions.assertEquals(b1.getProductQuantityMap().size(), 0);
        basketService.addProductToCart(p1, 10.0, u1);
        Assertions.assertEquals(b1.getProductQuantityMap().size(), 1);
        System.out.println(b1.getProductQuantityMap());
        System.out.println(p1);
        System.out.println((Product) b1.getProductQuantityMap().keySet().toArray()[0]);
        Assertions.assertEquals(((Product) b1.getProductQuantityMap().keySet().toArray()[0]).getProductId(), p1.getProductId());
        Assertions.assertEquals(b1.getProductQuantityMap().get(p1), 10.0);

        basketService.addProductToCart(p2, 12.0, u1);
        basketService.addProductToCart(p3, 15.2, u1);
        Assertions.assertEquals(b1.getProductQuantityMap().size(), 3);
        Assertions.assertEquals(((Product) b1.getProductQuantityMap().keySet().toArray()[1]).getProductId(), p2.getProductId());
        Assertions.assertEquals(((Product) b1.getProductQuantityMap().keySet().toArray()[2]).getProductId(), p3.getProductId());
        Assertions.assertEquals(b1.getProductQuantityMap().get(p2), 12.0);
        Assertions.assertEquals(b1.getProductQuantityMap().get(p3), 15.2);
    }

    @Test
    public void addNotStoredProductToBasket() {
        Product notStored = new Product("Unicorn Powder", "Unicornland", "gr", 10000.0, 10.0);
        BasketUserType u1 = userService.getBasketUserTypeByEmail("customer1@foomail.com");
        Basket b = u1.getBasket();

        Assertions.assertThrows(InvalidDataAccessApiUsageException.class, () -> {
            basketService.addProductToCart(notStored, 10.0, u1);
        });
    }

    @Test
    public void addNotAvailableProductToBasket() {
        Product p = productRepo.findAll().get(0);
        BasketUserType u1 = userService.getBasketUserTypeByEmail("customer1@foomail.com");
        Basket b = u1.getBasket();

        Assertions.assertFalse(basketService.addProductToCart(p, 100000000000.0, u1));
        Assertions.assertEquals(b.getProductQuantityMap().size(), 0);
    }
}
