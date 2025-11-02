package be.kdg.sa.land.service;

import be.kdg.sa.land.domain.FIFOQueue;
import be.kdg.sa.land.domain.Truck;
import be.kdg.sa.land.repository.FIFOQueueRepository;
import jakarta.persistence.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FIFOQueueService {
    private static final Logger logger = LoggerFactory.getLogger(FIFOQueueService.class);
    private final FIFOQueueRepository fifoQueueRepository;

    public FIFOQueueService(FIFOQueueRepository fifoQueueRepository) {
        this.fifoQueueRepository = fifoQueueRepository;
    }

    public void addTruckToQueue(Truck truck) {
        try {
            FIFOQueue queue = fifoQueueRepository.findFirstByOrderByQueueIDAsc()
                    .orElseThrow(() -> new EntityNotFoundException("Queue not found"));
            queue.addTruck(truck);
            logger.info("Added truck {} to queue {}", truck.getLicencePlate(), queue.getQueueID());
            fifoQueueRepository.save(queue);
        } catch (EntityNotFoundException e) {
            logger.error("Failed to add truck {} to queue: {}", truck.getLicencePlate(), e.getMessage());
            throw e;
        } catch (Exception e) {
            logger.error("An unexpected error occurred while adding truck {} to queue: {}", truck.getLicencePlate(), e.getMessage());
            throw new RuntimeException("Failed to add truck to queue", e);
        }
    }

    public List<Truck> getAllTrucksInQueue() {
        try {
            FIFOQueue queue = fifoQueueRepository.findFirstByOrderByQueueIDAsc()
                    .orElseThrow(() -> new EntityNotFoundException("Queue not found"));
            logger.info("Returning all trucks in queue {}", queue.getQueueID());
            return queue.getAllTrucks();
        } catch (EntityNotFoundException e) {
            logger.error("Failed to retrieve trucks from queue: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            logger.error("An unexpected error occurred while retrieving trucks from queue: {}", e.getMessage());
            throw new RuntimeException("Failed to retrieve trucks from queue", e);
        }
    }
}

