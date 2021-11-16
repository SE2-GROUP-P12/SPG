package it.polito.SE2.P12.SPG.service;


import it.polito.SE2.P12.SPG.entity.*;
import org.springframework.stereotype.Service;
import it.polito.SE2.P12.SPG.repository.BasketRepo;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Service
public class SpgBasketService {

    private BasketRepo basketRepo;
    public SpgBasketService(BasketRepo basketRepo) {
        this.basketRepo = basketRepo;
    }

    public Basket emptyBasket(User  user){
        Basket output = user.getBasket();
        basketRepo.deleteById(output.getBasketId());
        return  output;
    }

    public void dropBasket(User user) {
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

    public Map<String, String> addProductToCart(Product product, Double quantity, User customer) {
        Map<String, String> response = new HashMap<>();
        Basket basket = customer.getBasket();
        basket.addProduct(product, quantity);
        basketRepo.save(basket);
        response.put("responseStatus", "200-OK");
        return response;
    }

    public List<Product> getProductsInBasket(User customer) {
        Basket basket = customer.getBasket();
        List <Product> list = basket.getProductList();
        for (Product basketItem: list) {
            basketItem.setQuantityAvailable(basketRepo.findBasketByCust_UserId(customer.getUserId()).getProds().get(basketItem));
        }
        return  list;
    }

}
