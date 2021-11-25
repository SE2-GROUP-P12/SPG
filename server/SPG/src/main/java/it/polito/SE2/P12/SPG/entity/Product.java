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
    @Column(name = "producer", nullable = false)
    private String producer;
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
    @Column(name = "quantity_delivered", nullable = false)
    private Double quantityDelivered;
    @Column(name = "price", nullable = false)
    private Double price;
    @ManyToOne
    private Farmer farmer;

    public Product(String name, String producer, String unitOfMeasurement, Double totalQuantity, double price) {
        this.name = name;
        this.producer = producer;
        this.unitOfMeasurement = unitOfMeasurement;
        this.totalQuantity = totalQuantity;
        this.quantityAvailable = totalQuantity;
        this.quantityBaskets = 0.0;
        this.quantityOrdered = 0.0;
        this.quantityDelivered = 0.0;
        this.price = price;
        this.farmer = null;
    }

    public Product(String name, String producer, String unitOfMeasurement, Double totalQuantity, double price, Farmer farmer) {
        this.name = name;
        this.producer = producer;
        this.unitOfMeasurement = unitOfMeasurement;
        this.totalQuantity = totalQuantity;
        this.quantityAvailable = totalQuantity;
        this.quantityBaskets = 0.0;
        this.quantityOrdered = 0.0;
        this.quantityDelivered = 0.0;
        this.price = price;
        this.farmer = farmer;
    }

    public Product(String name, String unitOfMeasurement, Double totalQuantity, double price) {
        //WARNING this method sets the farmer to null, only to be used before we set farmer in the system
        this.name = name;
        this.producer = "default produces";
        this.unitOfMeasurement = unitOfMeasurement;
        this.totalQuantity = totalQuantity;
        this.quantityAvailable = totalQuantity;
        this.quantityBaskets = 0.0;
        this.quantityOrdered = 0.0;
        this.quantityDelivered = 0.0;
        this.price = price;
        this.farmer = null;
    }


    public Boolean moveFromAvailableToBasket(Double quantity) {
        if(this.quantityAvailable < quantity)
            return false;
        this.quantityAvailable -= quantity;
        this.quantityBaskets += quantity;
        return true;
    }
    public Boolean moveFromBasketToOrdered(Double quantity) {
        if(this.quantityBaskets < quantity)
            return false;
        this.quantityOrdered += quantity;
        this.quantityBaskets -= quantity;
        return true;
    }

    public Boolean moveFromBasketToAvailable(Double quantity) {
        if(this.quantityBaskets < quantity)
            return false;
        this.quantityAvailable += quantity;
        this.quantityBaskets -= quantity;
        return true;
    }

    public Boolean moveFromAvailableToOrdered(Double quantity) {
        if(this.quantityAvailable < quantity)
            return false;
        this.quantityAvailable -= quantity;
        this.quantityOrdered += quantity;
        return true;
    }

    public Boolean moveFromAvailableToDelivered(Double quantity) {
        if(this.quantityAvailable < quantity)
            return false;
        this.quantityAvailable -= quantity;
        this.quantityDelivered += quantity;
        return true;
    }

    public Boolean moveFromOrderedToDelivered(Double quantity) {
        if(this.quantityOrdered < quantity)
            return false;
        this.quantityOrdered -= quantity;
        this.quantityDelivered += quantity;
        return true;
    }
}
