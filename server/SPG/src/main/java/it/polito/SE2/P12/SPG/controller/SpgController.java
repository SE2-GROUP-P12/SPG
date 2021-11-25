package it.polito.SE2.P12.SPG.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import it.polito.SE2.P12.SPG.entity.*;
import it.polito.SE2.P12.SPG.service.SpgBasketService;
import it.polito.SE2.P12.SPG.service.SpgOrderService;
import it.polito.SE2.P12.SPG.service.SpgProductService;
import it.polito.SE2.P12.SPG.service.SpgUserService;
import it.polito.SE2.P12.SPG.entity.Basket;
import it.polito.SE2.P12.SPG.entity.Product;
import it.polito.SE2.P12.SPG.entity.User;
import it.polito.SE2.P12.SPG.service.*;
import it.polito.SE2.P12.SPG.utils.API;
import it.polito.SE2.P12.SPG.utils.JWTProviderImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URI;
import java.util.*;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.util.MimeTypeUtils.APPLICATION_JSON_VALUE;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * NOTE: in order to granted access to an api based on the user roles add roles into the 'PreAuthorization' annotation as follows:
 * PreAuthorize("hasAnyRole('ROLE_<role1>', 'ROLE_<role2>, ...)"). Possible available roles are provided into /security/ApplicationUserRole enum.
 */
@Slf4j
@RestController
@RequestMapping(value = API.HOME, produces = "application/json", consumes = "application/json")
public class SpgController {

    private final SpgProductService productService;
    private final SpgUserService userService;
    private final SpgOrderService orderService;
    private final SpgBasketService basketService;
    private final JWTUserHandlerService jwtUserHandlerService;


    @Autowired
    public SpgController(SpgProductService service, SpgUserService userService, SpgOrderService orderService, SpgBasketService basketService, JWTUserHandlerService jwtUserHandlerService, JWTUserHandlerService jwtUserHandlerService1) {
        this.productService = service;
        this.userService = userService;
        this.orderService = orderService;
        this.basketService = basketService;
        this.jwtUserHandlerService = jwtUserHandlerService1;
        this.userService.populateDB();
        this.productService.populateDB();
    }

    @GetMapping("/")
    public ResponseEntity<?> home() {
        return ResponseEntity.ok().build();
    }

    @GetMapping(API.ALL_PRODUCT)
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    public ResponseEntity<List<Product>> getAllProduct() {
        return ResponseEntity.ok(productService.getAllProduct());
    }

    @PostMapping(API.EXIST_CUSTOMER)
    //@PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_EMPLOYEE')")
    public ResponseEntity<Map<String, Boolean>> checkExistCustomerMailAndSsn(@RequestBody String jsonData) {
        Map<String, Object> requestMap = extractMapFromJsonString(jsonData);
        Map<String, Boolean> response = new HashMap<>();
        if (requestMap == null)
            return ResponseEntity.badRequest().build();
        if (requestMap.containsKey("email") && requestMap.containsKey("ssn")) {
            if(userService.checkPresenceOfUser((String) requestMap.get("email"), (String) requestMap.get("ssn")))
                response.put("exist", true);
            else
                response.put("exist", false);
            return ResponseEntity.ok(response);
        }
        return ResponseEntity.badRequest().build();
    }

    @GetMapping(API.EXIST_CUSTOMER_BY_MAIL)
    //@PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    public ResponseEntity checkExistCustomerMail(@RequestParam String email) {
        if (email == null)
            return ResponseEntity.badRequest().build();
        return ResponseEntity.ok(userService.checkPresenceOfMail(email));
    }

    @PostMapping(API.CREATE_CUSTOMER)
    //@PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    public ResponseEntity createCustomer(@RequestBody String userJsonData) {
        Map<String, String> error = new HashMap<>();
        URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/" + API.CREATE_CUSTOMER).toUriString());
        if (userJsonData == null || userJsonData.equals("")) {
            error.put("errorMessage", "Body is not valid");
            return ResponseEntity.badRequest().contentType(MediaType.APPLICATION_JSON).body(error);
        }
        Map<String, Object> requestMap = extractMapFromJsonString(userJsonData);
        if (requestMap == null)
            return ResponseEntity.badRequest().build();
        if (requestMap.containsKey("email") && requestMap.containsKey("ssn")
                && requestMap.containsKey("name") && requestMap.containsKey("surname")
                && requestMap.containsKey("phoneNumber") && requestMap.containsKey("password")
                && requestMap.containsKey("role")
                && Boolean.FALSE.equals(userService.checkPresenceOfMail(requestMap.get("email").toString()))
                && Boolean.FALSE.equals(userService.checkPresenceOfSSN(requestMap.get("ssn").toString()))
        )
            return ResponseEntity.created(uri).body(userService.addNewCustomer(
                    new Customer(requestMap.get("name").toString(), requestMap.get("surname").toString(),
                            requestMap.get("ssn").toString(), requestMap.get("phoneNumber").toString(),
                             requestMap.get("email").toString(),
                            requestMap.get("password").toString(), requestMap.get("address").toString())
            ));
        error.put("errorMessage", "email/ssn already present in the system");
        return ResponseEntity.badRequest()
                .contentType(MediaType.APPLICATION_JSON)
                .body(error);
    }
/* && requestMap.containsKey("role") && requestMap.containsKey("address")
        ) {
            Customer c = new Customer(requestMap.get("name").toString(), requestMap.get("surname").toString(),
                    requestMap.get("ssn").toString(), requestMap.get("phoneNumber").toString(),
                    requestMap.get("email").toString(), requestMap.get("password").toString(), requestMap.get("address").toString());
            if(userService.addNewCustomer(c))
                response.put("responseMessage", "Customer already present");
            else
                response.put("responseMessage", "Customer added successfully");
            return ResponseEntity.ok(response);
        }
        return ResponseEntity.badRequest().build();*/

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
            return ResponseEntity.notFound().build();
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
            Double value = (Double) requestMap.get("value");
            return ResponseEntity.ok(userService.topUp(email, value));
        }
        return ResponseEntity.badRequest().build();
    }

    @PostMapping(API.DELIVER_ORDER)
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    public ResponseEntity deliverOrder(@RequestBody Long orderId) {
        System.out.println(orderId);
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
    public ResponseEntity getOrders(@RequestParam String email) {
        User user = userService.getUserByEmail(email);
        if (user == null) return ResponseEntity.badRequest().build();
        String response = orderService.getOrdersProductsJson(user.getUserId());
        if (response.isEmpty())
            return ResponseEntity.badRequest().build();
        System.out.println(response);
        return ResponseEntity.ok(response);
    }

    @GetMapping(API.RETRIEVE_ERROR)
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    public ResponseEntity retrieveError(@RequestParam String email) {
        //Warning! only customer have a wallet and therefore this will send an error
        Customer user = userService.getCustomerByEmail(email);
        Map<String,String> response=new HashMap<String,String>();
        if (user == null){
            if(userService.getUserByEmail(email)==null) {
                //the user doesn't exist
                return ResponseEntity.badRequest().build();
            }
            //the user isn't a customer and therefore has no wallet
            response.put("exist", "false");
            return ResponseEntity.ok(response);
        }
        Double total = orderService.getTotalPrice(user.getUserId());
        if (total>user.getWallet()){
            response.put("exist", "true");
            response.put("message", "Balance insufficient, remember to top up!");
        }
        else response.put("exist", "false");
        return ResponseEntity.ok(response);
    }


    @GetMapping(API.REFRESH_TOKEN)
    public void getRefreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String authorizationHeader = request.getHeader(AUTHORIZATION);
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            String refreshToken = authorizationHeader.substring("Bearer ".length());
            JWTProviderImpl jwtProvider = new JWTProviderImpl();
            try {
                Map<String, String> responseBody = jwtProvider.verifyRefreshTokenAndRegenerateAccessToken(refreshToken, request.getRequestURL().toString(), this.userService);
                response.setContentType(APPLICATION_JSON_VALUE);
                jwtUserHandlerService.invalidateByUserRefreshTokens(userService.getUserByEmail(responseBody.get("email")), refreshToken);
                String accessToken = responseBody.get("accessToken");
                jwtUserHandlerService.addRelationUserTokens(userService.getUserByEmail(responseBody.get("email")),
                        accessToken, refreshToken);
                new ObjectMapper().writeValue(response.getOutputStream(), responseBody);
            } catch (Exception e) {
                log.error("Error  refreshing token: " + e.getMessage());
                response.setHeader("error", e.getMessage());
                response.setStatus(FORBIDDEN.value());
                response.setContentType(APPLICATION_JSON_VALUE);
                new ObjectMapper().writeValue(response.getOutputStream(), jwtProvider.setErrorMessage(e));
            }
        } else {
            throw new RuntimeException("Refresh token is missing");
        }
    }

    @GetMapping(API.LOGOUT)
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    public void doLogout(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String authorizationHeader = request.getHeader(AUTHORIZATION);
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            String accessToken = authorizationHeader.substring("Bearer ".length());
            JWTProviderImpl jwtProvider = new JWTProviderImpl();
            try {
                Map<String, String> okResponseBody = new HashMap<>();
                okResponseBody.put("status", "successful(user logged out correctly");
                String username = jwtProvider.verifyAccessToken(accessToken);
                User user = userService.getUserByEmail(username);
                jwtUserHandlerService.invalidateUserTokens(user, accessToken);
                response.setStatus(OK.value());
                response.setContentType(APPLICATION_JSON_VALUE);
                new ObjectMapper().writeValue(response.getOutputStream(), okResponseBody);
            } catch (Exception e) {
                log.error("Error logging out: " + e.getMessage());
                log.error("Error logging out: " + Arrays.toString(e.getStackTrace()));
                response.setHeader("error", e.getMessage());
                response.setStatus(FORBIDDEN.value());
                response.setContentType(APPLICATION_JSON_VALUE);
                new ObjectMapper().writeValue(response.getOutputStream(), jwtProvider.setErrorMessage(e));
            }
        } else {
            throw new RuntimeException("Refresh token is missing");
        }
    }

    @GetMapping(API.TEST)
    public ResponseEntity test() {
        double response = userService.topUp("mario.rossi@gmail.com", 12.70);
        System.out.println("top up quantity is: " + response);
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
