package it.polito.SE2.P12.SPG.service;

import it.polito.SE2.P12.SPG.entity.Product;
import it.polito.SE2.P12.SPG.repository.ProductRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SpgService {

    private ProductRepo productRepo;

    @Autowired
    public SpgService(ProductRepo productRepo) {
        this.productRepo = productRepo;
    }

    public List<Product> getAllProduct(){
        return productRepo.findAll();

    }
}
