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
import java.util.Date;
import java.util.List;
import java.util.Map;

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
    @Column(name = "date")
    private Date date;
    @ElementCollection(fetch = FetchType.EAGER)
    @MapKeyColumn(name = "product_id")
    @Column(name = "quantity")
    private Map<Product, Double> prods;
    @Column(name = "value")
    private Double value;

    public Order(OrderUserType cust, long date, Map<Product, Double> prods) {
        this.cust = (User) cust;
        this.date = date;
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
            jsonGenerator.writeNumberField("orderId", order.getOrderId());
            jsonGenerator.writeStringField("email", order.getCust().getEmail());
            jsonGenerator.writeNumberField("value", order.getValue());
            jsonGenerator.writeArrayFieldStart("productList");
            for (Map.Entry<Product, Double> e : order.getProds().entrySet()) {
                Product p = e.getKey();
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
