package it.polito.SE2.P12.SPG.entity;


import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import it.polito.SE2.P12.SPG.interfaceEntity.OrderUserType;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

;import static it.polito.SE2.P12.SPG.utils.OrderStatus.*;

@Entity
@Data
@Table(name = "order_tab") //Se volessi chiamare la tabella "order", hibernate la confonderebbe con un "order by"
@NoArgsConstructor
@JsonSerialize(using = Order.CustomSerializer.class)
public class Order {
    @Id
    @GeneratedValue(
            strategy = GenerationType.AUTO
    )
    private Long orderId;
    @ManyToOne
    private User cust;
    @Column(name = "creation_date")
    private LocalDateTime creation_date;
    @Column(name = "current_status_date")
    private LocalDateTime current_status_date;
    @ElementCollection(fetch = FetchType.EAGER)
    @MapKeyColumn(name = "product_id")
    @Column(name = "quantity")
    private Map<Product, Double> prods;
    @Column(name = "value")
    private Double value;
    @Column(name = "status")
    private String status = ORDER_STATUS_OPEN;


    public Order(OrderUserType cust, LocalDateTime date, Map<Product, Double> prods) {
        this.cust = (User) cust;
        this.creation_date = date;
        this.current_status_date = date;
        this.prods = prods;
        this.value = 0.0;
        for (Map.Entry<Product, Double> e : prods.entrySet()) {
            this.value += e.getKey().getPrice() * e.getValue();
        }
    }

    public List<Product> getProductList() {
        return new ArrayList<>(prods.keySet());
    }

    public User getCust() {
        return this.cust;
    }

    public boolean updateToPaidStatus() {
        //Order must be in open status
        if (!this.status.equals(ORDER_STATUS_CONFIRMED)) {
            return false;
        }
        //Update status and date
        this.status = ORDER_STATUS_PAID;
        this.current_status_date = LocalDateTime.now();
        return true;
    }

    public boolean updateToConfirmedStatus() {
        //Order must be in open status
        if (!this.status.equals(ORDER_STATUS_OPEN)) {
            return false;
        }
        //Update status and date
        this.status = ORDER_STATUS_CONFIRMED;
        this.current_status_date = LocalDateTime.now();
        return true;
    }


    public boolean updateToClosedStatus() {
        //Order must be in open status
        if (!this.status.equals(ORDER_STATUS_OPEN)) {
            return false;
        }
        //Update status and date
        this.status = ORDER_STATUS_CLOSED;
        this.current_status_date = LocalDateTime.now();
        return true;
    }

    public static class CustomSerializer extends StdSerializer<Order> {

        public CustomSerializer() {
            super(Order.class);
        }

        public CustomSerializer(Class<Order> t) {
            super(t);
        }

        @Override
        public void serialize(Order order, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {

            jsonGenerator.writeStartObject();
            //Set object parameter
            jsonGenerator.writeNumberField("orderId", order.getOrderId());
            jsonGenerator.writeStringField("email", order.getCust().getEmail());
            jsonGenerator.writeNumberField("value", order.getValue());
            //Set status parameters
            jsonGenerator.writeStringField("creationDate", order.creation_date.toString());
            jsonGenerator.writeStringField("currentStatusDate", order.current_status_date.toString());
            jsonGenerator.writeStringField("status", order.status);
            //Set products list
            jsonGenerator.writeArrayFieldStart("productList");
            for (Map.Entry<Product, Double> e : order.getProds().entrySet()) {
                Product p = e.getKey();
                //set product
                jsonGenerator.writeStartObject();
                jsonGenerator.writeStringField("productId", p.getProductId().toString());
                jsonGenerator.writeStringField("name", p.getName());
                jsonGenerator.writeStringField("producer", p.getFarmer().getCompanyName());
                jsonGenerator.writeStringField("unit", p.getUnitOfMeasurement());
                jsonGenerator.writeStringField("unit price", p.getPrice().toString());
                jsonGenerator.writeStringField("amount", e.getValue().toString());
                jsonGenerator.writeEndObject();
            }
            jsonGenerator.writeEndArray();
            jsonGenerator.writeEndObject();
        }
    }
}
