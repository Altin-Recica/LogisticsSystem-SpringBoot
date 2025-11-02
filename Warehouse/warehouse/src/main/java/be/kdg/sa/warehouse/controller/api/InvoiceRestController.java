package be.kdg.sa.warehouse.controller.api;

import be.kdg.sa.warehouse.domain.Invoice;
import be.kdg.sa.warehouse.service.ConfigurationService;
import be.kdg.sa.warehouse.service.InvoiceService;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@RestController
@RequestMapping("/api")
public class InvoiceRestController {
    private final InvoiceService invoiceService;
    private final ConfigurationService configService;
    private static final Logger logger = LoggerFactory.getLogger(InvoiceRestController.class);


    public InvoiceRestController(InvoiceService invoiceService, ConfigurationService configService) {
        this.configService = configService;
        this.invoiceService = invoiceService;
    }

    @GetMapping("/getCommission")
    public ResponseEntity<Double> getCommission() {
        try {
            double commissionRate = configService.getCommissionRate();
            return ResponseEntity.ok(commissionRate);
        } catch (Exception e) {
            logger.warn("Failed to retrieve commission rate: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping("/invoice")
    @PreAuthorize("hasAuthority('manager')")
    public void downloadInvoices(HttpServletResponse response) {
        int clientNumber = 1;

        try {
            List<Invoice> invoices = invoiceService.createDailyInvoices(clientNumber);
            for (Invoice invoice : invoices) {
                String todayDate = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
                String pdfFileName = "invoice_" + invoice.getInvoiceId() + "_" + todayDate + ".pdf";
                String pdfPath = "invoices/" + pdfFileName;

                response.setContentType("application/pdf");
                response.setHeader("Content-Disposition", "attachment; filename=" + pdfFileName);

                try (FileInputStream fileInputStream = new FileInputStream(pdfPath);
                     OutputStream responseOutputStream = response.getOutputStream()) {
                    int bytes;
                    while ((bytes = fileInputStream.read()) != -1) {
                        responseOutputStream.write(bytes);
                    }
                    responseOutputStream.flush();
                } catch (IOException e) {
                    logger.warn("Error streaming PDF file: {}", e.getMessage());
                    response.sendError(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Error streaming PDF file");
                }
            }
        } catch (Exception e) {
            logger.warn("Failed to generate invoices: {}", e.getMessage());
            try {
                response.sendError(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Failed to generate invoices");
            } catch (IOException ioException) {
                logger.error("Failed to send error response: {}", ioException.getMessage());
            }
        }
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Void> handleException(Exception e) {
        logger.warn("An unexpected error occurred: {}", e.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }
}
