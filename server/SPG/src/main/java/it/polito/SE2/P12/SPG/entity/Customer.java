package it.polito.SE2.P12.SPG.entity;

import it.polito.SE2.P12.SPG.utils.UserRole;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name="customer")
@Data
@NoArgsConstructor
public class Customer extends User{
    @Column(name = "address")
    private String address;

    public Customer(String name, String surname, String ssn,
                    String phoneNumber,
                    String email, String password,String address){
        super(name, surname,ssn, phoneNumber,email,password);
        this.address = address;
        this.setRole(UserRole.ROLE_CUSTOMER);
    }


}
