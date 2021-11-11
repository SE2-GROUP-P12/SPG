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
        if(jsonData == null)
            return ResponseEntity.badRequest().build();
        ObjectMapper mapper = new ObjectMapper();
        Map<String, String> requestMap;
        try {
             requestMap = mapper.readValue(jsonData, Map.class);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().build();
        }
        if(requestMap.containsKey("email") && requestMap.containsKey("ssn"))
            return ResponseEntity.ok(userService.checkPresenceOfUser(requestMap.get("email"), requestMap.get("ssn")));
        return ResponseEntity.badRequest().build();
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
    public ResponseEntity addToBasket(@RequestBody Long productId, String email, Double quantity) {
        System.out.println("CHECKPOINT: "+productId+" // "+email+" // "+quantity);
        return ResponseEntity.ok(basketService.addProductToCart(productService.getProductById(productId),quantity, userService.getUserByEmail(email) ));
    }

    @GetMapping(API.GET_CART)
    public ResponseEntity<List<Product>> getCart(@RequestBody  String email) {
        return ResponseEntity.ok(basketService.getProductsInBasket(userService.getUserByEmail(email) ));
    }

    @GetMapping(API.GET_WALLET)
    public ResponseEntity<Double> getWallet(@RequestParam String email) {
        System.out.println("CHECKPOINT " +email);
        return ResponseEntity.ok(userService.getWallet(email));
    }

    @PostMapping(API.TOP_UP)
    public ResponseEntity topUp(@RequestBody  String email, double value) {
        return ResponseEntity.ok(userService.topUp(email, value ));
    }

    @PostMapping(API.DELIVER_ORDER)
    public ResponseEntity topUp(@RequestBody Long orderId) {
        return ResponseEntity.ok(orderService.deliverOrder(orderId ));
    }

    @GetMapping(API.TEST)
    public ResponseEntity test(){
        return ResponseEntity.ok(productService.test());
    }


}
