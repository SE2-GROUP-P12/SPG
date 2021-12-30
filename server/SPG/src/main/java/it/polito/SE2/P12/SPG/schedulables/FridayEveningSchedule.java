package it.polito.SE2.P12.SPG.schedulables;

import it.polito.SE2.P12.SPG.entity.Order;
import it.polito.SE2.P12.SPG.repository.OrderRepo;
import it.polito.SE2.P12.SPG.service.SchedulerService;
import it.polito.SE2.P12.SPG.service.SpgOrderService;
import it.polito.SE2.P12.SPG.service.SpgUserService;
import it.polito.SE2.P12.SPG.utils.OrderStatus;

import java.util.List;

public class FridayEveningSchedule implements Schedulable {

    private SpgOrderService orderService;
    private SpgUserService userService;
    private SchedulerService schedulerService;
    private OrderRepo orderRepo;

    public FridayEveningSchedule(SpgUserService userService, SpgOrderService orderService, OrderRepo orderRepo, SchedulerService schedulerService) {
        this.schedulerService = schedulerService;
        this.orderService = orderService;
        this.userService = userService;
        this.orderRepo = orderRepo;
    }

    @Override
    public void execute() {
        //Get not retrieved orders -> orders are paid and delivery address != "" (if "" is assumed as delivery)
        List<Order> orderList = orderRepo.findAll().stream().filter(order -> order.getStatus().equals(OrderStatus.ORDER_STATUS_PAID)
                && !order.getDeliveryAddress().equals("")).toList();
        //for each order increase missed pick up and update state
        for (Order o : orderList) {
            //do something
            //TODO: merge with user-story 42-43 to handle un retrieved orders
        }

    }
}
