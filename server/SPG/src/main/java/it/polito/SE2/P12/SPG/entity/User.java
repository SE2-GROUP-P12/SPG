package it.polito.SE2.P12.SPG.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "user")
@Data
@NoArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long userId;
    private String name;
    private String surname;
    private String ssn;
    private String phoneNumber;
    private String role;
    private String email;
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

    @Override
    public String toString() {
        return "User{" +
                "name='" + name + '\'' +
                ", surname='" + surname + '\'' +
                ", role='" + role + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}