package it.polito.SE2.P12.SPG.repository;

import it.polito.SE2.P12.SPG.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepo
        extends JpaRepository<Product, Integer> {

    @Query(value = "FROM Product WHERE productId= ?1")
    Product findProductById(Integer id);


}
