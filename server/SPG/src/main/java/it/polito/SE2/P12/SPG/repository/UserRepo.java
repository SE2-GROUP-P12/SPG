package it.polito.SE2.P12.SPG.repository;

import it.polito.SE2.P12.SPG.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface UserRepo
        extends JpaRepository<User, Long> {
    User findUserByEmail(String email);
    User findUserBySsn(String ssn);
    User findUserByUserId(Long userId);
    Boolean existsByEmail(String email);
    Boolean existsBySsn(String ssn);
    List<User> findAllByRoleIn(List<String> roles);
    List<User> findAllByRole(String role);
}
