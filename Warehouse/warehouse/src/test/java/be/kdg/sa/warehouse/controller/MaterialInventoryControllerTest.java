package be.kdg.sa.warehouse.controller;

import be.kdg.sa.warehouse.service.MaterialInventoryService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class MaterialInventoryControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MaterialInventoryService materialInventoryService;

    @Test
    void testGetMaterialsInInventory_success() throws Exception {
        when(materialInventoryService.getMaterialsInInventory()).thenReturn(new ArrayList<>()); // Replace with test data if needed

        mockMvc.perform(get("/inventory"))
                .andExpect(status().isOk())
                .andExpect(view().name("materialinventory"))
                .andExpect(model().attributeExists("materialItemsInventory"));
    }

    @Test
    void testGetMaterialsInInventoryOldestFirst_success() throws Exception {
        when(materialInventoryService.getOldestMaterialInventories()).thenReturn(new ArrayList<>()); // Replace with test data if needed

        mockMvc.perform(get("/inventory/oldestFirst"))
                .andExpect(status().isOk())
                .andExpect(view().name("materialinventory"))
                .andExpect(model().attributeExists("materialItemsInventory"));
    }

    @Test
    void testGetMaterialInventoryFromWarehouse_success() throws Exception {
        int warehouseNumber = 1;
        when(materialInventoryService.getMaterialInventoriesForWarehouse(warehouseNumber)).thenReturn(new ArrayList<>()); // Replace with test data if needed

        mockMvc.perform(get("/inventory/{warehouseNumber}", warehouseNumber))
                .andExpect(status().isOk())
                .andExpect(view().name("materialinventoryfromwarehouse"))
                .andExpect(model().attributeExists("materialItemsInventory"));
    }


}