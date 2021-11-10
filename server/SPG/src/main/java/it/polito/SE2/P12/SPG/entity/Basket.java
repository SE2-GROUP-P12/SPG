package it.polito.SE2.P12.SPG.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

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
    private Integer basketId;
    @OneToOne
    private Customer cust;
    @OneToMany
    private List<Product> prods;

    public Basket(Customer cust, List<Product> prods) {
        this.cust = cust;
        this.prods = prods;
    }
}
