package it.polito.SE2.P12.SPG.repository;

import it.polito.SE2.P12.SPG.utils.JWTUserHandlerImpl;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

@Transactional
@Repository
public interface JWTUserHandlerRepo extends JpaRepository<JWTUserHandlerImpl, Long> {

    JWTUserHandlerImpl findJWTUserHandlerImplByAccessToken(String accessToken);

    JWTUserHandlerImpl deleteJWTUserHandlerImplById(Long id);


    @Modifying
    @Query(value = "update JWTUserHandlerImpl j set j.valid = :valid where (j.userId = :userId and j.accessToken = :accessToken)")
    void updateTokenValidity(@Param(value = "valid") Boolean valid, @Param(value = "userId") Long userId, @Param(value = "accessToken") String accessToken);

}
