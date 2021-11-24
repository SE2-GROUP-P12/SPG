package it.polito.SE2.P12.SPG.entity;

import lombok.*;
import org.springframework.beans.factory.annotation.Autowired;

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
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn(name="role", discriminatorType = DiscriminatorType.STRING)
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
    @Column(name = "email", nullable = false)
    private String email;
    @Column(name = "password" /*, nullable = false*/)
    private String password;
    @Column(name = "active")
    private Boolean active;
    @Column(name = "role", insertable = false, updatable = false)
    private String role;
    @OneToOne(mappedBy = "cust")
    private Basket basket;
    @OneToMany(mappedBy = "cust")
    private List<Order> orderList;

    public User(String name, String surname, String ssn,
                String phoneNumber,
                String email, String password) {
        this.name = name;
        this.surname = surname;
        this.ssn = ssn;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.password = password;
        this.active = false;
    }

    public Basket getBasket(){
        if(this.basket == null) {
            Basket b = new Basket(this);
            this.basket = b;
        }

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
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", basket=" + (basket==null?"null":this.basket.getBasketId()) +
                '}';
    }


}