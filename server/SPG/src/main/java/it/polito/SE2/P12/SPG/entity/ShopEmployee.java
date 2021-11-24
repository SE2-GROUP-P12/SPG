package it.polito.SE2.P12.SPG.entity;

import it.polito.SE2.P12.SPG.utils.UserRole;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name="shop_employee")
@Data
@NoArgsConstructor
public class ShopEmployee extends User{

    public ShopEmployee(String name, String surname, String ssn,
                        String phoneNumber,
                        String email, String password){
        super(name,surname,ssn,phoneNumber,UserRole.ROLE_SHOP_EMPLOYEE,email,password );

        this.setRole(UserRole.ROLE_SHOP_EMPLOYEE);
    }
}
