package com.uda.accessdata.bonus;

import java.math.BigDecimal;

public record BonusResult(
        BonusType type,
        BigDecimal amount,
        String description
) {}
