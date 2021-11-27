package it.polito.SE2.P12.SPG.entity;

import it.polito.SE2.P12.SPG.interfaceEntity.BasketUserType;
import it.polito.SE2.P12.SPG.interfaceEntity.OrderUserType;
import it.polito.SE2.P12.SPG.utils.UserRole;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Entity
@DiscriminatorValue(UserRole.ROLE_CUSTOMER)
@Table(name="customer")
@Data
@NoArgsConstructor
public class Customer extends User implements BasketUserType, OrderUserType {
    @Column(name = "address")
    private String address;
    @Column(name = "wallet")
    private Double wallet=0.00;
    @OneToOne(mappedBy = "cust")
    private Basket basket;
    @OneToMany(mappedBy = "cust")
    private List<Order> orders;


    public Customer(String name, String surname, String ssn,
                    String phoneNumber, String email,
                    String password,String address){
        super(name, surname, ssn, phoneNumber,email,password);
        this.address = address;
    }

    public Customer(String name, String surname, String ssn,
                    String phoneNumber, String email,
                    String password,String address, Double wallet){
        super(name, surname, ssn, phoneNumber,email,password);
        this.address = address;
        this.wallet = wallet;
    }

    @Override
    public Basket getBasket(){
        if(this.basket == null) {
            this.basket = new Basket(this);
        }
        return this.basket;
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
                ", basket=" + (basket==null ? "null": basket.getBasketId()) +
                ", orders=" + (orders==null ? "null": orders.stream().mapToLong(o -> o.getOrderId()).toString()) +
                '}';
    }
}
