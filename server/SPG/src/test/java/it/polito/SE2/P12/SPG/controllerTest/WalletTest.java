package it.polito.SE2.P12.SPG.controllerTest;

import com.fasterxml.jackson.databind.ObjectMapper;
import it.polito.SE2.P12.SPG.controller.SpgController;
import it.polito.SE2.P12.SPG.entity.Customer;
import it.polito.SE2.P12.SPG.testSecurityConfig.SpringSecurityTestConfig;
import it.polito.SE2.P12.SPG.utils.API;
import it.polito.SE2.P12.SPG.utils.DBUtilsService;
import org.junit.Assert;
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
public class WalletTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private DBUtilsService dbUtilsService;
    @Autowired
    private SpgController spgController;

    @BeforeEach
    public void initContext() {
        dbUtilsService.dropAll();
        dbUtilsService.saveCustomer(new Customer("customer1", "", "123450001",
                "", "customer1@test.com", "password", ""));
        spgController.topUp("{\"email\" : \"customer1@test.com\", \"value\" : \"100.00\"}");

    }

    @Test
    @WithUserDetails("tester@test.com")
    public void getWalletTest() throws Exception {
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/api/" + API.GET_WALLET)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .param("email", "customer1@test.com"))
                .andExpect(status().isOk())
                .andReturn();
        Assertions.assertEquals(100.00, Double.parseDouble(result.getResponse().getContentAsString()), 0.0001);
    }

    @Test
    @WithUserDetails("tester@test.com")
    public void getWalletErrorHandlerTest() throws Exception {
        //user does not exist
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/api/" + API.GET_WALLET)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .param("email", "customer21@test.com"))
                .andExpect(status().isBadRequest())
                .andReturn();
        Assertions.assertTrue(result.getResponse().getContentAsString().isEmpty());
    }

    @Test
    @WithUserDetails("tester@test.com")
    public void topUpTest() throws Exception {
        //Top Up
        String jsonValid = "{\"email\":\"customer1@test.com\", \"value\":\"100.00\"}";
        String jsonInvalidMail = "{\"email\":\"customer123@test.com\", \"value\":100}";
        String jsonInvalidValue = "{\"email\":\"customer1@test.com\", \"value\":100}";
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/api/" + API.TOP_UP)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(jsonValid))
                .andExpect(status().isOk())
                .andReturn();
        Assertions.assertEquals(200.00, Double.parseDouble(result.getResponse().getContentAsString()), 0.0001);
        //Server confirmation
        result = mockMvc.perform(MockMvcRequestBuilders.get("/api/" + API.GET_WALLET)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .param("email", "customer1@test.com"))
                .andExpect(status().isOk())
                .andReturn();
        Assertions.assertEquals(200.00, Double.parseDouble(result.getResponse().getContentAsString()), 0.0001);
    }

    @Test
    @WithUserDetails("tester@test.com")
    public void topUpErrorHandlerTest() throws Exception {
        String jsonInvalidMail = "{\"email\":\"customer123@test.com\", \"value\":100}";
        String jsonNegativeValue = "{\"email\":\"customer1@test.com\", \"value\":-200}";
        String jsonInvalidValue = "{\"email\":\"customer1@test.com\", \"value\":\"stringValue\"}";

        //Invalid mail
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/api/" + API.TOP_UP)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(jsonInvalidMail))
                .andExpect(status().isBadRequest())
                .andReturn();
        Assertions.assertEquals("", result.getResponse().getContentAsString());
        //invalid value
        result = mockMvc.perform(MockMvcRequestBuilders.post("/api/" + API.TOP_UP)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(jsonInvalidMail))
                .andExpect(status().isBadRequest())
                .andReturn();
        Assertions.assertEquals("", result.getResponse().getContentAsString());
        //negative value
        result = mockMvc.perform(MockMvcRequestBuilders.post("/api/" + API.TOP_UP)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(jsonInvalidMail))
                .andExpect(status().isBadRequest())
                .andReturn();
        Assertions.assertEquals("", result.getResponse().getContentAsString());
    }


    @Test
    @WithUserDetails("tester@test.com")
    public void getWalletOperationsErrorHandlerTest() throws Exception {
        //Mail is not present
        MvcResult res = mockMvc.perform(MockMvcRequestBuilders.get(API.HOME + API.GET_WALLET_OPERATIONS+"?email=mario.rossi@gmail.com")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andReturn();
        String responseJson = res.getResponse().getContentAsString();
        Assertions.assertEquals("{\"errorMessage\":\"Invalid email\"}", responseJson);
    }

    @Test
    @WithUserDetails("tester@test.com")
    public void getWalletOperationsTest() throws Exception {
        MvcResult res = mockMvc.perform(MockMvcRequestBuilders.get(API.HOME + API.GET_WALLET_OPERATIONS+"?email=customer1@test.com")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
        String responseJson = res.getResponse().getContentAsString();
        Map<String, String> responseMap = new ObjectMapper().readValue(responseJson, Map.class);
    }


}
