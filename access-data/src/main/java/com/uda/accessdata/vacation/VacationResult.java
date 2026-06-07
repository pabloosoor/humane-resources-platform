package com.uda.accessdata.vacation;

public record VacationResult(
        boolean aprobada,
        String mensaje,
        int diasRestantes
) {}
