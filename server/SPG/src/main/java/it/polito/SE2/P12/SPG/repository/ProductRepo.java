package it.polito.SE2.P12.SPG.repository;

import it.polito.SE2.P12.SPG.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepo
        extends JpaRepository<Product, Long> {
    Product findProductByProductId(Long productId);
    Product findProductByName(String name);
    List<Product> findProductsByFarmer_Email(String email);
    List<Product> findProductsByFarmer_EmailAndAndQuantityForecastGreaterThan(String email, Double quantity);
    List<Product> findProductsByFarmer_EmailAndQuantityForecastEquals(String email, Double quantity);
}
