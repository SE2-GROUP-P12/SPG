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
        Product temp1 = new Product ("Mele","Kg",50.0,2.50);
        Product temp2 = new Product ("Farina","Kg",10.0,5.00);
        Product temp3 = new Product ("Uova","Units",36.0,6.25);
        Product temp4 = new Product ("Arance","Kg",12.0,2.10);
        Product temp5 = new Product ("Ciliege","Kg",15.0,4.00);
        Product temp6 = new Product ("Banane","Kg",22.0,6.25);
        Product temp7 = new Product ("Fragole","Kg",13.0,2.50);
        Product temp8 = new Product ("Kiwi","Kg",17.0,5.00);
        Product temp9 = new Product ("Asparagi","Kg",95.0,6.25);
        Product temp10 = new Product ("Limoni","Kg",37.0,2.50);
        Product temp11 = new Product ("Pere","Kg",12.0,5.00);
        Product temp12 = new Product ("Olive","Kg",46.0,6.25);
        Product temp13 = new Product ("Pesche","Kg",51.0,2.50);
        Product temp14 = new Product ("Uva","Kg",11.0,5.00);
        Product temp15 = new Product ("Cetrioli","Kg",37.0,6.25);
        Product temp16 = new Product ("Cavolfiori","Kg",43.0,2.50);
        Product temp17 = new Product ("Carote","Kg",10.0,5.00);
        Product temp18 = new Product ("Cipolle","Kg",12.0,6.25);
        Product temp19 = new Product ("Finocchi","Kg",7.0,2.50);
        Product temp20 = new Product ("Lattuga","Kg",19.0,5.00);
        Product temp21 = new Product ("Patate","Kg",68.0,6.25);
        Product temp22 = new Product ("Porri","Kg",54.0,2.50);
        Product temp23 = new Product ("Sedano","Kg",6.0,5.00);
        Product temp24 = new Product ("Spinaci","Kg",3.0,6.25);
        Product temp25 = new Product ("Peperoni","Kg",5.0,2.50);
        Product temp26 = new Product ("Pomodori","Kg",1.0,5.00);
        Product temp27 = new Product ("Cavoli","Kg",24.0,6.25);
        Product temp28 = new Product ("Carciofi","Kg",26.0,2.50);
        Product temp29 = new Product ("Angurie","Kg",65.0,5.00);
        Product temp30 = new Product ("Rape","Kg",61.0,6.25);
        Product temp31 = new Product ("Bietole","Kg",5.0,2.50);
        Product temp32 = new Product ("Radicchio","Kg",70.0,5.00);
        Product temp33 = new Product ("Cardi","Kg",30.0,6.25);
        Product temp34 = new Product ("Broccoli","Kg",40.0,2.50);
        Product temp35 = new Product ("Miele","Kg",13.0,5.00);
        Product temp36 = new Product ("Latte","L",6.0,6.25);
        Product temp37 = new Product ("Capperi","Kg",5.0,2.50);
        Product temp38 = new Product ("Aglio","Kg",17.0,5.00);
        Product temp39 = new Product ("Salame","Kg",41.0,6.25);
        Product temp40 = new Product ("Prosciutto Cotto","Kg",45.0,2.50);
        Product temp41 = new Product ("Prosciutto Crudo","Kg",14.0,5.00);
        Product temp42 = new Product ("Filetto di Coniglio","Kg",13.0,6.25);
        Product temp43 = new Product ("Hamburger di Fassona","Kg",52.0,2.50);
        Product temp44 = new Product ("Salsiccia fresca","Kg",13.0,5.00);
        Product temp45 = new Product ("Hamburger di Pollo","Kg",6.0,6.25);
        Product temp46 = new Product ("Burro","Kg",15.0,2.50);
        Product temp47 = new Product ("Toma","Kg",12.0,5.00);
        Product temp48 = new Product ("Parmigiano","Kg",13.0,6.25);
        Product temp49 = new Product ("Ricotta","Kg",67.0,2.50);
        Product temp50 = new Product ("Pecorino","Kg",13.0,5.00);







        if(productRepo.findProductByName("Mele")==null)productRepo.save(temp1);
        if(productRepo.findProductByName("Farina")==null)productRepo.save(temp2);
        if(productRepo.findProductByName("Uova")==null)productRepo.save(temp3);
        if(productRepo.findProductByName("Arance")==null)productRepo.save(temp4);
        if(productRepo.findProductByName("Ciliege")==null)productRepo.save(temp5);
        if(productRepo.findProductByName("Banane")==null)productRepo.save(temp6);
        if(productRepo.findProductByName("Fragole")==null)productRepo.save(temp7);
        if(productRepo.findProductByName("Kiwi")==null)productRepo.save(temp8);
        if(productRepo.findProductByName("Asparagi")==null)productRepo.save(temp9);
        if(productRepo.findProductByName("Limoni")==null)productRepo.save(temp10);
        if(productRepo.findProductByName("Pere")==null)productRepo.save(temp11);
        if(productRepo.findProductByName("Olive")==null)productRepo.save(temp12);
        if(productRepo.findProductByName("Pesche")==null)productRepo.save(temp13);
        if(productRepo.findProductByName("Uva")==null)productRepo.save(temp14);
        if(productRepo.findProductByName("Cetrioli")==null)productRepo.save(temp15);
        if(productRepo.findProductByName("Cavolfiori")==null)productRepo.save(temp16);
        if(productRepo.findProductByName("Carote")==null)productRepo.save(temp17);
        if(productRepo.findProductByName("Cipolle")==null)productRepo.save(temp18);
        if(productRepo.findProductByName("Finocchi")==null)productRepo.save(temp19);
        if(productRepo.findProductByName("Lattuga")==null)productRepo.save(temp20);
        if(productRepo.findProductByName("Patate")==null)productRepo.save(temp21);
        if(productRepo.findProductByName("Porri")==null)productRepo.save(temp22);
        if(productRepo.findProductByName("Sedano")==null)productRepo.save(temp23);
        if(productRepo.findProductByName("Spinaci")==null)productRepo.save(temp24);
        if(productRepo.findProductByName("Peperoni")==null)productRepo.save(temp25);
        if(productRepo.findProductByName("Pomodori")==null)productRepo.save(temp26);
        if(productRepo.findProductByName("Cavoli")==null)productRepo.save(temp27);
        if(productRepo.findProductByName("Carciofi")==null)productRepo.save(temp28);
        if(productRepo.findProductByName("Angurie")==null)productRepo.save(temp29);
        if(productRepo.findProductByName("Rape")==null)productRepo.save(temp30);
        if(productRepo.findProductByName("Bietole")==null)productRepo.save(temp31);
        if(productRepo.findProductByName("Radicchio")==null)productRepo.save(temp32);
        if(productRepo.findProductByName("Cardi")==null)productRepo.save(temp33);
        if(productRepo.findProductByName("Broccoli")==null)productRepo.save(temp34);
        if(productRepo.findProductByName("Miele")==null)productRepo.save(temp35);
        if(productRepo.findProductByName("Latte")==null)productRepo.save(temp36);
        if(productRepo.findProductByName("Capperi")==null)productRepo.save(temp37);
        if(productRepo.findProductByName("Aglio")==null)productRepo.save(temp38);
        if(productRepo.findProductByName("Salame")==null)productRepo.save(temp39);
        if(productRepo.findProductByName(" Prosciutto Cotto")==null)productRepo.save(temp40);
        if(productRepo.findProductByName("Prosciutto Crudo")==null)productRepo.save(temp41);
        if(productRepo.findProductByName("Filetto di Coniglio")==null)productRepo.save(temp42);
        if(productRepo.findProductByName("Hamburger di Fassona")==null)productRepo.save(temp43);
        if(productRepo.findProductByName("Salsiccia fresca")==null)productRepo.save(temp44);
        if(productRepo.findProductByName("Hamburger di Pollo")==null)productRepo.save(temp45);
        if(productRepo.findProductByName("Burro")==null)productRepo.save(temp46);
        if(productRepo.findProductByName("Toma")==null)productRepo.save(temp47);
        if(productRepo.findProductByName("Parmigiano")==null)productRepo.save(temp48);
        if(productRepo.findProductByName("Ricotta")==null)productRepo.save(temp49);
        if(productRepo.findProductByName("Pecorino")==null)productRepo.save(temp50);
    }
    public List<Product> getAllProduct(){
        List<Product> listProduct = productRepo.findAll();
        System.out.println(productRepo == null? "null" : "lista vuota");
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
