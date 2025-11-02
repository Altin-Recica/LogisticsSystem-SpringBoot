package be.kdg.sa.warehouse.service;

import be.kdg.sa.warehouse.domain.Invoice;
import be.kdg.sa.warehouse.domain.PurchaseOrder;
import be.kdg.sa.warehouse.repository.InvoiceRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyDouble;
import static org.mockito.Mockito.*;

@SpringBootTest
class InvoiceServiceTest {
    @Autowired
    private InvoiceService invoiceService;

    @MockBean
    private InvoiceRepository invoiceRepository;

    @MockBean
    private PurchaseOrderService purchaseOrderService;


    @Test
    void testCreateDailyInvoices_SuccessfulGeneration() throws IOException {
        long clientNumber = 123L;
        PurchaseOrder po = new PurchaseOrder();
        po.setClientNumber(clientNumber);
        when(purchaseOrderService.getAllPurchaseOrders()).thenReturn(List.of(po));
        when(invoiceRepository.existsByClientNumberAndInvoiceDate(clientNumber, LocalDate.now())).thenReturn(false);
        when(purchaseOrderService.getPurchaseOrdersByClientNumber(clientNumber)).thenReturn(Collections.singletonList(po));
        when(purchaseOrderService.calculateTotalAmount(any())).thenReturn(100.0);
        when(purchaseOrderService.calculateCommission(anyDouble())).thenReturn(10.0);

        List<Invoice> invoices = invoiceService.createDailyInvoices(clientNumber);

        assertNotNull(invoices);
        verify(invoiceRepository, times(1)).save(any(Invoice.class));
        verify(purchaseOrderService, times(1)).calculateTotalAmount(any());
        verify(purchaseOrderService, times(1)).calculateCommission(anyDouble());
    }

    @Test
    void testCreateDailyInvoices_SkipsDuplicateInvoiceForToday() throws IOException {
        long clientNumber = 123L;
        when(invoiceRepository.existsByClientNumberAndInvoiceDate(clientNumber, LocalDate.now())).thenReturn(true);

        List<Invoice> invoices = invoiceService.createDailyInvoices(clientNumber);

        assertTrue(invoices.isEmpty());
        verify(invoiceRepository, never()).save(any(Invoice.class));
    }

    @Test
    void testCreateDailyInvoices_ErrorDuringOrderRetrieval() throws IOException {
        long clientNumber = 123L;
        when(invoiceRepository.existsByClientNumberAndInvoiceDate(clientNumber, LocalDate.now())).thenReturn(false);
        when(purchaseOrderService.getPurchaseOrdersByClientNumber(clientNumber)).thenThrow(new RuntimeException("Error"));

        List<Invoice> invoices = invoiceService.createDailyInvoices(clientNumber);

        assertTrue(invoices.isEmpty());
        verify(invoiceRepository, never()).save(any(Invoice.class));
    }

    @Test
    void testScheduledDailyInvoiceCreation() {
        when(purchaseOrderService.getAllPurchaseOrders()).thenReturn(Collections.emptyList());

        invoiceService.createDailyInvoices();

        verify(purchaseOrderService, times(1)).getAllPurchaseOrders();
    }
}