package be.kdg.sa.land.domain;

import jakarta.persistence.*;

@Entity
public class Weighbridge {

    @Id
    private int weighbridgeID;

    @OneToOne(fetch = FetchType.EAGER)
    private Truck truck;

    private boolean available;

    protected Weighbridge() {  }

    public Weighbridge(Truck truck, boolean available) {
        this.truck = truck;
        this.available = available;
    }

    public int getWeighbridgeID() {
        return weighbridgeID;
    }

    public Truck getTruck() {
        return truck;
    }

    public void setTruck(Truck truck) {
        this.truck = truck;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }

    public boolean isAvailable() {
        return available;
    }
}
