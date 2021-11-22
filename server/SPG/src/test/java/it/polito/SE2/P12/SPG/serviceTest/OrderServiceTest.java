package it.polito.SE2.P12.SPG.serviceTest;

import it.polito.SE2.P12.SPG.controller.SpgController;
import it.polito.SE2.P12.SPG.entity.Basket;
import it.polito.SE2.P12.SPG.entity.Order;
import it.polito.SE2.P12.SPG.entity.Product;
import it.polito.SE2.P12.SPG.entity.User;
import it.polito.SE2.P12.SPG.repository.BasketRepo;
import it.polito.SE2.P12.SPG.repository.OrderRepo;
import it.polito.SE2.P12.SPG.repository.ProductRepo;
import it.polito.SE2.P12.SPG.repository.UserRepo;
import it.polito.SE2.P12.SPG.security.SecurityConfiguration;
import it.polito.SE2.P12.SPG.service.SpgBasketService;
import it.polito.SE2.P12.SPG.service.SpgOrderService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.junit.jupiter.api.Assertions;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SpringBootTest
public class OrderServiceTest {
    @Autowired
    private SpgController spgController;
    @Autowired
    private BasketRepo basketRepo;
    @Autowired
    private ProductRepo productRepo;
    @Autowired
    private UserRepo userRepo;
    @Autowired
    private OrderRepo orderRepo;
    @Autowired
    private SpgOrderService orderService;

    @BeforeEach
    public void initContext() {
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
        //Create some baskets
        Map<Product, Double> m1 = new HashMap<>();
        m1.put(prod1, 10.0);
        m1.put(prod2, 15.0);
        m1.put(prod3, 12.0);
        Map<Product, Double> m2 = new HashMap<>();
        m2.put(prod1, 12.7);
        m2.put(prod3, 13.2);
        Basket basket1 = new Basket(user1, m1);
        Basket basket2 = new Basket(user2, m2);
        basketRepo.save(basket1);
        basketRepo.save(basket2);
    }

    @Test
    public void addNewOrderFromBasketTest() {
        User u1 = userRepo.findUserByEmail("customer1@foomail.com");
        Basket b1 = u1.getBasket();

        List<Order> orders = orderRepo.findAll();
        Assertions.assertEquals(0, orders.size());
        Assertions.assertTrue(orderService.addNewOrderFromBasket(b1));
        orders = orderRepo.findAll();
        Assertions.assertEquals(1, orders.size());
        Assertions.assertEquals(orders.get(0).getCust(),u1);
        Assertions.assertEquals(orders.get(0).getProds().entrySet().size(),3);

        Product p1 = productRepo.findProductByName("Prod1");
        Product p2 = productRepo.findProductByName("Prod2");
        Product p3 = productRepo.findProductByName("Prod3");

        Assertions.assertTrue(orders.get(0).getProds().containsKey(p1));
        Assertions.assertTrue(orders.get(0).getProds().containsKey(p2));
        Assertions.assertTrue(orders.get(0).getProds().containsKey(p3));

        Assertions.assertEquals(10.0, orders.get(0).getProds().get(p1));
        Assertions.assertEquals(15.0, orders.get(0).getProds().get(p2));
        Assertions.assertEquals(12.0, orders.get(0).getProds().get(p3));
    }
}
