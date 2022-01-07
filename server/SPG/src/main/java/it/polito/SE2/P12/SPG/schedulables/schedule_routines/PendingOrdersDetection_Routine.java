package it.polito.SE2.P12.SPG.schedulables.schedule_routines;

import it.polito.SE2.P12.SPG.entity.Order;
import it.polito.SE2.P12.SPG.repository.OrderRepo;
import it.polito.SE2.P12.SPG.schedulables.Schedulable;
import it.polito.SE2.P12.SPG.service.SchedulerService;
import it.polito.SE2.P12.SPG.service.SpgOrderService;
import it.polito.SE2.P12.SPG.utils.OrderStatus;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.time.temporal.TemporalAdjusters;
import java.util.List;

public class PendingOrdersDetection_Routine implements Schedulable {
    private OrderRepo orderRepo;
    private SpgOrderService orderService;
    private SchedulerService schedulerService;


    public PendingOrdersDetection_Routine(OrderRepo orderRepo, SchedulerService schedulerService, SpgOrderService orderService) {
        this.schedulerService = schedulerService;
        this.orderRepo = orderRepo;
        this.orderService = orderService;
    }


    @Override
    public void execute() {
        //Get not payable orders -> orders are in open status
        List<Order> orderList = orderRepo.findAll().stream()
                .filter(order -> order.getStatus().equals(OrderStatus.ORDER_STATUS_OPEN))
                .toList();
        //for each order set cancelled status
        for (Order o : orderList) {
            orderService.setOrderStatus(o.getOrderId(), OrderStatus.ORDER_STATUS_CANCELLED, schedulerService.getTime());
        }
    }
}
