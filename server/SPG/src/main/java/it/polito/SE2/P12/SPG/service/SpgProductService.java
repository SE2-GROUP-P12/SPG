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

    @Autowired
    public SpgProductService(ProductRepo productRepo) {
        this.productRepo = productRepo;
    }

    public List<Product> getAllProduct(){
        List<Product> listProduct = productRepo.findAll();
        return listProduct;
    }
    public Product getProductById(Long productId){
        return productRepo.findProductByProductId(productId);
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
    public void addProduct(Product product){
        productRepo.save(product);
    }
}
