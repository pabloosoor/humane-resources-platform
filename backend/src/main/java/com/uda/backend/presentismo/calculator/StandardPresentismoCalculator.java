package com.uda.backend.presentismo.calculator;

import com.uda.accessdata.employee.Employee;
import com.uda.accessdata.presentismo.AttendanceRecord;
import com.uda.accessdata.presentismo.PresentismoCalculator;
import com.uda.accessdata.presentismo.PresentismoResult;

import java.math.BigDecimal;
import java.util.List;

public class StandardPresentismoCalculator implements PresentismoCalculator {

    private static final BigDecimal PORCENTAJE_BONO = new BigDecimal("0.10");

    @Override
    public PresentismoResult calculate(Employee employee, List<AttendanceRecord> records) {
        long ausenciasInjustificadas = records.stream()
                .filter(r -> !r.isPresente() && !r.isAusenciaJustificada())
                .count();

        if (ausenciasInjustificadas == 0) {
            BigDecimal monto = employee.getSalarioBase().multiply(PORCENTAJE_BONO);
            return new PresentismoResult(true, 0, monto, "Sin ausencias injustificadas → 10% del salario base");
        }

        return new PresentismoResult(false, (int) ausenciasInjustificadas, BigDecimal.ZERO,
                ausenciasInjustificadas + " ausencia(s) injustificada(s) → sin bono");
    }
}
