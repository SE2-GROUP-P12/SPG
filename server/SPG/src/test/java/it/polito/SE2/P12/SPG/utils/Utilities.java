package it.polito.SE2.P12.SPG.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import it.polito.SE2.P12.SPG.entity.Product;

import java.util.ArrayList;
import java.util.List;

public class Utilities {
    public static List<Product> getDeserializedProductList(String jsonString) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode tree = objectMapper.readTree(jsonString);
        List<Product> productList = new ArrayList<>();
        for (JsonNode i : tree) {
            productList.add(new Product(
                    i.get("name").textValue(),
                    i.get("producer").textValue(),
                    i.get("unitOfMeasurement").textValue(),
                    i.get("totalQuantity").asDouble(),
                    i.get("price").asDouble()
            ));
        }
        return productList;
    }
}
