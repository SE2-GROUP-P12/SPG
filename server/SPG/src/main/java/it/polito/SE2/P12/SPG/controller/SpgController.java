package it.polito.SE2.P12.SPG.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import it.polito.SE2.P12.SPG.entity.Product;
import it.polito.SE2.P12.SPG.entity.User;
import it.polito.SE2.P12.SPG.service.SpgBasketService;
import it.polito.SE2.P12.SPG.service.SpgOrderService;
import it.polito.SE2.P12.SPG.service.SpgProductService;
import it.polito.SE2.P12.SPG.service.SpgUserService;
import it.polito.SE2.P12.SPG.utils.API;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;


@RestController
@RequestMapping(API.HOME)
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
    public ResponseEntity home(){
        return ResponseEntity.ok().build();
    }

    @GetMapping(API.ALL_PRODUCT)
    public ResponseEntity<List<Product>> getAllProduct(){
        return ResponseEntity.ok(productService.getAllProduct());
    }

    @PostMapping(API.EXIST_CUSTOMER)
    public ResponseEntity<Map<String, Boolean>> checkExistCustomerMailAndSsn(@RequestBody String jsonData){
        Map<String, Object> requestMap = extractMapFromJsonString(jsonData);
        if(requestMap == null)
            return ResponseEntity.badRequest().build();
        if(requestMap.containsKey("email") && requestMap.containsKey("ssn"))
            return ResponseEntity.ok(userService.checkPresenceOfUser((String)requestMap.get("email"), (String)requestMap.get("ssn")));
        return ResponseEntity.badRequest().build();
    }

    @GetMapping(API.EXIST_CUSTOMER_BY_MAIL)
    public ResponseEntity checkExistCustomerMail(@RequestParam String email){
        if(email == null)
            return ResponseEntity.badRequest().build();
        return ResponseEntity.ok(userService.checkPresenceOfMail(email));
    }

    @PostMapping(API.CREATE_CUSTOMER)
    public ResponseEntity createCustomer(@RequestBody User user){
        if(user == null)
            return ResponseEntity.badRequest().build();
        return ResponseEntity.ok(userService.addNewClient(user));
    }

    @PostMapping(API.PLACE_ORDER)
    public ResponseEntity placeOrder(@RequestBody String email){
        return ResponseEntity.ok(orderService.addNewOrderFromBasket(basketService.emptyBasket(userService.getUserIdByEmail(email))));
    }
    @PostMapping(API.ADD_TO_BASKET)
    public ResponseEntity addToBasket(@RequestBody String jsonData) {
        Map<String, Object> requestMap = extractMapFromJsonString(jsonData);
        if(requestMap == null)
            return ResponseEntity.badRequest().build();
        if(requestMap.containsKey("productId") && requestMap.containsKey("email") && requestMap.containsKey("quantity")) {
            Product product  = productService.getProductById(Long.valueOf((Integer)requestMap.get("productId")));
            Double quantity = Double.valueOf((Integer)requestMap.get("quantity"));
            User user = userService.getUserByEmail((String)requestMap.get("email"));
            return ResponseEntity.ok(basketService.addProductToCart(product, quantity, user));
        }
        return ResponseEntity.badRequest().build();
    }

    @GetMapping(API.GET_CART)
    public ResponseEntity<List<Product>> getCart(@RequestParam String email) {
        System.out.println("CHECKPOINT " +email);
        User user =userService.getUserByEmail(email);
        if(user==null){
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(basketService.getProductsInBasket(user ));
    }

    @GetMapping(API.GET_WALLET)
    public ResponseEntity<Double> getWallet(@RequestParam String email) {
        return ResponseEntity.ok(userService.getWallet(email));
    }

    @PostMapping(API.TOP_UP)
    public ResponseEntity topUp(@RequestBody  String jsonData) {
        Map<String, Object> requestMap = extractMapFromJsonString(jsonData);
        if(requestMap == null)
            return ResponseEntity.badRequest().build();
        if(requestMap.containsKey("email") && requestMap.containsKey("value") ) {
            String email  = (String)requestMap.get("email");
            Double value = Double.valueOf((Integer)requestMap.get("value"));
            return ResponseEntity.ok(userService.topUp(email, value ));
        }
        return ResponseEntity.badRequest().build();

    }

    @PostMapping(API.DELIVER_ORDER)
    public ResponseEntity deliverOrder(@RequestBody Long orderId) {
        return ResponseEntity.ok(orderService.deliverOrder(orderId ));
    }

    @DeleteMapping(API.DROP_ORDER)
    public ResponseEntity dropOrder( @RequestBody String email){
        User user =userService.getUserByEmail(email);
        if(user==null){
            return ResponseEntity.badRequest().build();
        }
        basketService.emptyBasket(user.getUserId());
        return  ResponseEntity.ok().build();
    }

    @GetMapping(API.TEST)
    public ResponseEntity test(){
        return ResponseEntity.ok(productService.test());
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
