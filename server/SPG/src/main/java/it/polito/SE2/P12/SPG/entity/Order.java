package it.polito.SE2.P12.SPG.entity;


import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Entity
@Data
@Table(name= "order_tab") //Se volessi chiamare la tabella corrispondente "order", hibernate la confonderebbe con un "order by"
@NoArgsConstructor
public class Order {
    @Id
    @GeneratedValue(
            strategy = GenerationType.AUTO
    )
    private String order_id;
    @OneToOne
    private Customer cust;
    @Column(name="date")
    private LocalDateTime date;
    @OneToMany
    private List<Product> prods;

    public Order(Customer cust, LocalDateTime d, List<Product> prods) {
        this.cust=cust;
        this.date = date;
        this.prods = prods;
    }
}
