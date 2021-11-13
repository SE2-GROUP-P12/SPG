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
public class UserControllerTest {
    @Autowired
    private SpgController spgController;
    @Autowired
    private UserRepo userRepo;

    @BeforeEach
    public void initContext() {
        userRepo.deleteAll();
        User fooUser1 = new User("fooName1", "fooSurname1", "ssn_aaaaaaaaaaaa", "", "CUSTOMER", "foouser@foomail.com", "password1223ABC");
        userRepo.save(fooUser1);
    }

    /**
     * CUSTOMER CREATION/CHECK PRESENCE TESTING
     */

    @Test
    public void testNullCustomerCreation() {
        ResponseEntity response;
        response = spgController.createCustomer(null);
        Assertions.assertNull(response.getBody()); // Body is not present
        Assertions.assertTrue(response.getStatusCode().isError());
        Assertions.assertEquals(response.getStatusCode(), HttpStatus.BAD_REQUEST);
    }

    @Test
    public void testValidCustomerCreation() {
        User customer1 = new User("fooName1", "fooSurname1", "ssn_aaaaaaaaazzz", "", "CUSTOMER", "customer1@foomail.com", "password1223ABC");
        ResponseEntity response;
        response = spgController.createCustomer(customer1);
        Map<String, String> responseMap = (Map<String, String>) response.getBody();
        Assertions.assertEquals(response.getStatusCode(), HttpStatus.OK);
        Assertions.assertEquals("200-OK-(Customer added successfully)", responseMap.get("responseMessage"));
    }

    @Test
    public void alreadyPresentCustomerMailHandling() {
        User customer1 = new User("fooName1", "fooSurname1", "ssn_aaaaaaaaaaaa", "", "CUSTOMER", "customer1@foomail.com", "password1223ABC");
        ResponseEntity response;
        response = spgController.createCustomer(customer1);
        Map<String, String> responseMap = (Map<String, String>) response.getBody();
        Assertions.assertEquals(response.getStatusCode(), HttpStatus.OK);
        Assertions.assertEquals("200-OK-(Customer already present)", responseMap.get("responseMessage"));
    }

    @Test
    public void alreadyPresentCustomerSsnHandling() {
        User customer1 = new User("fooName1", "fooSurname1", "ssn_aaaaaaaaaaaa", "", "CUSTOMER", "customer1@foomail.com", "password1223ABC");
        ResponseEntity response;
        response = spgController.createCustomer(customer1);
        Map<String, String> responseMap = (Map<String, String>) response.getBody();
        Assertions.assertEquals(response.getStatusCode(), HttpStatus.OK);
        Assertions.assertEquals("200-OK-(Customer already present)", responseMap.get("responseMessage"));
    }

    @Test
    public void testNullCustomerCheck() {
        ResponseEntity response;
        response = spgController.checkExistCustomerMailAndSsn(null);
        Assertions.assertEquals(response.getStatusCode(), HttpStatus.BAD_REQUEST);
    }

    @Test
    public void testWrongFieldCustomerCheck() {
        ResponseEntity response;
        String jsonData = "{\"filed1\":\"foo1@foomail.com\",\"field2\":\"aaaaaaaaaaaaaaaa\"}";
        response = spgController.checkExistCustomerMailAndSsn(jsonData);
        Assertions.assertEquals(response.getStatusCode(), HttpStatus.BAD_REQUEST);
    }

    @Test
    public void testNotJsonBodyRequest() {
        ResponseEntity response;
        String jsonData = "foo-data <> not-a-json-structure!";
        response = spgController.checkExistCustomerMailAndSsn(jsonData);
        Assertions.assertEquals(response.getStatusCode(), HttpStatus.BAD_REQUEST);
    }
}
