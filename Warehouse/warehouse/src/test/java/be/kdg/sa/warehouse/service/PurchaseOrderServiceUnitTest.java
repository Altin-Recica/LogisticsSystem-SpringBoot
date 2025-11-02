package be.kdg.sa.warehouse.service;

import be.kdg.sa.warehouse.domain.OrderLine;
import be.kdg.sa.warehouse.repository.OrderLineRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;

@SpringBootTest
class PurchaseOrderServiceUnitTest {
    @Autowired
    private PurchaseOrderService purchaseOrderService;

    @MockBean
    private OrderLineRepository orderLineRepository;

    @Test
    public void calculateCommissionShouldUseCorrectRate() {
        // Arrange
        double commissionrate = 0.01;
        double amount = 1000.00;

        // Act
        double commission = purchaseOrderService.calculateCommission(amount);

        // Assert
        assertEquals(commissionrate * amount, commission);
    }

    @Test
    public void calculateTotalAmountShouldReturnCorrectSum() {
        // Arrange
        OrderLine line1 = new OrderLine();
        line1.setPricePerTon(50.0);
        line1.setQuantity(10);

        OrderLine line2 = new OrderLine();
        line2.setPricePerTon(70.0);
        line2.setQuantity(5);

        List<OrderLine> orderLines = Arrays.asList(line1, line2);

        given(orderLineRepository.findAll()).willReturn(orderLines);

        // Act
        double totalAmount = purchaseOrderService.calculateTotalAmount(orderLineRepository.findAll());

        // Assert
        assertEquals(850.0, totalAmount);
    }

}