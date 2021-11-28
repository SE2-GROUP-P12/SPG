package it.polito.SE2.P12.SPG.controllerTest;

import it.polito.SE2.P12.SPG.entity.Customer;
import it.polito.SE2.P12.SPG.entity.Product;
import it.polito.SE2.P12.SPG.testSecurityConfig.SpringSecurityTestConfig;
import it.polito.SE2.P12.SPG.utils.API;
import it.polito.SE2.P12.SPG.utils.DBUtilsService;
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

import java.util.Map;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        classes = SpringSecurityTestConfig.class
)
@AutoConfigureMockMvc
public class BasketControllerTest {
    @Autowired
    private DBUtilsService dbUtilsService;
    @Autowired
    private MockMvc mockMvc;
    private Long prod1Id;
    private Long prod2Id;
    private String jsonProductString;
    private String jsonProductInvalidMailString;
    private String jsonProductInvalidProdId;
    private String getJsonProductStringInvalidQuantity;
    private String jsonProductString1;


    @BeforeEach
    public void initContext() {
        dbUtilsService.dropAll();
        Customer customer1 = new Customer("customer1", "", "123450001",
                "", "customer1@test.com", "password", "");
        Customer customer2 = new Customer("customer2", "", "123450002",
                "", "customer2@test.com", "password", "");
        dbUtilsService.saveCustomer(customer1);
        dbUtilsService.saveCustomer(customer2);
        dbUtilsService.loadTestingProds();
        prod1Id = dbUtilsService.getProd1Object().getProductId();
        prod2Id = dbUtilsService.getProd2Object().getProductId();
        jsonProductString = "{" +
                "    \"productId\": " + prod1Id + "," +
                "    \"email\": \"customer1@test.com\"," +
                "    \"quantity\": 3" +
                "}";
        jsonProductString1 = "{" +
                "    \"productId\": " + prod2Id + "," +
                "    \"email\": \"customer1@test.com\"," +
                "    \"quantity\": 3" +
                "}";
        jsonProductInvalidMailString = "{" +
                "    \"productId\": " + prod1Id + "," +
                "    \"email\": \"customer123@test.com\"," +
                "    \"quantity\": 3" +
                "}";
        jsonProductInvalidProdId = "{" +
                "    \"productId\": 0," +
                "    \"email\": \"customer1@test.com\"," +
                "    \"quantity\": 2" +
                "}";
        getJsonProductStringInvalidQuantity = "{" +
                "    \"productId\": " + prod1Id + "," +
                "    \"email\": \"customer1@test.com\"," +
                "    \"quantity\": 20000" +
                "}";

    }

    @Test
    @WithUserDetails("tester@test.com")
    public void addProdToBasketTest() throws Exception {
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/api/" + API.ADD_TO_BASKET)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(jsonProductString))
                .andExpect(status().isOk())
                .andReturn();
        Assertions.assertEquals("{\"responseStatus\":\"200-OK\"}", result.getResponse().getContentAsString());
    }

    @Test
    @WithUserDetails("tester@test.com")
    public void addProdToBasketErrorHandlerTest() throws Exception {
        //Invalid mail
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/api/" + API.ADD_TO_BASKET)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(jsonProductInvalidMailString))
                .andExpect(status().isBadRequest())
                .andReturn();
        Assertions.assertTrue(result.getResponse().getContentAsString().isEmpty());
        //invalidProduct
        result = mockMvc.perform(MockMvcRequestBuilders.post("/api/" + API.ADD_TO_BASKET)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(jsonProductInvalidProdId))
                .andExpect(status().isBadRequest())
                .andReturn();
        Assertions.assertTrue(result.getResponse().getContentAsString().isEmpty());
        //unavailable quantity
        result = mockMvc.perform(MockMvcRequestBuilders.post("/api/" + API.ADD_TO_BASKET)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(getJsonProductStringInvalidQuantity))
                .andExpect(status().isBadRequest())
                .andReturn();
        Assertions.assertTrue(result.getResponse().getContentAsString().isEmpty());
    }

    @Test
    @WithUserDetails("tester@test.com")
    public void getCartTest() throws Exception {
        //Add 2 product (check the quantity in the initContext vars)
        //1
        mockMvc.perform(MockMvcRequestBuilders.post("/api/" + API.ADD_TO_BASKET)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(jsonProductString))
                .andExpect(status().isOk());
        //2
        mockMvc.perform(MockMvcRequestBuilders.post("/api/" + API.ADD_TO_BASKET)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(jsonProductString1))
                .andExpect(status().isOk());
        //Get the cart
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/api/" + API.GET_CART)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .param("email", "customer1@test.com"))
                .andExpect(status().isOk())
                .andReturn();
        //get response
        String response = result.getResponse().getContentAsString();
        //Utilities.
        Assertions.assertNotNull(result.getResponse().getContentAsString());
        Map<Product, Double> res = Utilities.basketProductDeserializer(response);
        Assertions.assertEquals(2, res.size());
        Assertions.assertTrue(res.keySet().stream().anyMatch(x -> x.getName().equals("Prod1")));
        Assertions.assertTrue(res.keySet().stream().anyMatch(x -> x.getName().equals("Prod2")));
    }

    @Test
    @WithUserDetails("tester@test.com")
    public void getCartNoProductTest() throws Exception {
        //Get the cart
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/api/" + API.GET_CART)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .param("email", "customer2@test.com"))
                .andExpect(status().isOk())
                .andReturn();
        Assertions.assertEquals("[]", result.getResponse().getContentAsString());
    }

    @Test
    @WithUserDetails("tester@test.com")
    public void getCartErrorHandlerTest() throws Exception {
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/api/" + API.GET_CART)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .param("email", "customer123@test.com"))
                .andExpect(status().isNotFound())
                .andReturn();
        Assertions.assertTrue(result.getResponse().getContentAsString().isEmpty());
    }


}
