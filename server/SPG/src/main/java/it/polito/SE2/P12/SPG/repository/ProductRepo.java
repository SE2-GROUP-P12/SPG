package it.polito.SE2.P12.SPG.repository;

import it.polito.SE2.P12.SPG.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepo
        extends JpaRepository<Product, Long> {
    Product findProductByProductId(Long productId);
    Product findProductByName(String name);
}
