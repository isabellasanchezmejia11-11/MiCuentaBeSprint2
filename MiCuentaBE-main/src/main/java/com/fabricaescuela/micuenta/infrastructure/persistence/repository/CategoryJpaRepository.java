package com.fabricaescuela.micuenta.infrastructure.persistence.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.fabricaescuela.micuenta.domain.model.MovementType;
import com.fabricaescuela.micuenta.infrastructure.persistence.entity.CategoryEntity;

public interface CategoryJpaRepository extends JpaRepository<CategoryEntity, Long> {

    @Query("SELECT c FROM CategoryEntity c WHERE (c.userId = :userId OR c.userId IS NULL) AND c.type = :type")
    List<CategoryEntity> findByUserIdOrUserIdIsNullAndType(@Param("userId") Long userId, @Param("type") MovementType type);

    boolean existsByNameAndUserId(String name, Long userId);

    boolean existsByNameAndUserIdAndIdNot(String name, Long userId, Long id);
}