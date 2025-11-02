package be.kdg.sa.warehouse.domain;

import jakarta.persistence.*;

@Entity
public class OrderLine {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long orderLineId;

    @ManyToOne
    private Material material;
    private double quantity;
    private double pricePerTon;

    public OrderLine() {}

    public OrderLine(Material material, double quantity, double pricePerTon) {
        this.material = material;
        this.quantity = quantity;
        this.pricePerTon = pricePerTon;
    }

    public Material getMaterial() {
        return material;
    }

    

    public void setMaterial(Material material) {
        this.material = material;
    }

    public double getQuantity() {
        return quantity;
    }

    public void setQuantity(double quantity) {
        this.quantity = quantity;
    }

    public double getPricePerTon() {
        return pricePerTon;
    }

    public void setPricePerTon(double pricePerTon) {
        this.pricePerTon = pricePerTon;
    }
}
