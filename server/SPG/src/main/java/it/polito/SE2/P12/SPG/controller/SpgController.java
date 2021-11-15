package it.polito.SE2.P12.SPG.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import it.polito.SE2.P12.SPG.entity.Basket;
import it.polito.SE2.P12.SPG.entity.Order;
import it.polito.SE2.P12.SPG.entity.Product;
import it.polito.SE2.P12.SPG.entity.User;
import it.polito.SE2.P12.SPG.service.SpgBasketService;
import it.polito.SE2.P12.SPG.service.SpgOrderService;
import it.polito.SE2.P12.SPG.service.SpgProductService;
import it.polito.SE2.P12.SPG.service.SpgUserService;
import it.polito.SE2.P12.SPG.utils.API;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * NOTE: in order to granted access to an api based on the user roles add roles into the 'PreAuthorization' annotation as follows:
 * PreAuthorize("hasAnyRole('ROLE_<role1>', 'ROLE_<role2>, ...)"). Possible available roles are provided into /security/ApplicationUserRole enum.
 */

@RestController
@RequestMapping(value = API.HOME,  produces = "application/json", consumes = "application/json")
public class SpgController {

    private final SpgProductService productService;
    private final SpgUserService userService;
    private final SpgOrderService orderService;
    private final SpgBasketService basketService;

    @Autowired
    public SpgController(SpgProductService service, SpgUserService userService, SpgOrderService orderService, SpgBasketService basketService) {
        this.productService = service;
        this.userService = userService;
        this.orderService = orderService;
        this.basketService = basketService;
        this.userService.populateDB();
        this.productService.populateDB();
    }

    @GetMapping("/")
    public ResponseEntity home() {
        return ResponseEntity.ok().build();
    }

    @GetMapping(API.ALL_PRODUCT)
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    public ResponseEntity<List<Product>> getAllProduct() {
        return ResponseEntity.ok(productService.getAllProduct());
    }

    @PostMapping(API.EXIST_CUSTOMER)
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_EMPLOYEE')")
    public ResponseEntity<Map<String, Boolean>> checkExistCustomerMailAndSsn(@RequestBody String jsonData) {
        Map<String, Object> requestMap = extractMapFromJsonString(jsonData);
        if (requestMap == null)
            return ResponseEntity.badRequest().build();
        if (requestMap.containsKey("email") && requestMap.containsKey("ssn"))
            return ResponseEntity.ok(userService.checkPresenceOfUser((String) requestMap.get("email"), (String) requestMap.get("ssn")));
        return ResponseEntity.badRequest().build();
    }

    @GetMapping(API.EXIST_CUSTOMER_BY_MAIL)
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    public ResponseEntity checkExistCustomerMail(@RequestParam String email) {
        if (email == null)
            return ResponseEntity.badRequest().build();
        return ResponseEntity.ok(userService.checkPresenceOfMail(email));
    }

    @PostMapping(API.CREATE_CUSTOMER)
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    public ResponseEntity createCustomer(@RequestBody String userJsonData) {
        if (userJsonData == null || userJsonData.equals(""))
            return ResponseEntity.badRequest().build();
        Map<String, Object> requestMap = extractMapFromJsonString(userJsonData);
        if(requestMap == null)
            return ResponseEntity.badRequest().build();
        if(requestMap.containsKey("email") && requestMap.containsKey("ssn")
                && requestMap.containsKey("name") && requestMap.containsKey("surname")
                && requestMap.containsKey("phoneNumber") && requestMap.containsKey("password")
                && requestMap.containsKey("role")
        )
            return ResponseEntity.ok(userService.addNewClient(
                    new User(requestMap.get("name").toString(), requestMap.get("surname").toString(),
                            requestMap.get("ssn").toString(), requestMap.get("phoneNumber").toString(),
                            requestMap.get("role").toString(), requestMap.get("email").toString(),
                            requestMap.get("password").toString())
            ));
        return ResponseEntity.badRequest().build();
    }

    @PostMapping(API.PLACE_ORDER)
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    public ResponseEntity placeOrder(@RequestBody String jsonData) {
        Map<String, Object> requestMap = extractMapFromJsonString(jsonData);
        if (requestMap == null)
            return ResponseEntity.badRequest().build();
        if (requestMap.containsKey("email")) {
            User user = userService.getUserByEmail((String) requestMap.get("email"));
            Basket basket = user.getBasket();
            basketService.dropBasket(basket);
            return ResponseEntity.ok(orderService.addNewOrderFromBasket(basket));
        }
        return ResponseEntity.badRequest().build();
    }

    @PostMapping(API.ADD_TO_BASKET)
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    public ResponseEntity addToBasket(@RequestBody String jsonData) {
        Map<String, Object> requestMap = extractMapFromJsonString(jsonData);
        if (requestMap == null)
            return ResponseEntity.badRequest().build();
        if (requestMap.containsKey("productId") && requestMap.containsKey("email") && requestMap.containsKey("quantity")) {
            Product product = productService.getProductById(Long.valueOf((Integer) requestMap.get("productId")));
            Double quantity = Double.valueOf((Integer) requestMap.get("quantity"));
            User user = userService.getUserByEmail((String) requestMap.get("email"));
            return ResponseEntity.ok(basketService.addProductToCart(product, quantity, user));
        }
        return ResponseEntity.badRequest().build();
    }

    @GetMapping(API.GET_CART)
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    public ResponseEntity<List<Product>> getCart(@RequestParam String email) {
        User user = userService.getUserByEmail(email);
        if (user == null) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(basketService.getProductsInBasket(user));
    }

    @GetMapping(API.GET_WALLET)
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    public ResponseEntity<Double> getWallet(@RequestParam String email) {
        return ResponseEntity.ok(userService.getWallet(email));
    }

    @PostMapping(API.TOP_UP)
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    public ResponseEntity topUp(@RequestBody String jsonData) {
        Map<String, Object> requestMap = extractMapFromJsonString(jsonData);
        if (requestMap == null)
            return ResponseEntity.badRequest().build();
        if (requestMap.containsKey("email") && requestMap.containsKey("value")) {
            String email = (String) requestMap.get("email");
            Double value = Double.valueOf((Integer) requestMap.get("value"));
            return ResponseEntity.ok(userService.topUp(email, value));
        }
        return ResponseEntity.badRequest().build();
    }

    @PostMapping(API.DELIVER_ORDER)
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    public ResponseEntity deliverOrder(@RequestBody Long orderId) {
        return ResponseEntity.ok(orderService.deliverOrder(orderId));
    }

    @DeleteMapping(API.DROP_ORDER)
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    public ResponseEntity dropOrder(@RequestBody String jsonData) {
        Map<String, Object> requestMap = extractMapFromJsonString(jsonData);
        if (requestMap == null)
            return ResponseEntity.badRequest().build();
        if (requestMap.containsKey("email")) {
            String email = (String) requestMap.get("email");
            User user = userService.getUserByEmail(email);
            if (user == null) {
                return ResponseEntity.badRequest().build();
            }
            basketService.dropBasket(user); //THAT'S HIM, OFFICER!
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.badRequest().build();
    }

    @GetMapping(API.GET_ORDERS)
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    public ResponseEntity getOrders(@RequestParam String email){
        User user = userService.getUserByEmail(email);
        if(user == null) return ResponseEntity.badRequest().build();
        String response = orderService.getOrdersProductsJson(user.getUserId());
        if(response == null)
            return ResponseEntity.badRequest().build();
        System.out.println(response);
        return ResponseEntity.ok(response);
    }

    @GetMapping(API.TEST)
    public ResponseEntity test() {
        double response = userService.topUp("mario.rossi@gmail.com", 12.70);
        System.out.println("top up quantity is: "+response);
        return ResponseEntity.ok(response);
    }

    public Map<String, Object> extractMapFromJsonString(String jsonData) {
        ObjectMapper mapper = new ObjectMapper();
        Map<String, Object> requestMap;
        try {
            requestMap = mapper.readValue(jsonData, Map.class);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return null;
        }
        return requestMap;
    }

}
