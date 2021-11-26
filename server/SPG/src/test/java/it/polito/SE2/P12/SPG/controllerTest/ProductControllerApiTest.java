package it.polito.SE2.P12.SPG.controllerTest;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import it.polito.SE2.P12.SPG.entity.Farmer;
import it.polito.SE2.P12.SPG.entity.Product;
import it.polito.SE2.P12.SPG.repository.FarmerRepo;
import it.polito.SE2.P12.SPG.repository.ProductRepo;
import it.polito.SE2.P12.SPG.repository.UserRepo;
import it.polito.SE2.P12.SPG.testSecurityConfig.SpringSecurityTestConfig;
import it.polito.SE2.P12.SPG.utils.API;
import it.polito.SE2.P12.SPG.utils.DBUtilsService;
import org.junit.jupiter.api.AfterEach;
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

import java.util.ArrayList;
import java.util.List;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        classes = SpringSecurityTestConfig.class
)
@AutoConfigureMockMvc
public class ProductControllerApiTest {
    @Autowired
    private ProductRepo productRepo;
    @Autowired
    private UserRepo userRepo;
    @Autowired
    private FarmerRepo farmerRepo;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private DBUtilsService dbUtilsService;

    @BeforeEach
    public void initContext() {
        dbUtilsService.dropAll();
        //Empty all the product
        productRepo.deleteAll();
        userRepo.deleteAll();
        //Create a farmer
        Farmer farmer = new Farmer("farmer_name", "farmer_surname", "ssn_faaaaaaarmer", "12345667", "far@mer.com", "password");
        farmerRepo.save(farmer);
        //Create some testing product
        Product prod1 = new Product("Prod1", "KG", 1000.0, 10.50F, farmer);
        Product prod2 = new Product("Prod2",  "KG", 100.0, 5.50F, farmer);
        Product prod3 = new Product("Prod3",  "KG", 20.0, 8.00F, farmer);
        productRepo.save(prod1);
        productRepo.save(prod2);
        productRepo.save(prod3);

    }


    @Test
    @WithUserDetails("tester@test.com")
    void listOfProductLoadingTest() throws Exception {
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/api/" + API.ALL_PRODUCT)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()).andReturn();
        String response = result.getResponse().getContentAsString();
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode tree = objectMapper.readTree(response);
        List<Product> productList = new ArrayList<>();
        for (JsonNode i : tree) {
            productList.add(new Product(
                    i.get("name").textValue(),
                    i.get("producer").textValue(),
                    i.get("unitOfMeasurement").textValue(),
                    i.get("totalQuantity").asDouble(),
                    i.get("price").asDouble()
            ));
        }
        Assertions.assertEquals(3, productList.size());
        Assertions.assertEquals("Prod1", productList.get(0).getName());
        Assertions.assertEquals(100.00, productList.get(1).getTotalQuantity());
        Assertions.assertEquals(8.00F, productList.get(2).getPrice());

    }

    @Test
    @WithUserDetails("tester@test.com")
    void emptyListOfProduct() throws Exception {
        productRepo.deleteAll();
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/api/" + API.ALL_PRODUCT)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()).andReturn();
        String response = result.getResponse().getContentAsString();
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode tree = objectMapper.readTree(response);
        List<Product> productList = new ArrayList<>();
        for (JsonNode i : tree) {
            productList.add(new Product(
                    i.get("name").textValue(),
                    i.get("producer").textValue(),
                    i.get("unitOfMeasurement").textValue(),
                    i.get("totalQuantity").asDouble(),
                    i.get("price").asDouble()
            ));
        }

        Assertions.assertEquals(0, productList.size());
    }

    @Test
    @WithUserDetails("user@test.com")
    void unauthorizedUserGetProductTest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/" + API.ALL_PRODUCT)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden()).andReturn();
    }

}
