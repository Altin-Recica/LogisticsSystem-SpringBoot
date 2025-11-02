package be.kdg.sa.warehouse.controller.api;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class MaterialInventoryRestControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Test
    void testGetMaterialsInInventory() throws Exception {
        mockMvc.perform(get("/api/inventory")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$").isArray())
                .andExpect(MockMvcResultMatchers.jsonPath("$.length()").value(11));
    }

    @Test
    void testGetMaterialsInventoryOldestFirst() throws Exception {
        mockMvc.perform(get("/api/inventory/oldestFirst")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$").isArray())
                .andExpect(MockMvcResultMatchers.jsonPath("$.length()").value(11))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].arrivalDate").exists());
    }

    @Test
    void testGetMaterialsInventoryByWarehouse_Found() throws Exception {
        int warehouseNumber = 1;

        mockMvc.perform(get("/api/inventory/{warehouseNumber}", warehouseNumber)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$").isArray())
                .andExpect(MockMvcResultMatchers.jsonPath("$.length()").value(4));
    }

    @Test
    void testGetMaterialsInventoryByWarehouse_NotFound() throws Exception {
        int warehouseNumber = 999;

        mockMvc.perform(get("/api/inventory/{warehouseNumber}", warehouseNumber)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$").isEmpty());
    }
}