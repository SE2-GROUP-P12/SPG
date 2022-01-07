package it.polito.SE2.P12.SPG.schedulables.schedule_routines;

import it.polito.SE2.P12.SPG.entity.Order;
import it.polito.SE2.P12.SPG.repository.OrderRepo;
import it.polito.SE2.P12.SPG.schedulables.Schedulable;
import it.polito.SE2.P12.SPG.service.SchedulerService;
import it.polito.SE2.P12.SPG.service.SpgOrderService;
import it.polito.SE2.P12.SPG.utils.OrderStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.time.temporal.TemporalAdjusters;
import java.util.List;

@Component
public class PendingOrdersDetection_Routine implements Schedulable {
    private SpgOrderService orderService;

    @Autowired
    public PendingOrdersDetection_Routine(SpgOrderService orderService) {
        this.orderService = orderService;
    }


    @Override
    public void execute() {
        orderService.deleteUnpayableOrders();
    }
}
