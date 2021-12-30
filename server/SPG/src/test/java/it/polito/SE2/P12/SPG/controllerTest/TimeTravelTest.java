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
public class TimeTravelTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private DBUtilsService dbUtilsService;


    @BeforeEach
    public void initContext() {
        dbUtilsService.dropAll();
    }

    @Test
    @WithUserDetails("tester@test.com")
    public void timeTravelTest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post(API.HOME + API.TIME_TRAVEL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content("{\"epoch time\":\"12345678\"}")
                ).andExpect(status().isOk())
                .andReturn();

    }

    @Test
    @WithUserDetails("tester@test.com")
    public void timeTravelErrorTest() throws Exception {
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post(API.HOME + API.TIME_TRAVEL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        //1639740466 is GMT: Friday 17 December 2021 11:27:46
                        // Your time zone: venerdì 17 dicembre 2021 12:27:46 GMT+01:00
                        .content("{\"invalid json\": \"abcd\"}")
                ).andExpect(status().isBadRequest())
                .andReturn();
    }

}

