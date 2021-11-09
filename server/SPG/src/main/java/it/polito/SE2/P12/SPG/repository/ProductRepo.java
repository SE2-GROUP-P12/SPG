package it.polito.SE2.P12.SPG.repository;

import it.polito.SE2.P12.SPG.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

@Repository
public interface ProductRepo
        extends JpaRepository<Product, Integer> {

    @Query(value = "SELECT * FROM product p WHERE p.product_id= ?1")
    User findProductById(Integer id);


}
