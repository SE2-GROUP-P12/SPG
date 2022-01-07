package it.polito.SE2.P12.SPG.schedulables.scheduleRoutines;

import it.polito.SE2.P12.SPG.schedulables.Schedulable;
import it.polito.SE2.P12.SPG.service.SpgOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class PendingOrdersDetectionRoutine implements Schedulable {
    private SpgOrderService orderService;

    @Autowired
    public PendingOrdersDetectionRoutine(SpgOrderService orderService) {
        this.orderService = orderService;
    }


    @Override
    public void execute() {
        orderService.deleteUnpayableOrders();
    }
}
