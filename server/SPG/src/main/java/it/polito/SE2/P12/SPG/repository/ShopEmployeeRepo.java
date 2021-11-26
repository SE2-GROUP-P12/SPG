package it.polito.SE2.P12.SPG.repository;

import it.polito.SE2.P12.SPG.entity.ShopEmployee;
import it.polito.SE2.P12.SPG.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface ShopEmployeeRepo
        extends JpaRepository<ShopEmployee, Long> {
}
