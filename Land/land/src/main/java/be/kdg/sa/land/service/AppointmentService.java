package be.kdg.sa.land.service;

import be.kdg.sa.land.domain.Appointment;
import be.kdg.sa.land.domain.Truck;
import be.kdg.sa.land.repository.AppointmentRepository;
import be.kdg.sa.land.repository.TruckRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Collections;
import java.util.Optional;

@Service
public class AppointmentService {
    private static final Logger logger = LoggerFactory.getLogger(AppointmentService.class);
    private final AppointmentRepository appointmentRepository;
    private final TruckRepository truckRepository;

    public AppointmentService(AppointmentRepository appointmentRepository, TruckRepository truckRepository) {
        this.appointmentRepository = appointmentRepository;
        this.truckRepository = truckRepository;
    }

    public Collection<Appointment> getAppointments() {
        logger.info("Getting all appointments");
        try {
            return this.appointmentRepository.findAll();
        } catch (Exception e) {
            logger.error("Error retrieving appointments: {}", e.getMessage());
            return Collections.emptyList();
        }
    }

    public Optional<Appointment> create(LocalDateTime scheduledTime, String licencePlate, String material, long clientId) {
        Appointment deliveryAppointment = new Appointment(scheduledTime, licencePlate, material, clientId);
        Optional<Truck> optionalTruck = truckRepository.findByLicencePlate(licencePlate);

        if (appointmentRepository.existsByLicencePlate(licencePlate)) {
            logger.warn("Appointment already exists for truck with licence plate: {}", licencePlate);
            return Optional.empty();
        }

        if (optionalTruck.isEmpty()) {
            logger.warn("No truck found with licence plate: {}", licencePlate);
            return Optional.empty();
        }

        Truck truck = optionalTruck.get();
        if (!truck.hasAppointment()) {
            truck.setHasAppointment(true);
            logger.info("Creating a new appointment for truck with licence plate: {}", truck.getLicencePlate());
            truckRepository.save(truck);
        } else {
            logger.warn("Truck {} already has an appointment", truck.getLicencePlate());
            return Optional.empty();
        }

        try {
            Appointment savedAppointment = appointmentRepository.save(deliveryAppointment);
            logger.info("New appointment created for truck: {}", savedAppointment.getLicencePlate());
            return Optional.of(savedAppointment);
        } catch (Exception e) {
            logger.error("Error saving appointment: {}", e.getMessage());
            return Optional.empty();
        }
    }

    public Optional<Appointment> getAppointment(String licencePlate) {
        logger.info("Retrieving appointment for truck: {}", licencePlate);
        Optional<Appointment> appointmentOptional = this.appointmentRepository.findByLicencePlate(licencePlate);

        if (appointmentOptional.isPresent()) {
            logger.info("Appointment retrieved successfully for truck: {}", licencePlate);
            return appointmentOptional;
        } else {
            logger.warn("No appointment found for truck with licence plate: {}", licencePlate);
            return Optional.empty();
        }
    }

    public long countAppointmentsInHour(LocalDateTime dateTime) {
        LocalDateTime startOfHour = dateTime.withMinute(0).withSecond(0).withNano(0);
        LocalDateTime endOfHour = startOfHour.plusHours(1);

        return appointmentRepository.countByScheduledTimeBetween(startOfHour, endOfHour);
    }
}
