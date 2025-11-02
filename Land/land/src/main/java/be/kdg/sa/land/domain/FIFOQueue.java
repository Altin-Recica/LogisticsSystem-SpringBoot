package be.kdg.sa.land.domain;

import jakarta.persistence.*;

import java.util.List;
import java.util.UUID;

@Entity
public class FIFOQueue {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID queueID;

    @OneToMany(fetch = FetchType.EAGER)
    private List<Truck> trucks;

    public FIFOQueue() {}

    public void addTruck(Truck truck) {
        trucks.add(truck);
    }

    public List<Truck> getAllTrucks() {
        return trucks;
    }

    public UUID getQueueID() {
        return queueID;
    }

    public List<Truck> getTrucks() {
        return trucks;
    }

    public void setTrucks(List<Truck> trucks) {
        this.trucks = trucks;
    }
}

