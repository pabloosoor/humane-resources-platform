package com.uda.backend.bonus.calculator;

import com.uda.accessdata.bonus.BonusCalculator;
import com.uda.accessdata.bonus.BonusResult;
import com.uda.accessdata.bonus.BonusType;
import com.uda.accessdata.employee.Employee;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Period;

public class AntiguedadBonusCalculator implements BonusCalculator {

    @Override
    public boolean supports(BonusType tipo) {
        return tipo == BonusType.ANTIGUEDAD;
    }

    @Override
    public BonusResult calculate(Employee employee, String periodo) {
        int años = Period.between(employee.getFechaIngreso(), LocalDate.now()).getYears();
        BigDecimal porcentaje = BigDecimal.valueOf(años).divide(BigDecimal.valueOf(100));
        BigDecimal monto = employee.getSalarioBase().multiply(porcentaje);
        String descripcion = años + " año(s) de antigüedad → " + años + "% del salario base";
        return new BonusResult(BonusType.ANTIGUEDAD, monto, descripcion);
    }
}
