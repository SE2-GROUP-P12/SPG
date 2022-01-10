package it.polito.SE2.P12.SPG.service;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import it.polito.SE2.P12.SPG.entity.*;
import it.polito.SE2.P12.SPG.interfaceEntity.OrderUserType;
import it.polito.SE2.P12.SPG.repository.*;
import it.polito.SE2.P12.SPG.utils.OrderStatus;
import it.polito.SE2.P12.SPG.utils.UserRole;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.*;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.*;

import static it.polito.SE2.P12.SPG.utils.OrderStatus.*;


@Service
public class SpgOrderService {

    private OrderRepo orderRepo;
    private SpgUserService spgUserService;
    private ProductRepo productRepo;
    private UserRepo userRepo;
    private CustomerRepo customerRepo;
    private SchedulerService schedulerService;


    @Autowired
    public SpgOrderService(OrderRepo orderRepo, SpgUserService spgUserService, ProductRepo productRepo, UserRepo userRepo, CustomerRepo customerRepo, SchedulerService schedulerService) {
        this.orderRepo = orderRepo;
        this.spgUserService = spgUserService;
        this.productRepo = productRepo;
        this.userRepo = userRepo;
        this.customerRepo = customerRepo;
        this.schedulerService = schedulerService;
    }

    public Map<String, List<Long>> getPendingOrdersMail() {
        Map<String, List<Long>> mailMap = new HashMap<>();
        //iterate on all orders
        for (Order order : orderRepo.findAll()) {
            //if an order is OPEN
            if (order.getStatus().equals(ORDER_STATUS_OPEN)) {
                //check if it is already in the map
                if (!mailMap.containsKey(order.getCust().getEmail())) {
                    //if it is not in the map add the reference and then add the orderId after had initialized the list
                    List<Long> vectorOfOrderId = new ArrayList<>();
                    vectorOfOrderId.add(order.getOrderId());
                    mailMap.put(order.getCust().getEmail(), vectorOfOrderId);
                } else {
                    //if it is present add orderId into the list of orderId
                    mailMap.get(order.getCust().getEmail()).add(order.getOrderId());
                }
            }
        }
        return mailMap;
    }

    public Boolean addNewOrder(Order order) {
        //Get some useful info
        Long userId = order.getCust().getUserId();
        Customer orderIssuer = (Customer) userRepo.findUserByUserId(userId);
        //check if customer exists
        if (orderIssuer == null)
            return false;
        Double currentAmount = 0.00;
        //case: empty order list -> just check the wallet
        if (getOrdersByUserId(userId).isEmpty()) {
            //check wallet availability for orders paying
            if (orderIssuer.getWallet() >= order.getValue()) {
                //Set order as confirmed (payable)
                order.updateToConfirmedStatus(LocalDateTime.ofInstant(order.getDate().toInstant(), ZoneId.systemDefault()));
                orderRepo.save(order);
                return true;
            }
            orderRepo.save(order);
            return true;
        }
        //case: we have some other order issued by this user, so we evaluate the total amount of OPEN order
        //Optimal function: sort order based on value in order to pay more order
        for (Order o : getOrdersByUserId(userId).stream()
                .filter(ord -> ord.getStatus().equals(ORDER_STATUS_CONFIRMED))
                .sorted(Comparator.comparingDouble(Order::getValue))
                .toList()) {
            //add current order value
            currentAmount += o.getValue();
        }
        //check if with new order value the current amount overflowed the wallet value
        if (currentAmount + order.getValue() > orderIssuer.getWallet()) {
            //leave the state on open then return true
            orderRepo.save(order);
            return true;
        }
        //the order can be paid correctly
        order.updateToConfirmedStatus(LocalDateTime.ofInstant(order.getDate().toInstant(), ZoneId.systemDefault()));
        orderRepo.save(order);
        return true;
    }

    //Basket viene convertito in un ordine per il possessore del basket
    public Boolean addNewOrderFromBasket(Basket basket, long epoch, Date deliveryDate, String deliveryAddress) {
        if (!spgUserService.isOrderUserType(basket.getCust()))
            return false;
        return addNewOrderFromBasket(basket, (OrderUserType) basket.getCust(), epoch, deliveryDate, deliveryAddress);
    }

    //Basket viene convertito in un ordine per user
    public Boolean addNewOrderFromBasket(Basket basket, OrderUserType user, long epoch, Date deliveryDate, String deliveryAddress) {
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
        Order order = new Order(user, epoch, basket.getProductQuantityMap(), deliveryDate, deliveryAddress);
        boolean result = addNewOrder(order);
        if (result) {
            user.getOrders().add(order);
            if (((User) user).getRole().equals(UserRole.ROLE_SHOP_EMPLOYEE))
                userRepo.save((ShopEmployee) user);
            else
                userRepo.save((Customer) user);
        }
        return result;
    }

    public Boolean deliverOrder(Long orderId, Instant currentSchedulerInstant) {
        Optional<Order> o = orderRepo.findById(orderId);
        if (!o.isPresent())
            return false;
        Order order = o.get();
        OrderUserType user = (OrderUserType) order.getCust();
        if (user.getWallet() < order.getValue())
            return false;
        //Check if the customer wallet has available amount
        if (user.getWallet() > order.getValue()) {
            //decrease the wallet amount
            user.setWallet(user.getWallet() - order.getValue());
            //set status paid
            order.updateToPaidStatus(LocalDateTime.ofInstant(currentSchedulerInstant, ZoneId.of(schedulerService.getZone())));
            //update db
            for (Map.Entry<Product, Double> set : order.getProds().entrySet()) {
                set.getKey().setQuantityDelivered(set.getValue());
                set.getKey().setQuantityOrdered(set.getKey().getQuantityOrdered() - set.getValue());
                productRepo.save(set.getKey());
            }
            orderRepo.save(order);
            return true;
        }
        //Else leave open status and return true
        return true;
    }

    public List<Order> getOrdersByUserId(Long userId) {
        List<Order> output = orderRepo.findAllByCust_UserId(userId);
        return output;
    }

    public String getUserOrdersProductsJson(Long userId) {
        List<Order> orders = orderRepo.findAllByCust_UserId(userId);
        ObjectMapper mapper = new ObjectMapper();
        String response = null;
        try {
            response = mapper.writeValueAsString(orders);

        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return response;
    }

    public Double getTotalPrice(Long userId) {
        Double output = 0.00;
        if (!userRepo.existsById(userId)) return -1.0;
        for (Order order : orderRepo.findAllByCust_UserId(userId)) {
            output += order.getValue();
        }
        return output;
    }

    public String getAllOrdersProductJson() {
        List<Order> orders = orderRepo.findAll();
        ObjectMapper mapper = new ObjectMapper();
        String response = null;
        try {
            response = mapper.writeValueAsString(orders);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return response;
    }

    public Boolean setDeliveryDate(Long orderId, Date date) {
        Order order = orderRepo.findOrderByOrderId(orderId);
        if (order == null) return false;
        order.setDeliveryDate(date);
        orderRepo.save(order);
        return true;
    }

    public Boolean setDeliveryDateAndAddress(Long orderId, Date date, String address) {
        Order order = orderRepo.findOrderByOrderId(orderId);
        if (order == null) return false;
        order.setDeliveryDate(date);
        order.setDeliveryAddress(address);
        orderRepo.save(order);
        return true;
    }

    public void updateConfirmedOrders() {
        for (Order order : orderRepo.findAll()) {
            if (order.getStatus().equals(ORDER_STATUS_PAID) || order.getStatus().equals(ORDER_STATUS_CLOSED)
                    || order.getStatus().equals(ORDER_STATUS_CANCELLED) || order.getStatus().equals(ORDER_STATUS_NOT_RETRIEVED))
                continue;
            Customer cust = customerRepo.findCustomerByEmail(order.getCust().getEmail());
            if (order.getStatus().equals(ORDER_STATUS_OPEN)) {
                order.setStatus(ORDER_STATUS_CANCELLED);
                orderRepo.save(order);
                continue;
            }
            if (order.getValue() > cust.getWallet()) continue;
            Double price = 0.0;
            for (Product product : order.getProductList()) {
                double quantity = Math.min(order.getProds().get(product), product.getQuantityConfirmed()); //TODO: recheck code crashes if there are two orders issued by same custgomer
                Map<Product, Double> prods = order.getProds();
                prods.remove(product);
                prods.put(product, quantity);
                order.setProds(prods);
                price += product.getPrice() * quantity;
                product.setQuantityConfirmed(product.getQuantityConfirmed() - quantity);
                productRepo.save(product);
            }
            cust.pay(price);
            if (price == 0.0)
                order.setStatus(ORDER_STATUS_CANCELLED);
            else
                order.setStatus(ORDER_STATUS_PAID);
            customerRepo.save(cust);
            orderRepo.save(order);
        }
    }

    public boolean setOrderStatus(Long orderId, String orderStatus, Instant schedulerCurrentInstant) {
        boolean res = false;
        Order order = orderRepo.findOrderByOrderId(orderId);
        if (order == null)
            return false;
        switch (orderStatus) {
            case ORDER_STATUS_CONFIRMED -> res = order.updateToConfirmedStatus(LocalDateTime.ofInstant(schedulerCurrentInstant, ZoneId.systemDefault()));
            case ORDER_STATUS_PAID -> res = order.updateToPaidStatus(LocalDateTime.ofInstant(schedulerCurrentInstant, ZoneId.systemDefault()));
            case ORDER_STATUS_CLOSED -> res = order.updateToClosedStatus(LocalDateTime.ofInstant(schedulerCurrentInstant, ZoneId.systemDefault()));
            case ORDER_STATUS_CANCELLED -> res = order.updateToCancelledStatus(LocalDateTime.ofInstant(schedulerCurrentInstant, ZoneId.systemDefault()));
            case ORDER_STATUS_NOT_RETRIEVED -> res = order.updateToNotRetrievedStatus(LocalDateTime.ofInstant(schedulerCurrentInstant, ZoneId.systemDefault()));
        }
        orderRepo.save(order);
        return res;
    }


    public List<Order> getUnRetrievedOrders(long currentTime) {
        return orderRepo.findAll()
                .stream()
                .filter(order -> {
                    //check order status
                    if (!order.getStatus().equals(ORDER_STATUS_NOT_RETRIEVED))
                        return false;
                    //check date range
                    return order.getCurrent_status_date().toEpochSecond(ZoneOffset.UTC) > (currentTime - 2592000L);
                })
                .sorted(Comparator.comparingLong(ord -> ord.getCurrent_status_date().toEpochSecond(ZoneOffset.UTC)))
                .toList();
    }
}

