package com.fabricaescuela.micuenta.infrastructure.persistence.mapper;

import org.springframework.stereotype.Component;

import com.fabricaescuela.micuenta.domain.model.Budget;
import com.fabricaescuela.micuenta.infrastructure.persistence.entity.BudgetEntity;

@Component
public class BudgetMapper {

    public Budget toDomain(BudgetEntity entity) {
        return new Budget(
                entity.getId(),
                entity.getAmountLimit(),
                entity.getMonth(),
                entity.getYear(),
                entity.getCategoryId(),
                entity.getUserId()
        );
    }

    public BudgetEntity toEntity(Budget budget) {
        return new BudgetEntity(
                budget.id(),
                budget.amountLimit(),
                budget.month(),
                budget.year(),
                budget.categoryId(),
                budget.userId()
        );
    }
}
