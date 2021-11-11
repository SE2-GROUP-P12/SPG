package it.polito.SE2.P12.SPG.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;


@Entity
@Table(name = "product")
@Data
@NoArgsConstructor
public class Product {

    @Id
    @GeneratedValue(
            strategy = GenerationType.AUTO
    )
    @Column(name = "product_id")
    private Long productId;
    @Column(name = "name", nullable = false)
    private String name;
    @Column(name = "unit_of_measurement", nullable = false)
    private String unitOfMeasurement;
    @Column(name = "quantity", nullable = false)
    private Integer quantity;
    @Column(name = "price", nullable = false)
    private double price;

    public Product(String name, String unitOfMeasurement, Integer quantity, double price) {
        this.name = name;
        this.unitOfMeasurement = unitOfMeasurement;
        this.quantity = quantity;
        this.price = price;
    }
}
