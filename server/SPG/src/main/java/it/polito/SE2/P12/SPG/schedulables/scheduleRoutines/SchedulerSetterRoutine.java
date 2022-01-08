package it.polito.SE2.P12.SPG.schedulables.scheduleRoutines;

import it.polito.SE2.P12.SPG.schedulables.Schedulable;
import it.polito.SE2.P12.SPG.service.SchedulerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.time.temporal.TemporalAdjusters;

@Component
public class SchedulerSetterRoutine implements Schedulable {

    private final SchedulerService schedulerService;
    private final MondayMorningRoutine mondayMorningRoutine;
    private final PendingOrdersDetectionRoutine pendingOrdersDetectionRoutine;
    private final SchedulerSetterRoutine schedulerSetterRoutine;
    private final UnRetrievedOrderDetectionRoutine unRetrievedOrderDetectionRoutine;
    private final MondayEveningRoutine mondayEveningRoutine;

    @Autowired
    SchedulerSetterRoutine(SchedulerService schedulerService, @Lazy MondayEveningRoutine mondayEveningRoutine, @Lazy MondayMorningRoutine mondayMorningRoutine, @Lazy PendingOrdersDetectionRoutine pendingOrdersDetectionRoutine, @Lazy SchedulerSetterRoutine schedulerSetterRoutine, @Lazy UnRetrievedOrderDetectionRoutine unRetrievedOrderDetectionRoutine) {
        this.schedulerService = schedulerService;
        this.mondayMorningRoutine = mondayMorningRoutine;
        this.pendingOrdersDetectionRoutine = pendingOrdersDetectionRoutine;
        this.schedulerSetterRoutine = schedulerSetterRoutine;
        this.unRetrievedOrderDetectionRoutine = unRetrievedOrderDetectionRoutine;
        this.mondayEveningRoutine = mondayEveningRoutine;
    }

    @Override
    public void execute() {

        // MONDAY
        //Manage product quantities for the begin of the new week
        schedulerService.addToSchedule(mondayMorningRoutine,
                LocalDate.now(schedulerService.getApplicationClock()).
                        atTime(9, 0).
                        toEpochSecond(ZoneOffset.ofHours(1)));
        schedulerService.addToSchedule(mondayEveningRoutine,
                LocalDate.now(schedulerService.getApplicationClock()).
                        atTime(23, 0).
                        toEpochSecond(ZoneOffset.ofHours(1)));
        // TUESDAY
        //Delete all unplayable orders
        schedulerService.addToSchedule(pendingOrdersDetectionRoutine,
                LocalDate.now(schedulerService.getApplicationClock())
                        .with(TemporalAdjusters.next(DayOfWeek.TUESDAY))
                        .atTime(0, 0).
                        toEpochSecond(ZoneOffset.ofHours(1)));
        // FRIDAY
        //Mark not retrieved orders
        schedulerService.addToSchedule(unRetrievedOrderDetectionRoutine,
                LocalDate.now(schedulerService.getApplicationClock()).
                        with(TemporalAdjusters.next(DayOfWeek.FRIDAY)).
                        atTime(22, 0).
                        toEpochSecond(ZoneOffset.ofHours(1)));
        // NEXT WEEK SCHEDULER SETTER
        schedulerService.addToSchedule(schedulerSetterRoutine,
                LocalDate.now(schedulerService.getApplicationClock()).
                        with(TemporalAdjusters.next(DayOfWeek.MONDAY)).
                        atTime(0, 0).
                        toEpochSecond(ZoneOffset.ofHours(1)));

    }
}
