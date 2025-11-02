package be.kdg.sa.land.repository;

import be.kdg.sa.land.domain.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

public interface AppointmentRepository extends JpaRepository<Appointment, UUID> {
    boolean existsByLicencePlate(String licencePlate);
    Optional<Appointment> findByLicencePlate(String licencePlate);

    long countByScheduledTimeBetween(LocalDateTime startOfHour, LocalDateTime endOfHour);
}
