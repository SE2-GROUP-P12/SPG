package it.polito.SE2.P12.SPG.serviceTest;

import it.polito.SE2.P12.SPG.entity.*;
import it.polito.SE2.P12.SPG.repository.*;
import it.polito.SE2.P12.SPG.service.SpgBasketService;
import it.polito.SE2.P12.SPG.service.SpgProductService;
import it.polito.SE2.P12.SPG.service.SpgUserService;
import it.polito.SE2.P12.SPG.utils.DBUtilsService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithUserDetails;

import java.util.List;

@SpringBootTest
public class ProductServiceTest {

    @Autowired
    private ProductRepo productRepo;
    @Autowired
    private CustomerRepo customerRepo;
    @Autowired
    private SpgBasketService basketService;
    @Autowired
    private SpgUserService userService;
    @Autowired
    DBUtilsService dbUtilsService;
    @Autowired
    BasketRepo basketRepo;
    @Autowired
    private ShopEmployeeRepo shopEmployeeRepo;
    @Autowired
    private FarmerRepo farmerRepo;
    @Autowired
    private AdminRepo adminRepo;
    @Autowired
    private SpgProductService productService;

    @BeforeEach
    public void initContext() {
        dbUtilsService.dropAll();
        //Create a farmer
        Farmer farm1 = new Farmer("aaa", "bbb", "ssn_faaarmer", "42342342", "farmer@farmer.com", "password","nice_farm");
        farmerRepo.save(farm1);
        //Create some product and then add them to the Customer Cart
        Product prod1 = new Product("Prod1", "KG", 1000.0, 10.50F, farm1);
        Product prod2 = new Product("Prod2", "KG", 100.0, 5.50F, farm1);
        Product prod3 = new Product("Prod3", "KG", 20.0, 8.00F, farm1);
        productRepo.save(prod1);
        productRepo.save(prod2);
        productRepo.save(prod3);
    }

    @Test
    public void getAllProductsTest(){
        Product p1 = productRepo.findProductByName("Prod1");
        Product p2 = productRepo.findProductByName("Prod2");
        Product p3 = productRepo.findProductByName("Prod3");

        List<Product> products = productService.getAllProduct();

        Assertions.assertEquals(products.size(), 3);
        Assertions.assertEquals(products.get(0).getName(), p1.getName());
        Assertions.assertEquals(products.get(1).getName(), p2.getName());
        Assertions.assertEquals(products.get(2).getName(), p3.getName());
    }

    @Test
    public void forecastTest() throws Exception {
        productService.setForecast(productRepo.findProductByName("Prod1").getProductId(),  farmerRepo.findFarmerByEmail("farmer@farmer.com"), 5.7, "aaa", "bbb");
        Assertions.assertEquals(5.7,productRepo.findProductByName("Prod1").getQuantityForecast());
    }

    @Test
    public void forecastOnNonExistingProduct(){
        Assertions.assertFalse(productService.setForecast(productRepo.findProductByName("Prod1").getProductId()*-1,farmerRepo.findFarmerByEmail("farmer@farmer.com"), 5.7, "aaa", "bbb"));
    }


}
