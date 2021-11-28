package it.polito.SE2.P12.SPG.entity;

import com.fasterxml.jackson.annotation.JsonValue;
import it.polito.SE2.P12.SPG.utils.UserRole;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Entity
@DiscriminatorValue(UserRole.ROLE_FARMER)
@Table(name="farmer")
@Data
@NoArgsConstructor
public class Farmer extends User{

    @Column(name="company_name")
    protected String companyName;
    @OneToMany(mappedBy = "farmer")
    protected List<Product> prods;


    public Farmer(String name, String surname, String ssn,
                  String phoneNumber,
                  String email, String password){
        super(name, surname,ssn,phoneNumber,email, password);
        this.companyName= name + " " + surname;
    }

    public Farmer(String name, String surname, String ssn,
                  String phoneNumber,
                  String email, String password, String companyName){
        super(name, surname,ssn,phoneNumber,email, password);
        this.companyName=companyName;
    }

    @Override
    @JsonValue
    public Long getUserId() {
        return super.getUserId();
    }

    @Override
    public String toString() {
        return "Farmer{" +
                "userId=" + userId +
                ", name='" + name + '\'' +
                ", surname='" + surname + '\'' +
                ", ssn='" + ssn + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", active=" + active +
                ", role='" + role + '\'' +
                ", products=" + (prods==null ? "null": prods.stream().mapToLong(p -> p.getProductId()).toString()) +
                '}';
    }
}
