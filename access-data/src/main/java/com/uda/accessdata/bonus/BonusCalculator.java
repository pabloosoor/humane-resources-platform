package com.uda.accessdata.bonus;

import com.uda.accessdata.employee.Employee;

public interface BonusCalculator {
    boolean supports(BonusType tipo);
    BonusResult calculate(Employee employee, String periodo);
}
