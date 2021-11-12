package it.polito.SE2.P12.SPG.entity;


import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Entity
@Data
@Table(name= "order_tab") //Se volessi chiamare la tabella "order", hibernate la confonderebbe con un "order by"
@NoArgsConstructor
public class Order {
    @Id
    @GeneratedValue(
            strategy = GenerationType.AUTO
    )
    private Long orderId;
    @OneToOne
    private User cust;
    @Column(name="date")
    private LocalDateTime date;
    @ElementCollection
    @MapKeyColumn(name="product_id")
    @Column(name="quantity")
    private Map<Product, Double> prods;

    public Order(User cust, LocalDateTime date, Map<Product, Double> prods) {
        this.cust=cust;
        this.date = date;
        this.prods = prods;
    }
}
