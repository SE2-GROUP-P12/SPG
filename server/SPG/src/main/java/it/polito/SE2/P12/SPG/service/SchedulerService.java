package it.polito.SE2.P12.SPG.service;

import it.polito.SE2.P12.SPG.entity.Order;
import it.polito.SE2.P12.SPG.repository.OrderRepo;
import it.polito.SE2.P12.SPG.schedulables.MondayMorningSchedule;
import it.polito.SE2.P12.SPG.schedulables.Schedulable;
import it.polito.SE2.P12.SPG.schedulables.TuesdayEveningSchedule;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.*;
import java.time.temporal.TemporalAdjusters;
import java.util.*;
import java.util.Map.Entry;


@Service
public class SchedulerService {

    @Value("${spring.mail.host}")
    private String emailHostAddress;
    private static final long POLLING_RATE = 5000L;
    private static final String ZONE = "Europe/Rome";
    private long iteration = 0L;

    private Clock applicationClock;
    private List<Entry<Schedulable, Long>> schedule;

    SpgProductService spgProductService;
    private OrderRepo orderRepo;
    private SpgOrderService orderService;

    @Autowired
    public SchedulerService(SpgProductService spgProductService, OrderRepo orderRepo, SpgOrderService orderService) {
        applicationClock = Clock.system(ZoneId.of(ZONE));
        schedule = new ArrayList<>();
        this.spgProductService = spgProductService;
        this.orderRepo = orderRepo;
        this.orderService = orderService;
    }

    public void initScheduler() {
        /* MONDAY */
        //1. Set forecast to zero for all product
        addToSchedule(new MondayMorningSchedule(spgProductService, this), LocalDate.now(applicationClock).with(TemporalAdjusters.next(DayOfWeek.MONDAY)).atTime(9, 0).toEpochSecond(ZoneOffset.ofHours(1)));
        /* TUESDAY */
        //1. Delete all unplayable orders
        addToSchedule(new TuesdayEveningSchedule(this.orderRepo, this, this.orderService),
                LocalDate.now(applicationClock)
                        .with(TemporalAdjusters.next(DayOfWeek.TUESDAY))
                        .atTime(0, 1).
                        toEpochSecond(ZoneOffset.ofHours(1)));
        /* FRIDAY */
    }

    @Scheduled(fixedDelay = POLLING_RATE)
    private void poll() {
        if (this.emailHostAddress.equals("127.0.0.1")) {
            System.out.println("TESTING ENV DETECTED: NO SCHEDULE(s) WILL BE ADDED!\n");
            return;
        }
        this.iteration++;
        System.out.println("application time: " + applicationClock.instant().atZone(ZoneId.of(ZONE)) + ", epoch time: " + applicationClock.instant().getEpochSecond() + ", iteration: " + this.iteration);

        Entry<Schedulable, Long> e;
        do {
            e = schedule.stream().
                    min((x, y) -> Math.toIntExact(x.getValue() - y.getValue())).orElse(null);
            if (e != null && e.getValue() <= applicationClock.instant().getEpochSecond()) {
                System.out.println(e.getKey().getClass().getSimpleName() + " executed");
                e.getKey().execute();
                schedule.remove(e);
            }
        } while (e != null && e.getValue() <= applicationClock.instant().getEpochSecond());
    }

    public Boolean addToSchedule(Schedulable s, Long t) {
        if (t < applicationClock.instant().getEpochSecond())
            return false;

        schedule.add(new AbstractMap.SimpleEntry<>(s, t));
        return true;
    }

    public Instant getTime() {
        return this.applicationClock.instant();
    }

    public Clock getClock() {
        return this.applicationClock;
    }


    public Boolean timeTravelAt(Long time) {
        if (applicationClock.instant().isAfter(Instant.ofEpochSecond(time)))
            return false;

        Duration offset = Duration.ofSeconds(time - applicationClock.instant().getEpochSecond());
        applicationClock = Clock.offset(applicationClock, offset).withZone(ZoneId.of(ZONE));
        //poll();
        return true;
    }

}
