package it.polito.SE2.P12.SPG.serviceTest;

import it.polito.SE2.P12.SPG.schedulables.scheduleRoutines.MondayMorningRoutine;
import it.polito.SE2.P12.SPG.service.SchedulerService;
import it.polito.SE2.P12.SPG.utils.DBUtilsService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;

@SpringBootTest
public class SchedulerServiceTest {

    @Autowired
    DBUtilsService dbUtilsService;
    @Autowired
    SchedulerService schedulerService;
    @Autowired
    MondayMorningRoutine mondayMorningRoutine;

    @BeforeEach
    public void initContext() {
        dbUtilsService.dropAll();
        schedulerService.setActive(true);
        schedulerService.setSchedule(new ArrayList<>());
    }

    @AfterEach
    public void after() {
        schedulerService.setActive(false);
    }

    @Test
    public void initSchedulerTest() {
        schedulerService.initScheduler();
        Assertions.assertTrue(schedulerService.getSchedule().size() > 0);
    }

    @Test
    public void initSchedulerNotActiveTest() {
        schedulerService.setActive(false);
        schedulerService.initScheduler();
        Assertions.assertTrue(schedulerService.getSchedule().size() == 0);
    }

    @Test
    public void addToSchedulerTest() {
        int schedulesBefore = schedulerService.getSchedule().size();
        schedulerService.addToSchedule(mondayMorningRoutine, schedulerService.getEpochTime() + 100000);
        Assertions.assertEquals(schedulesBefore + 1, schedulerService.getSchedule().size());
    }

    @Test
    public void timeTravelAtTest() {
        LocalDateTime now = LocalDateTime.now();
        //Time travel to tomorrow
        schedulerService.timeTravelAt(LocalDateTime.now().toEpochSecond(ZoneOffset.UTC) + 86400);
        Assertions.assertTrue(LocalDateTime.now().toEpochSecond(ZoneOffset.UTC) + 86400 < LocalDateTime.now(schedulerService.getApplicationClock()).toEpochSecond(ZoneOffset.UTC));
        Assertions.assertTrue(LocalDateTime.now().getDayOfWeek() != LocalDateTime.now(schedulerService.getApplicationClock()).getDayOfWeek());
        Assertions.assertTrue(LocalDateTime.now().getDayOfMonth() != LocalDateTime.now(schedulerService.getApplicationClock()).getDayOfMonth());
    }

    @Test
    public void timeTravelBackwardsTest() {
        LocalDateTime now = LocalDateTime.now();
        //Time travel to yesterday, should result in no travel at all
        schedulerService.timeTravelAt(LocalDateTime.now().toEpochSecond(ZoneOffset.UTC) - 86400);
        Assertions.assertTrue(LocalDateTime.now().getDayOfWeek() == LocalDateTime.now(schedulerService.getApplicationClock()).getDayOfWeek());
        Assertions.assertTrue(LocalDateTime.now().getDayOfMonth() == LocalDateTime.now(schedulerService.getApplicationClock()).getDayOfMonth());
    }

    @Test
    public void pollTest() {
        Assertions.assertEquals(0, schedulerService.getSchedule().size());
        schedulerService.addToSchedule(mondayMorningRoutine, schedulerService.getEpochTime() - 10);
        while (schedulerService.getSchedule().size() != 0) ;
        Assertions.assertEquals(0, schedulerService.getSchedule().size());
    }

    @Test
    public void pollNotActiveTest() {
        schedulerService.setActive(false);
        schedulerService.addToSchedule(mondayMorningRoutine, schedulerService.getEpochTime() + 1);
        Assertions.assertEquals(1, schedulerService.getSchedule().size());
        schedulerService.poll();
        Assertions.assertEquals(1, schedulerService.getSchedule().size());
    }
}
