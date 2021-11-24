package it.polito.SE2.P12.SPG.entity;

import it.polito.SE2.P12.SPG.utils.UserRole;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@DiscriminatorValue(UserRole.ROLE_CUSTOMER)
@Table(name="customer")
@Data
@NoArgsConstructor
public class Customer extends User{
    @Column(name = "address")
    private String address;
    @Column(name = "wallet")
    private Double wallet=0.00;

    public Customer(String name, String surname, String ssn,
                    String phoneNumber, String email,
                    String password,String address){
        super(name, surname, ssn, phoneNumber,email,password);
        this.address = address;
    }


}
