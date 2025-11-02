package be.kdg.sa.land.domain;


import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
public class WeighbridgeTicket {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID ticketID;

    private double tareWeight;

    private double grossWeight;
    private double netWeight;
    private String licencePlate;
    private LocalDateTime timestamp;

    private String materialType;
    private long clientId;

    protected WeighbridgeTicket() {}

    public WeighbridgeTicket(double tareWeight, double grossWeight, double netWeight, String licencePlate, LocalDateTime timestamp, String materialType, long clientId) {
        this.tareWeight = tareWeight;
        this.grossWeight = grossWeight;
        this.netWeight = netWeight;
        this.licencePlate = licencePlate;
        this.timestamp = timestamp;
        this.materialType = materialType;
        this.clientId = clientId;
    }

    public double getNetWeight() {
        return netWeight;
    }

    public double getTareWeight() {
        return tareWeight;
    }

    public double getGrossWeight() {
        return grossWeight;
    }

    public long getClientId() {
        return clientId;
    }

    public void setClientId(long clientId) {
        this.clientId = clientId;
    }

    public String getMaterialType() {
        return materialType;
    }

    public String getLicencePlate() {
        return licencePlate;
    }

    public void setLicencePlate(String licencePlate) {
        this.licencePlate = licencePlate;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }
}
