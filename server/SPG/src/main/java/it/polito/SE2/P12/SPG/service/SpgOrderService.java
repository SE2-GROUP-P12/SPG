package it.polito.SE2.P12.SPG.service;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import it.polito.SE2.P12.SPG.entity.Basket;
import it.polito.SE2.P12.SPG.entity.Order;
import it.polito.SE2.P12.SPG.entity.Product;
import it.polito.SE2.P12.SPG.repository.OrderRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.StringWriter;
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
        if (!o.isPresent())
            return false;
        Order order = o.get();
        for (Map.Entry<Product, Double> e : order.getProds().entrySet()) {
            Product p = e.getKey();
            Double q = e.getValue();
            if (!p.moveFromOrderedToDelivered(q))
                return false;
        }
        orderRepo.delete(order);
        return true;
    }

    public List<Order> getOrdersProducts(Long userId) {
        List<Order> output = orderRepo.findAllByCust_UserId(userId);
        return output;
    }

    public String getUserOrdersProductsJson(Long userId) {
        List<Order> orders = orderRepo.findAllByCust_UserId(userId);
        ObjectMapper mapper = new ObjectMapper();
        String response = new String();
        try {
            response = mapper.writeValueAsString(orders);

        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return response.toString();
    }

    public String getAllOrdersProductJson()
    {
        List<Order> orders = orderRepo.findAll();
        ObjectMapper mapper = new ObjectMapper();
        String response= new String();
        try{
            response=mapper.writeValueAsString(orders);
        }
        catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return response.toString();
    }
}

