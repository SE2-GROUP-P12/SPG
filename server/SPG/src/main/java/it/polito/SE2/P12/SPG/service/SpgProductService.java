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

    public void populateDB(){
        Product temp1 = new Product ("Apples", "Apples By Tonio Cartonio", "Kg",50.0,2.50);
        Product temp2 = new Product ("Flour", "Mulin Bianco Inc.", "Kg",10.0,5.00);
        Product temp3 = new Product ("Eggs","KFC Farms", "Units",36.0,6.25);

        if(productRepo.findProductByName("Apples")==null)productRepo.save(temp1);
        if(productRepo.findProductByName("Flour")==null)productRepo.save(temp2);
        if(productRepo.findProductByName("Eggs")==null)productRepo.save(temp3);
    }
    public List<Product> getAllProduct(){
        return productRepo.findAll();
    }
    public Product getProductById(Long productId){
        return productRepo.findProductByProductId(productId);
    }



    public Product test(){
        System.out.println("Test activated");
        Product p = new Product("Pompelmissimo", "Pompelmoducer", "quantità", 10.0, 12.30f);
        productRepo.save(p);
        return p;
    }





}
