package it.polito.SE2.P12.SPG.entity;

import it.polito.SE2.P12.SPG.interfaceEntity.BasketUserType;
import it.polito.SE2.P12.SPG.utils.UserRole;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@DiscriminatorValue(UserRole.ROLE_SHOP_EMPLOYEE)
@Table(name = "shop_employee")
@Data
@NoArgsConstructor
public class ShopEmployee extends User implements BasketUserType {

    @OneToOne(mappedBy = "cust")
    private Basket basket;

    public ShopEmployee(String name, String surname, String ssn,
                        String phoneNumber,
                        String email, String password) {
        super(name, surname, ssn, phoneNumber, email, password);
    }

    @Override
    public Basket getBasket(){
        if(this.basket == null) {
            this.basket = new Basket(this);
        }
        return this.basket;
    }
}
