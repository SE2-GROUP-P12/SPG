package it.polito.SE2.P12.SPG.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.apache.commons.validator.routines.UrlValidator;

import javax.persistence.*;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Base64;

@Slf4j
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
    @ManyToOne
    private Farmer farmer;
    @Column(name = "image_url")
    private String imageUrl;
    @Column(name = "base_64_image", columnDefinition = "LONGTEXT")
    private String base64Image;

    public Product(String name, String unitOfMeasurement, Double totalQuantity, double price) {
        this.name = name;
        this.unitOfMeasurement = unitOfMeasurement;
        this.totalQuantity = totalQuantity;
        this.quantityAvailable = totalQuantity;
        this.quantityForecast = totalQuantity;
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
        this.quantityForecast = totalQuantity;
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
        this.quantityForecast = totalQuantity;
        this.quantityConfirmed = 0.0;
        this.quantityBaskets = 0.0;
        this.quantityOrdered = 0.0;
        this.quantityDelivered = 0.0;
        this.price = price;
        this.farmer = null;
        this.imageUrl = imageUrl;
        this.base64Image = base64ImageFromPath(imageUrl);

    }

    private String base64ImageFromPath(String imageUrl) {
        String[] schemes = {"http", "https"}; // DEFAULT schemes = "http", "https"
        String base64Source = "";
        UrlValidator urlValidator = new UrlValidator(schemes);
        File imageFile = new File("tmp.jpg");
        if (urlValidator.isValid(imageUrl)) {
            try {
                FileUtils.copyURLToFile(new URL(imageUrl), imageFile);
            } catch (IOException e) {
                log.error(e.getMessage());
            }
        } else {
            imageFile = new File(imageUrl);
        }


        try {
            byte[] fileContent = FileUtils.readFileToByteArray(imageFile);
            base64Source = Base64.getEncoder().encodeToString(fileContent);
        } catch (IOException e) {
            log.error(e.getMessage());
        }
        return base64Source;
    }

    public Product(String name, Double totalQuantity, double price, Farmer farmer) {
        this.name = name;
        this.totalQuantity = totalQuantity;
        this.quantityAvailable = totalQuantity;
        this.quantityForecast = totalQuantity;
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
        this.quantityForecast = totalQuantity;
        this.quantityConfirmed = 0.0;
        this.quantityBaskets = 0.0;
        this.quantityOrdered = 0.0;
        this.quantityDelivered = 0.0;
        this.price = price;
        this.farmer = farmer;
        this.imageUrl = imageUrl;
        this.base64Image = base64ImageFromPath(this.imageUrl);

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
                ", farmer=" + (farmer == null ? "null" : farmer.getUserId()) +
                ", imageUrl='" + imageUrl + '\'' +
                '}';
    }
}
