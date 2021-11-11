package it.polito.SE2.P12.SPG.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Entity
@Table(name = "basket")
@Data
@NoArgsConstructor
public class Basket {
    @Id
    @GeneratedValue(
            strategy = GenerationType.AUTO
    )
    @Column(name = "basket_id")
    private Long basketId;
    @OneToOne
    private User cust;
    @ElementCollection
    @MapKeyColumn(name="product_id")
    @Column(name="quantity")
    private Map<Product, Double> prods;

    public Basket(User cust) {
        this.cust = cust;
        this.prods = new HashMap<>();
    }

    public Basket(User cust, Map<Product,Double> prods) {
        this.cust = cust;
        this.prods = prods;
    }
    public Basket addProductToBasket(Product product, Double quantity){
        prods.put(product, quantity);
        return this;
    }
    public List<Product> getProductList(){
        return new ArrayList<>(prods.keySet());
    }

    public Map<Product, Double> getProductMap() {
        return this.prods;
    }
}
