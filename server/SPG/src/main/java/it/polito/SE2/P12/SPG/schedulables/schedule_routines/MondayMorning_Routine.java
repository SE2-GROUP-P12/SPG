package it.polito.SE2.P12.SPG.schedulables.schedule_routines;


import it.polito.SE2.P12.SPG.schedulables.Schedulable;
import it.polito.SE2.P12.SPG.service.SchedulerService;
import it.polito.SE2.P12.SPG.service.SpgOrderService;
import it.polito.SE2.P12.SPG.service.SpgProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.time.temporal.TemporalAdjusters;

@Component
public class MondayMorning_Routine implements Schedulable {

    private SpgProductService productService;
    private SpgOrderService orderService;

    @Autowired
    public MondayMorning_Routine(SpgProductService spgProductService, SpgOrderService orderService) {
        this.productService = spgProductService;
        this.orderService = orderService;
    }

    @Override
    public void execute() {
        orderService.updateConfirmedOrders();
        productService.resetQuantities();
    }
}
