package it.polito.SE2.P12.SPG.schedulables;


import it.polito.SE2.P12.SPG.entity.Product;
import it.polito.SE2.P12.SPG.service.SchedulerService;
import it.polito.SE2.P12.SPG.service.SpgProductService;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAdjusters;
import java.time.temporal.TemporalUnit;
import java.util.List;

public class MondayMorningSchedule implements Schedulable {

    private SpgProductService spgProductService;
    private SchedulerService schedulerService;

    public MondayMorningSchedule(SpgProductService spgProductService, SchedulerService schedulerService) {
        this.spgProductService = spgProductService;
        this.schedulerService = schedulerService;
    }

    @Override
    public void execute() {
        List<Product> prods = spgProductService.getAllProduct();
        for (Product p : prods) {
            spgProductService.setForecast(p.getProductId(), 0.0);
        }
        schedulerService.addToSchedule(new MondayMorningSchedule(spgProductService, schedulerService), LocalDate.now(schedulerService.getClock()).with(TemporalAdjusters.next(DayOfWeek.MONDAY)).atTime(9, 0).toEpochSecond(ZoneOffset.ofHours(1)));
    }
}
