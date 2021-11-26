package it.polito.SE2.P12.SPG.controllerTest;

import it.polito.SE2.P12.SPG.controller.SpgController;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;

@SpringBootTest
public class HomeControllerTest {
    @Autowired
    private SpgController spgController;

    @Test
    public void homeControllerTest() {
        ResponseEntity response;
        response = spgController.home();
        Assertions.assertEquals(200, response.getStatusCodeValue());
        Assertions.assertNull(response.getBody());
    }
}
