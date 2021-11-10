package it.polito.SE2.P12.SPG.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name="basket")
@Data
@NoArgsConstructor
public class Basket {
    @Id
    Integer basket_id;
    @OneToOne
    Customer cust;
    @OneToMany
    List<Product> prods;
}
