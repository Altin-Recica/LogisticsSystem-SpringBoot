package be.kdg.sa.land.repository;

import be.kdg.sa.land.domain.Truck;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.Optional;
import java.util.UUID;

public interface TruckRepository extends JpaRepository<Truck, UUID> {
    Optional<Truck> findByLicencePlate(String licencePlate);
    Optional<Truck> findByLicencePlateAndHasAppointment(String licencePlate, boolean hasAppointment);
    Collection<Truck> findByHasAppointment(boolean hasAppointment);
}
