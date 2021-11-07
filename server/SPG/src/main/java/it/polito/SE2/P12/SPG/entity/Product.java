package it.polito.SE2.P12.SPG.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;


@Entity
@Table(name = "product")
@Data
@NoArgsConstructor
public class Product {

    @Id
    private Integer product_id;
    private String name;
    @Column(name = "unit_of_measurement")
    private String unitOfMeasurement;
    private Integer quantity;
    private Float price;



}
