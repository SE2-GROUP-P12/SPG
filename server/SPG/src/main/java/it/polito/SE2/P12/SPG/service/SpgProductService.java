package it.polito.SE2.P12.SPG.service;

import it.polito.SE2.P12.SPG.entity.Product;
import it.polito.SE2.P12.SPG.entity.User;
import it.polito.SE2.P12.SPG.repository.ProductRepo;
import it.polito.SE2.P12.SPG.repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class SpgProductService {

    private ProductRepo productRepo;

    @Autowired
    public SpgProductService(ProductRepo productRepo) {
        this.productRepo = productRepo;
    }

    public void populateDB(){
        Product temp1 = new Product ("Apples","Kg",50.0,2.50);
        Product temp2 = new Product ("Flour","Kg",10.0,5.00);
        Product temp3 = new Product ("Eggs","Units",36.0,6.25);
        Product temp4 = new Product ("Oranges","Kg",12.0,2.10);
        Product temp5 = new Product ("Cherries","Kg",15.0,4.00);
        Product temp6 = new Product ("Bananas","Kg",22.0,6.25);
        Product temp7 = new Product ("Strawberries","Kg",13.0,2.50);
        Product temp8 = new Product ("Kiwi","Kg",17.0,5.00);
        Product temp9 = new Product ("Asparagus","Kg",95.0,6.25);
        Product temp10 = new Product ("Lemons","Kg",37.0,2.50);
        Product temp11 = new Product ("Pears","Kg",12.0,5.00);
        Product temp12 = new Product ("Olives","Kg",46.0,6.25);
        Product temp13 = new Product ("Peaches","Kg",51.0,2.50);
        Product temp14 = new Product ("Grapes","Kg",11.0,5.00);
        Product temp15 = new Product ("Cucumber","Kg",37.0,6.25);
        Product temp16 = new Product ("Cauliflowers","Kg",43.0,2.50);
        Product temp17 = new Product ("Carrots","Kg",10.0,5.00);
        Product temp18 = new Product ("Onions","Kg",12.0,6.25);
        Product temp19 = new Product ("Fennel","Kg",7.0,2.50);
        Product temp20 = new Product ("Lettuce","Kg",19.0,5.00);
        Product temp21 = new Product ("Potatoes","Kg",68.0,6.25);
        Product temp22 = new Product ("Leeks","Kg",54.0,2.50);
        Product temp23 = new Product ("Celery","Kg",6.0,5.00);
        Product temp24 = new Product ("Spinach","Kg",3.0,6.25);
        Product temp25 = new Product ("Peppers","Kg",5.0,2.50);
        Product temp26 = new Product ("Tomatoes","Kg",1.0,5.00);
        Product temp27 = new Product ("Cabbages","Kg",24.0,6.25);
        Product temp28 = new Product ("Artichokes","Kg",26.0,2.50);
        Product temp29 = new Product ("Watermelon","Kg",65.0,5.00);
        Product temp30 = new Product ("Turnips","Kg",61.0,6.25);
        Product temp31 = new Product ("Chard","Kg",5.0,2.50);
        Product temp32 = new Product ("Radish","Kg",70.0,5.00);
        Product temp33 = new Product ("Thistles","Kg",30.0,6.25);
        Product temp34 = new Product ("Broccoli","Kg",40.0,2.50);
        Product temp35 = new Product ("Honey","Kg",13.0,5.00);
        Product temp36 = new Product ("Milk","L",6.0,6.25);
        Product temp37 = new Product ("Capers","Kg",5.0,2.50);
        Product temp38 = new Product ("Garlic","Kg",17.0,5.00);
        Product temp39 = new Product ("Salami","Kg",41.0,6.25);
        Product temp40 = new Product ("Ham","Kg",45.0,2.50);
        Product temp41 = new Product ("Raw Ham","Kg",14.0,5.00);
        Product temp42 = new Product ("Rabbit Fillet","Kg",13.0,6.25);
        Product temp43 = new Product ("Fassona Burger","Kg",52.0,2.50);
        Product temp44 = new Product ("Fresh sausage","Kg",13.0,5.00);
        Product temp45 = new Product ("Chicken Burger","Kg",6.0,6.25);
        Product temp46 = new Product ("Butter","Kg",15.0,2.50);
        Product temp47 = new Product ("Tomme","Kg",12.0,5.00);
        Product temp48 = new Product ("Parmesan","Kg",13.0,6.25);
        Product temp49 = new Product ("Ricotta","Kg",67.0,2.50);
        Product temp50 = new Product ("Pecorino","Kg",13.0,5.00);







        if(productRepo.findProductByName("Apples")==null)productRepo.save(temp1);
        if(productRepo.findProductByName("Flour")==null)productRepo.save(temp2);
        if(productRepo.findProductByName("Eggs")==null)productRepo.save(temp3);
        if(productRepo.findProductByName("Oranges")==null)productRepo.save(temp4);
        if(productRepo.findProductByName("Cherries")==null)productRepo.save(temp5);
        if(productRepo.findProductByName("Bananas")==null)productRepo.save(temp6);
        if(productRepo.findProductByName("Strawberries")==null)productRepo.save(temp7);
        if(productRepo.findProductByName("Kiwi")==null)productRepo.save(temp8);
        if(productRepo.findProductByName("Asparagus")==null)productRepo.save(temp9);
        if(productRepo.findProductByName("Lemons")==null)productRepo.save(temp10);
        if(productRepo.findProductByName("Pears")==null)productRepo.save(temp11);
        if(productRepo.findProductByName("Olives")==null)productRepo.save(temp12);
        if(productRepo.findProductByName("Peaches")==null)productRepo.save(temp13);
        if(productRepo.findProductByName("Grapes")==null)productRepo.save(temp14);
        if(productRepo.findProductByName("Cucumber")==null)productRepo.save(temp15);
        if(productRepo.findProductByName("Cauliflowers")==null)productRepo.save(temp16);
        if(productRepo.findProductByName("Carrots")==null)productRepo.save(temp17);
        if(productRepo.findProductByName("Onions")==null)productRepo.save(temp18);
        if(productRepo.findProductByName("Fennel")==null)productRepo.save(temp19);
        if(productRepo.findProductByName("Lettuce")==null)productRepo.save(temp20);
        if(productRepo.findProductByName("Potatoes")==null)productRepo.save(temp21);
        if(productRepo.findProductByName("Leek")==null)productRepo.save(temp22);
        if(productRepo.findProductByName("Celery")==null)productRepo.save(temp23);
        if(productRepo.findProductByName("Spinach")==null)productRepo.save(temp24);
        if(productRepo.findProductByName("Peppers")==null)productRepo.save(temp25);
        if(productRepo.findProductByName("Tomatoes")==null)productRepo.save(temp26);
        if(productRepo.findProductByName("Cabbages")==null)productRepo.save(temp27);
        if(productRepo.findProductByName("Artichokes")==null)productRepo.save(temp28);
        if(productRepo.findProductByName("Watermelon")==null)productRepo.save(temp29);
        if(productRepo.findProductByName("Turnip")==null)productRepo.save(temp30);
        if(productRepo.findProductByName("Chard")==null)productRepo.save(temp31);
        if(productRepo.findProductByName("Radish")==null)productRepo.save(temp32);
        if(productRepo.findProductByName("Thistles")==null)productRepo.save(temp33);
        if(productRepo.findProductByName("Broccoli")==null)productRepo.save(temp34);
        if(productRepo.findProductByName("Honey")==null)productRepo.save(temp35);
        if(productRepo.findProductByName("Milk")==null)productRepo.save(temp36);
        if(productRepo.findProductByName("Capers")==null)productRepo.save(temp37);
        if(productRepo.findProductByName("Garlic")==null)productRepo.save(temp38);
        if(productRepo.findProductByName("Salami")==null)productRepo.save(temp39);
        if(productRepo.findProductByName("Ham")==null)productRepo.save(temp40);
        if(productRepo.findProductByName("Raw Ham")==null)productRepo.save(temp41);
        if(productRepo.findProductByName("Rabbit Fillet")==null)productRepo.save(temp42);
        if(productRepo.findProductByName("Fassona Burger")==null)productRepo.save(temp43);
        if(productRepo.findProductByName("Fresh sausage")==null)productRepo.save(temp44);
        if(productRepo.findProductByName("Chicken Burger")==null)productRepo.save(temp45);
        if(productRepo.findProductByName("Butter")==null)productRepo.save(temp46);
        if(productRepo.findProductByName("Tomme")==null)productRepo.save(temp47);
        if(productRepo.findProductByName("Parmesan")==null)productRepo.save(temp48);
        if(productRepo.findProductByName("Ricotta")==null)productRepo.save(temp49);
        if(productRepo.findProductByName("Pecorino")==null)productRepo.save(temp50);
    }
    public List<Product> getAllProduct(){
        List<Product> listProduct = productRepo.findAll();
        return listProduct;
    }
    public Product getProductById(Long productId){
        return productRepo.findProductByProductId(productId);
    }



    public Product test(){
        System.out.println("Test activated");
        Product p = new Product("Pompelmissimo", "Pompelmoducer", "quantità", 10.0, 12.30f);
        productRepo.save(p);
        return p;
    }





}
