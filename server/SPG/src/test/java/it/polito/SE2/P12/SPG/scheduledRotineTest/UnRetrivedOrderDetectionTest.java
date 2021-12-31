package it.polito.SE2.P12.SPG.scheduledRotineTest;


import it.polito.SE2.P12.SPG.entity.Customer;
import it.polito.SE2.P12.SPG.entity.Order;
import it.polito.SE2.P12.SPG.entity.Product;
import it.polito.SE2.P12.SPG.repository.OrderRepo;
import it.polito.SE2.P12.SPG.schedulables.schedule_routines.UnRetrievedOrderDetection_Routine;
import it.polito.SE2.P12.SPG.service.SchedulerService;
import it.polito.SE2.P12.SPG.service.SpgOrderService;
import it.polito.SE2.P12.SPG.service.SpgUserService;
import it.polito.SE2.P12.SPG.utils.DBUtilsService;
import it.polito.SE2.P12.SPG.utils.OrderStatus;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.HashMap;
import java.util.Map;

@SpringBootTest
public class UnRetrivedOrderDetectionTest {
    @Autowired
    private DBUtilsService dbUtilsService;
    @Autowired
    private SpgUserService userService;
    @Autowired
    private SpgOrderService orderService;
    @Autowired
    private OrderRepo orderRepo;
    @Autowired
    private SchedulerService schedulerService;

    @BeforeEach
    public void initContext() {
        dbUtilsService.dropAll();
        dbUtilsService.loadTestingProds();
        //Create customer
        Customer customer = new Customer("name", "surname", "CUST_1234567", "1234567890'",
                "customer@test.com"
                , "password", "address");
        dbUtilsService.saveCustomer(customer);
        //Add product to cart then issue order
        Map<Product, Double> products = new HashMap<>();
        products.put(dbUtilsService.getProd1Object(), 5.00);
        //Paid order
        Order orderPaid = new Order(customer, System.currentTimeMillis(), products, null, "");
        orderPaid.setStatus(OrderStatus.ORDER_STATUS_CONFIRMED); //Check if correct after refactoring (Should be paid)
        dbUtilsService.saveOrder(orderPaid);
        //Unpaid order
        Order orderUnPaid = new Order(customer, System.currentTimeMillis(), products, null, "");
        //Check if correct after refactoring (Should be confirmed)
        dbUtilsService.saveOrder(orderUnPaid);
    }

    @Test
    public void routineExecutionTest() {
        UnRetrievedOrderDetection_Routine routine = new UnRetrievedOrderDetection_Routine(userService, orderService, orderRepo, schedulerService);
        routine.execute();
        Assertions.assertEquals(OrderStatus.ORDER_STATUS_NOT_RETRIEVED, orderRepo.findAll().get(0).getStatus());
        Assertions.assertEquals(OrderStatus.ORDER_STATUS_OPEN, orderRepo.findAll().get(1).getStatus());
    }

}
