package it.polito.SE2.P12.SPG.service;

import it.polito.SE2.P12.SPG.entity.Basket;
import it.polito.SE2.P12.SPG.entity.Order;
import it.polito.SE2.P12.SPG.repository.OrderRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Service
public class SpgOrderService {

    private OrderRepo orderRepo;


    @Autowired
    public SpgOrderService(OrderRepo orderRepo) {
        this.orderRepo = orderRepo;
    }

    public Map<String, String> addNewOrder(Order order) {
        Map<String, String> response = new HashMap<>();
        orderRepo.save(order);
        response.put("responseStatus", "200-OK");
        return response;
    }
    public Map<String, String> addNewOrderFromBasket(Basket basket){

        Order order = new Order(basket.getCust(), LocalDateTime.now(),  basket.getProductList());
        return addNewOrder(order);

        }
        public boolean deliverOrder(Long userId){
        orderRepo.deleteByCust_UserId(userId);
        return true;
        }
    }

