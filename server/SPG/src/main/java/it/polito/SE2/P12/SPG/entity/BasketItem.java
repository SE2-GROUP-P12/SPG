package it.polito.SE2.P12.SPG.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Map;
@Entity
@Table(name = "basketItem")
@Data
@NoArgsConstructor
public class BasketItem {
    @Id
    @GeneratedValue(
            strategy = GenerationType.AUTO
    )
    @Column(name = "basketId")
    private Long basketId;
    @OneToOne
    private User cust;
    @OneToOne
    private Product prod;
    private Integer quantity;

    public BasketItem(Long basketId, User cust, Product product, Integer quantity) {
        this.basketId = basketId;
        this.cust = cust;
        this.prod = product;
        this.quantity = quantity;
    }
}
