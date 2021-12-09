package it.polito.SE2.P12.SPG.entity;

import it.polito.SE2.P12.SPG.security.ApplicationUserRole;
import it.polito.SE2.P12.SPG.utils.UserRole;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@DiscriminatorValue(UserRole.ROLE_ADMIN)
@Table(name="admin")
@Data
@NoArgsConstructor
public class Admin extends User{

    public Admin(String name, String surname, String ssn,
                String phoneNumber,
                String email, String password) {
        super(name, surname, ssn, phoneNumber, email, password);
        this.role = UserRole.ROLE_ADMIN;
    }

    @Override
    public String toString() {
        return "Admin{" +
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
