package it.polito.SE2.P12.SPG.repository;

import it.polito.SE2.P12.SPG.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepo
        extends JpaRepository<User, Long> {

    @Query(value = "FROM User WHERE email= ?1")
    User findUserByEmail(String email);

}
