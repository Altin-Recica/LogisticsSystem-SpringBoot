package be.kdg.sa.land.service;

import be.kdg.sa.land.domain.Truck;
import be.kdg.sa.land.domain.Weighbridge;
import be.kdg.sa.land.repository.TruckRepository;
import be.kdg.sa.land.repository.WeighbridgeRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.webjars.NotFoundException;

import java.util.Collection;
import java.util.Optional;
import java.util.UUID;

@Service
public class WeighbridgeService {

    private static final Logger logger = LoggerFactory.getLogger(WeighbridgeService.class);
    private final TruckRepository truckRepository;
    private final WeighbridgeRepository weighbridgeRepository;
    private final TruckService truckService;

    public WeighbridgeService(TruckRepository truckRepository, WeighbridgeRepository weighbridgeRepository, TruckService truckService) {
        this.truckRepository = truckRepository;
        this.weighbridgeRepository = weighbridgeRepository;
        this.truckService = truckService;
    }

    public void assignWeighNumber(UUID truckId) {
        try {
            Truck truck = truckRepository.findById(truckId)
                    .orElseThrow(() -> new NotFoundException("Truck not found"));
            Weighbridge weighbridge = weighbridgeRepository.findFirstByAvailableTrue()
                    .orElseThrow(() -> new NotFoundException("Available weighbridge not found"));

            truckService.updateWeighNumber(truck.getLicencePlate(), weighbridge.getWeighbridgeID());
            weighbridge.setAvailable(false);
            weighbridge.setTruck(truck);
            logger.info("Truck {} assigned to weighbridge {}", truck.getLicencePlate(), weighbridge.getWeighbridgeID());
            weighbridgeRepository.save(weighbridge);
        } catch (NotFoundException e) {
            logger.error("Error assigning weigh number: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            logger.error("An unexpected error occurred while assigning weigh number for truck {}: {}", truckId, e.getMessage());
            throw new RuntimeException("Failed to assign weigh number", e);
        }
    }

    public void removeTruckFromWeighbridge(Truck truck) {
        try {
            Weighbridge weighbridge = weighbridgeRepository.findWeighbridgeByTruck(truck)
                    .orElseThrow(() -> new NotFoundException("Weighbridge not found for the truck"));

            truckService.updateWeighNumber(truck.getLicencePlate(), 0);
            weighbridge.setTruck(null);
            weighbridge.setAvailable(true);
            logger.info("Truck {} removed from weighbridge {}", truck.getLicencePlate(), weighbridge.getWeighbridgeID());
            weighbridgeRepository.save(weighbridge);
        } catch (NotFoundException e) {
            logger.error("Error removing truck from weighbridge: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            logger.error("An unexpected error occurred while removing truck from weighbridge for truck {}: {}", truck.getLicencePlate(), e.getMessage());
            throw new RuntimeException("Failed to remove truck from weighbridge", e);
        }
    }

    public Weighbridge getWeighbridgeByTruck(Truck truck) {
        try {
            logger.info("Retrieving weighbridge for truck {}", truck.getLicencePlate());
            Optional<Weighbridge> weighbridgeOptional = weighbridgeRepository.findWeighbridgeByTruck(truck);

            if (weighbridgeOptional.isPresent()) {
                logger.info("Weighbridge retrieved successfully: {}", weighbridgeOptional.get());
                return weighbridgeOptional.get();
            } else {
                logger.warn("No weighbridge found for truck with licence plate: {}", truck.getLicencePlate());
                return null;
            }
        } catch (Exception e) {
            logger.error("An unexpected error occurred while retrieving weighbridge for truck {}: {}", truck.getLicencePlate(), e.getMessage());
            throw new RuntimeException("Failed to retrieve weighbridge", e);
        }
    }

    public Collection<Truck> getTrucksByWeighbridge() {
        try {
            logger.info("Retrieving trucks by weighbridge");
            Collection<Truck> trucks = weighbridgeRepository.findAllTrucksInWeighbridges();
            logger.info("Retrieved {} trucks by weighbridge", trucks.size());
            return trucks;
        } catch (Exception e) {
            logger.error("An unexpected error occurred while retrieving trucks by weighbridge: {}", e.getMessage());
            throw new RuntimeException("Failed to retrieve trucks by weighbridge", e);
        }
    }

    public Collection<Weighbridge> getAvailableWeighbridges() {
        try {
            logger.info("Retrieving available weighbridges");
            Collection<Weighbridge> availableWeighbridges = weighbridgeRepository.findAllByAvailableTrue();
            logger.info("Retrieved {} available weighbridges", availableWeighbridges.size());
            return availableWeighbridges;
        } catch (Exception e) {
            logger.error("An unexpected error occurred while retrieving available weighbridges: {}", e.getMessage());
            throw new RuntimeException("Failed to retrieve available weighbridges", e);
        }
    }

    public Collection<Weighbridge> getWeighbridges() {
        try {
            logger.info("Retrieving all weighbridges");
            Collection<Weighbridge> weighbridges = weighbridgeRepository.findAll();
            logger.info("Retrieved {} weighbridges ", weighbridges.size());
            return weighbridges;
        } catch (Exception e) {
            logger.error("An unexpected error occurred while retrieving all weighbridges: {}", e.getMessage());
            throw new RuntimeException("Failed to retrieve weighbridges", e);
        }
    }

}
