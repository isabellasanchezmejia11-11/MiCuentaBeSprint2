package com.fabricaescuela.micuenta.domain.model;

import java.math.BigDecimal;

public record Budget(
    Long id,
    BigDecimal amountLimit,
    Integer month,
    Integer year,
    Long categoryId,
    Long userId

) {}
