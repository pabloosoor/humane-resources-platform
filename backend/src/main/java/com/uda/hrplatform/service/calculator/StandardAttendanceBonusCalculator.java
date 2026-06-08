package com.uda.hrplatform.service.calculator;

import com.uda.hrplatform.model.AttendanceRecord;
import com.uda.hrplatform.model.Employee;

import java.math.BigDecimal;
import java.util.List;

public class StandardAttendanceBonusCalculator implements AttendanceBonusCalculator {

    // 10% of base salary if no unjustified absences; zero otherwise.
    @Override
    public BigDecimal calculate(Employee employee, List<AttendanceRecord> records) {
        long unjustified = records.stream()
                .filter(r -> !r.isPresent() && !r.isJustifiedAbsence())
                .count();
        if (unjustified > 0) return BigDecimal.ZERO;
        return employee.getBaseSalary().multiply(new BigDecimal("0.10"));
    }
}
