package it.polito.SE2.P12.SPG.service;


import it.polito.SE2.P12.SPG.entity.Basket;
import org.springframework.stereotype.Service;
import it.polito.SE2.P12.SPG.repository.BasketRepo;

@Service
public class SpgBasketService {

    private BasketRepo basketRepo;
    public SpgBasketService(BasketRepo basketRepo) {
        this.basketRepo = basketRepo;
    }

    public Basket emptyBasket(Long  userId){
        Basket output = basketRepo.getById(userId);
        basketRepo.deleteById(userId);
        return  output;
    }

}
