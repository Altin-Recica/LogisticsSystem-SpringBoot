package be.kdg.sa.warehouse.domain;

import jakarta.persistence.*;
import java.util.UUID;


@Entity
public class Warehouse {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID warehouseID;

    private int warehouseNumber;
    private String materialType;
    private double currentCapacity;

    public Warehouse() {}

    public UUID getWarehouseID() {
        return warehouseID;
    }

    public int getNumber() {
        return warehouseNumber;
    }

    public String getMateriaalsoort() {
        return materialType;
    }

    public int getWarehouseNumber() {
        return warehouseNumber;
    }

    public double getCurrentCapacity() {
        return currentCapacity;
    }

    public void setCurrentCapacity(double currentCapacity) {
        this.currentCapacity = currentCapacity;
    }

    public void setMaterialType(String materialType) {
        this.materialType = materialType;
    }
}
