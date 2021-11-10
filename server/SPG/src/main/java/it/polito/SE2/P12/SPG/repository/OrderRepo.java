package it.polito.SE2.P12.SPG.repository;

import it.polito.SE2.P12.SPG.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepo
        extends JpaRepository<Order, Long> {
    public void deleteByCust_UserId(Long userId);
}
