package it.polito.SE2.P12.SPG.repository;

import it.polito.SE2.P12.SPG.entity.WalletOperation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WalletOperationRepo extends JpaRepository<WalletOperation, Long> {
    List<WalletOperation> findWalletOperationsByWalletEmail(String email);
}
