package it.polito.SE2.P12.SPG.schedulables.scheduleRoutines;

import it.polito.SE2.P12.SPG.schedulables.Schedulable;
import it.polito.SE2.P12.SPG.service.SpgOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MondayEveningRoutine implements Schedulable {

    private final SpgOrderService orderService;

    @Autowired
    public MondayEveningRoutine(SpgOrderService orderService) {
        this.orderService = orderService;
    }

    public void execute() {

    }

}
