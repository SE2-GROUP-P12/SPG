package it.polito.SE2.P12.SPG.schedulables.schedule_routines;


import it.polito.SE2.P12.SPG.entity.Product;
import it.polito.SE2.P12.SPG.schedulables.Schedulable;
import it.polito.SE2.P12.SPG.service.SchedulerService;
import it.polito.SE2.P12.SPG.service.SpgOrderService;
import it.polito.SE2.P12.SPG.service.SpgProductService;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.time.temporal.TemporalAdjusters;
import java.util.List;

public class MondayMorningSchedule implements Schedulable {

    private SpgProductService spgProductService;
    private SchedulerService schedulerService;
    private SpgOrderService orderService;

    public MondayMorningSchedule(SpgProductService spgProductService, SchedulerService schedulerService, SpgOrderService orderService) {
        this.spgProductService = spgProductService;
        this.schedulerService = schedulerService;
        this.orderService = orderService;

    }



    @Override
    public void execute() {
        orderService.updateConfirmedOrders();
        spgProductService.resetQuantities();
        schedulerService.addToSchedule(new MondayMorningSchedule(spgProductService, schedulerService,orderService),
                LocalDate.now(schedulerService.getClock()).with(TemporalAdjusters.next(DayOfWeek.MONDAY)).atTime(9, 0).toEpochSecond(ZoneOffset.ofHours(1)));
    }
}
