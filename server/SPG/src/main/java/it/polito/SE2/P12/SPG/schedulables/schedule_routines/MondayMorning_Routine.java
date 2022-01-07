package it.polito.SE2.P12.SPG.schedulables.schedule_routines;


import it.polito.SE2.P12.SPG.schedulables.Schedulable;
import it.polito.SE2.P12.SPG.service.SchedulerService;
import it.polito.SE2.P12.SPG.service.SpgOrderService;
import it.polito.SE2.P12.SPG.service.SpgProductService;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.time.temporal.TemporalAdjusters;

public class MondayMorning_Routine implements Schedulable {

    private SpgProductService spgProductService;
    private SpgOrderService orderService;

    public MondayMorning_Routine(SpgProductService spgProductService, SpgOrderService orderService) {
        this.spgProductService = spgProductService;
        this.orderService = orderService;
    }

    @Override
    public void execute() {
        orderService.updateConfirmedOrders();
        spgProductService.resetQuantities();
    }
}
