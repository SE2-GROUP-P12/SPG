package it.polito.SE2.P12.SPG.controllerTest;


import it.polito.SE2.P12.SPG.controller.SpgController;
import it.polito.SE2.P12.SPG.entity.Product;
import it.polito.SE2.P12.SPG.repository.BasketRepo;
import it.polito.SE2.P12.SPG.repository.OrderRepo;
import it.polito.SE2.P12.SPG.repository.ProductRepo;
import it.polito.SE2.P12.SPG.repository.UserRepo;
import it.polito.SE2.P12.SPG.security.SecurityConfiguration;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

@SpringBootTest
public class ProductServiceTest {
    @Autowired
    private SpgController spgController;
    @Autowired
    private ProductRepo productRepo;
    @Autowired
    private BasketRepo basketRepo;
    @Autowired
    private OrderRepo orderRepo;
    @Autowired
    private UserRepo userRepo;


    @BeforeEach
    public void initContext(){
        SecurityConfiguration.setTestContext();
        basketRepo.deleteAll();
        orderRepo.deleteAll();
        productRepo.deleteAll();
        userRepo.deleteAll();
        //Create some testing product
        Product prod1 = new Product("Prod1", "Producer1", "KG", 1000.0, 10.50F);
        Product prod2 = new Product("Prod2", "Producer2","KG", 100.0, 5.50F);
        Product prod3 = new Product("Prod3", "Producer3","KG", 20.0, 8.00F);
        productRepo.save(prod1);
        productRepo.save(prod2);
        productRepo.save(prod3);
    }

    /*@Test
    public void listOfProductLoadingTest(){
        ResponseEntity<List<Product>> response;
        response = spgController.getAllProduct();
        Assertions.assertEquals(response.getStatusCode(), HttpStatus.OK);
        Assertions.assertEquals(response.getBody().size(), 3);

        Assertions.assertEquals(response.getBody().get(0).getName(), "Prod1");
        Assertions.assertEquals(response.getBody().get(1).getName(), "Prod2");
        Assertions.assertEquals(response.getBody().get(2).getName(), "Prod3");

        Assertions.assertEquals(response.getBody().get(0).getUnitOfMeasurement(), "KG");
        Assertions.assertEquals(response.getBody().get(1).getUnitOfMeasurement(), "KG");
        Assertions.assertEquals(response.getBody().get(2).getUnitOfMeasurement(), "KG");

        Assertions.assertEquals(response.getBody().get(0).getTotalQuantity(), 1000.0);
        Assertions.assertEquals(response.getBody().get(1).getTotalQuantity(), 100.0);
        Assertions.assertEquals(response.getBody().get(2).getTotalQuantity(), 20.0);

        Assertions.assertEquals(response.getBody().get(0).getQuantityAvailable(), 1000.0);
        Assertions.assertEquals(response.getBody().get(1).getQuantityAvailable(), 100.0);
        Assertions.assertEquals(response.getBody().get(2).getQuantityAvailable(), 20.0);

        Assertions.assertEquals(response.getBody().get(0).getQuantityBaskets(), 0.0);
        Assertions.assertEquals(response.getBody().get(1).getQuantityBaskets(), 0.0);
        Assertions.assertEquals(response.getBody().get(2).getQuantityBaskets(), 0.0);

        Assertions.assertEquals(response.getBody().get(0).getQuantityOrdered(), 0.0);
        Assertions.assertEquals(response.getBody().get(1).getQuantityOrdered(), 0.0);
        Assertions.assertEquals(response.getBody().get(2).getQuantityOrdered(), 0.0);

        Assertions.assertEquals(response.getBody().get(0).getQuantityDelivered(), 0.0);
        Assertions.assertEquals(response.getBody().get(1).getQuantityDelivered(), 0.0);
        Assertions.assertEquals(response.getBody().get(2).getQuantityDelivered(), 0.0);

        Assertions.assertEquals(response.getBody().get(0).getPrice(), 10.50F);
        Assertions.assertEquals(response.getBody().get(1).getPrice(), 5.50F);
        Assertions.assertEquals(response.getBody().get(2).getPrice(), 8.00F);
    }

    @Test
    public void emptyListOfProduct(){
        productRepo.deleteAll();

        ResponseEntity<List<Product>> response;
        response = spgController.getAllProduct();
        Assertions.assertEquals(response.getStatusCode(), HttpStatus.OK);
        Assertions.assertEquals(response.getBody().size(), 0);
        Assertions.assertNotEquals(response, null);
    }*/

}
