package it.polito.SE2.P12.SPG.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import it.polito.SE2.P12.SPG.auth.UserDetailsImpl;
import it.polito.SE2.P12.SPG.entity.*;
import it.polito.SE2.P12.SPG.interfaceEntity.*;
import it.polito.SE2.P12.SPG.service.*;
import it.polito.SE2.P12.SPG.utils.*;
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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.HttpStatus.*;
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
    private final DBUtilsService dbUtilsService;
    private final WalletOperationService walletOperationService;
    private final SchedulerService schedulerService;
    private long timeOffset;


    @Autowired
    public SpgController(SpgProductService service, SpgUserService userService, SpgOrderService orderService, SpgBasketService basketService, JWTUserHandlerService jwtUserHandlerService1, DBUtilsService dbUtilsService, WalletOperationService walletOperationService, SchedulerService schedulerService) {
        this.productService = service;
        this.userService = userService;
        this.orderService = orderService;
        this.basketService = basketService;
        this.jwtUserHandlerService = jwtUserHandlerService1;
        this.timeOffset = 0;
        this.walletOperationService = walletOperationService;
        this.dbUtilsService = dbUtilsService;
        this.schedulerService = schedulerService;
        this.dbUtilsService.init();
    }

    @GetMapping("/")
    public ResponseEntity<Boolean> home() {
        return ResponseEntity.ok().build();
    }

    @GetMapping(API.ALL_PRODUCT)
    //@PreAuthorize("permitAll()")
    public ResponseEntity<List<Product>> getAllProduct() {
        return ResponseEntity.ok(productService.getAllProduct());
    }

    @GetMapping(API.BROWSE_PRODUCT_BY_FARMER)
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_EMPLOYEE' , 'ROLE_FARMER')")
    public ResponseEntity<List<Product>> getAllProductByFarmer(@RequestParam String farmer, @RequestParam String forecasted) {
        List<Product> list;
        if (userService.getFarmerByEmail(farmer) == null) {
            return ResponseEntity.badRequest().build();
        }
        switch (forecasted) {
            case "true": //get all products by farmer with quantityForecast>0
                list = productService.getAllForecastedProductByFarmerEmail(farmer);
                break;
            case "false": //get all products by farmer with quantityForecast==0
                list = productService.getAllUnforecastedProductByFarmerEmail(farmer);
                break;
            case "none": //get all products by farmer
                list = productService.getAllProductByFarmerEmail(farmer);
                break;
            default: //get all products by farmer
                list = productService.getAllProductByFarmerEmail(farmer);
        }
        return ResponseEntity.ok(list);
    }


    @PostMapping(API.ADD_PRODUCT)
    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_EMPLOYEE', 'ROLE_FARMER')")
    public ResponseEntity<Boolean> addProduct(@RequestBody String jsonData) {
        Map<String, Object> requestMap = extractMapFromJsonString(jsonData);
        if (requestMap.isEmpty())
            return ResponseEntity.badRequest().build();
        if (requestMap.containsKey(Constants.JSON_EMAIL) && requestMap.containsKey("productName") &&
                requestMap.containsKey("price") && requestMap.containsKey("unitOfMeasurement")) {
            String email = (String) requestMap.get(Constants.JSON_EMAIL);
            Farmer farmer = userService.getFarmerByEmail(email);
            if (farmer == null)
                return ResponseEntity.badRequest().build();
            String productName = (String) requestMap.get("productName");
            Double value = Double.valueOf(requestMap.get("price").toString());
            String uom = (String) requestMap.get("unitOfMeasurement");
            String imageUrl = "";
            if (requestMap.containsKey("imageUrl"))
                imageUrl = (String) requestMap.get("imageUrl");
            return ResponseEntity.ok(productService.addProduct(productName, value, uom, imageUrl, farmer));
        }
        return ResponseEntity.badRequest().build();
    }

    @GetMapping(API.EXIST_CUSTOMER)
    //@PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_EMPLOYEE')")
    public ResponseEntity<Map<String, Boolean>> checkExistCustomerMailAndSsn(@RequestParam String email, @RequestParam String ssn) {
        Map<String, Boolean> response = new HashMap<>();
        if (email == null || ssn == null)
            return ResponseEntity.badRequest().build();
        if (Boolean.TRUE.equals(userService.checkPresenceOfUser(email, ssn)))
            response.put(Constants.JSON_EXIST, true);
        else {
            response.put(Constants.JSON_EXIST, false);
        }
        return ResponseEntity.ok(response);
    }


    @GetMapping(API.EXIST_CUSTOMER_BY_EMAIL)
    //@PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_EMPLOYEE')")
    public ResponseEntity<Boolean> checkExistCustomerMail(@RequestParam String email) {
        if (email == null)
            return ResponseEntity.badRequest().build();
        return ResponseEntity.ok(userService.checkPresenceOfMail(email));
    }

    @PostMapping(API.CREATE_CUSTOMER)
    //@PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    public ResponseEntity<Map<String, String>> createCustomer(@RequestBody String userJsonData, HttpServletRequest request) {
        User tmp;
        Map<String, String> error = new HashMap<>();
        Map<String, String> responseMap;
        URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/" + API.CREATE_CUSTOMER).toUriString());
        if (userJsonData == null || userJsonData.equals("")) {
            error.put(Constants.JSON_ERROR_MESSAGE, "Body is not valid");
            return ResponseEntity.badRequest().contentType(MediaType.APPLICATION_JSON).body(error);
        }
        Map<String, Object> requestMap = extractMapFromJsonString(userJsonData);
        if (requestMap.isEmpty())
            return ResponseEntity.badRequest().build();
        if (requestMap.containsKey(Constants.JSON_EMAIL) && requestMap.containsKey("ssn")
                && requestMap.containsKey("name") && requestMap.containsKey("surname")
                && requestMap.containsKey("phoneNumber") && requestMap.containsKey("password")
                && requestMap.containsKey("address")
                && Boolean.FALSE.equals(userService.checkPresenceOfMail(requestMap.get(Constants.JSON_EMAIL).toString()))
                && Boolean.FALSE.equals(userService.checkPresenceOfSSN(requestMap.get("ssn").toString()))
        ) {
            userService.addNewCustomer(new Customer(requestMap.get("name").toString(), requestMap.get("surname").toString(),
                    requestMap.get("ssn").toString(), requestMap.get("phoneNumber").toString(),
                    requestMap.get(Constants.JSON_EMAIL).toString(),
                    requestMap.get("password").toString(), requestMap.get("address").toString()));
            tmp = userService.getUserByEmail(requestMap.get(Constants.JSON_EMAIL).toString());
            tmp.setRole("CUSTOMER");
            try {
                JWTProviderImpl jwtProvider = new JWTProviderImpl();
                responseMap = jwtProvider.getFrontEndUSerJWT(new UserDetailsImpl(tmp),
                        request.getRequestURL().toString());
                jwtUserHandlerService.addRelationUserTokens(tmp, responseMap.get("accessToken"), responseMap.get("refreshToken"));
                return ResponseEntity.created(uri).body(responseMap);
            } catch (Exception e) {
                error.put(Constants.JSON_ERROR_MESSAGE, e.getMessage());
                ResponseEntity.badRequest()
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(error);
            }
        }
        error.put(Constants.JSON_ERROR_MESSAGE, "email/ssn already present in the system");
        return ResponseEntity.badRequest()
                .contentType(MediaType.APPLICATION_JSON)
                .body(error);
    }

    @PostMapping(API.PLACE_ORDER)
    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_CUSTOMER','ROLE_EMPLOYEE')")
    public ResponseEntity<Boolean> placeOrder(@RequestBody String jsonData) throws ParseException {
        Map<String, Object> requestMap = extractMapFromJsonString(jsonData);
        Date date = null;
        String address = "";
        if (requestMap.isEmpty())
            return ResponseEntity.badRequest().build();
        if (requestMap.containsKey(Constants.JSON_DELIVERY_DATE)) {
            //Modified by Riki, original version from Ste has some problems on parsing long
            long timeStamp = Long.parseLong(requestMap.get(Constants.JSON_DELIVERY_DATE).toString());
            date = new java.util.Date(timeStamp * 1000);
        }
        if (requestMap.containsKey(Constants.JSON_DELIVERY_ADDRESS)) {
            address = requestMap.get(Constants.JSON_DELIVERY_ADDRESS).toString();
        }
        if (requestMap.containsKey(Constants.JSON_EMAIL) && requestMap.containsKey("customer")) {
            User orderIssuer = userService.getUserByEmail(requestMap.get("customer").toString());
            if (!userService.isOrderUserType(orderIssuer))
                return ResponseEntity.badRequest().build();
            if (requestMap.get(Constants.JSON_EMAIL).toString().isEmpty()) { //It is a customer order
                BasketUserType user = userService.getBasketUserTypeByEmail(orderIssuer.getEmail());
                Basket basket = user.getBasket();
                basketService.dropBasket(basket);
                return ResponseEntity.ok(orderService.addNewOrderFromBasket(basket, (OrderUserType) orderIssuer, (long) System.currentTimeMillis() + this.timeOffset, date, address));
            }
            //It's an order provided by the shopEmployee
            BasketUserType user = userService.getBasketUserTypeByEmail((String) requestMap.get(Constants.JSON_EMAIL));
            Basket basket = user.getBasket();
            basketService.dropBasket(basket);
            return ResponseEntity.ok(orderService.addNewOrderFromBasket(basket, (OrderUserType) orderIssuer, (long) System.currentTimeMillis() + this.timeOffset, date, address));
        }
        return ResponseEntity.badRequest().build();
    }

    @PostMapping(API.ADD_TO_BASKET)
    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_EMPLOYEE','ROLE_CUSTOMER')")
    public ResponseEntity<Map<String, String>> addToBasket(@RequestBody String jsonData) {
        Map<String, Object> requestMap = extractMapFromJsonString(jsonData);
        Map<String, String> response = new HashMap<>();
        if (requestMap.isEmpty())
            return ResponseEntity.badRequest().build();
        if (requestMap.containsKey(Constants.JSON_PRODUCT_ID) && requestMap.containsKey(Constants.JSON_EMAIL) && requestMap.containsKey(Constants.JSON_QUANTITY)) {
            Product product = productService.getProductById(Long.valueOf((Integer) requestMap.get(Constants.JSON_PRODUCT_ID)));
            Double quantity = Double.valueOf(requestMap.get(Constants.JSON_QUANTITY).toString());
            BasketUserType user = userService.getBasketUserTypeByEmail((String) requestMap.get(Constants.JSON_EMAIL));
            if (user == null || product == null || !basketService.addProductToBasket(product, quantity, user))
                return ResponseEntity.badRequest().build();
            response.put("responseStatus", "200-OK");
            return ResponseEntity.ok(response);
        }
        return ResponseEntity.badRequest().build();
    }

    @GetMapping(API.GET_CART)
    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_EMPLOYEE','ROLE_CUSTOMER')")
    public ResponseEntity<List<Product>> getBasket(@RequestParam String email) {
        BasketUserType user = userService.getBasketUserTypeByEmail(email);
        if (user == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(basketService.getProductsInBasket(user));
    }

    @GetMapping(API.GET_WALLET)
    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_EMPLOYEE','ROLE_CUSTOMER')")
    public ResponseEntity<Double> getWallet(@RequestParam String email) {
        if (!Boolean.TRUE.equals(userService.checkPresenceOfMail(email)))
            return ResponseEntity.badRequest().build();
        return ResponseEntity.ok(userService.getWallet(email));
    }

    @PostMapping(API.TOP_UP)
    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_EMPLOYEE')")
    public ResponseEntity<Double> topUp(@RequestBody String jsonData) {
        Map<String, Object> requestMap = extractMapFromJsonString(jsonData);
        if (requestMap.isEmpty())
            return ResponseEntity.badRequest().build();
        if (requestMap.containsKey(Constants.JSON_EMAIL) && requestMap.containsKey("value")) {
            String email = (String) requestMap.get(Constants.JSON_EMAIL);
            Double value = Double.valueOf(requestMap.get("value").toString());
            if (!userService.checkPresenceOfMail(email) || value <= 0)
                return ResponseEntity.badRequest().build();
            if (!userService.topUp(email, value))
                return ResponseEntity.badRequest().build();
            return ResponseEntity.ok(userService.getWallet(email));
        }
        return ResponseEntity.badRequest().build();
    }

    @PostMapping(API.DELIVER_ORDER)
    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_EMPLOYEE')")
    public ResponseEntity<Boolean> deliverOrder(@RequestBody Long orderId) {
        //record payment
        walletOperationService.recordPayment(orderId);
        //Process order
        return ResponseEntity.ok(orderService.deliverOrder(orderId));
    }

    @DeleteMapping(API.DROP_ORDER)
    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_CUSTOMER','ROLE_EMPLOYEE')")
    public ResponseEntity<Map<String, String>> dropOrder(@RequestBody String jsonData) {
        Map<String, Object> requestMap = extractMapFromJsonString(jsonData);
        if (requestMap.isEmpty())
            return ResponseEntity.badRequest().build();
        if (requestMap.containsKey(Constants.JSON_EMAIL)) {
            String email = (String) requestMap.get(Constants.JSON_EMAIL);
            BasketUserType user = userService.getBasketUserTypeByEmail(email);
            if (user == null) {
                return ResponseEntity.badRequest().build();
            }
            basketService.dropBasket(user); //THAT'S HIM, OFFICER!
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.badRequest().build();
    }

    @GetMapping(API.GET_ORDERS_BY_EMAIL)
    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_EMPLOYEE','ROLE_CUSTOMER')")
    public ResponseEntity<String> getOrdersByEmail(@RequestParam String email) {
        if (email == null)
            return ResponseEntity.badRequest().build();
        User user = userService.getUserByEmail(email);
        if (user == null) return ResponseEntity.badRequest().build();
        String response = orderService.getUserOrdersProductsJson(user.getUserId());
        if (response.isEmpty())
            return ResponseEntity.badRequest().build();
        return ResponseEntity.ok(response);
    }

    @GetMapping(API.GET_ORDERS)
    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_EMPLOYEE')")
    public ResponseEntity<String> getOrders() {
        String response = orderService.getAllOrdersProductJson();
        if (response.isEmpty())
            return ResponseEntity.badRequest().build();
        return ResponseEntity.ok(response);
    }

    @GetMapping(API.GET_WALLET_OPERATIONS)
    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_CUSTOMER','ROLE_EMPLOYEE')")
    public ResponseEntity<Map<String, Object>> getWalletOperation(@RequestParam String email) {
        Map<String, Object> responseMap = new HashMap<>();
        if (Boolean.FALSE.equals(userService.checkPresenceOfMail(email))) {
            responseMap.put(Constants.JSON_ERROR_MESSAGE, "Invalid email");
            return ResponseEntity.badRequest().body(responseMap);
        }
        //List<WalletOperation> walletOperations = walletOperationService.getWalletOperationsByEmail(email);
        Double walletValue = userService.getWallet(email);

        responseMap.put("walletValue", walletValue);
        responseMap.put("operations", walletOperationService.getWalletOperationsByEmail(email));
        return ResponseEntity.ok(responseMap);
    }

    @GetMapping(API.RETRIEVE_ERROR)
    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_CUSTOMER','ROLE_EMPLOYEE')")
    public ResponseEntity<Map<String, String>> retrieveError(@RequestParam String email) {
        //Warning! only customer have a wallet and therefore this will send an error
        OrderUserType user = userService.getOrderUserTypeByEmail(email);
        Map<String, String> response = new HashMap<String, String>();
        if (user == null) {
            //the user isn't a customer and therefore has no wallet
            response.put(Constants.JSON_EXIST, "false");
        } else {
            Double total = orderService.getTotalPrice(((User) user).getUserId());
            if (total > user.getWallet()) {
                response.put(Constants.JSON_EXIST, "true");
                response.put("message", "Balance insufficient, remember to top up!");
            } else
                response.put(Constants.JSON_EXIST, "false");
        }
        return ResponseEntity.ok(response);
    }

    @GetMapping(API.EXPECTED_PRODUCTS)
    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_EMPLOYEE','ROLE_FARMER')")
    public ResponseEntity<List<Product>> expectedProducts() {
        //returns all products with their respective validity dates
        List<Product> response = productService.getAllProduct();
        return ResponseEntity.ok(response);
    }


    @PostMapping(API.REPORT_EXPECTED)
    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_EMPLOYEE','ROLE_FARMER')")
    public ResponseEntity<Map<String, String>> reportExpected(@RequestBody String jsonData) {
        // sets expected values for products
        Map<String, Object> requestMap = extractMapFromJsonString(jsonData);
        Product product;
        Double forecast;
        /*String start;
        String end;
         */
        if (requestMap.isEmpty() ||
                !requestMap.containsKey(Constants.JSON_PRODUCT_ID) ||
                !requestMap.containsKey(Constants.JSON_QUANTITY)
        )
            return ResponseEntity.badRequest().build();
        Long productId = Long.parseLong(requestMap.get(Constants.JSON_PRODUCT_ID).toString());
        forecast = Double.valueOf(requestMap.get(Constants.JSON_QUANTITY).toString());
        /*start = (Double) requestMap.get("quantityForecast");
        end = (Double) requestMap.get("quantityForecast");*/
        if (!productService.setForecast(productId, forecast))
            return ResponseEntity.badRequest().build();
        return ResponseEntity.ok().build();
    }

    @PostMapping(API.REPORT_CONFIRMED)
    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_EMPLOYEE','ROLE_FARMER')")
    public ResponseEntity<Map<String, String>> reportConfirmed(@RequestBody String jsonData) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(jsonData);
        if (!jsonNode.isArray())
            return ResponseEntity.badRequest().build();
        for (int i = 0; i < jsonNode.size(); i++) {
            Long productId = jsonNode.get(i).get("productId").asLong();
            Double quantityConfirmed = jsonNode.get(i).get("quantityConfirmed").asDouble();
            if (!productService.confirmQuantity(productId, quantityConfirmed))
                return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok().build();
    }

    @PostMapping(API.DELIVERY_DATE)
    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_EMPLOYEE','ROLE_FARMER')")
    public ResponseEntity<Boolean> setDeliveryDate(@RequestBody String jsonData) throws ParseException {
        // sets expected values for products
        Map<String, Object> requestMap = extractMapFromJsonString(jsonData);
        if (requestMap.isEmpty() ||
                !requestMap.containsKey(Constants.JSON_ORDER_ID) ||
                !requestMap.containsKey(Constants.JSON_DELIVERY_DATE)
        ) return ResponseEntity.badRequest().build();
        long timeStamp = Long.parseLong(requestMap.get(Constants.JSON_DELIVERY_DATE).toString());
        Date date = new java.util.Date(timeStamp);
        //Date date = new SimpleDateFormat("dd/MM/yyyy").parse(requestMap.get(Constants.JSON_DELIVERY_DATE).toString());
        Long orderId = Long.parseLong(requestMap.get(Constants.JSON_ORDER_ID).toString());
        if (!orderService.setDeliveryDate(orderId, date)) return ResponseEntity.badRequest().build();
        return ResponseEntity.ok().build();
    }

    @PostMapping(API.DELIVERY_DATE_ADDRESS)
    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_EMPLOYEE','ROLE_FARMER')")
    public ResponseEntity<Boolean> setDeliveryDateAndAddress(@RequestBody String jsonData) throws ParseException {
        Map<String, Object> requestMap = extractMapFromJsonString(jsonData);
        if (requestMap.isEmpty() ||
                !requestMap.containsKey(Constants.JSON_ORDER_ID) ||
                !requestMap.containsKey(Constants.JSON_DELIVERY_DATE) ||
                !requestMap.containsKey(Constants.JSON_DELIVERY_ADDRESS)
        ) return ResponseEntity.badRequest().build();
        long timeStamp = Long.parseLong(requestMap.get(Constants.JSON_DELIVERY_DATE).toString());
        Date date = new java.util.Date(timeStamp);
        String address = requestMap.get(Constants.JSON_DELIVERY_DATE).toString();
        Long orderId = Long.parseLong(requestMap.get(Constants.JSON_ORDER_ID).toString());
        if (!orderService.setDeliveryDateAndAddress(orderId, date, address)) return ResponseEntity.badRequest().build();
        return ResponseEntity.ok().build();
    }

    @GetMapping(API.REFRESH_TOKEN)
    public void getRefreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String authorizationHeader = request.getHeader(AUTHORIZATION);
        if (authorizationHeader != null && authorizationHeader.startsWith(Constants.HEADER_BEARER)) {
            String refreshToken = authorizationHeader.substring(Constants.HEADER_BEARER.length());
            JWTProviderImpl jwtProvider = new JWTProviderImpl();
            try {
                Map<String, String> responseBody = jwtProvider.verifyRefreshTokenAndRegenerateAccessToken(refreshToken, request.getRequestURL().toString(), this.userService);
                response.setContentType(APPLICATION_JSON_VALUE);
                jwtUserHandlerService.invalidateByUserRefreshTokens(userService.getUserByEmail(responseBody.get(Constants.JSON_EMAIL)), refreshToken);
                String accessToken = responseBody.get("accessToken");
                jwtUserHandlerService.addRelationUserTokens(userService.getUserByEmail(responseBody.get(Constants.JSON_EMAIL)),
                        accessToken, refreshToken);
                new ObjectMapper().writeValue(response.getOutputStream(), responseBody);
            } catch (Exception e) {
                log.error("Error  refreshing token: " + e.getMessage());
                response.setHeader(Constants.HEADER_ERROR, e.getMessage());
                response.setStatus(FORBIDDEN.value());
                response.setContentType(APPLICATION_JSON_VALUE);
                new ObjectMapper().writeValue(response.getOutputStream(), jwtProvider.setErrorMessage(e));
            }
        } else {
            response.setHeader(Constants.HEADER_ERROR, "Headers value does not match!");
            response.setStatus(BAD_REQUEST.value());
            response.setContentType(APPLICATION_JSON_VALUE);
        }
    }


    @PostMapping(API.TIME_TRAVEL)
    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_CUSTOMER', 'ROLE_EMPLOYEE','ROLE_FARMER')")
    public ResponseEntity<Boolean> timeTravel(@RequestBody String jsonData) {
        Map<String, Object> requestMap = extractMapFromJsonString(jsonData);
        if (requestMap == null || !requestMap.containsKey(Constants.JSON_EPOCH_TIME))
            return ResponseEntity.badRequest().build();
        Long epochTime = Long.parseLong(requestMap.get(Constants.JSON_EPOCH_TIME).toString());
        return ResponseEntity.ok(schedulerService.timeTravelAt(epochTime));

        /*
        Map<String, Object> requestMap = extractMapFromJsonString(jsonData);
        if (requestMap == null ||
                !requestMap.containsKey(Constants.JSON_DATE) ||
                !requestMap.containsKey(Constants.JSON_TIME)
        )
            return ResponseEntity.badRequest().build();
        String travelDayOfWeek = requestMap.get(Constants.JSON_DATE).toString();
        String hhmm = requestMap.get(Constants.JSON_TIME).toString();
        int hh = Integer.parseInt(hhmm.split(":")[0]);
        int mm = Integer.parseInt(hhmm.split(":")[1]);
        java.util.Date time = new java.util.Date((long) System.currentTimeMillis() + this.timeOffset);
        Calendar c = Calendar.getInstance();
        c.setTime(time);
        int dayOfWeek = c.get(Calendar.DAY_OF_WEEK);
        int dayToTravel = 0;
        switch (travelDayOfWeek) {
            case "Sun":
                dayToTravel = 1;
                break;
            case "Mon":
                dayToTravel = 2;
                break;
            case "Tue":
                dayToTravel = 3;
                break;
            case "Wed":
                dayToTravel = 4;
                break;
            case "Thu":
                dayToTravel = 5;
                break;
            case "Fri":
                dayToTravel = 6;
                break;
            case "Sat":
                dayToTravel = 7;
                break;
            default:
                dayToTravel = 0;
        }
        timeOffset += ((7 + dayToTravel - dayOfWeek) % 7) * 24 * 60 * 60 * 1000;
        int currentHH = c.get(Calendar.HOUR_OF_DAY);
        int currentMM = c.get(Calendar.MINUTE);
        timeOffset += (hh - currentHH) * 60 * 60 * 1000;
        timeOffset += (mm - currentMM) * 60 * 1000;
        return ResponseEntity.ok().build();
        */
    }

    @GetMapping(API.LOGOUT)
    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_CUSTOMER', 'ROLE_EMPLOYEE','ROLE_FARMER')")
    public void doLogout(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String authorizationHeader = request.getHeader(AUTHORIZATION);
        if (authorizationHeader != null && authorizationHeader.startsWith(Constants.HEADER_BEARER)) {
            String accessToken = authorizationHeader.substring(Constants.HEADER_BEARER.length());
            JWTProviderImpl jwtProvider = new JWTProviderImpl();
            try {
                Map<String, String> okResponseBody = new HashMap<>();
                okResponseBody.put("status", "successful (user logged out correctly)");
                String username = jwtProvider.verifyAccessToken(accessToken);
                User user = userService.getUserByEmail(username);
                jwtUserHandlerService.invalidateUserTokens(user, accessToken);
                response.setStatus(OK.value());
                response.setContentType(APPLICATION_JSON_VALUE);
                new ObjectMapper().writeValue(response.getOutputStream(), okResponseBody);
            } catch (Exception e) {
                log.error("Error logging out: " + e.getMessage());
                response.setHeader(Constants.HEADER_ERROR, e.getMessage());
                response.setStatus(FORBIDDEN.value());
                response.setContentType(APPLICATION_JSON_VALUE);
                new ObjectMapper().writeValue(response.getOutputStream(), jwtProvider.setErrorMessage(e));
            }
        } else {
            response.setHeader(Constants.HEADER_ERROR, "Headers value does not match!");
            response.setStatus(BAD_REQUEST.value());
            response.setContentType(APPLICATION_JSON_VALUE);
        }
    }


    public Map<String, Object> extractMapFromJsonString(String jsonData) {
        ObjectMapper mapper = new ObjectMapper();
        Map<String, Object> requestMap;
        try {
            requestMap = mapper.readValue(jsonData, Map.class);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return new HashMap<>();
        }
        return requestMap;
    }

}
