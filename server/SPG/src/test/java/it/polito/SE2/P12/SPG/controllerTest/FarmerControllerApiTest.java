package it.polito.SE2.P12.SPG.controllerTest;

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

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        classes = SpringSecurityTestConfig.class
)
@AutoConfigureMockMvc
public class FarmerControllerApiTest {
    @Autowired
    private DBUtilsService dbUtilsService;
    @Autowired
    private MockMvc mockMvc;
    private String jsonBodyRequest = "";
    private String jsonBodyMissingFieldRequest = "";

    @BeforeEach
    public void initContext() {
        dbUtilsService.dropAll();
        dbUtilsService.loadTestingProds();
        jsonBodyRequest = "{\"productId\" : " + dbUtilsService.getProd1Object().getProductId() + "," +
                "\"producerId\" : " + dbUtilsService.getProd1Object().getFarmer().getUserId() + "," +
                "\"producer\" : \"producerName\"," +
                "\"quantityForecast\" : 20.00," +
                "\"startAvailability\" : \"12-12-2021\"," +
                "\"endAvailability\" : \"20-12-2021\"," +
                "\"price\" : 2.50," +
                "\"unitOfMeasurement\" : \"KG\"," +
                "\"name\" : \"farmer\"," +
                "}";
        jsonBodyMissingFieldRequest = "{\"productId\" : " + dbUtilsService.getProd1Object().getProductId() + "," +
                "\"producerId\" : " + dbUtilsService.getProd1Object().getFarmer().getUserId() + "," +
                "\"producer\" : \"producerName\"," +
                "\"quantityForecast\" : 20.00," +
                "\"endAvailability\" : \"20-12-2021\"," +
                "\"price\" : 2.50," +
                "\"unitOfMeasurement\" : \"KG\"," +
                "}";
    }

    @Test
    @WithUserDetails("tester@test.com")
    public void ExpectedProductTest() throws Exception {
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/api/" + API.EXPECTED_PRODUCTS)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
        String response = result.getResponse().getContentAsString();
        List<Product> productList = Utilities.getDeserializedProductList(response);
        Assertions.assertTrue(productList.size() > 0);
        Assertions.assertTrue(productList.stream().map(Product::getName).collect(Collectors.toList()).contains("Prod1"));
        Assertions.assertTrue(productList.stream().map(Product::getName).collect(Collectors.toList()).contains("Prod2"));
    }

    @Test
    @WithUserDetails("tester@test.com")
    public void reportExpectedProductTest() throws Exception {
        Long prodId = dbUtilsService.getProd1Object().getProductId();
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/api/" + API.REPORT_EXPECTED)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content("{\"productId\": " + prodId.toString() +
                                ",\n\"quantity\": 1}"))
                .andExpect(status().isOk())
                .andReturn();
        String response = result.getResponse().getContentAsString();
    }

    @Test
    @WithUserDetails("tester@test.com")
    public void reportExpectedProductErrorHandlerTest() throws Exception {
        //empty json body
        mockMvc.perform(MockMvcRequestBuilders.post("/api/" + API.REPORT_EXPECTED)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(""))
                .andExpect(status().isBadRequest());
        //missing fields
        mockMvc.perform(MockMvcRequestBuilders.post("/api/" + API.REPORT_EXPECTED)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(jsonBodyMissingFieldRequest))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithUserDetails("tester@test.com")
    public void addNewProductTest() throws Exception {
        //Correct execution
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/api/" + API.ADD_PRODUCT)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content("{\"email\" : \"farmer1@test.com\"," +
                                "\"productName\" : \"newProduct\"," +
                                "\"price\" : 2.00," +
                                "\"unitOfMeasurement\" : \"KG\"," +
                                "\"imageUrl\" : \"http://localhost/foo.png\"" +
                                "}")
                )
                .andExpect(status().isOk())
                .andReturn();
        String response = result.getResponse().getContentAsString();
        Assertions.assertNotNull(response);
        Assertions.assertEquals("true", response);
    }

    @Test
    @WithUserDetails("tester@test.com")
    public void addNewProductErrorHandlerTest() throws Exception {
        //not present mail
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/api/" + API.ADD_PRODUCT)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content("{\"email\" : \"invalidMail@test.com\"," +
                                "\"productName\" : \"newProduct\"," +
                                "\"price\" : 2.00," +
                                "\"unitOfMeasurement\" : \"KG\"," +
                                "\"imageUrl\" : \"http://localhost/foo.png\"" +
                                "}")
                )
                .andExpect(status().isBadRequest())
                .andReturn();
        String response = result.getResponse().getContentAsString();
        Assertions.assertNotNull(response);
        //No content present in request
        result = mockMvc.perform(MockMvcRequestBuilders.post("/api/" + API.ADD_PRODUCT)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content("")
                )
                .andExpect(status().isBadRequest())
                .andReturn();
        response = result.getResponse().getContentAsString();
        Assertions.assertNotNull(response);
    }

    @Test
    public void addNewProductPermissionErrorTest() throws Exception {
        //not logged user
        mockMvc.perform(MockMvcRequestBuilders.post("/api/" + API.ADD_PRODUCT)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content("{\"email\" : \"invalidMail@test.com\"," +
                                "\"productName\" : \"newProduct\"," +
                                "\"price\" : 2.00," +
                                "\"unitOfMeasurement\" : \"KG\"," +
                                "\"imageUrl\" : \"http://localhost/foo.png\"" +
                                "}")
                )
                .andExpect(status().isForbidden());
    }

    @Test
    @WithUserDetails("tester@test.com")
    public void farmerReportExpectedTest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post(API.HOME + API.REPORT_EXPECTED)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content("{\"productId\": " + dbUtilsService.getProd1Object().getProductId() + ", \"quantity\": 20}")
        ).andExpect(status().isOk());
    }

    @Test
    @WithUserDetails("tester@test.com")
    public void farmerReportExpectedErrorTest() throws Exception {
        //Invalid prod id
        mockMvc.perform(MockMvcRequestBuilders.post(API.HOME + API.REPORT_EXPECTED)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content("{\"productId\": " + (dbUtilsService.getProd1Object().getProductId() + 1000) + ", \"quantity\": 20}")
        ).andExpect(status().isBadRequest());
        //invalid json
        mockMvc.perform(MockMvcRequestBuilders.post(API.HOME + API.REPORT_EXPECTED)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content("invalid json")
        ).andExpect(status().isBadRequest());

    }

    @Test
    @WithUserDetails("tester@test.com")
    public void farmerReportConfirmedTest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post(API.HOME + API.REPORT_CONFIRMED)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content("[{\"productId\": " + dbUtilsService.getProd1Object().getProductId() + ", \"quantityConfirmed\": 20}]")
        ).andExpect(status().isOk());
    }

    @Test
    @WithUserDetails("tester@test.com")
    public void farmerReportConfirmedErrorTest() throws Exception {
        //Invalid prod id
        mockMvc.perform(MockMvcRequestBuilders.post(API.HOME + API.REPORT_CONFIRMED)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content("[{\"productId\": " + (dbUtilsService.getProd1Object().getProductId() + 1000) + ", \"quantityConfirmed\": 20}]")
        ).andExpect(status().isBadRequest());
        //invalid json
        mockMvc.perform(MockMvcRequestBuilders.post(API.HOME + API.REPORT_EXPECTED)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content("invalid json")
        ).andExpect(status().isBadRequest());

    }

    @Test
    @WithUserDetails("tester@test.com")
    public void browseProductByFarmerTest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(API.HOME + API.BROWSE_PRODUCT_BY_FARMER + "?farmer=farmer1@test.com&forecasted=undefined")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
        ).andExpect(status().isOk());
    }

    @Test
    @WithUserDetails("tester@test.com")
    public void browseProductByFarmerErrorTest() throws Exception {
        //Invalid forecasted
        mockMvc.perform(MockMvcRequestBuilders.get(API.HOME + API.BROWSE_PRODUCT_BY_FARMER + "?farmer=farmer1@test.com&forecasted=true")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
        ).andExpect(status().isOk());
        //Invalid email
        mockMvc.perform(MockMvcRequestBuilders.get(API.HOME + API.BROWSE_PRODUCT_BY_FARMER + "?farmer123=farmer1@test.com&forecasted=true")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
        ).andExpect(status().isBadRequest());
    }


}
