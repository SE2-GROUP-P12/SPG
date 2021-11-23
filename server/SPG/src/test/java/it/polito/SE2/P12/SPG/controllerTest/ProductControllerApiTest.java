package it.polito.SE2.P12.SPG.controllerTest;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import it.polito.SE2.P12.SPG.entity.Product;
import it.polito.SE2.P12.SPG.repository.ProductRepo;
import it.polito.SE2.P12.SPG.testSecurityConfig.SpringSecurityTestConfig;
import it.polito.SE2.P12.SPG.utils.API;
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

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        classes = SpringSecurityTestConfig.class
)
@AutoConfigureMockMvc
public
class ProductControllerApiTest {
    @Autowired
    private ProductRepo productRepo;
    @Autowired
    private MockMvc mockMvc;

    @BeforeEach
    public void initContext() {
        //Empty all the product
        productRepo.deleteAll();
        //Create some testing product
        Product prod1 = new Product("Prod1", "Producer1", "KG", 1000.0, 10.50F);
        Product prod2 = new Product("Prod2", "Producer2", "KG", 100.0, 5.50F);
        Product prod3 = new Product("Prod3", "Producer3", "KG", 20.0, 8.00F);
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
        List<Product> productList = new ObjectMapper().readValue(result.getResponse().getContentAsString(), new TypeReference<List<Product>>(){});
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
        List<Product> productList = new ObjectMapper().readValue(result.getResponse().getContentAsString(), new TypeReference<List<Product>>(){});
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
