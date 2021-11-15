package it.polito.SE2.P12.SPG.controllerTest;

import it.polito.SE2.P12.SPG.controller.SpgController;
import it.polito.SE2.P12.SPG.entity.Basket;
import it.polito.SE2.P12.SPG.entity.Product;
import it.polito.SE2.P12.SPG.entity.User;
import it.polito.SE2.P12.SPG.repository.BasketRepo;
import it.polito.SE2.P12.SPG.repository.ProductRepo;
import it.polito.SE2.P12.SPG.repository.UserRepo;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;

@SpringBootTest
public class BasketControllerTest {
    @Autowired
    private SpgController spgController;
    @Autowired
    private BasketRepo basketRepo;
    @Autowired
    private ProductRepo productRepo;
    @Autowired
    private UserRepo userRepo;

    @BeforeEach
    public void initContext(){
        productRepo.deleteAll();
        basketRepo.deleteAll();
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
/*
    @Test
    public void addCorrectlyProductToBasket(){
        //Get all the available product
        List<Product> productInventory = spgController.getAllProduct().getBody();
        //Initializer an empty user basket
        //get the user who issued that order then add some product
        User customer1 = userRepo.findUserByEmail("customer1@foomail.com");
        ResponseEntity response = spgController.addToBasket(productInventory.get(0).getProductId(), customer1.getUserId(),5.0);
        Assertions.assertEquals(response.getStatusCode(), HttpStatus.OK);
        ResponseEntity<List<Product>> response1 = spgController.getCart(customer1.getUserId());
        Assertions.assertEquals(response1.getStatusCode(),HttpStatus.OK);
        Assertions.assertEquals(response1.getBody().size(), 1);
        Assertions.assertEquals(response1.getBody().get(0).getName(), "Prod1");

    }

 */

}
