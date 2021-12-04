package it.polito.SE2.P12.SPG.service;

import it.polito.SE2.P12.SPG.entity.Farmer;
import it.polito.SE2.P12.SPG.entity.Product;
import it.polito.SE2.P12.SPG.repository.ProductRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SpgProductService {

    private ProductRepo productRepo;

    @Autowired
    public SpgProductService(ProductRepo productRepo) {
        this.productRepo = productRepo;
    }

    public List<Product> getAllProduct(){
        List<Product> listProduct = productRepo.findAll();
        return listProduct;
    }

    public List<Product> getAllProductByFarmerEmail(String email){
        List<Product> listProduct = productRepo.findProductsByFarmer_Email(email);
        return listProduct;
    }

    public Product getProductById(Long productId){
        return productRepo.findProductByProductId(productId);
    }

    public Boolean setForecast(Product product, Double forecast) {
        if(forecast < 0.0 || Double.isNaN(forecast) || Double.isInfinite(forecast))
            return false;
        product.setQuantityForecast(forecast);
        productRepo.save(product);
        return true;
    }

    public boolean setForecast(Product product, Farmer farmer, Double forecast, String start, String end) {
        product.setQuantityForecast(forecast);
        product.setStartAvailability(start);
        product.setEndAvailability(end);
        productRepo.save(product);
        return true;
    }

    /*
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
        if(imageUrl.isEmpty() || imageUrl.isBlank() || imageUrl == null)
            p = new Product(productName, unitOfMeasurement, 0.0, price, farmer);
        else
            p = new Product(productName, unitOfMeasurement, 0.0, price, imageUrl, farmer);
        productRepo.save(p);
        return true;
    }
}
