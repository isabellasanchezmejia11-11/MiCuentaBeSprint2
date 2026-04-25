package com.fabricaescuela.micuenta.application.dto.response;

import java.math.BigDecimal;

public record BudgetResponse(
        Long id,
        BigDecimal amountLimit,
        Integer month,
        Integer year,
        String categoryName,
        Long userId
) {
}
