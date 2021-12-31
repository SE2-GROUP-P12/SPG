package it.polito.SE2.P12.SPG.schedulables;

import it.polito.SE2.P12.SPG.entity.Customer;
import it.polito.SE2.P12.SPG.entity.Order;
import it.polito.SE2.P12.SPG.repository.OrderRepo;
import it.polito.SE2.P12.SPG.service.SchedulerService;
import it.polito.SE2.P12.SPG.service.SpgOrderService;
import it.polito.SE2.P12.SPG.service.SpgUserService;
import it.polito.SE2.P12.SPG.utils.OrderStatus;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.time.temporal.TemporalAdjusters;
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
        List<Order> orderList = orderRepo.findAll().stream().filter(order -> order.getStatus().equals(OrderStatus.ORDER_STATUS_CONFIRMED)
                && order.getDeliveryAddress().equals("")).toList();
        //for each order increase missed pick up and update state
        for (Order o : orderList) {
            //Update State
            orderService.setOrderStatus(o.getOrderId(), OrderStatus.ORDER_STATUS_NOT_RETRIEVED);
            //Add missed pick up and check if an alert has to be generated
            Customer tmpCustomer = (Customer) o.getCust();
            //if missed pick up is 3 or 4 sent a mail (increment upper-bounded at 5 then user is blocked)
            if (tmpCustomer.incrementMissedPickUp() > 2)
                userService.sentWarningPickUpAmountMail(tmpCustomer.getEmail());
        }
        schedulerService.addToSchedule(this, LocalDate.now(schedulerService.getClock()).with(TemporalAdjusters.next(DayOfWeek.FRIDAY)).atTime(22, 0).toEpochSecond(ZoneOffset.ofHours(1)));
    }
}
