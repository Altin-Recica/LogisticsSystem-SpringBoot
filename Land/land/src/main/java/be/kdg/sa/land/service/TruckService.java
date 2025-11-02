package be.kdg.sa.land.service;

import be.kdg.sa.land.domain.Truck;
import be.kdg.sa.land.repository.TruckRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Optional;

@Service
public class TruckService {
    private static final Logger logger = LoggerFactory.getLogger(TruckService.class);
    private final TruckRepository truckRepository;

    public TruckService(TruckRepository truckRepository) {
        this.truckRepository = truckRepository;
    }

    public Truck getTruck(String licencePlate) {
        try {
            logger.info("Retrieving truck: {}", licencePlate);
            Optional<Truck> truckOptional = this.truckRepository.findByLicencePlate(licencePlate);
            if (truckOptional.isPresent()) {
                logger.info("Truck retrieved successfully: {}", truckOptional.get());
                return truckOptional.get();
            } else {
                logger.warn("No truck found with licence plate: {}", licencePlate);
                return null;
            }
        } catch (Exception e) {
            logger.error("An unexpected error occurred while retrieving truck {}: {}", licencePlate, e.getMessage());
            throw new RuntimeException("Failed to retrieve truck", e);
        }
    }

    public Collection<Truck> getTrucks() {
        try {
            logger.info("Retrieving all trucks");
            Collection<Truck> trucks = this.truckRepository.findAll();
            logger.info("Retrieved {} trucks", trucks.size());
            return trucks;
        } catch (Exception e) {
            logger.error("An unexpected error occurred while retrieving all trucks: {}", e.getMessage());
            throw new RuntimeException("Failed to retrieve trucks", e);
        }
    }

    public Collection<Truck> getTrucksWithoutAppointment() {
        try {
            logger.info("Retrieving trucks without appointments");
            Collection<Truck> trucks = this.truckRepository.findByHasAppointment(false);
            logger.info("Retrieved {} trucks without appointments", trucks.size());
            return trucks;
        } catch (Exception e) {
            logger.error("An unexpected error occurred while retrieving trucks without appointments: {}", e.getMessage());
            throw new RuntimeException("Failed to retrieve trucks without appointments", e);
        }
    }

    public void updateWeighNumber(String licencePlate, Integer newWeighNumber) {
        try {
            Optional<Truck> truckOpt = truckRepository.findByLicencePlate(licencePlate);
            if (truckOpt.isPresent()) {
                Truck truck = truckOpt.get();
                truck.setWeighNumber(newWeighNumber);
                logger.info("Updated weigh number of truck {} to {}", licencePlate, newWeighNumber);
                truckRepository.save(truck);
            } else {
                logger.warn("Truck with licence plate {} not found", licencePlate);
            }
        } catch (Exception e) {
            logger.error("An unexpected error occurred while updating weigh number for truck {}: {}", licencePlate, e.getMessage());
            throw new RuntimeException("Failed to update weigh number", e);
        }
    }

    public Optional<Truck> hasAppointment(String licencePlate, boolean hasAppointment) {
        try {
            logger.info("Updating appointment of truck {} to {}", licencePlate, hasAppointment);
            return this.truckRepository.findByLicencePlateAndHasAppointment(licencePlate, hasAppointment);
        } catch (Exception e) {
            logger.error("An unexpected error occurred while updating appointment for truck {}: {}", licencePlate, e.getMessage());
            throw new RuntimeException("Failed to update appointment", e);
        }
    }

    public Optional<Truck> create(String licencePlate, double capacity, double currentWeight, double tareWeight, boolean hasAppointment) {
        try {
            LocalDateTime arrivalTime = LocalDateTime.now();
            Truck truck = new Truck(licencePlate, capacity, currentWeight, tareWeight, arrivalTime, hasAppointment, 0);
            Optional<Truck> optionalTruck = truckRepository.findByLicencePlate(licencePlate);

            if (optionalTruck.isEmpty()) {
                logger.info("Created new truck {}", licencePlate);
                truckRepository.save(truck);
                return Optional.of(truck);
            } else {
                logger.warn("Truck with licence plate {} already exists", licencePlate);
                return Optional.empty();
            }
        } catch (Exception e) {
            logger.error("An unexpected error occurred while creating truck {}: {}", licencePlate, e.getMessage());
            throw new RuntimeException("Failed to create truck", e);
        }
    }

    public void saveTruck(Truck truck) {
        try {
            logger.info("Saving truck {}", truck.getLicencePlate());
            truckRepository.save(truck);
        } catch (Exception e) {
            logger.error("An unexpected error occurred while saving truck {}: {}", truck.getLicencePlate(), e.getMessage());
            throw new RuntimeException("Failed to save truck", e);
        }
    }

}
