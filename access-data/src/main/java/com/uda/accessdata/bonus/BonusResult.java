package com.uda.accessdata.bonus;

import java.math.BigDecimal;

public record BonusResult(
        BonusType tipo,
        BigDecimal monto,
        String descripcion
) {}
