package it.polito.SE2.P12.SPG.entity;

import it.polito.SE2.P12.SPG.interfaceEntity.BasketUser;
import it.polito.SE2.P12.SPG.utils.UserRole;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.HashMap;

@Entity
@DiscriminatorValue(UserRole.ROLE_CUSTOMER)
@Table(name="customer")
@Data
@NoArgsConstructor
public class Customer extends User implements BasketUser {
    @Column(name = "address")
    private String address;
    @Column(name = "wallet")
    private Double wallet=0.00;
    @OneToOne(mappedBy = "cust")
    private Basket basket;

    public Customer(String name, String surname, String ssn,
                    String phoneNumber, String email,
                    String password,String address){
        super(name, surname, ssn, phoneNumber,email,password);
        this.address = address;
    }

    @Override
    public Basket getBasket(){
        if(this.basket == null) {
            this.basket = new Basket(this);
        }
        return this.basket;
    }


}
