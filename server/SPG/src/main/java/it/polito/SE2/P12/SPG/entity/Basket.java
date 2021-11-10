package it.polito.SE2.P12.SPG.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
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
    @Column(name = "basketId")
    private Long basketId;
    @OneToOne
    private User cust;
    @OneToMany
    private List<BasketItem > prods;

    public Basket(User cust, List<BasketItem> prods) {
        this.cust = cust;
        this.prods = prods;
    }
    public Basket addProductToBasket(Product product, Integer quantity, User cust){
        prods.add(new BasketItem(this.basketId, cust,product,quantity ));
        return  this;
    }
    public List <Product> getProducts(){
        List<Product>output = new ArrayList<Product>();
        for (BasketItem item: prods
             ) {output.add(item.getProd());

        }
        return output;
    }
}
