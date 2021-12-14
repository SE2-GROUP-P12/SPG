package it.polito.SE2.P12.SPG.entity;

import it.polito.SE2.P12.SPG.interfaceEntity.BasketUserType;
import it.polito.SE2.P12.SPG.interfaceEntity.OrderUserType;
import it.polito.SE2.P12.SPG.interfaceEntity.WalletUserType;
import it.polito.SE2.P12.SPG.utils.UserRole;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

@Entity
@DiscriminatorValue(UserRole.ROLE_CUSTOMER)
@Table(name = "customer")
@Data
@NoArgsConstructor
public class Customer extends User implements BasketUserType, OrderUserType, WalletUserType {
    @Column(name = "address")
    private String address;
    @Column(name = "wallet")
    private Double wallet = 0.00;
    @OneToOne(mappedBy = "cust")
    private Basket basket;
    @OneToMany(mappedBy = "cust", fetch = FetchType.EAGER)
    private List<Order> orders;
    @OneToMany(mappedBy = "cust")
    private List<WalletOperation> walletOperations;

    public Customer(String name, String surname, String ssn,
                    String phoneNumber, String email,
                    String password, String address) {
        super(name, surname, ssn, phoneNumber, email, password);
        this.address = address;
        this.role = UserRole.ROLE_CUSTOMER;
    }

    public Customer(String name, String surname, String ssn,
                    String phoneNumber, String email,
                    String password, String address, Double wallet) {
        super(name, surname, ssn, phoneNumber, email, password);
        this.address = address;
        this.wallet = wallet;
        this.role = UserRole.ROLE_CUSTOMER;
    }

    @Override
    public Basket getBasket() {
        if (this.basket == null) {
            this.basket = new Basket(this);
        }
        return this.basket;
    }

    @Override
    public List<Order> getOrders() {
        if (this.orders == null) {
            this.orders = new ArrayList<>();
        }
        return this.orders;
    }

    @Override
    public String toString() {
        return "Customer{" +
                "userId=" + userId +
                ", name='" + name + '\'' +
                ", surname='" + surname + '\'' +
                ", ssn='" + ssn + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", active=" + active +
                ", role='" + role + '\'' +
                ", address='" + address + '\'' +
                ", wallet=" + wallet +
                ", basket=" + (basket == null ? "null" : basket.getBasketId()) +
                ", orders=" + (orders == null ? "null" : orders.stream().mapToLong(o -> o.getOrderId()).toString()) +
                '}';
    }

    public void pay(Double amount) {
        this.wallet -= amount;
    }


}
