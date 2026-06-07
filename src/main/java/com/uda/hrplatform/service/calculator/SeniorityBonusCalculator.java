package com.uda.hrplatform.service.calculator;

import com.uda.hrplatform.model.BonusType;
import com.uda.hrplatform.model.Employee;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.Period;

public class SeniorityBonusCalculator implements BonusCalculator {

    @Override
    public boolean supports(BonusType type) {
        return type == BonusType.SENIORITY;
    }

    // 1% of base salary per year of seniority.
    @Override
    public BigDecimal calculate(Employee employee, String period) {
        int years = Period.between(employee.getHireDate(), LocalDate.now()).getYears();
        BigDecimal rate = BigDecimal.valueOf(years).multiply(BigDecimal.valueOf(0.01));
        return employee.getBaseSalary().multiply(rate).setScale(2, RoundingMode.HALF_UP);
    }
}
