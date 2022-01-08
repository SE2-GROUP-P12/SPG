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
    protected Long userId;
    @Column(name = "name", nullable = false)
    protected String name;
    @Column(name = "surname", nullable = false)
    protected String surname;
    @Column(name = "ssn", nullable = false)
    protected String ssn;
    @Column(name = "phone_number", nullable = false)
    protected String phoneNumber;
    @Column(name = "email", nullable = false)
    protected String email;
    @Column(name = "password" /*, nullable = false*/)
    protected String password;
    @Column(name = "active")
    protected Boolean active;
    @Column(name = "role", insertable = false, updatable = false)
    protected String role;
    @Column(name = "chatId", nullable = true)
    protected  String chatId="";

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
        this.chatId = "";
    }

    public User(String name, String surname, String ssn,
                String phoneNumber, String role,
                String email, String password) {
        this.name = name;
        this.surname = surname;
        this.ssn = ssn;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.password = password;
        this.active = false;
        this.role = role;
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
                ", active=" + active +
                ", role='" + role + '\'' +
                '}';
    }


}