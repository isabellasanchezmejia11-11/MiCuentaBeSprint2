package com.fabricaescuela.micuenta.infrastructure.persistence.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.fabricaescuela.micuenta.domain.model.MovementType;
import com.fabricaescuela.micuenta.infrastructure.persistence.entity.CategoryEntity;

public interface CategoryJpaRepository extends JpaRepository<CategoryEntity, Long> {

    List<CategoryEntity> findByUserIdOrUserIdIsNullAndType(Long userId, MovementType type);

    boolean existsByNameAndUserId(String name, Long userId);

    boolean existsByNameAndUserIdAndIdNot(String name, Long userId, Long id);
}