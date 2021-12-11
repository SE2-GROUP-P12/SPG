package it.polito.SE2.P12.SPG.controllerTest;


import com.fasterxml.jackson.databind.ObjectMapper;
import it.polito.SE2.P12.SPG.entity.Customer;
import it.polito.SE2.P12.SPG.service.SpgBasketService;
import it.polito.SE2.P12.SPG.service.SpgOrderService;
import it.polito.SE2.P12.SPG.testSecurityConfig.SpringSecurityTestConfig;
import it.polito.SE2.P12.SPG.utils.API;
import it.polito.SE2.P12.SPG.utils.DBUtilsService;
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

import java.util.Map;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        classes = SpringSecurityTestConfig.class
)
@AutoConfigureMockMvc
public class RetrieveErrorApiTest {
    @Autowired
    private DBUtilsService dbUtilsService;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private SpgBasketService basketService;
    @Autowired
    private SpgOrderService orderService;


    @BeforeEach
    public void initContext() {
        dbUtilsService.dropAll();
        dbUtilsService.loadTestingProds();
        Customer customer = new Customer("customer1", "", "123450001", "",
                "customer@test.com", "password", "", 0.00);
        dbUtilsService.saveCustomer(customer);
        basketService.addProductToBasket(dbUtilsService.getProd1Object(), 2.00, customer);
        basketService.addProductToBasket(dbUtilsService.getProd2Object(), 4.00, customer);
        orderService.addNewOrderFromBasket(customer.getBasket(), (long)System.currentTimeMillis(),null, "");
    }

    @Test
    @WithUserDetails("tester@test.com")
    public void retrieveErrorTopUpRequiredTest() throws Exception {
        //Correct execution (valid email but top up is required)
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/api/" + API.RETRIEVE_ERROR)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .param("email", "customer@test.com"))
                .andExpect(status().isOk())
                .andReturn();
        String response = result.getResponse().getContentAsString();
        Map<String, String> responseMap = new ObjectMapper().readValue(response, Map.class);
        Assertions.assertNotNull(responseMap);
        Assertions.assertEquals("true", responseMap.get("exist"));
        Assertions.assertEquals("Balance insufficient, remember to top up!", responseMap.get("message"));
    }


    @Test
    @WithUserDetails("tester@test.com")
    public void retrieveErrorSufficientBalanceTest() throws Exception {
        //First top up the current balance
        mockMvc.perform(MockMvcRequestBuilders.post("/api/" + API.TOP_UP)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content("{\"email\":\"customer@test.com\", \"value\":100}"))
                .andExpect(status().isOk());
        //Now check no error is retrieved
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/api/" + API.RETRIEVE_ERROR)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .param("email", "customer@test.com"))
                .andExpect(status().isOk())
                .andReturn();
        String response = result.getResponse().getContentAsString();
        Map<String, String> responseMap = new ObjectMapper().readValue(response, Map.class);
        Assertions.assertNotNull(responseMap);
        Assertions.assertEquals("false", responseMap.get("exist"));
    }


    @Test
    @WithUserDetails("tester@test.com")
    public void retrieveErrorInvalidMailTest() throws Exception {
        //Process with invalid mail
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/api/" + API.RETRIEVE_ERROR)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .param("email", "customerNotRegistered@test.com"))
                .andExpect(status().isOk())
                .andReturn();
        String response = result.getResponse().getContentAsString();
        Map<String, String> responseMap = new ObjectMapper().readValue(response, Map.class);
        Assertions.assertNotNull(responseMap);
        Assertions.assertEquals("false", responseMap.get("exist"));
    }


}
