package it.polito.SE2.P12.SPG.service;

import it.polito.SE2.P12.SPG.schedulables.scheduleRoutines.*;
import it.polito.SE2.P12.SPG.schedulables.Schedulable;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.util.ClassUtils;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;
import java.util.*;
import java.util.Map.Entry;


@Service
@Data
public class SchedulerService {

    @Value("${scheduler.print-poll}")
    private boolean pollPrint;
    @Value("${scheduler.print-schedule-execution}")
    private boolean scheduleExecutionPrint;
    @Value("${scheduler.active}")
    private boolean active;
    private static final long POLLING_RATE = 5000L;
    private static final String ZONE = "Europe/Rome";
    private long iteration = 0L;
    private boolean isPolling = false;
    private boolean timeTravelling = false;

    private Clock applicationClock;
    private List<Entry<Schedulable, Long>> schedule;

    private MondayMorningRoutine mondayMorningRoutine;
    private PendingOrdersDetectionRoutine pendingOrdersDetectionRoutine;
    private SchedulerSetterRoutine schedulerSetterRoutine;
    private UnRetrievedOrderDetectionRoutine unRetrievedOrderDetectionRoutine;
    private MondayEveningRoutine mondayEveningRoutine;

    @Autowired
    public SchedulerService(@Lazy MondayMorningRoutine mondayMorningRoutine, @Lazy MondayEveningRoutine mondayEveningRoutine, @Lazy PendingOrdersDetectionRoutine pendingOrdersDetectionRoutine, @Lazy SchedulerSetterRoutine schedulerSetterRoutine, @Lazy UnRetrievedOrderDetectionRoutine unRetrievedOrderDetectionRoutine) {
        applicationClock = Clock.system(ZoneId.of(ZONE));
        schedule = new ArrayList<>();

        this.mondayMorningRoutine = mondayMorningRoutine;
        this.pendingOrdersDetectionRoutine = pendingOrdersDetectionRoutine;
        this.schedulerSetterRoutine = schedulerSetterRoutine;
        this.unRetrievedOrderDetectionRoutine = unRetrievedOrderDetectionRoutine;
        this.mondayEveningRoutine = mondayEveningRoutine;
    }

    public void initScheduler() {
        if (!this.active) {
            System.out.println("TESTING ENV DETECTED: NO SCHEDULE(s) WILL BE ADDED!\n");
            return;
        }
        LocalDateTime rn = LocalDateTime.now(applicationClock);
        //switch case volutamente senza break, di modo che una volta entrato in un case, vengano aggiunti anche quelli
        //dei giorni successivi
        switch (rn.getDayOfWeek()) {
            case MONDAY:
                //Manage product quantities for the begin of the new week
                if (rn.getHour() < 9) {
                    addToSchedule(mondayMorningRoutine,
                            rn.withHour(9).withMinute(0).toEpochSecond(ZoneOffset.ofHours(1)));
                    System.out.println("MondayMorningRoutine added");
                }
                if (rn.getHour() < 23) {
                    addToSchedule(mondayEveningRoutine,
                            rn.withHour(23).withMinute(0).toEpochSecond(ZoneOffset.ofHours(1)));
                    System.out.println("MondayEveningRoutine added");

                }
            case TUESDAY:
                //Delete all unplayable orders
                if (rn.getDayOfWeek() != DayOfWeek.TUESDAY) {
                    addToSchedule(pendingOrdersDetectionRoutine,
                            rn.with(TemporalAdjusters.next(DayOfWeek.TUESDAY)).withHour(0).withMinute(0).toEpochSecond(ZoneOffset.of(ZONE)));
                    //System.out.println("PendingOrdersDetection_Routine added");
                }
            case WEDNESDAY:
                //Wednesday schedule not set
            case THURSDAY:
                //Thursday schedule not set
            case FRIDAY:
                //Mark not retrieved orders
                if (((rn.getDayOfWeek() == DayOfWeek.FRIDAY) && (rn.getHour() < 20)) || (rn.getDayOfWeek() != DayOfWeek.FRIDAY)) {
                    addToSchedule(unRetrievedOrderDetectionRoutine,
                            rn.with(TemporalAdjusters.nextOrSame(DayOfWeek.FRIDAY)).withHour(22).withMinute(0).toEpochSecond(ZoneOffset.ofHours(1)));
                    System.out.println("UnretrievedOrderDetection_Routine added");
                }
            case SATURDAY:
                //Saturday schedule not set
            case SUNDAY:
                //Set schedule for the next week
                addToSchedule(schedulerSetterRoutine,
                        rn.with(TemporalAdjusters.next(DayOfWeek.MONDAY)).withHour(0).withMinute(0).toEpochSecond(ZoneOffset.ofHours(1)));
                System.out.println("SchedulerSetter_Routine added");

        }
        /*
        // MONDAY
        //1. Manage product quantities for the begin of the new week
        addToSchedule(new MondayMorning_Routine(productService, this, orderService), LocalDate.now(applicationClock).with(TemporalAdjusters.next(DayOfWeek.MONDAY)).atTime(9, 0).toEpochSecond(ZoneOffset.ofHours(1)));
        // TUESDAY
        //1. Delete all unplayable orders
        addToSchedule(new PendingOrdersDetection_Routine(this.orderRepo, this, this.orderService),
                LocalDate.now(applicationClock)
                        .with(TemporalAdjusters.next(DayOfWeek.TUESDAY))
                        .atTime(0, 1).
                        toEpochSecond(ZoneOffset.ofHours(1)));
        // FRIDAY
        //1. mark not retrieved orders
        addToSchedule(new UnRetrievedOrderDetection_Routine(this.userService, this.orderService, this.orderRepo, this),
                LocalDate.now(this.getClock()).with(TemporalAdjusters.next(DayOfWeek.FRIDAY)).atTime(20, 0).toEpochSecond(ZoneOffset.ofHours(1)));
        */
    }

    @Scheduled(fixedDelay = POLLING_RATE)
    public void poll() {
        if (!this.active || this.timeTravelling) {
            return;
        }

        //Set semaphore variable
        isPolling = true;

        this.iteration++;
        if (this.pollPrint)
            System.out.println("application time: " + applicationClock.instant().atZone(ZoneId.of(ZONE)) + ", epoch time: " + applicationClock.instant().getEpochSecond() + ", iteration: " + this.iteration);

        Entry<Schedulable, Long> e;
        do {
            e = schedule.stream().
                    min((x, y) -> Math.toIntExact(x.getValue() - y.getValue())).orElse(null);
            if (e != null && e.getValue() <= applicationClock.instant().getEpochSecond()) {
                e.getKey().execute();
                if (this.scheduleExecutionPrint)
                    System.out.println(ClassUtils.getUserClass(e.getKey().getClass()).getSimpleName() + " executed - End execution instant: " + applicationClock.instant().getEpochSecond() + " - " + LocalDateTime.now(applicationClock).format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss")));
                schedule.remove(e);
            }
        } while (e != null && e.getValue() <= applicationClock.instant().getEpochSecond());

        //Unset semaphore variable
        isPolling = false;
    }

    public Boolean addToSchedule(Schedulable s, Long t) {
        if (t < applicationClock.instant().getEpochSecond())
            return false;

        schedule.add(new AbstractMap.SimpleEntry<>(s, t));
        return true;
    }

    public Long getEpochTime() {
        return this.applicationClock.instant().getEpochSecond();
    }

    public Instant getTime() {
        return this.applicationClock.instant();
    }

    public Boolean timeTravelAt(Long timeDestination) {
        //Cannot time travel backwards. If so, return
        if (applicationClock.instant().isAfter(Instant.ofEpochSecond(timeDestination)))
            return false;

        //Wait for the actual polling to finish
        while (isPolling)
            System.out.println("Polling Conflict");
        System.out.println("End Conflict");


        //Set semaphore variable
        timeTravelling = true;

        Entry<Schedulable, Long> e;
        do {
            //fetch the routine that must be executed first
            e = schedule.stream().
                    min((x, y) -> Math.toIntExact(x.getValue() - y.getValue())).orElse(null);
            //if found, and has execution time lower than the final time destination
            if (e != null && e.getValue() <= timeDestination) {
                //Jump to the execution time, then execute
                Duration offset = Duration.ofSeconds(e.getValue() - applicationClock.instant().getEpochSecond());
                applicationClock = Clock.offset(applicationClock, offset).withZone(ZoneId.of(ZONE));
                e.getKey().execute();
                if (this.scheduleExecutionPrint)
                    System.out.println(ClassUtils.getUserClass(e.getKey().getClass()).getSimpleName() + " executed - End execution instant: " + applicationClock.instant().getEpochSecond() + " - " + LocalDateTime.now(applicationClock).format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss")));
                schedule.remove(e);
            }
        } while (e != null && e.getValue() <= timeDestination);


        //Finally, go to the final time destionation
        Duration offset = Duration.ofSeconds(timeDestination - applicationClock.instant().getEpochSecond());
        applicationClock = Clock.offset(applicationClock, offset).withZone(ZoneId.of(ZONE));
        System.out.println("time travel at " + timeDestination);

        //Unset semaphore variable
        timeTravelling = false;

        return true;
    }

    public String getZone() {
        return ZONE;
    }
}
