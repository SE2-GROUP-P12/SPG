package it.polito.SE2.P12.SPG.schedulables;

import it.polito.SE2.P12.SPG.entity.Order;
import it.polito.SE2.P12.SPG.repository.OrderRepo;
import it.polito.SE2.P12.SPG.service.SchedulerService;
import it.polito.SE2.P12.SPG.service.SpgOrderService;
import it.polito.SE2.P12.SPG.utils.OrderStatus;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.time.temporal.TemporalAdjusters;
import java.util.List;

public class TuesdayEveningSchedule implements Schedulable {
    private OrderRepo orderRepo;
    private SpgOrderService orderService;
    private SchedulerService schedulerService;


    public TuesdayEveningSchedule(OrderRepo orderRepo, SchedulerService schedulerService, SpgOrderService orderService) {
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
            orderService.setOrderStatus(o.getOrderId(), OrderStatus.ORDER_STATUS_CANCELLED);
        }
        schedulerService.addToSchedule(new TuesdayEveningSchedule(orderRepo, schedulerService, orderService),
                LocalDate.now(schedulerService.getClock())
                        .with(TemporalAdjusters.next(DayOfWeek.TUESDAY))
                        .atTime(0, 1).
                        toEpochSecond(ZoneOffset.ofHours(1)));
    }
}
