package it.polito.SE2.P12.SPG.securityFiltersTest;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import it.polito.SE2.P12.SPG.entity.Admin;
import it.polito.SE2.P12.SPG.entity.Product;
import it.polito.SE2.P12.SPG.entity.User;
import it.polito.SE2.P12.SPG.repository.JWTUserHandlerRepo;
import it.polito.SE2.P12.SPG.repository.UserRepo;
import it.polito.SE2.P12.SPG.service.JWTUserHandlerService;
import it.polito.SE2.P12.SPG.utils.API;
import it.polito.SE2.P12.SPG.utils.DBUtilsService;
import it.polito.SE2.P12.SPG.utils.Utilities;
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

import java.util.List;
import java.util.Map;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class AuthorizationFilterTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private UserRepo userRepo;
    @Autowired
    private JWTUserHandlerRepo jwtUserHandlerRepo;
    @Autowired
    private JWTUserHandlerService jwtUserHandlerService;
    @Autowired
    private DBUtilsService dbUtilsService;

    @BeforeEach
    public void initContext() {
        dbUtilsService.dropAll();
        jwtUserHandlerRepo.deleteAll();
        dbUtilsService.loadTestingProds();
        User tester = new Admin("tester", "tester", "tester_aaaaaaaaaaaa", "", "tester@test.com", "password");
        userRepo.save(tester);
    }

    @AfterEach
    public void restDB() {
        jwtUserHandlerRepo.deleteAll();
    }

    @Test
    public void AuthorizationFilterTest() throws Exception {
        //Do log in, in order to get valid access token
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/api/login")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                        .param("username", "tester@test.com")
                        .param("password", "password"))
                .andExpect(status().isOk())
                .andReturn();
        //Map response body
        Map<String, String> response = new ObjectMapper().readValue(result.getResponse().getContentAsString(), Map.class);
        String accessToken = response.get("accessToken");
        //Request products require user authentication. TODO: for now
        result = mockMvc.perform(MockMvcRequestBuilders.get("/api/" + API.ALL_PRODUCT)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(AUTHORIZATION, "Bearer " + accessToken))
                .andExpect(status().isOk())
                .andReturn();
        //Map response body
        List<Product> productList = Utilities.getDeserializedProductList(result.getResponse().getContentAsString());
        Assertions.assertTrue(productList.size() > 0);
    }

    @Test
    public void AuthorizationFilterTestErrorHandler() throws Exception {
        //Do log in, in order to get valid access token
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/api/login")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                        .param("username", "tester@test.com")
                        .param("password", "password"))
                .andExpect(status().isOk())
                .andReturn();
        Map<String, String> response = new ObjectMapper().readValue(result.getResponse().getContentAsString(), Map.class);
        String accessToken = response.get("accessToken");
        //empty access token
        result = mockMvc.perform(MockMvcRequestBuilders.get("/api/" + API.ALL_PRODUCT)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(AUTHORIZATION, ""))
                .andExpect(status().isForbidden())
                .andReturn();
        //jwt not valid token(maybe due to a logout or token expiration)
        //Token invalidation
        jwtUserHandlerService.invalidateUserTokens(userRepo.findUserByEmail("tester@test.com"), accessToken);
        result = mockMvc.perform(MockMvcRequestBuilders.get("/api/" + API.ALL_PRODUCT)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(AUTHORIZATION, "Bearer " + accessToken))
                .andExpect(status().isBadRequest())
                .andReturn();
    }
}
