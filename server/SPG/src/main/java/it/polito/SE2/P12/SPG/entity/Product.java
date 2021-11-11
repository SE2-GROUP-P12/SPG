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
    @Column(name = "total_quantity", nullable = false)
    private Double totalQuantity;
    @Column(name = "quantity_available", nullable = false)
    private Double quantityAvailable;
    @Column(name = "quantity_baskets", nullable = false)
    private Double quantityBaskets;
    @Column(name = "quantity_ordered", nullable = false)
    private Double quantityOrdered;
    @Column(name = "price", nullable = false)
    private double price;

    public Product(String name, String unitOfMeasurement, Double totalQuantity, double price) {
        this.name = name;
        this.unitOfMeasurement = unitOfMeasurement;
        this.totalQuantity = totalQuantity;
        this.quantityAvailable = totalQuantity;
        this.quantityBaskets = 0.0;
        this.quantityOrdered = 0.0;
        this.price = price;
    }

    public boolean moveToBasket(Double quantity) {
        if(this.quantityAvailable > quantity) {
            this.quantityAvailable -= quantity;
            this.quantityBaskets += quantity;
            return true;
        }
        return false;
    }
    public boolean moveFromBasket(Double quantity) {
        if(this.quantityAvailable >quantity) {
            this.quantityAvailable += quantity;
            this.quantityBaskets -= quantity;
            return true;
        }
        return false;
    }
}
