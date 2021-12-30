package it.polito.SE2.P12.SPG.utils;

import it.polito.SE2.P12.SPG.entity.*;
import it.polito.SE2.P12.SPG.repository.*;
import it.polito.SE2.P12.SPG.utils.Constants;
import it.polito.SE2.P12.SPG.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

@Service
public class DBUtilsService {

    private final UserRepo userRepo;
    private final CustomerRepo customerRepo;
    private final FarmerRepo farmerRepo;
    private final AdminRepo adminRepo;
    private final ShopEmployeeRepo shopEmployeeRepo;
    private final BasketRepo basketRepo;
    private final OrderRepo orderRepo;
    private final ProductRepo productRepo;
    private final JWTUserHandlerRepo jwtUserHandlerRepo;
    private final WalletOperationRepo walletOperationRepo;

    @Autowired
    public DBUtilsService(UserRepo userRepo,
                          CustomerRepo customerRepo,
                          FarmerRepo farmerRepo,
                          AdminRepo adminRepo,
                          ShopEmployeeRepo shopEmployeeRepo,
                          BasketRepo basketRepo,
                          OrderRepo orderRepo,
                          ProductRepo productRepo,
                          JWTUserHandlerRepo jwtUserHandlerRepo, WalletOperationRepo walletOperationRepo) {
        this.userRepo = userRepo;
        this.customerRepo = customerRepo;
        this.farmerRepo = farmerRepo;
        this.adminRepo = adminRepo;
        this.shopEmployeeRepo = shopEmployeeRepo;
        this.basketRepo = basketRepo;
        this.orderRepo = orderRepo;
        this.productRepo = productRepo;
        this.jwtUserHandlerRepo = jwtUserHandlerRepo;
        this.walletOperationRepo = walletOperationRepo;
    }

    public void init() {
        populateUsers();
        populateProducts();
    }

    public void dropAll() {
        basketRepo.deleteAll();
        orderRepo.deleteAll();
        walletOperationRepo.deleteAll();
        productRepo.deleteAll();
        userRepo.deleteAll();
        jwtUserHandlerRepo.deleteAll();
    }

    //Original password is 'password' (don't modify salt on FE else all password will be different)
    private final String hashedPassword = "9acfb5c4e244f6a63f54536a08eb6bdd9315c50747e136e8bfd7df95edeecfa1";


    private void populateUsers() {
        //ADMIN
        Admin admin = new Admin("admin", "admin", "ADMIN00000000000", "0000000000",
                Constants.EMAIL_ADMIN, hashedPassword);
        //Customers
        Customer temp1 = new Customer("Mario", "Rossi", "RSSMRA00D12N376V", "01234567892", Constants.EMAIL_MARIO_ROSSI, hashedPassword, "Main street 1234", 95.0);
        Customer temp2 = new Customer("Paolo", "Bianchi", "BNCPLA00D12N376V", "01234567893",
                Constants.EMAIL_PAOLO_BIANCHI, hashedPassword, "Main street 1456", 12.50);
        //Shop Employee
        ShopEmployee temp3 = new ShopEmployee("Francesco", "Conte", "CNTFRN00D12N376V", "01234567894",
                Constants.EMAIL_FRANCESCO_CONTE, hashedPassword);
        //Farmer
        Farmer temp4 = new Farmer("Thomas", "Jefferson", "JFRTHM00D12N376V", "01234567895",
                Constants.EMAIL_THOMAS_JEFFERSON, hashedPassword);
        Farmer temp5 = new Farmer("Alexander", "Hamilton", "HMLLND00A11Z501E", "0123456743", Constants.EMAIL_ALEXANDER_HAMILTON, hashedPassword);
        if (userRepo.findUserByEmail(Constants.EMAIL_MARIO_ROSSI) == null) customerRepo.save(temp1);
        if (userRepo.findUserByEmail(Constants.EMAIL_PAOLO_BIANCHI) == null) customerRepo.save(temp2);
        if (userRepo.findUserByEmail(Constants.EMAIL_FRANCESCO_CONTE) == null) shopEmployeeRepo.save(temp3);
        if (userRepo.findUserByEmail(Constants.EMAIL_ADMIN) == null) adminRepo.save(admin);
        if (userRepo.findUserByEmail(Constants.EMAIL_THOMAS_JEFFERSON) == null) farmerRepo.save(temp4);
        if (userRepo.findUserByEmail(Constants.EMAIL_ALEXANDER_HAMILTON) == null) farmerRepo.save(temp5);
    }

    private void populateProducts() {
        Farmer farmer1 = farmerRepo.findFarmerByEmail(Constants.EMAIL_THOMAS_JEFFERSON);
        Farmer farmer2 = farmerRepo.findFarmerByEmail(Constants.EMAIL_ALEXANDER_HAMILTON);
        List<Product> prodList = new ArrayList<Product>();
        //Product temp1 =
        prodList.add(new Product("Apples", "Kg", 50.0, 2.50, "server/SPG/src/main/java/it/polito/SE2/P12/SPG/images/Apples.jpg", farmer1));
        prodList.add(new Product("Flour", "Kg", 10.0, 5.00, "server/SPG/src/main/java/it/polito/SE2/P12/SPG/images/Flour.jpg", farmer2));
        prodList.add(new Product("Eggs", "Units", 36.0, 6.25, "server/SPG/src/main/java/it/polito/SE2/P12/SPG/images/Eggs.jpg", farmer1));
        prodList.add(new Product("Oranges", "Kg", 12.0, 2.10, "server/SPG/src/main/java/it/polito/SE2/P12/SPG/images/Oranges.jpg", farmer2));
        prodList.add(new Product("Cherries", "Kg", 15.0, 4.00, "server/SPG/src/main/java/it/polito/SE2/P12/SPG/images/Cherries.jpg", farmer1));
        prodList.add(new Product("Bananas", "Kg", 22.0, 6.25, "server/SPG/src/main/java/it/polito/SE2/P12/SPG/images/Bananas.jpg", farmer1));
        prodList.add(new Product("Strawberries", "Kg", 13.0, 2.50, "server/SPG/src/main/java/it/polito/SE2/P12/SPG/images/Strawberries.jpg", farmer1));
        prodList.add(new Product("Kiwi", "Kg", 17.0, 5.00, "server/SPG/src/main/java/it/polito/SE2/P12/SPG/images/Kiwi.jpg", farmer2));
        prodList.add(new Product("Asparagus", "Kg", 95.0, 6.25, "server/SPG/src/main/java/it/polito/SE2/P12/SPG/images/Asparagus.jpg", farmer1));
        prodList.add(new Product("Lemons", "Kg", 37.0, 2.50, "server/SPG/src/main/java/it/polito/SE2/P12/SPG/images/Lemons.jpg", farmer2));
        prodList.add(new Product("Pears", "Kg", 12.0, 5.00, "server/SPG/src/main/java/it/polito/SE2/P12/SPG/images/Pears.jpg", farmer1));
        prodList.add(new Product("Olives", "Kg", 46.0, 6.25, "server/SPG/src/main/java/it/polito/SE2/P12/SPG/images/Olives.jpg", farmer1));
        prodList.add(new Product("Peaches", "Kg", 51.0, 2.50, "server/SPG/src/main/java/it/polito/SE2/P12/SPG/images/Peaches.jpg", farmer2));
        prodList.add(new Product("Grapes", "Kg", 11.0, 5.00, "server/SPG/src/main/java/it/polito/SE2/P12/SPG/images/Grapes.jpg", farmer1));
        prodList.add(new Product("Cucumber", "Kg", 37.0, 6.25, "server/SPG/src/main/java/it/polito/SE2/P12/SPG/images/Cucumber.jpg", farmer2));
        prodList.add(new Product("Cauliflowers", "Kg", 43.0, 2.50, "server/SPG/src/main/java/it/polito/SE2/P12/SPG/images/Cauliflowers.jpg", farmer2));
        prodList.add(new Product("Carrots", "Kg", 10.0, 5.00, "server/SPG/src/main/java/it/polito/SE2/P12/SPG/images/Carrots.jpg", farmer1));
        prodList.add(new Product("Onions", "Kg", 12.0, 6.25, "server/SPG/src/main/java/it/polito/SE2/P12/SPG/images/Onions.jpg", farmer2));
        prodList.add(new Product("Lettuce", "Kg", 19.0, 5.00, "server/SPG/src/main/java/it/polito/SE2/P12/SPG/images/Lettuce.jpg", farmer1));
        prodList.add(new Product("Potatoes", "Kg", 68.0, 6.25, "server/SPG/src/main/java/it/polito/SE2/P12/SPG/images/Potatoes.jpg", farmer2));
        prodList.add(new Product("Leeks", "Kg", 54.0, 2.50, "server/SPG/src/main/java/it/polito/SE2/P12/SPG/images/Leek.jpg", farmer1));
        prodList.add(new Product("Celery", "Kg", 6.0, 5.00, "server/SPG/src/main/java/it/polito/SE2/P12/SPG/images/Celery.jpg", farmer2));
        prodList.add(new Product("Spinach", "Kg", 3.0, 6.25, "server/SPG/src/main/java/it/polito/SE2/P12/SPG/images/Spinach.jpg", farmer1));
        prodList.add(new Product("Peppers", "Kg", 5.0, 2.50, "server/SPG/src/main/java/it/polito/SE2/P12/SPG/images/Peppers.jpg", farmer2));
        prodList.add(new Product("Tomatoes", "Kg", 1.0, 5.00, "server/SPG/src/main/java/it/polito/SE2/P12/SPG/images/Tomatoes.jpg", farmer2));
        prodList.add(new Product("Watermelon", "Kg", 65.0, 5.00, "server/SPG/src/main/java/it/polito/SE2/P12/SPG/images/Watermelon.jpg", farmer2));
        prodList.add(new Product("Chard", "Kg", 5.0, 2.50, "server/SPG/src/main/java/it/polito/SE2/P12/SPG/images/Chard.jpg", farmer2));
        prodList.add(new Product("Radish", "Kg", 70.0, 5.00, "server/SPG/src/main/java/it/polito/SE2/P12/SPG/images/Radish.jpg", farmer1));
        prodList.add(new Product("Thistles", "Kg", 30.0, 6.25, "server/SPG/src/main/java/it/polito/SE2/P12/SPG/images/Thistles.jpg", farmer1));
        prodList.add(new Product("Broccoli", "Kg", 40.0, 2.50, "server/SPG/src/main/java/it/polito/SE2/P12/SPG/images/Broccoli.jpg", farmer2));
        prodList.add(new Product("Honey", "Kg", 13.0, 5.00, "server/SPG/src/main/java/it/polito/SE2/P12/SPG/images/Honey.jpg", farmer2));
        prodList.add(new Product("Milk", "L", 6.0, 6.25, "server/SPG/src/main/java/it/polito/SE2/P12/SPG/images/Milk.jpg", farmer1));
        prodList.add(new Product("Capers", "Kg", 5.0, 2.50, "server/SPG/src/main/java/it/polito/SE2/P12/SPG/images/Capers.jpg", farmer2));
        prodList.add(new Product("Garlic", "Kg", 17.0, 5.00, "server/SPG/src/main/java/it/polito/SE2/P12/SPG/images/Garlic.jpg", farmer1));
        prodList.add(new Product("Salami", "Kg", 41.0, 6.25, "server/SPG/src/main/java/it/polito/SE2/P12/SPG/images/Salami.jpg", farmer2));
        prodList.add(new Product("Ham", "Kg", 45.0, 2.50, "server/SPG/src/main/java/it/polito/SE2/P12/SPG/images/Ham.jpg", farmer1));
        prodList.add(new Product("Raw Ham", "Kg", 14.0, 5.00, "server/SPG/src/main/java/it/polito/SE2/P12/SPG/images/Raw Ham.jpg", farmer2));
        /*
        Product temp43 = new Product("Fassona Burger", "Kg", 52.0, 2.50, "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQZTbfh7Rsc6-xzK1oS5c1lHoNl3ARS4Xjnm0zPwTfu12mqK9fHqCw9w-anhwHFfX2TE-k&usqp=CAU", farmer2);
        Product temp44 = new Product("Fresh sausage", "Kg", 13.0, 5.00, "https://www.intavoliamo.it/Info/media/k2/items/cache/79e08f32fa8a036f84441baab7b7a7ff_XL.jpg?t=20210715_100607", farmer2);
        Product temp45 = new Product("Chicken Burger", "Kg", 6.0, 6.25, "https://blog.giallozafferano.it/annacreazioniincucina/wp-content/uploads/2016/05/IMG_8397p-1.jpg", farmer1);
        Product temp46 = new Product("Butter", "Kg", 15.0, 2.50, "https://static.gamberorosso.it/media/k2/items/src/1a8a316d47234667b12332b98c8d3692-768x509.jpg", farmer1);
        Product temp47 = new Product("Tomme", "Kg", 12.0, 5.00, "https://lh3.googleusercontent.com/proxy/XO-XHANOU230D9Mh_BCchFCF14MIOCmZki9pfSNlvgSdbjkgS-z98f2Ma2AOW6rYvjV-1-lNPSdTPIj9zY4b06ycFtDhzzjnSPtLusH_zYemHfBxo-O6phJlEA", farmer2);
        Product temp48 = new Product("Parmesan", "Kg", 13.0, 6.25, "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTlTaWx1It0lENNKOzwwqnRyOfQzOD-fpBTRg&usqp=CAU", farmer2);
        Product temp49 = new Product("Ricotta", "Kg", 67.0, 2.50, "https://blog.giallozafferano.it/allacciateilgrembiule/wp-content/uploads/2019/05/come-fare-la-ricotta.jpg", farmer1);
        Product temp50 = new Product("Pecorino", "Kg", 13.0, 5.00, "https://www.tartufidinorcia.it/wp-content/uploads/2018/08/pecorino-semi-stagionato_da_raw.jpg", farmer2);
*/
        for (Product prod : prodList
        ) {
            if (productRepo.findProductByName(prod.getName()) == null) productRepo.save(prod);
        }
        /*
        if (productRepo.findProductByName("Fassona Burger") == null) productRepo.save(temp43);
        if (productRepo.findProductByName("Fresh sausage") == null) productRepo.save(temp44);
        if (productRepo.findProductByName("Chicken Burger") == null) productRepo.save(temp45);
        if (productRepo.findProductByName("Butter") == null) productRepo.save(temp46);
        if (productRepo.findProductByName("Tomme") == null) productRepo.save(temp47);
        if (productRepo.findProductByName("Parmesan") == null) productRepo.save(temp48);
        if (productRepo.findProductByName("Ricotta") == null) productRepo.save(temp49);
        if (productRepo.findProductByName("Pecorino") == null) productRepo.save(temp50);
        */
    }

    public void loadTestingProds() {
        Farmer farmer1 = new Farmer("farmer1", "farmer1Surname", "farmer_1234567890",
                "1234567890", "farmer1@test.com", "password");
        Farmer farmer2 = new Farmer("farmer2", "farmer2Surname", "farmer_1234567891",
                "1234567891", "farmer2@test.com", "password");
        farmerRepo.save(farmer1);
        farmerRepo.save(farmer2);
        Product prod1 = new Product("Prod1", "KG", 1000.0, 10.50F, farmer1);
        Product prod2 = new Product("Prod2", "KG", 100.0, 5.50F, farmer2);
        Product prod3 = new Product("Prod3", "KG", 20.0, 8.00F, farmer1);
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

    public void deleteOrderRepo() {
        this.orderRepo.deleteAll();
    }

}
