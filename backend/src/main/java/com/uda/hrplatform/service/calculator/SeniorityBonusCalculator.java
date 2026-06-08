package com.uda.hrplatform.service.calculator;

import com.uda.hrplatform.model.BonusType;
import com.uda.hrplatform.model.Employee;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Period;

public class SeniorityBonusCalculator implements BonusCalculator {

    @Override
    public boolean supports(BonusType type) {
        return type == BonusType.SENIORITY;
    }

    // 1% del salario base por cada año de antigüedad.
    @Override
    public BigDecimal calculate(Employee employee, String period) {
        int years = Period.between(employee.getHireDate(), LocalDate.now()).getYears();
        return employee.getBaseSalary()
                .multiply(BigDecimal.valueOf(years))
                .multiply(new BigDecimal("0.01"));
    }
}
