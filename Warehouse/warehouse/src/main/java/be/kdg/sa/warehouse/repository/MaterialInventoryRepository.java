package be.kdg.sa.warehouse.repository;

import be.kdg.sa.warehouse.domain.Material;
import be.kdg.sa.warehouse.domain.MaterialInventory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MaterialInventoryRepository extends JpaRepository<MaterialInventory, Integer> {

    List<MaterialInventory> findByWarehouse_WarehouseNumber(int warehouseNumber);


    @Query("""
        SELECT mi
        FROM MaterialInventory mi
        WHERE mi.material = :material
    """)
    List<MaterialInventory> findByMaterial(@Param("material") Material material);

    List<MaterialInventory> findAllByOrderByArrivalDateAsc();

    List<MaterialInventory> findByClientNumberOrderByArrivalDate(long clientNumber);
}
