package be.kdg.sa.warehouse.domain;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
public class PurchaseOrder {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long orderId;

    private LocalDate orderDate;
    private long clientNumber;
    private String clientName;
    private double totalAmount;
    private boolean fulfilled;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<OrderLine> orderLines;

    public PurchaseOrder() {
    }

    public PurchaseOrder(long clientNumber, String clientName, LocalDate orderDate) {
        this.clientNumber = clientNumber;
        this.clientName = clientName;
        this.orderDate = orderDate;
        this.orderLines = new ArrayList<>();
    }

    public long getClientNumber() {
        return clientNumber;
    }

    public void setClientNumber(long clientNumber) {
        this.clientNumber = clientNumber;
    }

    public String getClientName() {
        return clientName;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public boolean isFulfilled() {
        return fulfilled;
    }

    public void setFulfilled(boolean fulfilled) {
        this.fulfilled = fulfilled;
    }

    public List<OrderLine> getOrderLines() {
        return orderLines;
    }

    public LocalDate getOrderDate() {
        return orderDate;
    }

    public long getOrderId() {
        return orderId;
    }
}
