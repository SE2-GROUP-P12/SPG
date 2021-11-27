package it.polito.SE2.P12.SPG.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import it.polito.SE2.P12.SPG.entity.Order;
import it.polito.SE2.P12.SPG.entity.Product;
import org.aspectj.weaver.ast.Or;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Utilities {
    //Product
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

    //Orders
    public static List<String> getDeserializedOrderIssuerList(String jsonString) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode tree = objectMapper.readTree(jsonString);
        List<String> orderIssuerList = new ArrayList<>();
        for (JsonNode i : tree) {
            orderIssuerList.add(i.get("email").asText());
        }
        return orderIssuerList;
    }

    /*
ProductList like that;
    [{
        "orderId":128,
        "email":"customer2@test.com",
        "productList":[
                        {"productId":"124","name":"Prod2","producer":"default producer","unit":"KG","unit price":"5.5","amount":"5.001"},
                        {"productId":"123","name":"Prod1","producer":"default producer","unit":"KG","unit price":"10.5","amount":"2.001"}
        ]
    }]
   */
    public static List<Map<String, Double>> getDeserializedOrderProductsByEmail(String jsonString, String email) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode tree = objectMapper.readTree(jsonString);
        List<Map<String, Double>> result = new ArrayList<>();
        for (JsonNode i : tree) {
            if (i.get("email").asText().equals(email)) {
                JsonNode subTree = i.get("productList");
                Map<String, Double> productList = new HashMap<>();
                for (JsonNode j : subTree)
                    productList.put(j.get("name").asText(), j.get("amount").asDouble());
                result.add(productList);
            }
        }
        return result;
    }

    public static Long getOrderIdFromResponse(String jsonString, String email) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode tree = objectMapper.readTree(jsonString);
        List<Map<String, Double>> result = new ArrayList<>();
        for (JsonNode i : tree) {
            if (i.get("email").asText().equals(email))
                return i.get("orderId").asLong();
        }
        return null;
    }

    public static Map<Product, Double> basketProductDeserializer(String jsonString) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode tree = objectMapper.readTree(jsonString);
        Map<Product, Double> productQuantity = new HashMap<>();
        for (JsonNode prod : tree) {
            Product tmp = new Product(
                    prod.get("name").asText(),
                    prod.get("unitOfMeasurement").asText(),
                    prod.get("totalQuantity").asDouble(),
                    prod.get("price").asDouble(),
                    "");
            productQuantity.put(tmp, prod.get("quantityAvailable").asDouble());
        }
        return productQuantity;
    }
}
