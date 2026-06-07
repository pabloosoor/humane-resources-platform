package com.uda.accessdata.presentismo;

import java.math.BigDecimal;

public record PresentismoResult(
        boolean califica,
        int ausenciasInjustificadas,
        BigDecimal monto,
        String descripcion
) {}
