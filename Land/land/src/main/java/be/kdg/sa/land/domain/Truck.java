package be.kdg.sa.land.domain;


import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
public class Truck {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID truckID;
    private String licencePlate;
    private double capacity;
    private double currentWeight;
    private double tareWeight;
    private LocalDateTime arrivalTime;
    private boolean hasAppointment;
    private Integer weighNumber;

    protected Truck() {}

    public Truck(String licencePlate, double capacity, double currentWeight, double tareWeight, LocalDateTime arrivalTime, boolean hasAppointment, Integer weighNumber) {
        this.licencePlate = licencePlate;
        this.capacity = capacity;
        this.currentWeight = currentWeight;
        this.tareWeight = tareWeight;
        this.arrivalTime = arrivalTime;
        this.hasAppointment = hasAppointment;
        this.weighNumber = weighNumber;
    }

    public UUID getTruckID() {
        return truckID;
    }

    public String getLicencePlate() {
        return licencePlate;
    }

    public double getCapacity() {
        return capacity;
    }

    public double getCurrentWeight() {
        return currentWeight;
    }

    public boolean hasAppointment() {
        return hasAppointment;
    }

    public void setHasAppointment(boolean hasAppointment) {
        this.hasAppointment = hasAppointment;
    }

    public LocalDateTime getArrivalTime() {
        return arrivalTime;
    }

    public void setWeighNumber(Integer weighNumber) {
        this.weighNumber = weighNumber;
    }
}
