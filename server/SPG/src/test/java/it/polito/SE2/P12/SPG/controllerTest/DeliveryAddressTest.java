package it.polito.SE2.P12.SPG.controllerTest;

import it.polito.SE2.P12.SPG.entity.Customer;
import it.polito.SE2.P12.SPG.interfaceEntity.OrderUserType;
import it.polito.SE2.P12.SPG.service.SpgBasketService;
import it.polito.SE2.P12.SPG.service.SpgOrderService;
import it.polito.SE2.P12.SPG.service.SpgUserService;
import it.polito.SE2.P12.SPG.testSecurityConfig.SpringSecurityTestConfig;
import it.polito.SE2.P12.SPG.utils.API;
import it.polito.SE2.P12.SPG.utils.DBUtilsService;
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

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        classes = SpringSecurityTestConfig.class
)
@AutoConfigureMockMvc
public class DeliveryAddressTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private DBUtilsService dbUtilsService;
    @Autowired
    private SpgBasketService basketService;
    @Autowired
    private SpgOrderService orderService;
    private Long orderId;


    @BeforeEach
    public void initContext() {
        dbUtilsService.dropAll();
        //create customer, get product, issue an order
        dbUtilsService.loadTestingProds();
        Customer customer = new Customer("customer", "customer", "CUST_10293847865", "1234567890",
                "customer@text.com", "password", "address");
        dbUtilsService.saveCustomer(customer);
        basketService.addProductToBasket(dbUtilsService.getProd1Object(), 5.00, customer);
        orderService.addNewOrderFromBasket(customer.getBasket(),  customer,0, null, "");
        //set orderId for testing
        orderId = customer.getOrders().get(0).getOrderId();
    }

    @Test
    @WithUserDetails("tester@test.com")
    public void setDeliveryAddressTest() throws Exception {
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post(API.HOME + API.DELIVERY_DATE_ADDRESS)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        //1639740466 is GMT: Friday 17 December 2021 11:27:46
                        // Your time zone: venerdì 17 dicembre 2021 12:27:46 GMT+01:00
                        .content("{\"orderId\":" + orderId + ",\"deliveryDate\":\"1639740466\", \"deliveryAddress\":\"1234 Street, New York\"}")
                ).andExpect(status().isOk())
                .andReturn();

    }

    @Test
    @WithUserDetails("tester@test.com")
    public void setDeliveryAddressErrorTest() throws Exception {
        //Bad order ID
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post(API.HOME + API.DELIVERY_DATE_ADDRESS)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        //1639740466 is GMT: Friday 17 December 2021 11:27:46
                        // Your time zone: venerdì 17 dicembre 2021 12:27:46 GMT+01:00
                        .content("{\"orderId\":" + orderId+1000 + ",\"deliveryDate\":\"1639740466\", \"deliveryAddress\":\"1234 Street, New York\"}")
                ).andExpect(status().isBadRequest())
                .andReturn();
        //Invalid json
        result = mockMvc.perform(MockMvcRequestBuilders.post(API.HOME + API.DELIVERY_DATE_ADDRESS)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        //1639740466 is GMT: Friday 17 December 2021 11:27:46
                        // Your time zone: venerdì 17 dicembre 2021 12:27:46 GMT+01:00
                        .content("invalid json")
                ).andExpect(status().isBadRequest())
                .andReturn();


    }
}
