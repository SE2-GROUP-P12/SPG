package it.polito.SE2.P12.SPG.service;

import it.polito.SE2.P12.SPG.entity.Farmer;
import it.polito.SE2.P12.SPG.entity.Product;
import it.polito.SE2.P12.SPG.repository.ProductRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SpgProductService {

    private ProductRepo productRepo;
    private SchedulerService schedulerService;

    @Autowired
    public SpgProductService(ProductRepo productRepo) {
        this.productRepo = productRepo;
    }

    public List<Product> getAllProduct() {
        List<Product> listProduct = productRepo.findAll();
        return listProduct;
    }

    public List<Product> getAllProductByFarmerEmail(String email) {
        List<Product> listProduct = productRepo.findProductsByFarmer_Email(email);
        return listProduct;
    }

    public List<Product> getAllForecastedProductByFarmerEmail(String email) {
        List<Product> listProduct = productRepo.findProductsByFarmer_EmailAndAndQuantityForecastGreaterThan(email, 0.0);
        return listProduct;
    }

    public List<Product> getAllUnforecastedProductByFarmerEmail(String email) {
        List<Product> listProduct = productRepo.findProductsByFarmer_EmailAndQuantityForecastEquals(email, 0.0);
        return listProduct;
    }

    public Product getProductById(Long productId) {
        return productRepo.findProductByProductId(productId);
    }

    public Boolean setForecast(Long productId, Double forecast) {
        if (forecast < 0.0 || Double.isNaN(forecast) || Double.isInfinite(forecast))
            return false;
        Product product = productRepo.findProductByProductId(productId);
        if (product == null)
            return false;
        product.setQuantityForecast(forecast);
        product.setQuantityAvailable(forecast - product.getQuantityBaskets() - product.getQuantityOrdered());
        productRepo.save(product);
        return true;
    }

    public Boolean setForecastNext(Long productId, Double forecast) {
        if (forecast < 0.0 || Double.isNaN(forecast) || Double.isInfinite(forecast))
            return false;
        Product product = productRepo.findProductByProductId(productId);
        if (product == null)
            return false;
        product.setQuantityForecastNext(forecast);
        productRepo.save(product);
        return true;
    }

    /*
    public boolean setForecast(Product product, Double forecast, String start, String end) {
        if (forecast < 0.0 || Double.isNaN(forecast) || Double.isInfinite(forecast))
            return false;
        product.setQuantityForecast(forecast);
        product.setStartAvailability(start);
        product.setEndAvailability(end);
        productRepo.save(product);
        return true;
    }

    public boolean setForecast(Long productId, Farmer farmer, Double forecast, String start, String end) {
        Product product = productRepo.findProductByProductId(productId);
        if (product == null){
            return false;
        }
        product.setFarmer(farmer);
        product.setQuantityForecast(forecast);
        product.setStartAvailability(start);
        product.setEndAvailability(end);
        productRepo.save(product);
        return true;
    }
    */

    public boolean addProduct(String productName, Double price, String unitOfMeasurement, String imageUrl, Farmer farmer) {
        Product p;
        if (imageUrl == null || imageUrl.isEmpty() || imageUrl.isBlank())
            p = new Product(productName, unitOfMeasurement, 0.0, price, farmer);
        else
            p = new Product(productName, unitOfMeasurement, 0.0, price, imageUrl, farmer);
        productRepo.save(p);
        return true;
    }

    public Boolean confirmQuantity(Long productId, Double quantityConfirmed) {
        if (productId == null || quantityConfirmed < 0 || quantityConfirmed.isInfinite() || quantityConfirmed.isNaN())
            return false;
        Product p = productRepo.findProductByProductId(productId);
        if (p == null)
            return false;
        p.setQuantityConfirmed(quantityConfirmed);
        productRepo.save(p);
        return true;
    }
}
