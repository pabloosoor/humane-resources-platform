package com.uda.hrplatform.service.calculator;

import com.uda.hrplatform.model.BonusType;
import com.uda.hrplatform.model.Employee;
import java.math.BigDecimal;

public interface BonusCalculator {
    boolean supports(BonusType type);
    BigDecimal calculate(Employee employee, String period);
}
