package it.polito.SE2.P12.SPG.controllerTest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.j2objc.annotations.AutoreleasePool;
import it.polito.SE2.P12.SPG.controller.SpgController;
import it.polito.SE2.P12.SPG.entity.Customer;
import it.polito.SE2.P12.SPG.entity.User;
import it.polito.SE2.P12.SPG.repository.CustomerRepo;
import it.polito.SE2.P12.SPG.repository.UserRepo;
import it.polito.SE2.P12.SPG.utils.API;
import it.polito.SE2.P12.SPG.utils.DBUtilsService;
import org.checkerframework.checker.nullness.qual.AssertNonNullIfNonNull;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;


import java.sql.SQLException;
import java.util.Map;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerApiTest {
    private static final String customerJsonFormat = "{" +
            "  \"name\": \"fooName1\"," +
            "  \"surname\": \"fooSurname1\"," +
            "  \"ssn\": \"ssn_aaaaaaaaaaab\"," +
            "  \"phoneNumber\": \"1234567890\"," +
            "  \"email\": \"foouser1@foomail.com\"," +
            "  \"password\": \"password\"," +
            "  \"address\": \"address\"" +
            "}";

    private static final String customerJsonFormat1 = "{" +
            "  \"name\": \"fooName1\"," +
            "  \"surname\": \"fooSurname1\"," +
            "  \"ssn\": \"ssn_aaaaaaaaaabb\"," +
            "  \"phoneNumber\": \"1234567890\"," +
            "  \"email\": \"foouser2@foomail.com\"," +
            "  \"password\": \"password\"," +
            "  \"address\": \"address\"" +
            "}";

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private SpgController spgController;
    @Autowired
    private UserRepo userRepo;
    @Autowired
    private CustomerRepo customerRepo;
    @Autowired
    private DBUtilsService dbUtilsService;

    @BeforeEach
    public void initContext() {
        dbUtilsService.dropAll();
        Customer fooUser1 = new Customer("fooName1", "fooSurname1", "ssn_aaaaaaaaaaaa", "123456789", "foouser@foomail.com", "password", "address");
        customerRepo.save(fooUser1);
    }

    @AfterEach
    public void restDB() {
        userRepo.deleteAll();
    }

    /**
     * CUSTOMER CREATION/CHECK PRESENCE TESTING
     */

    @Test
    public void nullCustomerCreationTest() throws Exception {
        ResponseEntity response;
        response = spgController.createCustomer(null, null);
        Map<String, String> responseMap = (Map<String, String>) response.getBody();
        Assertions.assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        Assertions.assertEquals("Body is not valid", responseMap.get("errorMessage"));
    }

    @Test
    public void validCustomerCreationTest() throws Exception {
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/api/" + API.CREATE_CUSTOMER)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(customerJsonFormat)
                )
                .andExpect(status().isCreated())
                .andReturn();
        Assertions.assertNotNull(result.getResponse().getContentAsString());
        Map<String, String> response = new ObjectMapper().readValue(result.getResponse().getContentAsString(), Map.class);
        Assertions.assertNotNull(response);
        Assertions.assertTrue(response.containsKey("accessToken"));
        Assertions.assertTrue(response.containsKey("refreshToken"));
        Assertions.assertTrue(response.containsKey("email"));
        Assertions.assertEquals("foouser1@foomail.com", response.get("email"));

    }

    @Test
    public void invalidCustomerStringCreationTest() {
        ResponseEntity response;
        response = spgController.createCustomer("an invalid string", null);
        Boolean responseBoolean = (Boolean) response.getBody();
        Assertions.assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        Assertions.assertNull(responseBoolean);
    }


    @Test
    public void alreadyPresentCustomerMailHandlingTest() {
        ResponseEntity response;
        response = spgController.createCustomer(customerJsonFormat, null);
        Map<String, String> responseMap = (Map<String, String>) response.getBody();
        Assertions.assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        Assertions.assertEquals("email/ssn already present in the system", responseMap.get("errorMessage"));
    }

    @Test
    public void alreadyPresentCustomerSsnHandlingTest() {
        ResponseEntity response;
        response = spgController.createCustomer(customerJsonFormat, null);
        Map<String, String> responseMap = (Map<String, String>) response.getBody();
        Assertions.assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assert responseMap != null;
        Assertions.assertEquals("email/ssn already present in the system", responseMap.get("errorMessage"));
    }

    @Test
    public void validCustomerInfoCheckTest() {
        ResponseEntity response;
        //Correct information
        response = spgController.checkExistCustomerMailAndSsn("foouser@foomail.com", "ssn_aaaaaaaaaaaa");
        Map<String, Boolean> responseMap = (Map<String, Boolean>) response.getBody();
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertNotNull(response.getBody());
        Assertions.assertNotNull(responseMap);
        Assertions.assertTrue(responseMap.containsKey("exist"));
        Assertions.assertTrue((Boolean) responseMap.get("exist"));
    }

    @Test
    public void invalidCustomerInfoCheckTest() {
        ResponseEntity response;
        //Empty String
        response = spgController.checkExistCustomerMailAndSsn("", "");
        Map<String, Boolean> responseMap = (Map<String, Boolean>) response.getBody();
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertNotNull(response.getBody());
        Assertions.assertNotNull(responseMap);
        Assertions.assertTrue(responseMap.containsKey("exist"));
        Assertions.assertFalse((Boolean) responseMap.get("exist"));
        //null String
        response = spgController.checkExistCustomerMailAndSsn(null, null);
        responseMap = (Map<String, Boolean>) response.getBody();
        Assertions.assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        Assertions.assertNull(response.getBody());
    }

    @Test
    public void existCustomerByEmailTest() {
        ResponseEntity response;
        //Nor present mail
        response = spgController.checkExistCustomerMail("tester@test.com");
        Assertions.assertNotNull(response);
        Assertions.assertEquals(200, response.getStatusCodeValue());
        Assertions.assertNotNull(response.getBody());
        Assertions.assertFalse((Boolean) response.getBody());
        //present mail
        response = spgController.checkExistCustomerMail("foouser@foomail.com");
        Assertions.assertEquals(200, response.getStatusCodeValue());
        Assertions.assertNotNull(response.getBody());
        Assertions.assertTrue((Boolean) response.getBody());
    }

    @Test
    public void existCustomerByEmailErrorHandlerTest() {
        //null mail
        ResponseEntity response;
        response = spgController.checkExistCustomerMail(null);
        Assertions.assertNotNull(response);
        Assertions.assertEquals(400, response.getStatusCodeValue());
        Assertions.assertNull(response.getBody());
    }
}
