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
    Integer basket_id;
    @OneToOne
    Customer cust;
    @OneToMany
    List<Product> prods;

    public Basket(Customer cust, List<Product> prods) {
        this.cust = cust;
        this.prods = prods;
    }
}
