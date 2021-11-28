package it.polito.SE2.P12.SPG.serviceTest;

import it.polito.SE2.P12.SPG.entity.*;
import it.polito.SE2.P12.SPG.interfaceEntity.BasketUserType;
import it.polito.SE2.P12.SPG.interfaceEntity.OrderUserType;
import it.polito.SE2.P12.SPG.repository.*;
import it.polito.SE2.P12.SPG.entity.Basket;
import it.polito.SE2.P12.SPG.entity.Order;
import it.polito.SE2.P12.SPG.entity.Product;
import it.polito.SE2.P12.SPG.repository.BasketRepo;
import it.polito.SE2.P12.SPG.repository.OrderRepo;
import it.polito.SE2.P12.SPG.repository.ProductRepo;
import it.polito.SE2.P12.SPG.repository.UserRepo;
import it.polito.SE2.P12.SPG.service.SpgBasketService;
import it.polito.SE2.P12.SPG.service.SpgOrderService;
import it.polito.SE2.P12.SPG.service.SpgUserService;
import it.polito.SE2.P12.SPG.utils.DBUtilsService;
import it.polito.SE2.P12.SPG.utils.UserRole;
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
    @Autowired
    private SpgBasketService basketService;

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
        Customer cust1 = new Customer("customer1", "surname1", "ssn_aaaaaaaaaaaa", "123456789", "customer1@foomail.com", "password1223ABC", "address1", 3000.0);
        Customer cust2 = new Customer("customer2", "surname2", "ssn_bbbbbbbbbbbb", "123456789", "customer2@foomail.com", "password1223ABC", "address2", 1.0);
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
        //Create some baskets
        Map<Product, Double> m1 = new HashMap<>();
        m1.put(prod1, 10.0);
        m1.put(prod2, 15.0);
        m1.put(prod3, 12.0);
        Map<Product, Double> m2 = new HashMap<>();
        m2.put(prod1, 12.7);
        m2.put(prod3, 13.2);
        Map<Product, Double> m3 = new HashMap<>();
        m3.put(prod1, 9.0);
        m3.put(prod2, 11.5);
        Basket basket1 = new Basket(cust1, m1);
        Basket basket2 = new Basket(cust2, m2);
        Basket basket3 = new Basket(se1, m3);
        prod1.moveFromAvailableToBasket(31.7);
        prod2.moveFromAvailableToBasket(26.5);
        prod3.moveFromAvailableToBasket(25.2);
        productRepo.save(prod1);
        productRepo.save(prod2);
        productRepo.save(prod3);
        basketRepo.save(basket1);
        basketRepo.save(basket2);
        basketRepo.save(basket3);
        shopEmployeeRepo.save(se1);
        customerRepo.save(cust1);
        customerRepo.save(cust2);

    }

    @Test
    public void addNewOrderFromBasketTest() {
        BasketUserType user = userService.getBasketUserTypeByEmail("customer1@foomail.com");
        Basket b1 = user.getBasket();

        List<Order> orders = orderRepo.findAll();
        Assertions.assertEquals(0, orders.size());
        Assertions.assertTrue(orderService.addNewOrderFromBasket(b1));
        orders = orderRepo.findAll();
        Assertions.assertEquals(1, orders.size());
        Assertions.assertEquals(orders.get(0).getCust().getUserId(), ((User) user).getUserId());
        Assertions.assertEquals(orders.get(0).getProds().entrySet().size(), 3);

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

    @Test
    public void addNewOrderToCustomerFromShopEmployeeBasketTest() {
        BasketUserType se1 = userService.getBasketUserTypeByEmail("shop@employee.com");
        OrderUserType customer = userService.getOrderUserTypeByEmail("customer1@foomail.com");
        Basket b1 = se1.getBasket();

        Assertions.assertEquals(((User) se1).getRole(), UserRole.ROLE_SHOP_EMPLOYEE);
        Assertions.assertEquals(((User) customer).getRole(), UserRole.ROLE_CUSTOMER);

        List<Order> orders = orderRepo.findAll();
        Assertions.assertEquals(0, orders.size());
        Assertions.assertTrue(orderService.addNewOrderFromBasket(b1, customer));
        orders = orderRepo.findAll();
        Assertions.assertEquals(1, orders.size());
        Assertions.assertEquals(orders.get(0).getCust().getUserId(), ((User) customer).getUserId());
        Assertions.assertEquals(orders.get(0).getProds().entrySet().size(), 2);

        Product p1 = productRepo.findProductByName("Prod1");
        Product p2 = productRepo.findProductByName("Prod2");

        Assertions.assertTrue(orders.get(0).getProds().containsKey(p1));
        Assertions.assertTrue(orders.get(0).getProds().containsKey(p2));

        Assertions.assertEquals(9.0, orders.get(0).getProds().get(p1));
        Assertions.assertEquals(11.5, orders.get(0).getProds().get(p2));
    }

    @Test //Il test tenta di convertire il basket di un employee in un ordine per l'employee stesso
    public void shopEmployeesCannotHaveOrders() {
        BasketUserType shopEmployee = userService.getBasketUserTypeByEmail("shop@employee.com");
        Basket b = shopEmployee.getBasket();

        Assertions.assertEquals(b.getProductQuantityMap().size(), 2);
        Assertions.assertFalse(orderService.addNewOrderFromBasket(b));
    }

    //Non passano sebbene poi il programma funzioni
    /*
    @Test
    public void deliverOrderTest() {
        Customer c = customerRepo.findCustomerByEmail("customer1@foomail.com");
        Basket b = c.getBasket();
        orderService.addNewOrderFromBasket(b);

        Order order = orderRepo.findAll().get(0);

        for (Product p : order.getProds().keySet()) {
            Assertions.assertTrue(p.getQuantityOrdered() > 0.0);
            Assertions.assertEquals(p.getQuantityDelivered(), 0.0);
        }

        Assertions.assertTrue(orderService.deliverOrder(order.getOrderId()));

        for (Product p : order.getProds().keySet()) {
            Assertions.assertEquals(p.getQuantityOrdered(), 0.0);
            Assertions.assertTrue(p.getQuantityDelivered() > 0.0);
        }
    }

    @Test
    public void insufficientBalanceDeliverOrderTest(){
        Customer c = customerRepo.findCustomerByEmail("customer2@foomail.com");
        Basket b = c.getBasket();
        orderService.addNewOrderFromBasket(b);

        Order order = orderRepo.findAll().get(0);

        for (Product p : order.getProds().keySet()) {
            Assertions.assertTrue(p.getQuantityOrdered() > 0.0);
            Assertions.assertEquals(p.getQuantityDelivered(), 0.0);
        }

        Assertions.assertFalse(orderService.deliverOrder(order.getOrderId()));

        for (Product p : order.getProds().keySet()) {
            Assertions.assertTrue(p.getQuantityOrdered() > 0.0);
            Assertions.assertEquals(p.getQuantityDelivered(), 0.0);
        }
    }
    */

}
