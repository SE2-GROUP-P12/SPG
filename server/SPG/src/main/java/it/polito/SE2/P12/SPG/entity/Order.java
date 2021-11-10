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
    String order_id;
    @OneToOne
    Customer cust;
    @Column(name="date")
    Date d;
    @OneToMany
    List<Product> prods;
}
