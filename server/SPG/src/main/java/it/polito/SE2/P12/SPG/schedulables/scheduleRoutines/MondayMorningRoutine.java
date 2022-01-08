package it.polito.SE2.P12.SPG.schedulables.scheduleRoutines;


import it.polito.SE2.P12.SPG.schedulables.Schedulable;
import it.polito.SE2.P12.SPG.service.SpgOrderService;
import it.polito.SE2.P12.SPG.service.SpgProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MondayMorningRoutine implements Schedulable {

    private SpgProductService productService;


    @Autowired
    public MondayMorningRoutine(SpgProductService spgProductService) {
        this.productService = spgProductService;

    }

    @Override
    public void execute() {
        productService.resetQuantities();
    }
}
