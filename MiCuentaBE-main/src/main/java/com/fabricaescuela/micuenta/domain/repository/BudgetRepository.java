package com.fabricaescuela.micuenta.domain.repository;

import java.util.List;
import java.util.Optional;

import com.fabricaescuela.micuenta.domain.model.Budget;

public interface BudgetRepository {
    Budget save(Budget budget);

    Optional<Budget> findByUserIdAndCategoryIdAndMonthAndYear(Long userId, Long categoryId, Integer month, Integer year);

    List<Budget> findByUserIdAndMonthAndYear(Long userId, Integer month, Integer year);

    List<Budget> findByUserId(Long userId);

    boolean existsByUserIdAndCategoryIdAndMonthAndYear(Long userId, Long categoryId, Integer month, Integer year);
}
