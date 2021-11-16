package it.polito.SE2.P12.SPG.entity;

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

    public Customer(String address){
        super();
        this.address = address;
        this.setRole("ROLE_CUSTOMER");
    }
}
