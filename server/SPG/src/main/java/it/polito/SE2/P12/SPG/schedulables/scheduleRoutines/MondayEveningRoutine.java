package it.polito.SE2.P12.SPG.schedulables.scheduleRoutines;

import it.polito.SE2.P12.SPG.schedulables.Schedulable;
import it.polito.SE2.P12.SPG.service.SpgOrderService;
import it.polito.SE2.P12.SPG.service.SpgProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MondayEveningRoutine implements Schedulable {

    private final SpgOrderService orderService;
    private final SpgProductService productService;

    @Autowired
    public MondayEveningRoutine(SpgOrderService orderService, SpgProductService productService) {
        this.orderService = orderService;
        this.productService = productService;
    }

    public void execute() {
        orderService.updateConfirmedOrders();
        productService.resetConfirmedQuantities();
    }

}
