package it.polito.SE2.P12.SPG.service;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import it.polito.SE2.P12.SPG.entity.*;
import it.polito.SE2.P12.SPG.interfaceEntity.OrderUserType;
import it.polito.SE2.P12.SPG.repository.BasketRepo;
import it.polito.SE2.P12.SPG.repository.OrderRepo;
import it.polito.SE2.P12.SPG.repository.ProductRepo;
import it.polito.SE2.P12.SPG.repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.data.domain.Example;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.StringWriter;
import java.time.LocalDateTime;
import java.util.*;

@Service
public class SpgOrderService {

    private OrderRepo orderRepo;
    private BasketRepo basketRepo;
    private SpgUserService spgUserService;
    private ProductRepo productRepo;
    private UserRepo userRepo;


    @Autowired
    public SpgOrderService(OrderRepo orderRepo, BasketRepo basketRepo, SpgUserService spgUserService, ProductRepo productRepo, UserRepo userRepo) {
        this.orderRepo = orderRepo;
        this.basketRepo = basketRepo;
        this.spgUserService = spgUserService;
        this.productRepo = productRepo;
        this.userRepo = userRepo;
    }

    public Boolean addNewOrder(Order order) {
        orderRepo.save(order);
        return true;
    }

    //Basket viene convertito in un ordine per il possessore del basket
    public Boolean addNewOrderFromBasket(Basket basket) {
        if (!spgUserService.isOrderUserType(basket.getCust()))
            return false;
        return addNewOrderFromBasket(basket, (OrderUserType) basket.getCust());
    }

    //Basket viene convertito in un ordine per user
    public Boolean addNewOrderFromBasket(Basket basket, OrderUserType user) {
        //Controlla se le quantità ordinate sono disponibile
        for (Map.Entry<Product, Double> e : basket.getProductQuantityMap().entrySet()) {
            if (e.getValue() > e.getKey().getQuantityAvailable())
                return false;
        }
        //Sposta le diverse quantità su ordered
        for (Map.Entry<Product, Double> e : basket.getProductQuantityMap().entrySet()) {
            Product p = e.getKey();
            Double q = e.getValue();
            p.moveFromAvailableToOrdered(q);
            productRepo.save(p);
        }
        Order order = new Order((OrderUserType) user, LocalDateTime.now(), basket.getProductQuantityMap());
        return addNewOrder(order);
    }

    public Boolean deliverOrder(Long orderId) {
        Optional<Order> o = orderRepo.findById(orderId);
        if (!o.isPresent())
            return false;
        Order order = o.get();
        OrderUserType user = (OrderUserType) order.getCust();
        if (user.getWallet() < order.getValue())
            return false;
        //Controlla se la quantità ordinata è disponibile
        for (Map.Entry<Product, Double> e : order.getProds().entrySet()) {
            if (e.getValue() > e.getKey().getQuantityOrdered())
                return false;
        }
        for (Map.Entry<Product, Double> e : order.getProds().entrySet()) {
            Product p = e.getKey();
            Double q = e.getValue();
            if(!p.moveFromOrderedToDelivered(q))
                return false;
            productRepo.save(p);
        }
        user.setWallet(user.getWallet() - order.getValue());

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

    public Double getTotalPrice(Long userId) {
        Double output = 0.00;
        if(!userRepo.existsById(userId)) return -1.0;
        for (Order order : orderRepo.findAllByCust_UserId(userId)) {
            output += order.getValue();
        }
        return output;
    }

    public String getAllOrdersProductJson() {
        List<Order> orders = orderRepo.findAll();
        ObjectMapper mapper = new ObjectMapper();
        String response = new String();
        try {
            response = mapper.writeValueAsString(orders);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return response.toString();
    }
}

