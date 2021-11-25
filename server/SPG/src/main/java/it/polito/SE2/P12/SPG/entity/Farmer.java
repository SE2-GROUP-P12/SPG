package it.polito.SE2.P12.SPG.entity;

import com.fasterxml.jackson.annotation.JsonValue;
import it.polito.SE2.P12.SPG.utils.UserRole;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@DiscriminatorValue(UserRole.ROLE_FARMER)
@Table(name="farmer")
@Data
@NoArgsConstructor
public class Farmer extends User{

    public Farmer(String name, String surname, String ssn,
                  String phoneNumber,
                  String email, String password){
        super(name, surname,ssn,phoneNumber,email, password);
    }

    @Override
    @JsonValue
    public Long getUserId() {
        return super.getUserId();
    }
}
