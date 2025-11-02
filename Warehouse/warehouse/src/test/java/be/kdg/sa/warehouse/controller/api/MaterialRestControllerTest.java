package be.kdg.sa.warehouse.controller.api;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class MaterialRestControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Test
    void testGetAllMaterials() throws Exception {
        mockMvc.perform(get("/api/materials")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$").isArray())
                .andExpect(MockMvcResultMatchers.jsonPath("$.length()").value(5));
    }

    @Test
    void testGetMaterialByName_Found() throws Exception {
        String materialName = "Gips";

        mockMvc.perform(get("/api/materials/{name}", materialName)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value(materialName))
                .andExpect(MockMvcResultMatchers.jsonPath("$.sellingPrice").isNotEmpty());
    }

    @Test
    void testGetMaterialByName_NotFound() throws Exception {
        String materialName = "nonExistentMaterial";

        mockMvc.perform(get("/api/materials/{name}", materialName)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNotFound());
    }
}