package it.polito.SE2.P12.SPG.service;


import it.polito.SE2.P12.SPG.entity.*;
import it.polito.SE2.P12.SPG.interfaceEntity.BasketUserType;
import org.springframework.stereotype.Service;
import it.polito.SE2.P12.SPG.repository.BasketRepo;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class SpgBasketService {

    private BasketRepo basketRepo;
    public SpgBasketService(BasketRepo basketRepo) {
        this.basketRepo = basketRepo;
    }

    public Basket emptyBasket(BasketUserType user){
        Basket output = user.getBasket();
        basketRepo.deleteById(output.getBasketId());
        return  output;
    }

    public void dropBasket(BasketUserType user) {
        this.dropBasket(user.getBasket());
    }

    public void dropBasket(Basket basket) {
        for (Map.Entry<Product, Double> e : basket.getProductQuantityMap().entrySet()) {
            Product p = e.getKey();
            Double q = e.getValue();
            p.moveFromBasketToAvailable(q);
        }
        basketRepo.delete(basket);
    }

    public Boolean addProductToCart(Product product, Double quantity, BasketUserType user) {
        if(product.getQuantityAvailable() < quantity  || quantity <=0 || Double.isInfinite(quantity) || Double.isNaN(quantity))
            return false;
        product.moveFromAvailableToBasket(quantity);
        Basket basket = user.getBasket();
        basket.add(product, quantity);
        basketRepo.save(basket);
        return true;
    }

    public List<Product> getProductsInBasket(BasketUserType user) {
        Basket basket = user.getBasket();
        List <Product> list = basket.getProductList();
        for (Product basketItem: list) {
            basketItem.setQuantityAvailable(basketRepo.findBasketByCust_UserId(((User)user).getUserId()).getProds().get(basketItem));
        }
        return  list;
    }

}
