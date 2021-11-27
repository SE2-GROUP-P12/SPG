package it.polito.SE2.P12.SPG.utils;

import it.polito.SE2.P12.SPG.entity.*;
import it.polito.SE2.P12.SPG.repository.*;
import it.polito.SE2.P12.SPG.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

@Service
public class DBUtilsService {

    private UserRepo userRepo;
    private CustomerRepo customerRepo;
    private FarmerRepo farmerRepo;
    private AdminRepo adminRepo;
    private ShopEmployeeRepo shopEmployeeRepo;
    private BasketRepo basketRepo;
    private OrderRepo orderRepo;
    private ProductRepo productRepo;
    private JWTUserHandlerRepo jwtUserHandlerRepo;

    @Autowired
    public DBUtilsService(UserRepo userRepo,
                          CustomerRepo customerRepo,
                          FarmerRepo farmerRepo,
                          AdminRepo adminRepo,
                          ShopEmployeeRepo shopEmployeeRepo,
                          BasketRepo basketRepo,
                          OrderRepo orderRepo,
                          ProductRepo productRepo,
                          JWTUserHandlerRepo jwtUserHandlerRepo) {
        this.userRepo = userRepo;
        this.customerRepo = customerRepo;
        this.farmerRepo = farmerRepo;
        this.adminRepo = adminRepo;
        this.shopEmployeeRepo = shopEmployeeRepo;
        this.basketRepo = basketRepo;
        this.orderRepo = orderRepo;
        this.productRepo = productRepo;
        this.jwtUserHandlerRepo = jwtUserHandlerRepo;
    }

    @PostConstruct
    public void init() {
        populateUsers();
        populateProducts();
    }

    public void dropAll() {
        basketRepo.deleteAll();
        orderRepo.deleteAll();
        productRepo.deleteAll();
        userRepo.deleteAll();
        jwtUserHandlerRepo.deleteAll();
    }

    private void populateUsers() {
        //ADMIN
        Admin admin = new Admin("admin", "admin", "ADMIN00000000000", "0000000000",
                "admin@foomail.com", "password");
        //Customers
        Customer temp1 = new Customer("Mario", "Rossi", "RSSMRA00D12N376V", "01234567892", "mario.rossi@gmail.com", "password", "Main street 1234");
        Customer temp2 = new Customer("Paolo", "Bianchi", "BNCPLA00D12N376V", "01234567892",
                "paolo.bianchi@gmail.com", "password", "Main street 1456");
        //Shop Employee
        ShopEmployee temp3 = new ShopEmployee("Francesco", "Conte", "CNTFRN00D12N376V", "01234567892",
                "francesco.conte@gmail.com", "password");
        //Farmer
        Farmer temp4 = new Farmer("Thomas", "Jefferson", "JFRTHM00D12N376V", "01234567892",
                "thomas.jefferson@gmail.com", "password");
        Farmer temp5 = new Farmer("Alexander", "Hamilton", "HMLLND00A11Z501E", "0123456743", "alexander.hamilton@yahoo.com", "password");
        if (userRepo.findUserByEmail("mario.rossi@gmail.com") == null) customerRepo.save(temp1);
        if (userRepo.findUserByEmail("paolo.bianchi@gmail.com") == null) customerRepo.save(temp2);
        if (userRepo.findUserByEmail("francesco.conte@gmail.com") == null) shopEmployeeRepo.save(temp3);
        if (userRepo.findUserByEmail("admin@foomail.com") == null) adminRepo.save(admin);
        if (userRepo.findUserByEmail("thomas.jefferson@gmail.com") == null) farmerRepo.save(temp4);
        if (userRepo.findUserByEmail("alexander.hamilton@yahoo.com") == null) farmerRepo.save(temp5);
    }

    private void populateProducts() {
        Farmer farmer1 = farmerRepo.findFarmerByEmail("thomas.jefferson@gmail.com");
        Farmer farmer2 = farmerRepo.findFarmerByEmail("alexander.hamilton@yahoo.com");

        Product temp1 = new Product("Apples", "Kg", 50.0, 2.50, "https://www.dissapore.com/wp-content/uploads/2020/11/come-conservare-le-mele.jpg", farmer1);
        Product temp2 = new Product("Flour", "Kg", 10.0, 5.00, "https://www.misya.info/wp-content/uploads/2012/11/farina-0-2.jpg", farmer2);
        Product temp3 = new Product("Eggs", "Units", 36.0, 6.25, "https://images.fidhouse.com/fidelitynews/wp-content/uploads/sites/6/2019/11/Come-pastorizzare-le-uova-7.jpg?w=580", farmer1);
        Product temp4 = new Product("Oranges", "Kg", 12.0, 2.10, "https://www.ortiadomicilio.it/fattorialavalledeicavalli/wp-content/uploads/sites/15/2014/11/product_oranges.jpg", farmer2);
        Product temp5 = new Product("Cherries", "Kg", 15.0, 4.00, "https://galleria.riza.it/files/2018-06/ciliegie.jpg", farmer1);
        Product temp6 = new Product("Bananas", "Kg", 22.0, 6.25, "https://www.nonsprecare.it/wp-content/uploads/2018/12/benefici-delle-banane.jpg", farmer1);
        Product temp7 = new Product("Strawberries", "Kg", 13.0, 2.50, "https://static.silhouettedonna.it/wp-content/uploads/2020/06/fragole-1070x669.jpg", farmer1);
        Product temp8 = new Product("Kiwi", "Kg", 17.0, 5.00, "https://www.clinicamedicasanluca.it/wp-content/uploads/2021/01/kiwi-vitamine-900x450.jpg", farmer2);
        Product temp9 = new Product("Asparagus", "Kg", 95.0, 6.25, "https://idsb.tmgrup.com.tr/ly/uploads/images/2021/08/03/133396.jpeg", farmer1);
        Product temp10 = new Product("Lemons", "Kg", 37.0, 2.50, "https://www.gardeningknowhow.com/wp-content/uploads/2012/09/lemon-harvest-1.jpg", farmer2);
        Product temp11 = new Product("Pears", "Kg", 12.0, 5.00, "https://snaped.fns.usda.gov/sites/default/files/styles/crop_ratio_7_5/public/seasonal-produce/2018-05/pears.jpg", farmer1);
        Product temp12 = new Product("Olives", "Kg", 46.0, 6.25, "https://www.internationaloliveoil.org/wp-content/uploads/2020/08/CIOTOLA-DI-OLIVE.png", farmer1);
        Product temp13 = new Product("Peaches", "Kg", 51.0, 2.50, "https://extension.usu.edu/preserve-the-harvest/images/peaches.jpg", farmer2);
        Product temp14 = new Product("Grapes", "Kg", 11.0, 5.00, "https://www.nonsprecare.it/wp-content/uploads/2017/09/benefici-delluva-5.jpg", farmer1);
        Product temp15 = new Product("Cucumber", "Kg", 37.0, 6.25, "https://upload.wikimedia.org/wikipedia/commons/thumb/3/3e/Australia_Cucumbers_for_sale_at_store.jpg/1200px-Australia_Cucumbers_for_sale_at_store.jpg", farmer2);
        Product temp16 = new Product("Cauliflowers", "Kg", 43.0, 2.50, "https://www.ortodacoltivare.it/wp-content/uploads/2019/09/corimbo.jpg", farmer2);
        Product temp17 = new Product("Carrots", "Kg", 10.0, 5.00, "https://www.ilgiornaledelcibo.it/wp-content/uploads/2017/05/shutterstock_440493100.jpg", farmer1);
        Product temp18 = new Product("Onions", "Kg", 12.0, 6.25, "https://www.turismo.it/typo3temp/pics/be047004bd.jpg", farmer2);
        Product temp19 = new Product("Fennel", "Kg", 7.0, 2.50, "https://media-cdn.greatbritishchefs.com/media/xzdhnc4b/img63560.jpg", farmer1);
        Product temp20 = new Product("Lettuce", "Kg", 19.0, 5.00, "https://www.nonsprecare.it/wp-content/uploads/2019/01/benefici-della-lattuga-4.jpg", farmer1);
        Product temp21 = new Product("Potatoes", "Kg", 68.0, 6.25, "https://www.bottegadicalabria.it/media/magefan_blog/patate_sila_igp_1200x628.jpg", farmer2);
        Product temp22 = new Product("Leeks", "Kg", 54.0, 2.50, "https://www.tasteofhome.com/wp-content/uploads/2019/04/leeks-shutterstock_555301699.jpg", farmer1);
        Product temp23 = new Product("Celery", "Kg", 6.0, 5.00, "https://cdn-prod.medicalnewstoday.com/content/images/articles/270/270678/fresh-celery-on-a-table.jpg", farmer2);
        Product temp24 = new Product("Spinach", "Kg", 3.0, 6.25, "https://www.ilgiornaledelcibo.it/wp-content/uploads/2019/02/spinaci-proprieta.jpg", farmer1);
        Product temp25 = new Product("Peppers", "Kg", 5.0, 2.50, "https://www.ricette.com/wp-content/uploads/2013/10/peperoni.jpg", farmer2);
        Product temp26 = new Product("Tomatoes", "Kg", 1.0, 5.00, "https://www.altroconsumo.it/-/media/altroconsumo/images/themes/alimentazione/sicurezza%20alimentare/dossiers/pomodori%20guida%20allacquisto/pomodoro_pachino_dani_1600x900.jpg", farmer2);
        Product temp27 = new Product("Cabbages", "Kg", 24.0, 6.25, "https://www.thespruceeats.com/thmb/pHyypYT3R7HUorjg9748gZnx-BQ=/2000x1500/filters:no_upscale():max_bytes(150000):strip_icc()/Savoy-cabbage-GettyImages-533691280-58a48ca25f9b58819cc41ec4.jpg", farmer2);
        Product temp28 = new Product("Artichokes", "Kg", 26.0, 2.50, "https://blog.giallozafferano.it/allacciateilgrembiule/wp-content/uploads/2019/12/carciofi.jpg", farmer1);
        Product temp29 = new Product("Watermelon", "Kg", 65.0, 5.00, "https://upload.wikimedia.org/wikipedia/commons/4/40/Watermelons.jpg", farmer2);
        Product temp30 = new Product("Turnips", "Kg", 61.0, 6.25, "https://i0.wp.com/post.healthline.com/wp-content/uploads/2019/11/turnip-root-vegetable-1296x728-header-1296x728.jpg?w=1155&h=1528", farmer1);
        Product temp31 = new Product("Chard", "Kg", 5.0, 2.50, "https://www.gruppomacro.com/data/blog/big/b/bietola1-64186.jpg", farmer2);
        Product temp32 = new Product("Radish", "Kg", 70.0, 5.00, "https://wips.plug.it/cips/buonissimo.org/cms/2020/01/tagliare-radicchio.jpg?w=713&a=c&h=407", farmer1);
        Product temp33 = new Product("Thistles", "Kg", 30.0, 6.25, "https://www.dissapore.com/wp-content/uploads/2020/03/cardi.jpg", farmer1);
        Product temp34 = new Product("Broccoli", "Kg", 40.0, 2.50, "https://www.greenme.it/wp-content/uploads/2021/02/broccoli.jpg", farmer2);
        Product temp35 = new Product("Honey", "Kg", 13.0, 5.00, "https://wips.plug.it/cips/paginegialle.it/magazine/cms/2021/06/miele-in-favo.jpg", farmer2);
        Product temp36 = new Product("Milk", "L", 6.0, 6.25, "https://st.ilfattoquotidiano.it/wp-content/uploads/2020/01/15/latte.jpg", farmer1);
        Product temp37 = new Product("Capers", "Kg", 5.0, 2.50, "https://www.nonsprecare.it/wp-content/uploads/2016/08/capperi-propriet%C3%A0-e-benefici-per-la-salute.jpg", farmer2);
        Product temp38 = new Product("Garlic", "Kg", 17.0, 5.00, "https://file.cure-naturali.it/site/image/content/21683.jpg?format=jpg", farmer1);
        Product temp39 = new Product("Salami", "Kg", 41.0, 6.25, "https://www.assaggiassisi.com/wp-content/uploads/2020/08/WhatsApp-Image-2020-09-07-at-17.49.22.jpeg", farmer2);
        Product temp40 = new Product("Ham", "Kg", 45.0, 2.50, "https://www.tastafood.it/wp-content/uploads/2020/10/Prosciutto-cotto-Rosa.jpg", farmer1);
        Product temp41 = new Product("Raw Ham", "Kg", 14.0, 5.00, "https://mymarca.logico.cloud/tmp/tt/1200x628x1/img_product/old_77187fef82a35ab77c729b36bd72524d.jpg", farmer2);
        Product temp42 = new Product("Rabbit Fillet", "Kg", 13.0, 6.25, "https://image.migros.ch/product-zoom/fc736b88f50e131c34c2e021cf1236090a98cba3.jpg", farmer1);
        Product temp43 = new Product("Fassona Burger", "Kg", 52.0, 2.50, "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQZTbfh7Rsc6-xzK1oS5c1lHoNl3ARS4Xjnm0zPwTfu12mqK9fHqCw9w-anhwHFfX2TE-k&usqp=CAU", farmer2);
        Product temp44 = new Product("Fresh sausage", "Kg", 13.0, 5.00, "https://www.intavoliamo.it/Info/media/k2/items/cache/79e08f32fa8a036f84441baab7b7a7ff_XL.jpg?t=20210715_100607", farmer2);
        Product temp45 = new Product("Chicken Burger", "Kg", 6.0, 6.25, "https://blog.giallozafferano.it/annacreazioniincucina/wp-content/uploads/2016/05/IMG_8397p-1.jpg", farmer1);
        Product temp46 = new Product("Butter", "Kg", 15.0, 2.50, "https://static.gamberorosso.it/media/k2/items/src/1a8a316d47234667b12332b98c8d3692-768x509.jpg", farmer1);
        Product temp47 = new Product("Tomme", "Kg", 12.0, 5.00, "https://lh3.googleusercontent.com/proxy/XO-XHANOU230D9Mh_BCchFCF14MIOCmZki9pfSNlvgSdbjkgS-z98f2Ma2AOW6rYvjV-1-lNPSdTPIj9zY4b06ycFtDhzzjnSPtLusH_zYemHfBxo-O6phJlEA", farmer2);
        Product temp48 = new Product("Parmesan", "Kg", 13.0, 6.25, "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTlTaWx1It0lENNKOzwwqnRyOfQzOD-fpBTRg&usqp=CAU", farmer2);
        Product temp49 = new Product("Ricotta", "Kg", 67.0, 2.50, "https://blog.giallozafferano.it/allacciateilgrembiule/wp-content/uploads/2019/05/come-fare-la-ricotta.jpg", farmer1);
        Product temp50 = new Product("Pecorino", "Kg", 13.0, 5.00, "https://www.tartufidinorcia.it/wp-content/uploads/2018/08/pecorino-semi-stagionato_da_raw.jpg", farmer2);

        if (productRepo.findProductByName("Apples") == null) productRepo.save(temp1);
        if (productRepo.findProductByName("Flour") == null) productRepo.save(temp2);
        if (productRepo.findProductByName("Eggs") == null) productRepo.save(temp3);
        if (productRepo.findProductByName("Oranges") == null) productRepo.save(temp4);
        if (productRepo.findProductByName("Cherries") == null) productRepo.save(temp5);
        if (productRepo.findProductByName("Bananas") == null) productRepo.save(temp6);
        if (productRepo.findProductByName("Strawberries") == null) productRepo.save(temp7);
        if (productRepo.findProductByName("Kiwi") == null) productRepo.save(temp8);
        if (productRepo.findProductByName("Asparagus") == null) productRepo.save(temp9);
        if (productRepo.findProductByName("Lemons") == null) productRepo.save(temp10);
        if (productRepo.findProductByName("Pears") == null) productRepo.save(temp11);
        if (productRepo.findProductByName("Olives") == null) productRepo.save(temp12);
        if (productRepo.findProductByName("Peaches") == null) productRepo.save(temp13);
        if (productRepo.findProductByName("Grapes") == null) productRepo.save(temp14);
        if (productRepo.findProductByName("Cucumber") == null) productRepo.save(temp15);
        if (productRepo.findProductByName("Cauliflowers") == null) productRepo.save(temp16);
        if (productRepo.findProductByName("Carrots") == null) productRepo.save(temp17);
        if (productRepo.findProductByName("Onions") == null) productRepo.save(temp18);
        if (productRepo.findProductByName("Fennel") == null) productRepo.save(temp19);
        if (productRepo.findProductByName("Lettuce") == null) productRepo.save(temp20);
        if (productRepo.findProductByName("Potatoes") == null) productRepo.save(temp21);
        if (productRepo.findProductByName("Leek") == null) productRepo.save(temp22);
        if (productRepo.findProductByName("Celery") == null) productRepo.save(temp23);
        if (productRepo.findProductByName("Spinach") == null) productRepo.save(temp24);
        if (productRepo.findProductByName("Peppers") == null) productRepo.save(temp25);
        if (productRepo.findProductByName("Tomatoes") == null) productRepo.save(temp26);
        if (productRepo.findProductByName("Cabbages") == null) productRepo.save(temp27);
        if (productRepo.findProductByName("Artichokes") == null) productRepo.save(temp28);
        if (productRepo.findProductByName("Watermelon") == null) productRepo.save(temp29);
        if (productRepo.findProductByName("Turnip") == null) productRepo.save(temp30);
        if (productRepo.findProductByName("Chard") == null) productRepo.save(temp31);
        if (productRepo.findProductByName("Radish") == null) productRepo.save(temp32);
        if (productRepo.findProductByName("Thistles") == null) productRepo.save(temp33);
        if (productRepo.findProductByName("Broccoli") == null) productRepo.save(temp34);
        if (productRepo.findProductByName("Honey") == null) productRepo.save(temp35);
        if (productRepo.findProductByName("Milk") == null) productRepo.save(temp36);
        if (productRepo.findProductByName("Capers") == null) productRepo.save(temp37);
        if (productRepo.findProductByName("Garlic") == null) productRepo.save(temp38);
        if (productRepo.findProductByName("Salami") == null) productRepo.save(temp39);
        if (productRepo.findProductByName("Ham") == null) productRepo.save(temp40);
        if (productRepo.findProductByName("Raw Ham") == null) productRepo.save(temp41);
        if (productRepo.findProductByName("Rabbit Fillet") == null) productRepo.save(temp42);
        if (productRepo.findProductByName("Fassona Burger") == null) productRepo.save(temp43);
        if (productRepo.findProductByName("Fresh sausage") == null) productRepo.save(temp44);
        if (productRepo.findProductByName("Chicken Burger") == null) productRepo.save(temp45);
        if (productRepo.findProductByName("Butter") == null) productRepo.save(temp46);
        if (productRepo.findProductByName("Tomme") == null) productRepo.save(temp47);
        if (productRepo.findProductByName("Parmesan") == null) productRepo.save(temp48);
        if (productRepo.findProductByName("Ricotta") == null) productRepo.save(temp49);
        if (productRepo.findProductByName("Pecorino") == null) productRepo.save(temp50);
    }

    public void loadTestingProds() {
        Product prod1 = new Product("Prod1", "Producer1", "KG", 1000.0, 10.50F);
        Product prod2 = new Product("Prod2", "Producer2", "KG", 100.0, 5.50F);
        Product prod3 = new Product("Prod3", "Producer3", "KG", 20.0, 8.00F);
        productRepo.save(prod1);
        productRepo.save(prod2);
        productRepo.save(prod3);
    }

    public Product getProd1Object() {
        return this.productRepo.findProductByName("Prod1");
    }

    public Product getProd2Object() {
        return this.productRepo.findProductByName("Prod2");
    }

    public void saveUser(User user) {
        this.userRepo.save(user);
    }

    public void saveCustomer(Customer customer) {
        this.customerRepo.save(customer);
    }

    public void saveFarmer(Farmer farmer) {
        this.farmerRepo.save(farmer);
    }

    public void saveAdmin(Admin admin) {
        this.adminRepo.save(admin);
    }

    public void saveShopEmployee(ShopEmployee shopEmployee) {
        this.shopEmployeeRepo.save(shopEmployee);
    }

    public void saveBasket(Basket basket) {
        this.basketRepo.save(basket);
    }

    public void saveOrder(Order order) {
        this.orderRepo.save(order);
    }

    public void saveProduct(Product product) {
        this.productRepo.save(product);
    }

    public void saveJwtUserHandler(JWTUserHandlerImpl jwtUserHandler) {
        this.jwtUserHandlerRepo.save(jwtUserHandler);
    }

}
