package it.polito.SE2.P12.SPG.repository;


import it.polito.SE2.P12.SPG.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface CustomerRepo
        extends JpaRepository<Customer, Long> {
    Customer findCustomerByEmail(String email);
}


