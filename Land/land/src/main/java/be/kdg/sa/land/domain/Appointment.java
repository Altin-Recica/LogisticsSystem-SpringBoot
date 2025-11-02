package be.kdg.sa.land.domain;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
public class Appointment {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID appointmentID;

    private LocalDateTime scheduledTime;
    private String licencePlate;
    private String material;
    private long clientId;

    protected Appointment() {}

    public Appointment(LocalDateTime scheduledTime, String licencePlate, String material, long clientId) {
        this.scheduledTime = scheduledTime;
        this.licencePlate = licencePlate;
        this.material = material;
        this.clientId = clientId;
    }

    public long getClientId() {
        return clientId;
    }

    public void setClientId(long clientId) {
        this.clientId = clientId;
    }

    public LocalDateTime getScheduledTime() {
        return scheduledTime;
    }

    public String getLicencePlate() {
        return licencePlate;
    }

    public void setLicencePlate(String licencePlate) {
        this.licencePlate = licencePlate;
    }

    public String getMaterial() {
        return material;
    }
}