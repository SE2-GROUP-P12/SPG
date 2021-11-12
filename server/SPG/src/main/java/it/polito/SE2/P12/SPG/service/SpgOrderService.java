package it.polito.SE2.P12.SPG.service;

import it.polito.SE2.P12.SPG.entity.Basket;
import it.polito.SE2.P12.SPG.entity.Order;
import it.polito.SE2.P12.SPG.entity.Product;
import it.polito.SE2.P12.SPG.entity.User;
import it.polito.SE2.P12.SPG.repository.OrderRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class SpgOrderService {

    private OrderRepo orderRepo;


    @Autowired
    public SpgOrderService(OrderRepo orderRepo) {
        this.orderRepo = orderRepo;
    }

    public boolean addNewOrder(Order order) {
        orderRepo.save(order);
        return true;
    }

    public boolean addNewOrderFromBasket(Basket basket) {
        Order order = new Order(basket.getCust(), LocalDateTime.now(), basket.getProductQuantityMap());
        return addNewOrder(order);
    }

    public boolean deliverOrder(Long userId) {
        orderRepo.deleteByCust_UserId(userId);
        return true;
    }
    public List<List<Product>> getOrdersProducts(Long userId){
        List<List<Product>>output = new ArrayList<List<Product>>();
        for (Order order : orderRepo.findAllByCust_UserId(userId)){
            List <Product> list = order.getProductList();
            for (Product orderItem: list) {
                orderItem.setQuantityAvailable(orderRepo.findByOrderId(order.getOrderId()).getProds().get(orderItem));
            }
            output.add(list);
        }
        return output;
    }
}

