package it.polito.SE2.P12.SPG.entity;

import lombok.*;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(
        name = "user",
        uniqueConstraints = {
                @UniqueConstraint(name = "ssn_unique", columnNames = "ssn"),
                @UniqueConstraint(name = "email_unique", columnNames = "email")
        }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
//@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public class User {
    @Id
    @GeneratedValue(
            strategy = GenerationType.AUTO
    )
    @Column(name="user_id")
    private Long userId;
    @Column(name = "name", nullable = false)
    private String name;
    @Column(name = "surname", nullable = false)
    private String surname;
    @Column(name = "ssn", nullable = false)
    private String ssn;
    @Column(name = "phone_number", nullable = false)
    private String phoneNumber;
    @Column(name = "role")
    private String role;
    @Column(name = "email", nullable = false)
    private String email;
    @Column(name = "password" /*, nullable = false*/)
    private String password;
    @Column(name = "wallet")
    private double wallet=0.00;
    @Column(name = "active")
    private Boolean active;
    @OneToOne(mappedBy = "cust")
    private Basket basket;
    @OneToMany(mappedBy = "cust")
    private List<Order> orderList;

    public User(String name, String surname, String ssn,
                String phoneNumber, String role,
                String email, String password) {
        this.name = name;
        this.surname = surname;
        this.ssn = ssn;
        this.phoneNumber = phoneNumber;
        this.role = role;
        this.email = email;
        this.password = password;
        this.active = false;
    }

    public Basket getBasket(){
        if(this.basket == null)
            this.basket = new Basket(this);
        return this.basket;
    }

    @Override
    public String toString() {
        return "User{" +
                "userId=" + userId +
                ", name='" + name + '\'' +
                ", surname='" + surname + '\'' +
                ", ssn='" + ssn + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", role='" + role + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", wallet=" + wallet +
                ", basket=" + (basket==null?"null":this.basket.getBasketId()) +
                '}';
    }
}