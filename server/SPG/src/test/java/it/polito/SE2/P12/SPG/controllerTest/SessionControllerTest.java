package it.polito.SE2.P12.SPG.controllerTest;

import com.fasterxml.jackson.databind.ObjectMapper;
import it.polito.SE2.P12.SPG.entity.Customer;
import it.polito.SE2.P12.SPG.service.JWTUserHandlerService;
import it.polito.SE2.P12.SPG.service.SpgUserService;
import it.polito.SE2.P12.SPG.utils.API;
import it.polito.SE2.P12.SPG.utils.DBUtilsService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Map;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class SessionControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private DBUtilsService dbUtilsService;
    @Autowired
    private JWTUserHandlerService jwtUserHandlerService;
    @Autowired
    private SpgUserService userService;


    @BeforeEach
    public void initContext() {
        dbUtilsService.dropAll();
        dbUtilsService.saveCustomer(new Customer("customer1", "", "123450001",
                "", "customer@test.com", "password", ""));
    }

    @Test
    public void refreshTokenTest() throws Exception {
        //Perform Authentication
        String refreshToken;
        String oldAccessToken;
        String newAccessToken;
        String newRefreshToken;
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/api/login")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                        .param("username", "customer@test.com")
                        .param("password", "password")
                )
                .andExpect(status().isOk())
                .andReturn();
        //Get tokens
        refreshToken = (new ObjectMapper().readValue(result.getResponse().getContentAsString(), Map.class))
                .get("refreshToken")
                .toString();
        oldAccessToken = (new ObjectMapper().readValue(result.getResponse().getContentAsString(), Map.class))
                .get("accessToken")
                .toString();
        //Request new accessToken sending current refresh token
        result = mockMvc.perform(MockMvcRequestBuilders.get("/api/" + API.REFRESH_TOKEN)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + refreshToken)
                )
                .andExpect(status().isOk())
                .andReturn();
        //Get new accessToken
        newAccessToken = (new ObjectMapper().readValue(result.getResponse().getContentAsString(), Map.class))
                .get("accessToken")
                .toString();
        newRefreshToken = (new ObjectMapper().readValue(result.getResponse().getContentAsString(), Map.class))
                .get("refreshToken")
                .toString();
        Assertions.assertNotEquals(newAccessToken, oldAccessToken);
        Assertions.assertEquals(newRefreshToken, refreshToken);
    }

    @Test
    public void refreshTokenErrorHandlerTest() throws Exception {
        //Request new accessToken with some error
        //Token is not present
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/api/" + API.REFRESH_TOKEN)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer ")
                )
                .andExpect(status().isForbidden())
                .andReturn();
        Assertions.assertNotNull(result.getResponse().getContentAsString());
        String errorMessage = (new ObjectMapper().readValue(result.getResponse().getContentAsString(), Map.class))
                .get("errorMessage")
                .toString();
        Assertions.assertEquals("The token was expected to have 3 parts, but got 1.", errorMessage);
        //Header not present
        result = mockMvc.perform(MockMvcRequestBuilders.get("/api/" + API.REFRESH_TOKEN)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isBadRequest())
                .andReturn();
        Assertions.assertTrue(result.getResponse().getContentAsString().isEmpty());
        errorMessage = result.getResponse().getHeader("error");
        Assertions.assertEquals("Headers value does not match!", errorMessage);
    }

    @Test
    public void refreshTokenErrorHandlerInvalidRefreshTokenTest() throws Exception {
        //Refresh not valid (due to Log out for example)
        //do log in
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/api/login")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                        .param("username", "customer@test.com")
                        .param("password", "password")
                )
                .andExpect(status().isOk())
                .andReturn();
        //Get the token
        String refreshToken = (new ObjectMapper().readValue(result.getResponse().getContentAsString(), Map.class))
                .get("refreshToken")
                .toString();
        String accessToken = (new ObjectMapper().readValue(result.getResponse().getContentAsString(), Map.class))
                .get("accessToken")
                .toString();
        //Invalidate it
        jwtUserHandlerService.invalidateUserTokens(userService.getUserByEmail("customer@test.com"), accessToken);
        //Now try to refresh tokens
        result = mockMvc.perform(MockMvcRequestBuilders.get("/api/" + API.REFRESH_TOKEN)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + refreshToken)
                )
                .andExpect(status().isForbidden())
                .andReturn();
        Assertions.assertNotNull(result.getResponse().getContentAsString());
        String errorMessage = (new ObjectMapper().readValue(result.getResponse().getContentAsString(), Map.class))
                .get("errorMessage")
                .toString();
        Assertions.assertEquals("token is not valid anymore", errorMessage);
    }

    @Test
    public void doLogoutTest() throws Exception {
        //do log in
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/api/login")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                        .param("username", "customer@test.com")
                        .param("password", "password")
                )
                .andExpect(status().isOk())
                .andReturn();
        String accessToken = (new ObjectMapper().readValue(result.getResponse().getContentAsString(), Map.class))
                .get("accessToken")
                .toString();
        //do log out
        result = mockMvc.perform(MockMvcRequestBuilders.get("/api/logout")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + accessToken)

                )
                .andExpect(status().isOk())
                .andReturn();
        Assertions.assertNotNull(result.getResponse().getContentAsString());
        String responseMessage = (new ObjectMapper().readValue(result.getResponse().getContentAsString(), Map.class))
                .get("status")
                .toString();
        Assertions.assertEquals("successful (user logged out correctly)", responseMessage);
    }

    @Test
    public void doLogoutErrorHandlerTest() throws Exception {
        //do log in
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/api/login")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                        .param("username", "customer@test.com")
                        .param("password", "password")
                )
                .andExpect(status().isOk())
                .andReturn();
        String accessToken = (new ObjectMapper().readValue(result.getResponse().getContentAsString(), Map.class))
                .get("accessToken")
                .toString();
        //do log out (wrong token)
        result = mockMvc.perform(MockMvcRequestBuilders.get("/api/logout")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer ")
                )
                .andExpect(status().isBadRequest())
                .andReturn();
        Assertions.assertNotNull(result.getResponse().getContentAsString());
        String errorMessage = (new ObjectMapper().readValue(result.getResponse().getContentAsString(), Map.class))
                .get("errorMessage")
                .toString();
        Assertions.assertEquals("The token was expected to have 3 parts, but got 1.", errorMessage);
        //do logout(no authorization header)
        result = mockMvc.perform(MockMvcRequestBuilders.get("/api/logout")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isForbidden())
                .andReturn();
        Assertions.assertNotNull(result.getResponse().getContentAsString());
        //do logout(invalid user token)
        //invalidate the token
        jwtUserHandlerService.invalidateUserTokens(userService.getUserByEmail("customer@test.com"), accessToken);
        //perform logout
        result = mockMvc.perform(MockMvcRequestBuilders.get("/api/logout")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isForbidden())
                .andReturn();
        Assertions.assertNotNull(result.getResponse().getContentAsString());
    }

}
