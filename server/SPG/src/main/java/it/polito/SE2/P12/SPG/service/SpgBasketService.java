package it.polito.SE2.P12.SPG.service;


import it.polito.SE2.P12.SPG.entity.*;
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

    public Basket emptyBasket(Long  userId){
        Basket output = basketRepo.getById(userId);
        basketRepo.deleteById(userId);
        return  output;
    }

    public Map<String, String> addProductToCart(Product product, Double quantity, User customer) {
        //the method modifies a basket, then the basket is saved in local and the old one gets removed,
        // before inserting the modified version
        Map<String, String> response = new HashMap<>();
        Basket basket = customer.getBasket();
        basket.addProduct(product, quantity);
        basketRepo.save(basket);
        response.put("responseStatus", "200-OK");
        return response;
    }

    public List<Product> getProductsInBasket(User customer) {
        List <Product> list = basketRepo.findBasketByCust_UserId(customer.getUserId()).getProductList();
        for (Product basketItem: list) {
            basketItem.setQuantity(basketRepo.findBasketByCust_UserId(customer.getUserId()).getProds().get(basketItem));
        }
        return  list;
    }

}
