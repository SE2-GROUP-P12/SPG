package it.polito.SE2.P12.SPG.controllerTest;

import it.polito.SE2.P12.SPG.controller.SpgController;
import it.polito.SE2.P12.SPG.entity.User;
import it.polito.SE2.P12.SPG.repository.UserRepo;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;


import java.util.Map;


@SpringBootTest
public class UserControllerApiTest {
    private static final String customerJsonFormat = "{" +
            "  \"name\": \"fooName1\"," +
            "  \"surname\": \"fooSurname1\"," +
            "  \"ssn\": \"ssn_aaaaaaaaaaaa\"," +
            "  \"phoneNumber\": \"\"," +
            "  \"role\": \"ROLE_CUSTOMER\"," +
            "  \"email\": \"foouser@foomail.com\"," +
            "  \"password\": \"password\"" +
            "}";

    private static final String customerJsonFormat1 = "{" +
            "  \"name\": \"fooName1\"," +
            "  \"surname\": \"fooSurname1\"," +
            "  \"ssn\": \"ssn_aaaaaaaaaabb\"," +
            "  \"phoneNumber\": \"\"," +
            "  \"role\": \"ROLE_CUSTOMER\"," +
            "  \"email\": \"foouser1@foomail.com\"," +
            "  \"password\": \"password\"" +
            "}";

    @Autowired
    private SpgController spgController;
    @Autowired
    private UserRepo userRepo;

    @BeforeEach
    public void initContext() {
        userRepo.deleteAll();
        User fooUser1 = new User("fooName1", "fooSurname1", "ssn_aaaaaaaaaaaa", "", "ROLE_CUSTOMER", "foouser@foomail.com", "password");
        userRepo.save(fooUser1);
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
        Map<String, String> responseMap = (Map<String, String>) response.getBody();
        Assertions.assertEquals(HttpStatus.CREATED, response.getStatusCode());
        Assertions.assertEquals("200-OK-(Customer added successfully)", responseMap.get("responseMessage"));
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
    public void testEmptyCustomerCheck() {
        ResponseEntity response;
        response = spgController.checkExistCustomerMailAndSsn("");
        Assertions.assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    public void testWrongFieldCustomerCheck() {
        ResponseEntity response;
        String jsonData = "{\"filed1\":\"foo1@foomail.com\",\"field2\":\"aaaaaaaaaaaaaaaa\"}";
        response = spgController.checkExistCustomerMailAndSsn(jsonData);
        Assertions.assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    public void testNotJsonBodyRequest() {
        ResponseEntity response;
        String jsonData = "foo-data <> not-a-json-structure!";
        response = spgController.checkExistCustomerMailAndSsn(jsonData);
        Assertions.assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    public void testCustomerExistsByEmail(){
        String email = "admin";
        ResponseEntity response;
        response = spgController.checkExistCustomerMail(email);
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
    }
}
