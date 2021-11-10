package it.polito.SE2.P12.SPG.entity;


import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

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
    @OneToMany
    private List<Product> prods;

    public Order(User cust, LocalDateTime date, List<Product> prods) {
        this.cust=cust;
        this.date = date;
        this.prods = prods;
    }
}
