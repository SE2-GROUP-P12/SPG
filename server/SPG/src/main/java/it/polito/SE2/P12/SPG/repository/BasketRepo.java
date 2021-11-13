package it.polito.SE2.P12.SPG.repository;


import it.polito.SE2.P12.SPG.entity.Basket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


    @Repository
    public interface BasketRepo
            extends JpaRepository<Basket, Long> {
        public Basket findBasketByCust_UserId(Long userId);
    }


