package it.polito.SE2.P12.SPG.controllerTest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.j2objc.annotations.AutoreleasePool;
import it.polito.SE2.P12.SPG.controller.SpgController;
import it.polito.SE2.P12.SPG.entity.Customer;
import it.polito.SE2.P12.SPG.entity.User;
import it.polito.SE2.P12.SPG.repository.CustomerRepo;
import it.polito.SE2.P12.SPG.repository.UserRepo;
import org.checkerframework.checker.nullness.qual.AssertNonNullIfNonNull;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;


import java.sql.SQLException;
import java.util.Map;

@SpringBootTest
public class UserControllerApiTest {
    private static final String customerJsonFormat = "{" +
            "  \"name\": \"fooName1\"," +
            "  \"surname\": \"fooSurname1\"," +
            "  \"ssn\": \"ssn_aaaaaaaaaaaa\"," +
            "  \"phoneNumber\": \"1234567890\"," +
            "  \"email\": \"foouser@foomail.com\"," +
            "  \"password\": \"password\"" +
            "}";

    private static final String customerJsonFormat1 = "{" +
            "  \"name\": \"fooName1\"," +
            "  \"surname\": \"fooSurname1\"," +
            "  \"ssn\": \"ssn_aaaaaaaaaabb\"," +
            "  \"phoneNumber\": \"1234567890\"," +
            "  \"email\": \"foouser1@foomail.com\"," +
            "  \"password\": \"password\"," +
            "  \"address\": \"address\"" +
            "}";

    @Autowired
    private SpgController spgController;
    @Autowired
    private UserRepo userRepo;
    @Autowired
    private CustomerRepo customerRepo;

    @BeforeEach
    public void initContext() {
        userRepo.deleteAll();
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
    public void testNullCustomerCreation() throws Exception {
        ResponseEntity response;
        response = spgController.createCustomer(null);
        Map<String, String> responseMap = (Map<String, String>) response.getBody();
        Assertions.assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        Assertions.assertEquals("Body is not valid", responseMap.get("errorMessage"));
    }

    @Test
    public void testValidCustomerCreation() {
        ResponseEntity response;
        response = spgController.createCustomer(customerJsonFormat1);
        Boolean responseBoolean = (Boolean) response.getBody();
        Assertions.assertEquals(HttpStatus.CREATED, response.getStatusCode());
        Assertions.assertTrue(responseBoolean);
    }

    @Test
    public void alreadyPresentCustomerMailHandling() {
        ResponseEntity response;
        response = spgController.createCustomer(customerJsonFormat);
        Map<String, String> responseMap = (Map<String, String>) response.getBody();
        Assertions.assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        Assertions.assertEquals("email/ssn already present in the system", responseMap.get("errorMessage"));
    }

    @Test
    public void alreadyPresentCustomerSsnHandling() {
        ResponseEntity response;
        response = spgController.createCustomer(customerJsonFormat);
        Map<String, String> responseMap = (Map<String, String>) response.getBody();
        Assertions.assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assert responseMap != null;
        Assertions.assertEquals("email/ssn already present in the system", responseMap.get("errorMessage"));
    }

    @Test
    public void testEmptyCustomerCheck() throws JsonProcessingException {
        ResponseEntity response;
        response = spgController.checkExistCustomerMailAndSsn("", "");
        Map<String, Boolean> responseMap = (Map<String, Boolean>) response.getBody();
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertNotNull(response.getBody());
        Assertions.assertNotNull(responseMap);
        Assertions.assertTrue(responseMap.containsKey("exist"));
        Assertions.assertFalse((Boolean) responseMap.get("exist"));
    }
}
