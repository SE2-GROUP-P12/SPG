package it.polito.SE2.P12.SPG.service;

import it.polito.SE2.P12.SPG.entity.Product;
import it.polito.SE2.P12.SPG.entity.User;
import it.polito.SE2.P12.SPG.repository.ProductRepo;
import it.polito.SE2.P12.SPG.repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class SpgProductService {

    private ProductRepo productRepo;

    @Autowired
    public SpgProductService(ProductRepo productRepo) {
        this.productRepo = productRepo;
    }

    public List<Product> getAllProduct(){
        return productRepo.findAll();
    }
    public Product getProductById(Long productId){
        return productRepo.findProductByProductId(productId);
    }


    public Product test(){
        System.out.println("Test activated");
        Product p = new Product("Pompelmissimo", "quantità", 10, 12.30f);
        productRepo.save(p);
        return p;
    }





}
