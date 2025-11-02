package be.kdg.sa.warehouse.repository;

import be.kdg.sa.warehouse.domain.Invoice;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;

public interface InvoiceRepository extends JpaRepository<Invoice, Long> {
    boolean existsByClientNumberAndInvoiceDate(long clientNumber, LocalDate today);

}
