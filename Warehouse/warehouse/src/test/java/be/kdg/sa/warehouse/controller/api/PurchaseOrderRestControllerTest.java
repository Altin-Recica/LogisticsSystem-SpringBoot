package be.kdg.sa.warehouse.controller.api;

import be.kdg.sa.warehouse.domain.PurchaseOrder;
import be.kdg.sa.warehouse.service.PurchaseOrderService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class PurchaseOrderRestControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private PurchaseOrderService purchaseOrderService;

    @Test
    public void getAllOrders_ShouldReturnOrders() throws Exception {
        // Arrange
        purchaseOrderService.createPurchaseOrder(1, "Client 1").orElseThrow();


        // Act & Assert
        mockMvc.perform(get("/api/orders"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].clientNumber").exists());
    }

    @Test
    public void fulfillOrder_ShouldFulfillAndReturnOrder() throws Exception {
        // Arrange
        PurchaseOrder po = purchaseOrderService.createPurchaseOrder(2, "Client 2").orElseThrow();

        // Act & Assert
        mockMvc.perform(post("/api/fulfill-order/" + po.getOrderId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "orderId": %d,
                                    "clientNumber": 2,
                                    "clientName": "Client 2",
                                    "orderDate": "2024-11-01"
                                }
                                """.formatted(po.getOrderId()))
                        .with(SecurityMockMvcRequestPostProcessors.jwt().authorities(new SimpleGrantedAuthority("manager"))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.fulfilled").value(true))
                .andReturn();
    }

    @Test
    public void createPO_ShouldCreateAndReturnNewOrder() throws Exception {
        // Act & Assert
        mockMvc.perform(post("/api/orders/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "clientNumber": 3,
                                    "clientName": "Client 3",
                                    "orderDate": "2024-11-01"
                                }
                                """)
                        .with(SecurityMockMvcRequestPostProcessors.jwt().authorities(new SimpleGrantedAuthority("manager"))))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.clientNumber").value(3))
                .andExpect(jsonPath("$.clientName").value("Client 3"))
                .andReturn();
    }

    @Test
    public void createPurchaseOrders_ShouldReturnCreatedCount() throws Exception {
        // Act & Assert
        mockMvc.perform(post("/api/purchase-orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content("""
                                [
                                    {
                                        "clientNumber": 4,
                                        "clientName": "Client 4",
                                        "orderDate": "2024-11-01"
                                    },
                                    {
                                        "clientNumber": 5,
                                        "clientName": "Client 5",
                                        "orderDate": "2024-11-01"
                                    }
                                ]
                                """)
                        .with(SecurityMockMvcRequestPostProcessors.jwt().authorities(new SimpleGrantedAuthority("manager"))))
                .andExpect(status().isOk())
                .andExpect(content().string("Created 2 purchase orders"))
                .andReturn();
    }
}