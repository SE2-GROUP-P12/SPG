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
        Farmer farm1 = new Farmer("aaa", "bbb", "ssn_faaarmer", "42342342", "farmer@farmer.com", "password", "nice_farm");
        Farmer farm2 = new Farmer("ccc", "ddd", "ssn_faaarmer2", "42342341", "farmer2@farmer.com", "password", "nice_farm2");
        farmerRepo.save(farm1);
        farmerRepo.save(farm2);
        //Create some product and then add them to the Customer Cart
        Product prod1 = new Product("Prod1", "KG", 1000.0, 10.50F, farm1);
        Product prod2 = new Product("Prod2", "KG", 100.0, 5.50F, farm1);
        Product prod3 = new Product("Prod3", "KG", 20.0, 8.00F, farm1);
        Product prod4 = new Product("Prod4", "Units", 15.0, 9.00F, farm2);
        productRepo.save(prod1);
        productRepo.save(prod2);
        productRepo.save(prod3);
        productRepo.save(prod4);
    }

    @Test
    public void getAllProductsTest() {
        Product p1 = productRepo.findProductByName("Prod1");
        Product p2 = productRepo.findProductByName("Prod2");
        Product p3 = productRepo.findProductByName("Prod3");
        Product p4 = productRepo.findProductByName("Prod4");

        List<Product> products = productService.getAllProduct();

        Assertions.assertEquals(4, products.size());
        Assertions.assertEquals(products.get(0).getName(), p1.getName());
        Assertions.assertEquals(products.get(1).getName(), p2.getName());
        Assertions.assertEquals(products.get(2).getName(), p3.getName());
        Assertions.assertEquals(products.get(3).getName(), p4.getName());
    }

    @Test
    public void getEmptyProductListTest() {
        productRepo.deleteAll();

        List<Product> products = productService.getAllProduct();
        Assertions.assertEquals(0, products.size());
        Assertions.assertTrue(products.isEmpty());
        Assertions.assertNotNull(products);
    }

    @Test
    public void getAllProductFarmerByEmailTest() {
        List<Product> products = productService.getAllProductByFarmerEmail("farmer@farmer.com");

        Assertions.assertEquals(3, products.size());
    }

    @Test
    public void getAllProductFarmerByEmailNullEmailTest() {
        List<Product> products = productService.getAllProductByFarmerEmail(null);

        Assertions.assertEquals(0, products.size());
        Assertions.assertNotNull(products);
    }

    @Test
    public void getAllForecastedProductFarmerByEmailTest() {
        Product p = productRepo.findProductByName("Prod1");
        p.setQuantityForecast(10.0);
        productRepo.save(p);
        List<Product> products = productService.getAllForecastedProductByFarmerEmail("farmer@farmer.com");

        Assertions.assertEquals(1, products.size());
        Assertions.assertEquals("Prod1",products.get(0).getName() );
    }

    @Test
    public void getAllUnforecastedProductFarmerByEmailTest() {
        Product p1 = productRepo.findProductByName("Prod1");
        Product p2 = productRepo.findProductByName("Prod2");
        p1.setQuantityForecast(10.0);
        p2.setQuantityForecast(12.0);
        productRepo.save(p1);
        productRepo.save(p2);
        List<Product> products = productService.getAllUnforecastedProductByFarmerEmail("farmer@farmer.com");

        Assertions.assertEquals(1, products.size());
        Assertions.assertEquals("Prod3",products.get(0).getName());
    }


    @Test
    public void forecastTest() {
        productService.setForecast(productRepo.findProductByName("Prod1").getProductId(), 5.7);
        Assertions.assertEquals(5.7, productRepo.findProductByName("Prod1").getQuantityForecast());
    }

    @Test
    public void forecastOnNonExistingProduct() {
        Long randomId = 4328492349832487432L;
        Assertions.assertFalse(productService.setForecast(randomId, 5.7));
    }

    @Test
    public void forecastNegativeQuantityTest() {
        Assertions.assertFalse(productService.setForecast(productRepo.findProductByName("Prod1").getProductId(), -2.0));
        Assertions.assertEquals(0.0,productRepo.findProductByName("Prod1").getQuantityForecast());
    }

    @Test
    public void forecastNanQuantityTest() {
        Assertions.assertFalse(productService.setForecast(productRepo.findProductByName("Prod1").getProductId(), Double.NaN));
        Assertions.assertEquals(0.0,productRepo.findProductByName("Prod1").getQuantityForecast());
    }

    @Test
    public void forecastInfiniteQuantityTest() {
        Assertions.assertFalse(productService.setForecast(productRepo.findProductByName("Prod1").getProductId(), Double.POSITIVE_INFINITY));
        Assertions.assertEquals(0.0,productRepo.findProductByName("Prod1").getQuantityForecast());
    }

    @Test
    public void getProductByIdTest() {
        Assertions.assertNotNull(productService.getProductById(productRepo.findProductByName("Prod1").getProductId()));
    }

    @Test
    public void getProductByNullIdTest() {
        Assertions.assertNull(productService.getProductById(null));
    }

    @Test
    public void confirmQuantityCorrectlyTest() {
        Long productId = productRepo.findProductByName("Prod1").getProductId();
        Assertions.assertTrue(productService.confirmQuantity(productId, 12.5));
        Assertions.assertEquals(12.5, productRepo.findProductByName("Prod1").getQuantityConfirmed());
    }

    @Test
    public void confirmQuantityNonExistingProductTest() {
        Long randomId = 4328492349832487432L;
        Assertions.assertFalse(productService.confirmQuantity(randomId, 12.5));
    }

    @Test
    public void confirmQuantityNullIdTest() {
        Assertions.assertFalse(productService.confirmQuantity(null, 12.5));
    }

    @Test
    public void confirmQuantityNegativeTest() {
        Long productId = productRepo.findProductByName("Prod1").getProductId();
        Assertions.assertFalse(productService.confirmQuantity(productId, -12.5));
        Assertions.assertEquals(0.0, productRepo.findProductByName("Prod1").getQuantityConfirmed());
    }

    @Test
    public void confirmQuantityNanTest() {
        Long productId = productRepo.findProductByName("Prod1").getProductId();
        Assertions.assertFalse(productService.confirmQuantity(productId, Double.NaN));
        Assertions.assertEquals(0.0, productRepo.findProductByName("Prod1").getQuantityConfirmed());
    }

    @Test
    public void confirmQuantityInfiniteTest() {
        Long productId = productRepo.findProductByName("Prod1").getProductId();
        Assertions.assertFalse(productService.confirmQuantity(productId, Double.POSITIVE_INFINITY));
        Assertions.assertEquals(0.0, productRepo.findProductByName("Prod1").getQuantityConfirmed());
    }

    @Test
    public void addProductWithImageTest() {
        String name = "newProd";
        Double price = 12.50;
        String unitOfMeasurement = "KG";
        String imageUrl = "very_cool_url";
        Farmer f1 = userService.getFarmerByEmail("farmer@farmer.com");

        Assertions.assertTrue(productService.addProduct(name, price, unitOfMeasurement, imageUrl, f1));
        Product p = productRepo.findProductByName("newProd");
        Assertions.assertNotNull(p);
        Assertions.assertEquals("newProd", p.getName());
        Assertions.assertEquals(12.50, p.getPrice());
        Assertions.assertEquals("KG", p.getUnitOfMeasurement());
        Assertions.assertEquals("very_cool_url", p.getImageUrl());
        Assertions.assertEquals(f1.getUserId(), p.getFarmer().getUserId());
        Assertions.assertEquals(0.0, p.getQuantityAvailable());
        Assertions.assertEquals(0.0, p.getQuantityConfirmed());
        Assertions.assertEquals(0.0, p.getQuantityBaskets());
        Assertions.assertEquals(0.0, p.getQuantityDelivered());
        Assertions.assertEquals(0.0, p.getQuantityForecast());
        Assertions.assertEquals(0.0, p.getQuantityOrdered());
    }

    @Test
    public void addProductWithoutImageTest() {
        String name = "newProd";
        Double price = 12.50;
        String unitOfMeasurement = "KG";
        Farmer f1 = userService.getFarmerByEmail("farmer@farmer.com");

        Assertions.assertTrue(productService.addProduct(name, price, unitOfMeasurement, null, f1));
        Product p = productRepo.findProductByName("newProd");
        Assertions.assertNotNull(p);
        Assertions.assertEquals("newProd", p.getName());
        Assertions.assertEquals(12.50, p.getPrice());
        Assertions.assertEquals("KG", p.getUnitOfMeasurement());
        Assertions.assertNull(p.getImageUrl());
        Assertions.assertEquals(f1.getUserId(), p.getFarmer().getUserId());
        Assertions.assertEquals(0.0, p.getQuantityAvailable());
        Assertions.assertEquals(0.0, p.getQuantityConfirmed());
        Assertions.assertEquals(0.0, p.getQuantityBaskets());
        Assertions.assertEquals(0.0, p.getQuantityDelivered());
        Assertions.assertEquals(0.0, p.getQuantityForecast());
        Assertions.assertEquals(0.0, p.getQuantityOrdered());
    }

}
