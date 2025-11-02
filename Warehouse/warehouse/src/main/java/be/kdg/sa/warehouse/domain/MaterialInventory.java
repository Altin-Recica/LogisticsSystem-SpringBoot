package be.kdg.sa.warehouse.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
public class MaterialInventory {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID materialInventoryId;

    @ManyToOne
    @JoinColumn(name = "material_id")
    private Material material;

    @ManyToOne
    @JoinColumn(name = "warehouse_id")
    private Warehouse warehouse;

    private int clientNumber;
    private double quantity;
    private LocalDateTime arrivalDate;

    public MaterialInventory() {}

    public MaterialInventory(Material material, Warehouse warehouse, int clientNumber, double quantity, LocalDateTime arrivalDate) {
        this.material = material;
        this.warehouse = warehouse;
        this.clientNumber = clientNumber;
        this.quantity = quantity;
        this.arrivalDate = arrivalDate;
    }

    public UUID getMaterialInventoryId() {
        return materialInventoryId;
    }

    public Material getMaterial() {
        return material;
    }

    public void setMaterial(Material material) {
        this.material = material;
    }

    public Warehouse getWarehouse() {
        return warehouse;
    }

    public void setWarehouse(Warehouse warehouse) {
        this.warehouse = warehouse;
    }

    public int getClientNumber() {
        return clientNumber;
    }

    public void setClientNumber(int clientNumber) {
        this.clientNumber = clientNumber;
    }

    public double getQuantity() {
        return quantity;
    }

    public void setQuantity(double quantity) {
        this.quantity = quantity;
    }

    public LocalDateTime getArrivalDate() {
        return arrivalDate;
    }
}
