package it.polito.SE2.P12.SPG.entity;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.io.FileUtils;

import javax.persistence.*;
import java.io.File;
import java.io.IOException;
import java.util.Base64;


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
    //Quantità ufficialmente messa a disposizione dal farmer a inizio settimana.
    //È la somma di quantityAvailable, quantityBasket, quantityOrdered e quantityDelivered
    @Column(name = "total_quantity", nullable = false)
    private Double totalQuantity;
    //Quantità disponibile all'acquisto (non impegnata in basket, ordini o consegnata)
    @Column(name = "quantity_available", nullable = false)
    private Double quantityAvailable;
    //Quantità prevista per la settimana seguente dal farmer. Da confermare.
    // Settata nel periodo da lunedì mattina a sabato mattina
    @Column(name = "quantity_forecast", nullable = false)
    private Double quantityForecast;
    //Quantità prevista per la settimana seguente dal farmer. Da confermare.
    // Settata nel periodo da sabato mattina a lunedì mattina, dove diventerà quantityForecast
    @Column(name = "quantity_forecast_next", nullable = false)
    private Double quantityForecastNext;
    //Quantità attualmente confermata dal farmer,
    //che alla fine della finestra di tempo di conferma, diventerà totalQuantity
    @Column(name = "quantity_confirmed", nullable = false)
    private Double quantityConfirmed;
    //Quantità di totalQuantity inserita nei carelli degli user
    @Column(name = "quantity_baskets", nullable = false)
    private Double quantityBaskets;
    //Quantità di totalQuantity inserita negli ordini degli user
    @Column(name = "quantity_ordered", nullable = false)
    private Double quantityOrdered;
    //Quantità di totalQuantity consegnata agli acquirenti tramite deliverOrder
    @Column(name = "quantity_delivered", nullable = false)
    private Double quantityDelivered;
    @Column(name = "price", nullable = false)
    private Double price;
    //start time for last prediction given
    @Column(name = "startAvailability", nullable = true)
    private String startAvailability;
    //end time for last prediction given
    @Column(name = "endAvailability", nullable = true)
    private String endAvailability;
    @ManyToOne
    private Farmer farmer;
    @Column(name = "image_url")
    private String imageUrl;
    @Column(name = "base_64_image",columnDefinition = "LONGTEXT")
    private String base64Image;

    public Product(String name, String unitOfMeasurement, Double totalQuantity, double price) {
        this.name = name;
        this.unitOfMeasurement = unitOfMeasurement;
        this.totalQuantity = totalQuantity;
        this.quantityAvailable = totalQuantity;
        this.quantityForecast = 0.0;
        this.quantityForecastNext = 0.0;
        this.quantityConfirmed = 0.0;
        this.quantityBaskets = 0.0;
        this.quantityOrdered = 0.0;
        this.quantityDelivered = 0.0;
        this.price = price;
        this.farmer = null;
        this.imageUrl = null;
        this.base64Image = null;
    }

    public Product(String name, String unitOfMeasurement, Double totalQuantity, double price, Farmer farmer) {
        this.name = name;
        this.unitOfMeasurement = unitOfMeasurement;
        this.totalQuantity = totalQuantity;
        this.quantityAvailable = totalQuantity;
        this.quantityForecast = 0.0;
        this.quantityForecastNext = 0.0;
        this.quantityConfirmed = 0.0;
        this.quantityBaskets = 0.0;
        this.quantityOrdered = 0.0;
        this.quantityDelivered = 0.0;
        this.price = price;
        this.farmer = farmer;
        this.imageUrl = null;
        this.base64Image = null;
    }

    public Product(String name, String unitOfMeasurement, Double totalQuantity, double price, String imageUrl) {
        this.name = name;
        this.unitOfMeasurement = unitOfMeasurement;
        this.totalQuantity = totalQuantity;
        this.quantityAvailable = totalQuantity;
        this.quantityForecast = 0.0;
        this.quantityForecastNext = 0.0;
        this.quantityConfirmed = 0.0;
        this.quantityBaskets = 0.0;
        this.quantityOrdered = 0.0;
        this.quantityDelivered = 0.0;
        this.price = price;
        this.farmer = null;
        this.imageUrl = imageUrl;
        try {
            byte[] fileContent = FileUtils.readFileToByteArray(new File(imageUrl));
            this.base64Image = Base64.getEncoder().encodeToString(fileContent);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public Product(String name, Double totalQuantity, double price, Farmer farmer) {
        this.name = name;
        this.totalQuantity = totalQuantity;
        this.quantityAvailable = totalQuantity;
        this.quantityForecast = 0.0;
        this.quantityForecastNext = 0.0;
        this.quantityConfirmed = 0.0;
        this.quantityBaskets = 0.0;
        this.quantityOrdered = 0.0;
        this.quantityDelivered = 0.0;
        this.price = price;
        this.farmer = farmer;
        this.imageUrl = null;
        this.base64Image = null;
    }


    public Product(String name, String unitOfMeasurement, Double totalQuantity, double price, String imageUrl, Farmer farmer) {
        this.name = name;
        this.unitOfMeasurement = unitOfMeasurement;
        this.totalQuantity = totalQuantity;
        this.quantityAvailable = totalQuantity;
        this.quantityForecast = 0.0;
        this.quantityForecastNext = 0.0;
        this.quantityConfirmed = 0.0;
        this.quantityBaskets = 0.0;
        this.quantityOrdered = 0.0;
        this.quantityDelivered = 0.0;
        this.price = price;
        this.farmer = farmer;
        this.imageUrl = imageUrl;
        try {
            byte[] fileContent = FileUtils.readFileToByteArray(new File(imageUrl));
            this.base64Image = Base64.getEncoder().encodeToString(fileContent);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public Boolean moveFromAvailableToBasket(Double quantity) {
        if (this.quantityAvailable < quantity)
            return false;
        this.quantityAvailable -= quantity;
        this.quantityBaskets += quantity;
        return true;
    }

    public Boolean moveFromBasketToOrdered(Double quantity) {
        if (this.quantityBaskets < quantity)
            return false;
        this.quantityOrdered += quantity;
        this.quantityBaskets -= quantity;
        return true;
    }

    public Boolean moveFromBasketToAvailable(Double quantity) {
        if (this.quantityBaskets < quantity)
            return false;
        this.quantityAvailable += quantity;
        this.quantityBaskets -= quantity;
        return true;
    }

    public Boolean moveFromAvailableToOrdered(Double quantity) {
        if (this.quantityAvailable < quantity)
            return false;
        this.quantityAvailable -= quantity;
        this.quantityOrdered += quantity;
        return true;
    }

    public Boolean moveFromAvailableToDelivered(Double quantity) {
        if (this.quantityAvailable < quantity)
            return false;
        this.quantityAvailable -= quantity;
        this.quantityDelivered += quantity;
        return true;
    }

    public Boolean moveFromOrderedToDelivered(Double quantity) {
        if (this.quantityOrdered < quantity)
            return false;
        this.quantityOrdered -= quantity;
        this.quantityDelivered += quantity;
        return true;
    }


    @Override
    public String toString() {
        return "Product{" +
                "productId=" + productId +
                ", name='" + name + '\'' +
                ", unitOfMeasurement='" + unitOfMeasurement + '\'' +
                ", totalQuantity=" + totalQuantity +
                ", quantityAvailable=" + quantityAvailable +
                ", quantityForecast=" + quantityForecast +
                ", quantityBaskets=" + quantityBaskets +
                ", quantityOrdered=" + quantityOrdered +
                ", quantityDelivered=" + quantityDelivered +
                ", price=" + price +
                ", startAvailability='" + startAvailability + '\'' +
                ", endAvailability='" + endAvailability + '\'' +
                ", farmer=" + (farmer == null ? "null" : farmer.getUserId()) +
                ", imageUrl='" + imageUrl + '\'' +
                '}';
    }
}
