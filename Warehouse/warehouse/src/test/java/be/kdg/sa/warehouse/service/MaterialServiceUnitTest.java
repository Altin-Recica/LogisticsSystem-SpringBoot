package be.kdg.sa.warehouse.service;

import be.kdg.sa.warehouse.domain.Material;
import be.kdg.sa.warehouse.repository.MaterialRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
class MaterialServiceUnitTest {
    @MockBean
    private MaterialRepository materialRepository;

    @Autowired
    private MaterialService materialService;

    @Test
    public void testGetMaterial_Success() {
        // Arrange
        Material material = new Material("Steel", "Stainless steel");
        when(materialRepository.findByName("Steel")).thenReturn(Optional.of(material));

        // Act
        Optional<Material> result = materialService.getMaterial("Steel");

        // Assert
        assertTrue(result.isPresent());
        assertEquals("Steel", result.get().getName());
        verify(materialRepository, times(1)).findByName("Steel");
    }

    @Test
    public void testGetMaterial_NotFound() {
        // Arrange
        when(materialRepository.findByName("Wood")).thenReturn(Optional.empty());

        // Act
        Optional<Material> result = materialService.getMaterial("Wood");

        // Assert
        assertFalse(result.isPresent());
        verify(materialRepository, times(1)).findByName("Wood");
    }

    @Test
    public void testGetMaterial_Exception() {
        // Arrange
        when(materialRepository.findByName("Iron")).thenThrow(new RuntimeException("Database error"));

        // Act
        Optional<Material> result = materialService.getMaterial("Iron");

        // Assert
        assertFalse(result.isPresent());
        verify(materialRepository, times(1)).findByName("Iron");
    }

    @Test
    public void testGetMaterials_Success() {
        // Arrange
        List<Material> materials = Arrays.asList(new Material("Steel", "Stainless steel"), new Material("Copper", "Copper"));
        when(materialRepository.findAll()).thenReturn(materials);

        // Act
        Collection<Material> result = materialService.getMaterials();

        // Assert
        assertEquals(2, result.size());
        verify(materialRepository, times(1)).findAll();
    }

    @Test
    public void testGetMaterials_EmptyList() {
        // Arrange
        when(materialRepository.findAll()).thenReturn(Collections.emptyList());

        // Act
        Collection<Material> result = materialService.getMaterials();

        // Assert
        assertTrue(result.isEmpty());
        verify(materialRepository, times(1)).findAll();
    }

    @Test
    public void testGetMaterials_Exception() {
        // Arrange
        when(materialRepository.findAll()).thenThrow(new RuntimeException("Database error"));

        // Act
        Collection<Material> result = materialService.getMaterials();

        // Assert
        assertTrue(result.isEmpty());
        verify(materialRepository, times(1)).findAll();
    }

    @Test
    public void testAddMaterial_Success() {
        // Arrange
        Material material = new Material("Steel", "Stainless steel");
        when(materialRepository.save(material)).thenReturn(material);

        // Act
        Material result = materialService.addMaterial(material);

        // Assert
        assertNotNull(result);
        assertEquals("Steel", result.getName());
        verify(materialRepository, times(1)).save(material);
    }

    @Test
    public void testAddMaterial_Exception() {
        // Arrange
        Material material = new Material("Iron", "Iron");
        when(materialRepository.save(material)).thenThrow(new RuntimeException("Database error"));

        // Act
        Material result = materialService.addMaterial(material);

        // Assert
        assertNull(result);
        verify(materialRepository, times(1)).save(material);
    }
}