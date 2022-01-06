package it.polito.SE2.P12.SPG.schedulables.schedule_routines;

import it.polito.SE2.P12.SPG.schedulables.Schedulable;
import it.polito.SE2.P12.SPG.service.SchedulerService;
import it.polito.SE2.P12.SPG.service.SpgProductService;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.time.temporal.TemporalAdjusters;

public class SundayEveningSchedule implements Schedulable {

    SchedulerService schedulerService;
    SpgProductService productService;

    public SundayEveningSchedule(SchedulerService schedulerService, SpgProductService productService) {
        this.schedulerService = schedulerService;
        this.productService = productService;
    }

    @Override
    public void execute() {
        productService.shiftForecasts();

        schedulerService.addToSchedule(new SundayEveningSchedule(this.schedulerService, this.productService),
                LocalDate.now(schedulerService.getClock()).with(TemporalAdjusters.next(DayOfWeek.SUNDAY)).atTime(23, 0).toEpochSecond(ZoneOffset.ofHours(1)));
    }
}
