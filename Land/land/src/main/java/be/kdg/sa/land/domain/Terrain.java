package be.kdg.sa.land.domain;

import jakarta.persistence.*;

import java.util.List;
import java.util.UUID;

@Entity
public class Terrain {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID terrainID;

    @OneToMany(fetch = FetchType.EAGER)
    private List<Truck> trucks;

    public Terrain() {}

    public List<Truck> getTrucks() {
        return trucks;
    }

    public void addTruck(Truck truck) {
        trucks.add(truck);
    }

    public void setTrucks(List<Truck> trucks) {
        this.trucks = trucks;
    }

    public UUID getTerrainID() {
        return terrainID;
    }
}

