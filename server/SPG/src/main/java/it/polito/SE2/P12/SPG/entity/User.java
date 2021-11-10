package it.polito.SE2.P12.SPG.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(
        name = "user",
        uniqueConstraints = {
                @UniqueConstraint(name = "ssn_unique", columnNames = "ssn"),
                @UniqueConstraint(name = "email_unique", columnNames = "email")
        }
)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
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
    }
}