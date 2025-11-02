package be.kdg.sa.warehouse.service;


import be.kdg.sa.warehouse.domain.*;
import be.kdg.sa.warehouse.repository.InvoiceRepository;
import com.openhtmltopdf.pdfboxout.PdfRendererBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service
public class InvoiceService {
    private static final Logger logger = LoggerFactory.getLogger(InvoiceService.class);
    private final SpringTemplateEngine templateEngine;
    private final InvoiceRepository invoiceRepository;
    private final PurchaseOrderService purchaseOrderService;

    public InvoiceService(InvoiceRepository invoiceRepository,
                          SpringTemplateEngine templateEngine, PurchaseOrderService purchaseOrderService) {
        this.invoiceRepository = invoiceRepository;
        this.templateEngine = templateEngine;
        this.purchaseOrderService = purchaseOrderService;
    }


    @Scheduled(cron = "0 0 9 * * *")
    public void createDailyInvoices() {
        logger.info("Creating daily invoices...");
        try {
            for (PurchaseOrder po : purchaseOrderService.getAllPurchaseOrders()) {
                try {
                    createDailyInvoices(po.getClientNumber());
                } catch (IOException e) {
                    logger.error("Failed to create invoices for client {}: {}", po.getClientNumber(), e.getMessage());
                }
            }
            logger.info("Daily invoices created.");
        } catch (Exception e) {
            logger.error("Unexpected error occurred during daily invoice creation: {}", e.getMessage());
        }
    }

    public List<Invoice> createDailyInvoices(long clientNumber) throws IOException {
        List<Invoice> invoices = new ArrayList<>();
        LocalDate today = LocalDate.now();

        if (invoiceRepository.existsByClientNumberAndInvoiceDate(clientNumber, today)) {
            logger.info("Invoice for client {} already exists for today; skipping.", clientNumber);
            return invoices;
        }

        try {
            List<PurchaseOrder> orders = purchaseOrderService.getPurchaseOrdersByClientNumber(clientNumber);

            for (PurchaseOrder order : orders) {
                double totalStorageCosts = purchaseOrderService.calculateTotalAmount(order.getOrderLines());
                double totalCommission = purchaseOrderService.calculateCommission(order.getTotalAmount());
                double totalStorageCostsWithCommission = totalStorageCosts + totalCommission;

                Invoice invoice = new Invoice(LocalDate.now(), clientNumber);
                invoiceRepository.save(invoice);

                String htmlContent = generateInvoiceHtml(invoice, order, totalStorageCosts, totalCommission, totalStorageCostsWithCommission);
                generatePDF(htmlContent, invoice.getInvoiceId());
                invoices.add(invoice);

                logger.info("Invoice for client {} created successfully.", clientNumber);
            }
        } catch (Exception e) {
            logger.error("Error retrieving orders for client {}: {}", clientNumber, e.getMessage());
        }
        return invoices;
    }

    private String generateInvoiceHtml(Invoice invoice, PurchaseOrder order, double totalStorageCosts, double totalCommission, double totalStorageCostsWithCommission) {
        try {
            Context context = new Context();
            context.setVariable("invoice", invoice);
            context.setVariable("order", order);
            context.setVariable("totalStorageCosts", totalStorageCosts);
            context.setVariable("totalCommission", totalCommission);
            context.setVariable("totalStorageCostsWithCommission", totalStorageCostsWithCommission);

            return templateEngine.process("invoice", context);
        } catch (Exception e) {
            logger.error("Failed to generate HTML for invoice {}: {}", invoice.getInvoiceId(), e.getMessage());
            return "";
        }
    }

    private void generatePDF(String htmlContent, long invoiceId) {
        logger.info("Generating PDF for invoice {}...", invoiceId);
        try {
            Path invoicesDirectory = Paths.get("invoices");
            if (!Files.exists(invoicesDirectory)) {
                Files.createDirectories(invoicesDirectory);
            }

            String todayDate = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
            Path pdfPath = invoicesDirectory.resolve("invoice_" + invoiceId + "_" + todayDate + ".pdf");

            try (FileOutputStream outputStream = new FileOutputStream(pdfPath.toFile())) {
                PdfRendererBuilder builder = new PdfRendererBuilder();
                String baseUrl = FileSystems.getDefault().getPath("src/main/resources/templates/").toUri().toURL().toString();
                builder.withHtmlContent(htmlContent, baseUrl);
                builder.toStream(outputStream);
                builder.run();
            }
            logger.info("PDF for invoice {} generated.", invoiceId);
        } catch (IOException e) {
            logger.error("Failed to generate PDF for invoice {}: {}", invoiceId, e.getMessage());
        }
    }

}