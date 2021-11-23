package it.polito.SE2.P12.SPG.securityFiltersTest;

import com.fasterxml.jackson.databind.ObjectMapper;
import it.polito.SE2.P12.SPG.entity.User;
import it.polito.SE2.P12.SPG.repository.JWTUserHandlerRepo;
import it.polito.SE2.P12.SPG.repository.UserRepo;
import it.polito.SE2.P12.SPG.testSecurityConfig.SpringSecurityTestConfig;
import it.polito.SE2.P12.SPG.testUtils.SQLUtilities;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.event.annotation.AfterTestClass;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.sql.SQLException;
import java.util.Map;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        classes = SpringSecurityTestConfig.class
)
@AutoConfigureMockMvc
public class AuthenticationFilterTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private UserRepo userRepo;
    @Autowired
    private JWTUserHandlerRepo jwtUserHandlerRepo;


    @BeforeEach
    public void initContext() throws SQLException {
        userRepo.deleteAll();
        User tester = new User("tester", "tester", "tester_aaaaaaaaaaaa", "", "ADMIN", "tester@test.com", "password");
        userRepo.save(tester);
    }



    @Test
    public void attemptAuthenticationTest() throws Exception {
        //Try using login HttpServletRequest
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/api/login")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                        .param("username", "tester@test.com")
                        .param("password", "password"))
                .andExpect(status().isOk())
                .andReturn();
        Map<String, String> responseMap = new ObjectMapper().readValue(result.getResponse().getContentAsString(), Map.class);
        Assertions.assertNotNull(responseMap);
        Assertions.assertEquals(responseMap.get("accessToken"), jwtUserHandlerRepo.findJWTUserHandlerImplByUserId(userRepo.findUserByEmail("tester@test.com").getUserId()).getAccessToken());
        Assertions.assertEquals("tester@test.com", responseMap.get("email"));
    }

    @Test
    public void attemptAuthenticationErrorHandlerTest() throws Exception {
        //Wrong credentials
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/api/login")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("username", "tester@test.com")
                        .param("password", "wrongPassword"))
                .andExpect(status().isUnauthorized())
                .andReturn();
        result = mockMvc.perform(MockMvcRequestBuilders.post("/api/login")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                        .param("username", "unknow@test.com")
                        .param("password", "password"))
                .andExpect(status().isUnauthorized())
                .andReturn();
        result = mockMvc.perform(MockMvcRequestBuilders.post("/api/login")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                        .param("username", "unknow@test.com")
                        .param("password", "wrongPassword"))
                .andExpect(status().isUnauthorized())
                .andReturn();
        //Wrong content type
        result = mockMvc.perform(MockMvcRequestBuilders.post("/api/login")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized()) //TODO: check why thew filter is not able to understand the content type header (SHOULD RETURN 415-UnsupportedMediaType)
                .andReturn();
    }
}
