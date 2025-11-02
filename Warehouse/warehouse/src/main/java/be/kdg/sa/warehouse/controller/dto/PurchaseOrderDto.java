package be.kdg.sa.warehouse.controller.dto;

import be.kdg.sa.warehouse.domain.OrderLine;

import java.time.LocalDate;
import java.util.List;

public record PurchaseOrderDto(
        long orderId,
        LocalDate orderDate,
        long clientNumber,
        String clientName,
        double totalAmount,
        boolean fulfilled,
        List<OrderLine> orderLines
) {
}
