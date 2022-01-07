package it.polito.SE2.P12.SPG.schedulables.schedule_routines;

import it.polito.SE2.P12.SPG.entity.Order;
import it.polito.SE2.P12.SPG.repository.OrderRepo;
import it.polito.SE2.P12.SPG.schedulables.Schedulable;
import it.polito.SE2.P12.SPG.service.SchedulerService;
import it.polito.SE2.P12.SPG.service.SpgOrderService;
import it.polito.SE2.P12.SPG.service.SpgProductService;
import it.polito.SE2.P12.SPG.service.SpgUserService;
import org.checkerframework.checker.units.qual.C;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.time.temporal.TemporalAdjusters;

@Component
public class SchedulerSetter_Routine implements Schedulable {

    private SchedulerService schedulerService;
    private MondayMorning_Routine mondayMorning_routine;
    private PendingOrdersDetection_Routine pendingOrdersDetection_routine;
    private SchedulerSetter_Routine schedulerSetter_routine;
    private UnRetrievedOrderDetection_Routine unRetrievedOrderDetection_routine;

    @Autowired
    SchedulerSetter_Routine(SchedulerService schedulerService, @Lazy MondayMorning_Routine mondayMorning_routine, @Lazy PendingOrdersDetection_Routine pendingOrdersDetection_routine, @Lazy SchedulerSetter_Routine schedulerSetter_routine, @Lazy UnRetrievedOrderDetection_Routine unRetrievedOrderDetection_routine) {
        this.schedulerService = schedulerService;
        this.mondayMorning_routine = mondayMorning_routine;
        this.pendingOrdersDetection_routine = pendingOrdersDetection_routine;
        this.schedulerSetter_routine = schedulerSetter_routine;
        this.unRetrievedOrderDetection_routine = unRetrievedOrderDetection_routine;
    }

    @Override
    public void execute() {

        // MONDAY
        //Manage product quantities for the begin of the new week
        schedulerService.addToSchedule(mondayMorning_routine,
                LocalDate.now(schedulerService.getClock()).
                        atTime(9, 0).
                        toEpochSecond(ZoneOffset.ofHours(1)));
        // TUESDAY
        //Delete all unplayable orders
        schedulerService.addToSchedule(pendingOrdersDetection_routine,
                LocalDate.now(schedulerService.getClock())
                        .with(TemporalAdjusters.next(DayOfWeek.TUESDAY))
                        .atTime(0, 0).
                        toEpochSecond(ZoneOffset.ofHours(1)));
        // FRIDAY
        //Mark not retrieved orders
        schedulerService.addToSchedule(unRetrievedOrderDetection_routine,
                LocalDate.now(schedulerService.getClock()).
                        with(TemporalAdjusters.next(DayOfWeek.FRIDAY)).
                        atTime(22, 0).
                        toEpochSecond(ZoneOffset.ofHours(1)));
        // NEXT WEEK SCHEDULER SETTER
        schedulerService.addToSchedule(schedulerSetter_routine,
                LocalDate.now(schedulerService.getClock()).
                        with(TemporalAdjusters.next(DayOfWeek.MONDAY)).
                        atTime(0, 0).
                        toEpochSecond(ZoneOffset.ofHours(1)));

    }
}
