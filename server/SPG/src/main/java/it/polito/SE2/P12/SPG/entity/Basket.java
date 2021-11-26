package it.polito.SE2.P12.SPG.entity;

import it.polito.SE2.P12.SPG.interfaceEntity.BasketUserType;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.lang.model.element.ModuleElement;
import javax.persistence.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Entity
@Table(name = "basket")
@Getter
@Setter
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
    @ElementCollection(fetch = FetchType.EAGER)
    @MapKeyColumn(name = "product_id")
    @Column(name = "quantity")
    private Map<Product, Double> prods;

    public Basket(BasketUserType cust) {
        this.cust = (User) cust;
        this.prods = new HashMap<>();
    }

    public Basket(BasketUserType cust, Map<Product, Double> prods) {
        this.cust = (User) cust;
        this.prods = prods;
    }

    //Returns false if quantity is not available
    public Boolean addProduct(Product product, Double quantity) {
        if(product.moveFromAvailableToBasket(quantity)) {
            if(prods.containsKey(product)){
                prods.put(product,prods.get(product)+quantity);
            }
            else {
                prods.put(product, quantity);
            }
            return true;
        }
        return false;
    }

    public List<Product> getProductList() {
        return new ArrayList<>(prods.keySet());
    }

    public Map<Product, Double> getProductQuantityMap() {
        return this.prods;
    }

    @Override
    public String toString() {
        StringBuilder prodString = new StringBuilder("");
        for (Product p : this.prods.keySet()) {
            prodString.append("[(id=" + p.getProductId() + ")" + p.getName() + ", " + prods.get(p) + "],");
        }
        prodString.deleteCharAt(prodString.lastIndexOf(","));
        return "Basket{" +
                "basketId= " + basketId +
                ", cust= " + (cust == null ? "null" : ((User)cust).getUserId()) +
                ", prods= " + prodString +
                '}';
    }
}
