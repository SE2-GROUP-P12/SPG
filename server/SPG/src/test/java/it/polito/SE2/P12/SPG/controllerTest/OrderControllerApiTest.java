package it.polito.SE2.P12.SPG.controllerTest;


import com.fasterxml.jackson.databind.ObjectMapper;
import it.polito.SE2.P12.SPG.entity.*;
import it.polito.SE2.P12.SPG.repository.*;
import it.polito.SE2.P12.SPG.service.SpgBasketService;
import it.polito.SE2.P12.SPG.service.SpgOrderService;
import it.polito.SE2.P12.SPG.testSecurityConfig.SpringSecurityTestConfig;
import it.polito.SE2.P12.SPG.utils.API;
import it.polito.SE2.P12.SPG.utils.DBUtilsService;
import it.polito.SE2.P12.SPG.utils.OrderStatus;
import it.polito.SE2.P12.SPG.utils.Utilities;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import javax.lang.model.type.ReferenceType;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        classes = SpringSecurityTestConfig.class
)
@AutoConfigureMockMvc
public class OrderControllerApiTest {
    @Autowired
    private SpgOrderService orderService;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ShopEmployeeRepo shopEmployeeRepo;
    @Autowired
    private FarmerRepo farmerRepo;
    @Autowired
    private CustomerRepo customerRepo;
    @Autowired
    private ProductRepo productRepo;
    @Autowired
    private SpgBasketService basketService;
    @Autowired
    private DBUtilsService dbUtilsService;


    @BeforeEach
    public void initContext() {
        dbUtilsService.dropAll();
        Customer customer1 = new Customer("customer1", "", "123450001", "", "customer1@test.com", "password", "", 1000.0);
        Customer customer2 = new Customer("customer2", "", "123450000", "", "customer2@test.com", "password", "", 1000.0);
        Customer customer3 = new Customer("customer3", "", "123450002", "", "customer3@test.com", "password", "", 0.00);
        Customer customer4 = new Customer("customer4", "", "123450042", "", "customer4@test.com", "password", "", 0.00);


        ShopEmployee employee = new ShopEmployee("employee", "", "employee_123456", "", "employee@test.com", "password");
        customerRepo.save(customer1);
        customerRepo.save(customer2);
        customerRepo.save(customer3);
        customerRepo.save(customer4);
        shopEmployeeRepo.save(employee);
        Farmer farmer = new Farmer("farmer_name", "farmer_surname", "ssn_faaaaaaarmer", "12345667", "far@mer.com", "password");
        farmerRepo.save(farmer);
        //Create some testing product
        Product prod1 = new Product("Prod1", "KG", 1000.0, 10.50F, farmer);
        Product prod2 = new Product("Prod2", "KG", 100.0, 5.50F, farmer);
        Product prod3 = new Product("Prod3", "KG", 20.0, 8.00F, farmer);
        productRepo.save(prod1);
        productRepo.save(prod2);
        productRepo.save(prod3);
        //Add products to basket
        //done by shopEmployee (in test will be for customer1)
        basketService.addProductToBasket(prod1, 2.001, employee);
        basketService.addProductToBasket(prod3, 5.001, employee);
        //done by customer2
        basketService.addProductToBasket(prod1, 2.001, customer2);
        basketService.addProductToBasket(prod2, 5.001, customer2);
        basketService.addProductToBasket(prod1, 20.00, customer3);
        //Issue order for customer3
        orderService.addNewOrderFromBasket(customer3.getBasket(), customer3, 0, null, null);
        basketService.addProductToBasket(prod1, 20.00, customer3);
        //orderService.addNewOrderFromBasket(customer3.getBasket(), customer3, 123);
        Map<Product, Double> prods = new HashMap<>();
        prods.put(dbUtilsService.getProd1Object(), 3.00);
        Order orderUnRetrieved = new Order(customer4, System.currentTimeMillis(), prods, null, "");
        orderUnRetrieved.setStatus(OrderStatus.ORDER_STATUS_NOT_RETRIEVED);
        dbUtilsService.saveOrder(orderUnRetrieved);
    }

    @Test
    @WithUserDetails("tester@test.com")
    public void placeOrderInvalidBodyTest() throws Exception {
        //Not json format
        mockMvc.perform(MockMvcRequestBuilders.post("/api/" + API.PLACE_ORDER)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("an invalid string")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest()).andReturn();
        //Not correct field
        String invalidJsonFeString = "{\"wrongField1\":\"field1\", \"wrongField2\":\"field2\"}";
        mockMvc.perform(MockMvcRequestBuilders.post("/api/" + API.PLACE_ORDER)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidJsonFeString)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest()).andReturn();
    }

    @Test
    @WithUserDetails("tester@test.com")
    public void placeOrderValidBodyTest() throws Exception {
        String jsonFeString = "{\"email\":\"employee@test.com\", \"customer\":\"customer1@test.com\"}";
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/api/" + API.PLACE_ORDER)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonFeString)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()).andReturn();
        Assertions.assertNotNull(result.getResponse().getContentAsString());
        Assertions.assertEquals("true", result.getResponse().getContentAsString());
    }

    @Test
    @WithUserDetails("tester@test.com")
    public void dropOrderValidBodyTest() throws Exception {
        String jsonFeString = "{\"email\":\"customer2@test.com\"}";
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.delete("/api/" + API.DROP_ORDER)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonFeString)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()).andReturn();
        Assertions.assertTrue(result.getResponse().getContentAsString().isEmpty());
    }

    @Test
    @WithUserDetails("tester@test.com")
    public void dropOrderInvalidBodyTest() throws Exception {
        //wrong json key
        String jsonFeString = "{\"wrongField\":\"customer1@test.com\"}";
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.delete("/api/" + API.DROP_ORDER)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonFeString)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest()).andReturn();
        Assertions.assertTrue(result.getResponse().getContentAsString().isEmpty());
        //empty string (null is not accepted into the content offered by MockMvc interface)
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/" + API.DROP_ORDER)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest()).andReturn();
        //not present mail
        jsonFeString = "{\"email\":\"invalidMail@test.com\"}";
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/" + API.DROP_ORDER)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonFeString)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest()).andReturn();
    }

    @Test
    @WithUserDetails("tester@test.com")
    public void getOrderTest() throws Exception {
        /*SEE INITCONTEXT FOR ORDER SPECIFICS*/
        //Issue an order(done by customer)
        mockMvc.perform(MockMvcRequestBuilders.post("/api/" + API.PLACE_ORDER)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"email\":\"\", \"customer\":\"customer2@test.com\"}")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        //Issue am order(done by shop employee)
        mockMvc.perform(MockMvcRequestBuilders.post("/api/" + API.PLACE_ORDER)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"email\":\"employee@test.com\", \"customer\":\"customer1@test.com\"}")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        //Get all orders
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/api/" + API.GET_ORDERS)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()).andReturn();
        String responseJson = result.getResponse().getContentAsString();
        //Check on order issuers
        List<String> issuerList = Utilities.getDeserializedOrderIssuerList(responseJson);
        Assertions.assertTrue(issuerList.contains("customer2@test.com"));
        Assertions.assertTrue(issuerList.contains("customer1@test.com"));
        Assertions.assertFalse(issuerList.contains("employee@test.com"));
        //Check on order products
        List<Map<String, Double>> productListCustomer2 = Utilities.getDeserializedOrderProductsByEmail(responseJson, "customer2@test.com");
        Assertions.assertEquals(1, productListCustomer2.size());
        Assertions.assertTrue(productListCustomer2.get(0).containsKey("Prod1"));
        Assertions.assertTrue(productListCustomer2.get(0).containsKey("Prod2"));
        Assertions.assertEquals(2, productListCustomer2.get(0).size());
        Assertions.assertEquals(2.001, productListCustomer2.get(0).get("Prod1"), 0.000001);
        Assertions.assertEquals(5.001, productListCustomer2.get(0).get("Prod2"), 0.000001);
    }

    @Test
    @WithUserDetails("tester@test.com")
    public void getOrderEmptyOrderListTest() throws Exception {
        //No issued orders returns 200 and empty json-list
        dbUtilsService.deleteOrderRepo();
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/api/" + API.GET_ORDERS)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()).andReturn();
        String response = result.getResponse().getContentAsString();
        Assertions.assertEquals("[]", response);
    }

    @Test
    @WithUserDetails("tester@test.com")
    public void getOrderByEmail() throws Exception {
        //Issue order for customer2@test.com
        mockMvc.perform(MockMvcRequestBuilders.post("/api/" + API.PLACE_ORDER)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"email\":\"\", \"customer\":\"customer2@test.com\"}")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        //let's perform the request
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/api/" + API.GET_ORDERS_BY_EMAIL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .param("email", "customer2@test.com"))
                .andExpect(status().isOk()).andReturn();
        String responseJson = result.getResponse().getContentAsString();
        Assertions.assertNotNull(responseJson);
        List<Map<String, Double>> productListCustomer2 = Utilities.getDeserializedOrderProductsByEmail(responseJson, "customer2@test.com");
        Assertions.assertEquals(1, productListCustomer2.size());
        Assertions.assertTrue(productListCustomer2.get(0).containsKey("Prod1"));
        Assertions.assertTrue(productListCustomer2.get(0).containsKey("Prod2"));
        Assertions.assertEquals(2, productListCustomer2.get(0).size());
        Assertions.assertEquals(2.001, productListCustomer2.get(0).get("Prod1"), 0.000001);
        Assertions.assertEquals(5.001, productListCustomer2.get(0).get("Prod2"), 0.000001);
    }

    @Test
    @WithUserDetails("tester@test.com")
    public void getOrderByEmailErrorHandlerTest() throws Exception {
        //Issue order for customer2@test.com
        mockMvc.perform(MockMvcRequestBuilders.post("/api/" + API.PLACE_ORDER)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"email\":\"\", \"customer\":\"customer2@test.com\"}")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        //let's perform some bad request
        //invalid mail
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/api/" + API.GET_ORDERS_BY_EMAIL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .param("email", "customer123@test.com"))
                .andExpect(status().isBadRequest()).andReturn();
        Assertions.assertTrue(result.getResponse().getContentAsString().isEmpty());
        //User exists but does not have issued orders
        result = mockMvc.perform(MockMvcRequestBuilders.get("/api/" + API.GET_ORDERS_BY_EMAIL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .param("email", "customer1@test.com"))
                .andExpect(status().isOk()).andReturn();
        Assertions.assertEquals("[]", result.getResponse().getContentAsString());
    }

    @Test
    @WithUserDetails("tester@test.com")
    public void deliverOrderTest() throws Exception {
        //Issue order for customer2@test.com
        mockMvc.perform(MockMvcRequestBuilders.post("/api/" + API.PLACE_ORDER)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"email\":\"\", \"customer\":\"customer2@test.com\"}")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        //Now proceed with deliver order
        //Get the order from a specific email(suppose one order at time else complexity increase in initContext())
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/api/" + API.GET_ORDERS_BY_EMAIL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .param("email", "customer2@test.com"))
                .andExpect(status().isOk()).andReturn();
        //Get orderId from it
        Long orderId = Utilities.getOrderIdFromResponse(result.getResponse().getContentAsString(), "customer2@test.com");
        Assertions.assertNotNull(orderId);
        //deliverOrder in action
        result = mockMvc.perform(MockMvcRequestBuilders.post("/api/" + API.DELIVER_ORDER)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(String.valueOf(orderId)))
                .andExpect(status().isOk()).andReturn();
        String response = result.getResponse().getContentAsString();
        Assertions.assertEquals("true", response);
    }

    @WithUserDetails("tester@test.com")
    @Test
    public void getPendingOrderTest() throws Exception {
        //Pending order created in the initContext()
        //Customer2 not has pending orders
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/api/mail/getPendingOrdersEmail")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
        Map<String, List<Long>> response = new ObjectMapper().readValue(result.getResponse().getContentAsString(), Map.class);
        Assertions.assertTrue(response.containsKey("customer3@test.com"));
    }

    @WithUserDetails("tester@test.com")
    @Test
    public void getUnRetrievedOrderTest() throws Exception {
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/api/manager/getUnRetrievedOrders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
        String jsonResult = result.getResponse().getContentAsString();
        Assertions.assertNotEquals("[]", jsonResult);
    }

}
