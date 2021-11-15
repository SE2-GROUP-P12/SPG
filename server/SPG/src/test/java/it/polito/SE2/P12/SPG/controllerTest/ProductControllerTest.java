package it.polito.SE2.P12.SPG.controllerTest;


import it.polito.SE2.P12.SPG.controller.SpgController;
import it.polito.SE2.P12.SPG.entity.Product;
import it.polito.SE2.P12.SPG.repository.ProductRepo;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

@SpringBootTest
public class ProductControllerTest {
    @Autowired
    private SpgController spgController;
    @Autowired
    private ProductRepo productRepo;

    @BeforeEach
    public void initContext(){
        //Empty all the product
        productRepo.deleteAll();
        //Create some testing product
        Product prod1 = new Product("Prod1", "Producer1", "KG", 1000.0, 10.50F);
        Product prod2 = new Product("Prod2", "Producer2","KG", 100.0, 5.50F);
        Product prod3 = new Product("Prod3", "Producer3","KG", 20.0, 8.00F);
        productRepo.save(prod1);
        productRepo.save(prod2);
        productRepo.save(prod3);
    }

    @Test
    public void listOfProductLoadingTest(){
        ResponseEntity<List<Product>> response;
        response = spgController.getAllProduct();
        Assertions.assertEquals(response.getStatusCode(), HttpStatus.OK);
        Assertions.assertEquals(response.getBody().size(), 3);
        Assertions.assertEquals(response.getBody().get(0).getName(), "Prod1");
    }

}
