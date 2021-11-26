package it.polito.SE2.P12.SPG.repository;

import it.polito.SE2.P12.SPG.entity.Farmer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FarmerRepo
        extends JpaRepository<Farmer, Long> {
    Farmer findFarmerByUserId(Long farmerId);
    Farmer findFarmerByEmail(String email);
}
