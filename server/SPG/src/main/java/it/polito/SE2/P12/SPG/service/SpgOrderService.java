package it.polito.SE2.P12.SPG.service;

import it.polito.SE2.P12.SPG.entity.Basket;
import it.polito.SE2.P12.SPG.entity.Order;
import it.polito.SE2.P12.SPG.entity.Product;
import it.polito.SE2.P12.SPG.repository.OrderRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

@Service
public class SpgOrderService {

    private OrderRepo orderRepo;


    @Autowired
    public SpgOrderService(OrderRepo orderRepo) {
        this.orderRepo = orderRepo;
    }

    public Boolean addNewOrder(Order order) {
        orderRepo.save(order);
        return true;
    }

    public Boolean addNewOrderFromBasket(Basket basket) {
        for (Map.Entry<Product, Double> e : basket.getProductQuantityMap().entrySet()) {
            Product p = e.getKey();
            Double q = e.getValue();
            p.moveFromAvailableToOrdered(q);
        }
        Order order = new Order(basket.getCust(), LocalDateTime.now(), basket.getProductQuantityMap());
        return addNewOrder(order);
    }

    public Boolean deliverOrder(Long orderId) {
        Optional<Order> o = orderRepo.findById(orderId);
        if(!o.isPresent())
            return false;
        Order order = o.get();
        orderRepo.delete(order);
        for(Map.Entry<Product, Double> e : order.getProds().entrySet()){
            Product p = e.getKey();
            Double q = e.getValue();
            if(!p.moveFromOrderedToDelivered(q))
                return false;
        }
        return true;
    }

    public List<List<Product>> getOrdersProducts(Long userId) {
        List<List<Product>> output = new ArrayList<List<Product>>();
        for (Order order : orderRepo.findAllByCust_UserId(userId)) {
            List<Product> list = order.getProductList();
            for (Product orderItem : list) {
                orderItem.setQuantityAvailable(orderRepo.findById(order.getOrderId()).get().getProds().get(orderItem));
            }
            output.add(list);
        }
        return output;
    }
}

