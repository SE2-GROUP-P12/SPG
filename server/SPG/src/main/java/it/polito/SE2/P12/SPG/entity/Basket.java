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
    @Column(name = "value")
    private Double value;
    @ElementCollection(fetch = FetchType.EAGER)
    @MapKeyColumn(name = "product_id")
    @Column(name = "quantity")
    private Map<Product, Double> prods;

    public Basket(BasketUserType cust) {
        this.cust = (User) cust;
        this.prods = new HashMap<>();
        this.value = 0.0;
    }

    public Basket(BasketUserType cust, Map<Product, Double> prods) {
        this.cust = (User) cust;
        this.prods = prods;
        this.value = 0.0;
        for (Map.Entry<Product, Double> e : prods.entrySet()) {
            this.value += e.getKey().getPrice() * e.getValue();
        }
    }

    //Returns false if quantity is not available
    public Boolean addProduct(Product product, Double quantity) {
        if (product.moveFromAvailableToBasket(quantity)) {
            if (prods.containsKey(product)) {
                prods.put(product, prods.get(product) + quantity);
            } else {
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

    public void add(Product product, Double quantity) {
        Double price = product.getPrice();
        Double val = 0.0;
        for (Map.Entry<Product, Double> entry : prods.entrySet()
        ) {
            if (entry.getKey().getProductId().equals(product.getProductId())) {
                val = entry.getValue();
            }
        }
        if (val != 0.0) {
            val += quantity;
            prods.remove(product);
            prods.put(product, val);
        } else {
            prods.put(product, quantity);
        }
        this.value += price * quantity;
    }

    public Boolean remove(Product product) {
        if (!prods.containsKey(product))
            return false;
        Double price = product.getPrice();
        this.value -= price * prods.get(product).doubleValue();
        prods.remove(product);
        return true;
    }

    @Override
    public String toString() {
        StringBuilder prodString = new StringBuilder("");
        for (Map.Entry<Product, Double> entry : this.prods.entrySet()) {
            prodString.append("[(id=" + entry.getKey().getProductId() + ")" + entry.getKey().getName() + ", " + entry.getValue() + "],");
        }
        prodString.deleteCharAt(prodString.lastIndexOf(","));
        return "Basket{" +
                "basketId= " + basketId +
                ", cust= " + (cust == null ? "null" : ((User) cust).getUserId()) +
                ", prods= " + prodString +
                '}';
    }
}
