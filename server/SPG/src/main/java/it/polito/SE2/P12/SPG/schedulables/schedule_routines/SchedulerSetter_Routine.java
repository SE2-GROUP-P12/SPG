package it.polito.SE2.P12.SPG.schedulables.schedule_routines;

import it.polito.SE2.P12.SPG.entity.Order;
import it.polito.SE2.P12.SPG.repository.OrderRepo;
import it.polito.SE2.P12.SPG.schedulables.Schedulable;
import it.polito.SE2.P12.SPG.service.SchedulerService;
import it.polito.SE2.P12.SPG.service.SpgOrderService;
import it.polito.SE2.P12.SPG.service.SpgProductService;
import it.polito.SE2.P12.SPG.service.SpgUserService;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.time.temporal.TemporalAdjusters;

public class SchedulerSetter_Routine implements Schedulable {

    private SchedulerService schedulerService;
    private SpgProductService productService;
    private SpgOrderService orderService;
    private OrderRepo orderRepo;
    private SpgUserService userService;

    public SchedulerSetter_Routine(SchedulerService schedulerService, SpgProductService productService, SpgOrderService orderService, OrderRepo orderRepo, SpgUserService userService) {
        this.schedulerService = schedulerService;
        this.productService = productService;
        this.orderService = orderService;
        this.orderRepo = orderRepo;
        this.userService = userService;
    }

    @Override
    public void execute() {

        // MONDAY
        //Manage product quantities for the begin of the new week
        schedulerService.addToSchedule(new MondayMorning_Routine(productService, orderService),
                LocalDate.now(schedulerService.getClock()).
                        atTime(9, 0).
                        toEpochSecond(ZoneOffset.ofHours(1)));
        // TUESDAY
        //Delete all unplayable orders
        schedulerService.addToSchedule(new PendingOrdersDetection_Routine(this.orderRepo, schedulerService, this.orderService),
                LocalDate.now(schedulerService.getClock())
                        .with(TemporalAdjusters.next(DayOfWeek.TUESDAY))
                        .atTime(0, 1).
                        toEpochSecond(ZoneOffset.ofHours(1)));
        // FRIDAY
        //Mark not retrieved orders
        schedulerService.addToSchedule(new UnRetrievedOrderDetection_Routine(this.userService, this.orderService, this.orderRepo, this.schedulerService),
                LocalDate.now(schedulerService.getClock()).
                        with(TemporalAdjusters.next(DayOfWeek.FRIDAY)).
                        atTime(20, 0).
                        toEpochSecond(ZoneOffset.ofHours(1)));
        // NEXT WEEK SCHEDULER SETTER
        schedulerService.addToSchedule(new SchedulerSetter_Routine(this.schedulerService, this.productService, this.orderService, this.orderRepo, this.userService),
                LocalDate.now(schedulerService.getClock()).
                        with(TemporalAdjusters.next(DayOfWeek.MONDAY)).
                        atTime(0, 1).
                        toEpochSecond(ZoneOffset.ofHours(1)));

    }
}
