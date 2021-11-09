package it.polito.SE2.P12.SPG.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;


@Entity
@Table(name = "product")
@Data
@NoArgsConstructor
public class Product {

    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "product_id_generator"
    )
    @Id
    private Integer product_id;
    @Column(name = "name", nullable = false)
    private String name;
    @Column(name = "unit_of_measurement", nullable = false)
    private String unitOfMeasurement;
    @Column(name = "quantity", nullable = false)
    private Integer quantity;
    @Column(name = "price", nullable = false)
    private Float price;
}
