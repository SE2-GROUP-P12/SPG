package it.polito.SE2.P12.SPG.service;

import it.polito.SE2.P12.SPG.entity.Farmer;
import it.polito.SE2.P12.SPG.entity.Product;
import it.polito.SE2.P12.SPG.repository.ProductRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SpgProductService {

    private ProductRepo productRepo;

    @Autowired
    public SpgProductService(ProductRepo productRepo) {
        this.productRepo = productRepo;
    }

    public void populateDB(){
        Product temp1 = new Product ("Apples","Kg",50.0,2.50, "https://www.dissapore.com/wp-content/uploads/2020/11/come-conservare-le-mele.jpg");
        Product temp2 = new Product ("Flour","Kg",10.0,5.00, "https://www.misya.info/wp-content/uploads/2012/11/farina-0-2.jpg");
        Product temp3 = new Product ("Eggs","Units",36.0,6.25, "https://images.fidhouse.com/fidelitynews/wp-content/uploads/sites/6/2019/11/Come-pastorizzare-le-uova-7.jpg?w=580");
        Product temp4 = new Product ("Oranges","Kg",12.0,2.10, "https://www.ortiadomicilio.it/fattorialavalledeicavalli/wp-content/uploads/sites/15/2014/11/product_oranges.jpg");
        Product temp5 = new Product ("Cherries","Kg",15.0,4.00, "https://galleria.riza.it/files/2018-06/ciliegie.jpg");
        Product temp6 = new Product ("Bananas","Kg",22.0,6.25, "https://www.nonsprecare.it/wp-content/uploads/2018/12/benefici-delle-banane.jpg");
        Product temp7 = new Product ("Strawberries","Kg",13.0,2.50, "https://static.silhouettedonna.it/wp-content/uploads/2020/06/fragole-1070x669.jpg");
        Product temp8 = new Product ("Kiwi","Kg",17.0,5.00, "https://www.clinicamedicasanluca.it/wp-content/uploads/2021/01/kiwi-vitamine-900x450.jpg");
        Product temp9 = new Product ("Asparagus","Kg",95.0,6.25, "https://idsb.tmgrup.com.tr/ly/uploads/images/2021/08/03/133396.jpeg");
        Product temp10 = new Product ("Lemons","Kg",37.0,2.50,"https://www.gardeningknowhow.com/wp-content/uploads/2012/09/lemon-harvest-1.jpg");
        Product temp11 = new Product ("Pears","Kg",12.0,5.00, "https://snaped.fns.usda.gov/sites/default/files/styles/crop_ratio_7_5/public/seasonal-produce/2018-05/pears.jpg");
        Product temp12 = new Product ("Olives","Kg",46.0,6.25, "https://www.internationaloliveoil.org/wp-content/uploads/2020/08/CIOTOLA-DI-OLIVE.png");
        Product temp13 = new Product ("Peaches","Kg",51.0,2.50, "https://extension.usu.edu/preserve-the-harvest/images/peaches.jpg");
        Product temp14 = new Product ("Grapes","Kg",11.0,5.00, "https://www.nonsprecare.it/wp-content/uploads/2017/09/benefici-delluva-5.jpg");
        Product temp15 = new Product ("Cucumber","Kg",37.0,6.25, "https://upload.wikimedia.org/wikipedia/commons/thumb/3/3e/Australia_Cucumbers_for_sale_at_store.jpg/1200px-Australia_Cucumbers_for_sale_at_store.jpg");
        Product temp16 = new Product ("Cauliflowers","Kg",43.0,2.50, "https://www.ortodacoltivare.it/wp-content/uploads/2019/09/corimbo.jpg");
        Product temp17 = new Product ("Carrots","Kg",10.0,5.00, "https://www.ilgiornaledelcibo.it/wp-content/uploads/2017/05/shutterstock_440493100.jpg");
        Product temp18 = new Product ("Onions","Kg",12.0,6.25, "https://www.turismo.it/typo3temp/pics/be047004bd.jpg");
        Product temp19 = new Product ("Fennel","Kg",7.0,2.50, "https://media-cdn.greatbritishchefs.com/media/xzdhnc4b/img63560.jpg");
        Product temp20 = new Product ("Lettuce","Kg",19.0,5.00, "https://www.nonsprecare.it/wp-content/uploads/2019/01/benefici-della-lattuga-4.jpg");
        Product temp21 = new Product ("Potatoes","Kg",68.0,6.25, "https://www.bottegadicalabria.it/media/magefan_blog/patate_sila_igp_1200x628.jpg");
        Product temp22 = new Product ("Leeks","Kg",54.0,2.50, "https://www.tasteofhome.com/wp-content/uploads/2019/04/leeks-shutterstock_555301699.jpg");
        Product temp23 = new Product ("Celery","Kg",6.0,5.00, "https://cdn-prod.medicalnewstoday.com/content/images/articles/270/270678/fresh-celery-on-a-table.jpg");
        Product temp24 = new Product ("Spinach","Kg",3.0,6.25, "https://www.ilgiornaledelcibo.it/wp-content/uploads/2019/02/spinaci-proprieta.jpg");
        Product temp25 = new Product ("Peppers","Kg",5.0,2.50, "https://www.ricette.com/wp-content/uploads/2013/10/peperoni.jpg");
        Product temp26 = new Product ("Tomatoes","Kg",1.0,5.00, "https://www.altroconsumo.it/-/media/altroconsumo/images/themes/alimentazione/sicurezza%20alimentare/dossiers/pomodori%20guida%20allacquisto/pomodoro_pachino_dani_1600x900.jpg");
        Product temp27 = new Product ("Cabbages","Kg",24.0,6.25, "https://www.thespruceeats.com/thmb/pHyypYT3R7HUorjg9748gZnx-BQ=/2000x1500/filters:no_upscale():max_bytes(150000):strip_icc()/Savoy-cabbage-GettyImages-533691280-58a48ca25f9b58819cc41ec4.jpg");
        Product temp28 = new Product ("Artichokes","Kg",26.0,2.50, "https://blog.giallozafferano.it/allacciateilgrembiule/wp-content/uploads/2019/12/carciofi.jpg");
        Product temp29 = new Product ("Watermelon","Kg",65.0,5.00, "https://upload.wikimedia.org/wikipedia/commons/4/40/Watermelons.jpg");
        Product temp30 = new Product ("Turnips","Kg",61.0,6.25, "https://i0.wp.com/post.healthline.com/wp-content/uploads/2019/11/turnip-root-vegetable-1296x728-header-1296x728.jpg?w=1155&h=1528");
        Product temp31 = new Product ("Chard","Kg",5.0,2.50, "https://www.gruppomacro.com/data/blog/big/b/bietola1-64186.jpg");
        Product temp32 = new Product ("Radish","Kg",70.0,5.00, "https://wips.plug.it/cips/buonissimo.org/cms/2020/01/tagliare-radicchio.jpg?w=713&a=c&h=407");
        Product temp33 = new Product ("Thistles","Kg",30.0,6.25, "https://www.dissapore.com/wp-content/uploads/2020/03/cardi.jpg");
        Product temp34 = new Product ("Broccoli","Kg",40.0,2.50, "https://www.greenme.it/wp-content/uploads/2021/02/broccoli.jpg");
        Product temp35 = new Product ("Honey","Kg",13.0,5.00, "https://wips.plug.it/cips/paginegialle.it/magazine/cms/2021/06/miele-in-favo.jpg");
        Product temp36 = new Product ("Milk","L",6.0,6.25, "https://st.ilfattoquotidiano.it/wp-content/uploads/2020/01/15/latte.jpg");
        Product temp37 = new Product ("Capers","Kg",5.0,2.50, "https://www.nonsprecare.it/wp-content/uploads/2016/08/capperi-propriet%C3%A0-e-benefici-per-la-salute.jpg");
        Product temp38 = new Product ("Garlic","Kg",17.0,5.00, "https://file.cure-naturali.it/site/image/content/21683.jpg?format=jpg");
        Product temp39 = new Product ("Salami","Kg",41.0,6.25, "https://www.assaggiassisi.com/wp-content/uploads/2020/08/WhatsApp-Image-2020-09-07-at-17.49.22.jpeg");
        Product temp40 = new Product ("Ham","Kg",45.0,2.50, "https://www.tastafood.it/wp-content/uploads/2020/10/Prosciutto-cotto-Rosa.jpg");
        Product temp41 = new Product ("Raw Ham","Kg",14.0,5.00, "https://mymarca.logico.cloud/tmp/tt/1200x628x1/img_product/old_77187fef82a35ab77c729b36bd72524d.jpg");
        Product temp42 = new Product ("Rabbit Fillet","Kg",13.0,6.25, "https://image.migros.ch/product-zoom/fc736b88f50e131c34c2e021cf1236090a98cba3.jpg");
        Product temp43 = new Product ("Fassona Burger","Kg",52.0,2.50, "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQZTbfh7Rsc6-xzK1oS5c1lHoNl3ARS4Xjnm0zPwTfu12mqK9fHqCw9w-anhwHFfX2TE-k&usqp=CAU");
        Product temp44 = new Product ("Fresh sausage","Kg",13.0,5.00, "https://www.intavoliamo.it/Info/media/k2/items/cache/79e08f32fa8a036f84441baab7b7a7ff_XL.jpg?t=20210715_100607");
        Product temp45 = new Product ("Chicken Burger","Kg",6.0,6.25, "https://blog.giallozafferano.it/annacreazioniincucina/wp-content/uploads/2016/05/IMG_8397p-1.jpg");
        Product temp46 = new Product ("Butter","Kg",15.0,2.50, "https://static.gamberorosso.it/media/k2/items/src/1a8a316d47234667b12332b98c8d3692-768x509.jpg");
        Product temp47 = new Product ("Tomme","Kg",12.0,5.00, "https://lh3.googleusercontent.com/proxy/XO-XHANOU230D9Mh_BCchFCF14MIOCmZki9pfSNlvgSdbjkgS-z98f2Ma2AOW6rYvjV-1-lNPSdTPIj9zY4b06ycFtDhzzjnSPtLusH_zYemHfBxo-O6phJlEA");
        Product temp48 = new Product ("Parmesan","Kg",13.0,6.25, "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTlTaWx1It0lENNKOzwwqnRyOfQzOD-fpBTRg&usqp=CAU");
        Product temp49 = new Product ("Ricotta","Kg",67.0,2.50, "https://blog.giallozafferano.it/allacciateilgrembiule/wp-content/uploads/2019/05/come-fare-la-ricotta.jpg");
        Product temp50 = new Product ("Pecorino","Kg",13.0,5.00, "https://www.tartufidinorcia.it/wp-content/uploads/2018/08/pecorino-semi-stagionato_da_raw.jpg");







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


    public void setForecast(Long productId, Farmer farmer, Double forecast, String start, String end) {
        Product product = productRepo.findProductByProductId(productId);
        product.setFarmer(farmer);
        product.setQuantityForcast(forecast);
        product.setStartAvailability(start);
        product.setEndAvailability(end);
    }
}
