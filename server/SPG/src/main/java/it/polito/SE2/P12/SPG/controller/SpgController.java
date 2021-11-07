package it.polito.SE2.P12.SPG.controller;

import it.polito.SE2.P12.SPG.entity.Product;
import it.polito.SE2.P12.SPG.service.SpgService;
import it.polito.SE2.P12.SPG.utils.API;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RestController
@RequestMapping(API.HOME)
public class SpgController {

    private final SpgService service;

    @Autowired
    public SpgController(SpgService service) {
        this.service = service;
    }

    @GetMapping("/")
    public ResponseEntity home(){
        return ResponseEntity.ok().build();
    }

    @GetMapping(API.ALL_PRODUCT)
    public ResponseEntity<List<Product>> getAllProduct(){
        return ResponseEntity.ok(service.getAllProduct());
    }
}
