package it.polito.SE2.P12.SPG.entity;


import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
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
    String order_id;
    @OneToOne
    Customer cust;
    @Column(name="date")
    Date date;
    @OneToMany
    List<Product> prods;

    public Order(Customer cust, Date d, List<Product> prods) {
        this.cust=cust;
        this.date = date;
        this.prods = prods;
    }
}
