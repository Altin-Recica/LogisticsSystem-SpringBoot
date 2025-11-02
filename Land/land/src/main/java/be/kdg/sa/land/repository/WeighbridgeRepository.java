package be.kdg.sa.land.repository;

import be.kdg.sa.land.domain.Truck;
import be.kdg.sa.land.domain.Weighbridge;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Collection;
import java.util.Optional;
import java.util.UUID;

public interface WeighbridgeRepository extends JpaRepository<Weighbridge, UUID> {
    Optional<Weighbridge> findFirstByAvailableTrue();
    Optional<Weighbridge> findWeighbridgeByTruck(Truck truck);
    Collection<Weighbridge> findAllByAvailableTrue();

    @Query("SELECT w.truck FROM Weighbridge w WHERE w.truck IS NOT NULL")
    Collection<Truck> findAllTrucksInWeighbridges();
}


