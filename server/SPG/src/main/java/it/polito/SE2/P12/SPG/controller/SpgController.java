package it.polito.SE2.P12.SPG.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import it.polito.SE2.P12.SPG.entity.Product;
import it.polito.SE2.P12.SPG.entity.User;
import it.polito.SE2.P12.SPG.service.SpgOrderService;
import it.polito.SE2.P12.SPG.service.SpgService;
import it.polito.SE2.P12.SPG.service.SpgUserService;
import it.polito.SE2.P12.SPG.utils.API;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


@RestController
@RequestMapping(API.HOME)
public class SpgController {

    private final SpgService service;
    private final SpgUserService userService;
    private final SpgOrderService orderService;

    @Autowired
    public SpgController(SpgService service, SpgUserService userService, SpgOrderService orderService) {
        this.service = service;
        this.userService = userService;
        this.orderService = orderService;
    }

    @GetMapping("/")
    public ResponseEntity home(){
        return ResponseEntity.ok().build();
    }

    @GetMapping(API.ALL_PRODUCT)
    public ResponseEntity<List<Product>> getAllProduct(){
        return ResponseEntity.ok(service.getAllProduct());
    }

    @PostMapping(API.EXIST_CUSTOMER)
    public ResponseEntity<Map<String, Boolean>> checkExistCustomerMail(@RequestBody String jsonData){
        ObjectMapper mapper = new ObjectMapper();
        Map<String, String> requestMap = null;
        try {
             requestMap = mapper.readValue(jsonData, Map.class);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        //System.out.println("Controller: " + requestMap.get("email"));
        //System.out.println("Controller: " + requestMap.get("ssn"));
        return ResponseEntity.ok(userService.checkPresenceOfUser(requestMap.get("email"), requestMap.get("ssn")));
    }

    @PostMapping(API.CREATE_CUSTOMER)
    public ResponseEntity createCustomer(@RequestBody User user){
        return ResponseEntity.ok(userService.addNewClient(user));
    }

    @GetMapping(API.TEST)
    public ResponseEntity test(){
        return ResponseEntity.ok(service.test());
    }
}
