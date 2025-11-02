package be.kdg.sa.warehouse.domain;

import jakarta.persistence.*;

import java.util.UUID;

@Entity
public class Material {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID materialId;

    @Column(unique = true)
    private String name;

    @Column(length = 500)
    private String description;

    public Material() {}

    public Material(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public UUID getMaterialId() {
        return materialId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }
}
