package be.kdg.sa.warehouse.domain;

import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
public class Invoice {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long invoiceId;
    private LocalDate invoiceDate;
    private long clientNumber;

    public Invoice() {}

    public Invoice(LocalDate invoiceDate, long clientNumber) {
        this.invoiceDate = invoiceDate;
        this.clientNumber = clientNumber;
    }

    public long getInvoiceId() {
        return invoiceId;
    }

    public LocalDate getInvoiceDate() {
        return invoiceDate;
    }

    public long getClientNumber() {
        return clientNumber;
    }

    public void setClientNumber(long clientNumber) {
        this.clientNumber = clientNumber;
    }
}

