package it.polito.SE2.P12.SPG.repository;

import it.polito.SE2.P12.SPG.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;



@Repository
public interface UserRepo
        extends JpaRepository<User, Long> {

    User findUserByEmail(String email);

    User findUserBySsn(String ssn);
    User findUserByUserId(Long userId);
}
