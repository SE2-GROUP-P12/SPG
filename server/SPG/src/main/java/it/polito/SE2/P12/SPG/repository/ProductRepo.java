package it.polito.SE2.P12.SPG.repository;

import it.polito.SE2.P12.SPG.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;


public interface ProductRepo
        extends JpaRepository<Product, Integer> {



}
