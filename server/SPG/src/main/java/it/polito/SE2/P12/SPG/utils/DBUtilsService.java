package it.polito.SE2.P12.SPG.utils;

import it.polito.SE2.P12.SPG.entity.*;
import it.polito.SE2.P12.SPG.repository.*;
import it.polito.SE2.P12.SPG.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

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

    //Original password is 'password' (don't modify salt on FE else all password will be different)
    private final String hashedPassword = "9acfb5c4e244f6a63f54536a08eb6bdd9315c50747e136e8bfd7df95edeecfa1";


    private void populateUsers() {
        //ADMIN
        Admin admin = new Admin("admin", "admin", "ADMIN00000000000", "0000000000",
                "admin@foomail.com", hashedPassword);
        //Customers
        Customer temp1 = new Customer("Mario", "Rossi", "RSSMRA00D12N376V", "01234567892", "mario.rossi@gmail.com", hashedPassword, "Main street 1234", 95.0);
        Customer temp2 = new Customer("Paolo", "Bianchi", "BNCPLA00D12N376V", "01234567892",
                "paolo.bianchi@gmail.com", hashedPassword, "Main street 1456", 12.50);
        //Shop Employee
        ShopEmployee temp3 = new ShopEmployee("Francesco", "Conte", "CNTFRN00D12N376V", "01234567892",
                "francesco.conte@gmail.com", hashedPassword);
        //Farmer
        Farmer temp4 = new Farmer("Thomas", "Jefferson", "JFRTHM00D12N376V", "01234567892",
                "thomas.jefferson@gmail.com", hashedPassword);
        Farmer temp5 = new Farmer("Alexander", "Hamilton", "HMLLND00A11Z501E", "0123456743", "alexander.hamilton@yahoo.com", hashedPassword);
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

        Product temp1 = new Product("Apples", "Kg", 50.0, 2.50, "https://images.unsplash.com/photo-1560806887-1e4cd0b6cbd6", farmer1);
        Product temp2 = new Product("Flour", "Kg", 10.0, 5.00, "https://images.unsplash.com/photo-1610725664285-7c57e6eeac3f", farmer2);
        Product temp3 = new Product("Eggs", "Units", 36.0, 6.25, "https://images.unsplash.com/photo-1498654077810-12c21d4d6dc3", farmer1);
        Product temp4 = new Product("Oranges", "Kg", 12.0, 2.10, "https://images.unsplash.com/photo-1611080626919-7cf5a9dbab5b", farmer2);
        Product temp5 = new Product("Cherries", "Kg", 15.0, 4.00, "https://images.unsplash.com/photo-1611096265583-5d745206f2a0", farmer1);
        Product temp6 = new Product("Bananas", "Kg", 22.0, 6.25, "https://images.unsplash.com/photo-1571771894821-ce9b6c11b08e", farmer1);
        Product temp7 = new Product("Strawberries", "Kg", 13.0, 2.50, "https://images.unsplash.com/photo-1566804770468-867f6158bda5", farmer1);
        Product temp8 = new Product("Kiwi", "Kg", 17.0, 5.00, "https://images.unsplash.com/photo-1616684000067-36952fde56ec", farmer2);
        Product temp9 = new Product("Asparagus", "Kg", 95.0, 6.25, "https://images.unsplash.com/photo-1589928009551-e9bded9ee7eb", farmer1);
        Product temp10 = new Product("Lemons", "Kg", 37.0, 2.50, "https://images.unsplash.com/photo-1609639643505-3c158a56de42", farmer2);
        Product temp11 = new Product("Pears", "Kg", 12.0, 5.00, "https://images.unsplash.com/photo-1542820242-a3699d4f2bfe", farmer1);
        Product temp12 = new Product("Olives", "Kg", 46.0, 6.25, "https://images.unsplash.com/photo-1572777856134-4e658bbf3b78", farmer1);
        Product temp13 = new Product("Peaches", "Kg", 51.0, 2.50, "https://images.unsplash.com/photo-1532704868953-d85f24176d73", farmer2);
        Product temp14 = new Product("Grapes", "Kg", 11.0, 5.00, "https://images.unsplash.com/photo-1599819177626-b50f9dd21c9b", farmer1);
        Product temp15 = new Product("Cucumber", "Kg", 37.0, 6.25, "https://images.unsplash.com/photo-1449300079323-02e209d9d3a6", farmer2);
        Product temp16 = new Product("Cauliflowers", "Kg", 43.0, 2.50, "https://images.unsplash.com/photo-1568584711075-3d021a7c3ca3", farmer2);
        Product temp17 = new Product("Carrots", "Kg", 10.0, 5.00, "https://images.unsplash.com/photo-1447175008436-054170c2e979", farmer1);
        Product temp18 = new Product("Onions", "Kg", 12.0, 6.25, "https://images.unsplash.com/photo-1580201092675-a0a6a6cafbb1", farmer2);
        Product temp20 = new Product("Lettuce", "Kg", 19.0, 5.00, "https://images.unsplash.com/photo-1622206151226-18ca2c9ab4a1", farmer1);
        Product temp21 = new Product("Potatoes", "Kg", 68.0, 6.25, "https://images.unsplash.com/photo-1518977676601-b53f82aba655", farmer2);
        Product temp22 = new Product("Leeks", "Kg", 54.0, 2.50, "https://images.unsplash.com/photo-1602769515559-e15133a7e992", farmer1);
        Product temp23 = new Product("Celery", "Kg", 6.0, 5.00, "https://images.unsplash.com/photo-1633653745758-7ef9d5b95f0f", farmer2);
        Product temp24 = new Product("Spinach", "Kg", 3.0, 6.25, "https://images.unsplash.com/photo-1576045057995-568f588f82fb", farmer1);
        Product temp25 = new Product("Peppers", "Kg", 5.0, 2.50, "https://images.unsplash.com/photo-1506365069540-904bcc762636", farmer2);
        Product temp26 = new Product("Tomatoes", "Kg", 1.0, 5.00, "https://images.unsplash.com/photo-1606588260160-0c4707ab7db5", farmer2);
        Product temp29 = new Product("Watermelon", "Kg", 65.0, 5.00, "https://images.unsplash.com/photo-1563015181-85d2e7c46e20", farmer2);
        Product temp30 = new Product("Turnips", "Kg", 61.0, 6.25, "https://images.unsplash.com/photo-1478900160460-2bfa23c92a4a", farmer1);
        Product temp31 = new Product("Chard", "Kg", 5.0, 2.50, "https://images.unsplash.com/photo-1593352769539-d7be34841f51", farmer2);
        Product temp32 = new Product("Radish", "Kg", 70.0, 5.00, "https://images.unsplash.com/photo-1593026122758-19bebc625104", farmer1);
        Product temp33 = new Product("Thistles", "Kg", 30.0, 6.25, "https://images.unsplash.com/photo-1592171403156-8bdf91b1d057", farmer1);
        Product temp34 = new Product("Broccoli", "Kg", 40.0, 2.50, "https://images.unsplash.com/photo-1614336215203-05a588f74627", farmer2);
        Product temp35 = new Product("Honey", "Kg", 13.0, 5.00, "https://images.unsplash.com/photo-1587049352895-5caf69ef3b6e", farmer2);
        Product temp36 = new Product("Milk", "L", 6.0, 6.25, "https://images.unsplash.com/photo-1523473827533-2a64d0d36748", farmer1);
        Product temp37 = new Product("Capers", "Kg", 5.0, 2.50, "https://images.unsplash.com/photo-1625009431843-18569fd7331b", farmer2);
        Product temp38 = new Product("Garlic", "Kg", 17.0, 5.00, "https://images.unsplash.com/photo-1588167109140-e81c3080d557", farmer1);
        Product temp39 = new Product("Salami", "Kg", 41.0, 6.25, "https://images.unsplash.com/photo-1620578504566-6d883f9aaddb", farmer2);
        Product temp40 = new Product("Ham", "Kg", 45.0, 2.50, "https://images.unsplash.com/photo-1607756794535-ba48a526b73a", farmer1);
        Product temp41 = new Product("Raw Ham", "Kg", 14.0, 5.00, "https://images.unsplash.com/photo-1609518317991-10acee259279", farmer2);
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
        if (productRepo.findProductByName("Lettuce") == null) productRepo.save(temp20);
        if (productRepo.findProductByName("Potatoes") == null) productRepo.save(temp21);
        if (productRepo.findProductByName("Leek") == null) productRepo.save(temp22);
        if (productRepo.findProductByName("Celery") == null) productRepo.save(temp23);
        if (productRepo.findProductByName("Spinach") == null) productRepo.save(temp24);
        if (productRepo.findProductByName("Peppers") == null) productRepo.save(temp25);
        if (productRepo.findProductByName("Tomatoes") == null) productRepo.save(temp26);
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

}
