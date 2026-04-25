package com.fabricaescuela.micuenta.infrastructure.persistence.entity;



import com.fabricaescuela.micuenta.domain.model.MovementType;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "categories")
public class CategoryEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Enumerated(EnumType.STRING)
    private MovementType type;

    private Long userId;

    public CategoryEntity() {}

    public CategoryEntity(Long id, String name, MovementType type, Long userId) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.userId = userId;
    }

    // 🔥 GETTERS
    public Long getId() { return id; }
    public String getName() { return name; }
    public MovementType getType() { return type; }
    public Long getUserId() { return userId; }

    // 🔥 SETTERS
    public void setId(Long id) { this.id = id; }
    public void setName(String name) { this.name = name; }
    public void setType(MovementType type) { this.type = type; }
    public void setUserId(Long userId) { this.userId = userId; }
}