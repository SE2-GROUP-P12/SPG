package it.polito.SE2.P12.SPG.service;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import it.polito.SE2.P12.SPG.entity.*;
import it.polito.SE2.P12.SPG.interfaceEntity.OrderUserType;
import it.polito.SE2.P12.SPG.repository.BasketRepo;
import it.polito.SE2.P12.SPG.repository.OrderRepo;
import it.polito.SE2.P12.SPG.repository.ProductRepo;
import it.polito.SE2.P12.SPG.repository.UserRepo;
import it.polito.SE2.P12.SPG.utils.UserRole;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.util.*;
import java.util.stream.Collectors;

import static it.polito.SE2.P12.SPG.utils.OrderStatus.*;


@Service
public class SpgOrderService {

    private OrderRepo orderRepo;
    private SpgUserService spgUserService;
    private ProductRepo productRepo;
    private UserRepo userRepo;


    @Autowired
    public SpgOrderService(OrderRepo orderRepo, BasketRepo basketRepo, SpgUserService spgUserService, ProductRepo productRepo, UserRepo userRepo) {
        this.orderRepo = orderRepo;
        this.spgUserService = spgUserService;
        this.productRepo = productRepo;
        this.userRepo = userRepo;
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
        Double currentAmount = order.getValue();
        //case: empty order list -> just check the wallet
        if (getOrdersByUserId(userId).isEmpty()) {
            //check wallet availability for orders paying
            if (orderIssuer.getWallet() >= currentAmount) {
                //Set order as confirmed (payable)
                order.updateToConfirmedStatus();
                orderRepo.save(order);
                return true;
            }
            orderRepo.save(order);
            return true;
        }
        //case: we have some other order issued by this user, so we evaluate the total amount of OPEN order
        //Optimal function: sort order based on value in order to pay more order
        for (Order o : getOrdersByUserId(userId).stream()
                .filter(ord -> ord.getStatus().equals(ORDER_STATUS_OPEN))
                .sorted(Comparator.comparingDouble(Order::getValue))
                .toList()) {
            //add current order value
            currentAmount += o.getValue();
            //check if with new order value the current amount overflowed the wallet value
            if (currentAmount > orderIssuer.getWallet()) {
                //leave the state on open then return true
                orderRepo.save(order);
                return true;
            }
        }
        //if we exit the for loop we assume to have sufficient amount in the wallet
        order.updateToConfirmedStatus();

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
        Order order = new Order((OrderUserType) user, epoch, basket.getProductQuantityMap(), deliveryDate, deliveryAddress);
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
            Product p = e.getKey();
            Double q = e.getValue();
            if (!p.moveFromOrderedToDelivered(q))
                return false;
            productRepo.save(p);
        }
        //Check if the customer wallet has available amount
        if (user.getWallet() > order.getValue()) {
            //decrease the wallet amount
            user.setWallet(user.getWallet() - order.getValue());
            //set status paid
            order.updateToPaidStatus();
            //update db
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

    public boolean setOrderStatus(Long orderId, String orderStatus) {
        boolean res = false;
        Order order = orderRepo.findOrderByOrderId(orderId);
        if (order == null)
            return false;
        switch (orderStatus) {
            case ORDER_STATUS_CONFIRMED -> res = order.updateToConfirmedStatus();
            case ORDER_STATUS_PAID -> res = order.updateToPaidStatus();
            case ORDER_STATUS_CLOSED -> res = order.updateToClosedStatus();
            case ORDER_STATUS_CANCELLED -> res = order.updateToCancelledStatus();
            case ORDER_STATUS_NOT_RETRIEVED -> res = order.updateToNotRetrievedStatus();
        }
        orderRepo.save(order);
        return res;
    }
}

