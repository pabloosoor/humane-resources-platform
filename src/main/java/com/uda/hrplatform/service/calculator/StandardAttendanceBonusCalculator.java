package com.uda.hrplatform.service.calculator;

import com.uda.hrplatform.model.AttendanceRecord;
import com.uda.hrplatform.model.Employee;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

public class StandardAttendanceBonusCalculator implements AttendanceBonusCalculator {

    // 10% of base salary if zero unjustified absences, otherwise no bonus.
    @Override
    public BigDecimal calculate(Employee employee, List<AttendanceRecord> records) {
        if (countUnjustifiedAbsences(records) > 0) {
            return BigDecimal.ZERO;
        }
        return employee.getBaseSalary().multiply(BigDecimal.valueOf(0.10)).setScale(2, RoundingMode.HALF_UP);
    }

    @Override
    public int countUnjustifiedAbsences(List<AttendanceRecord> records) {
        return (int) records.stream()
                .filter(r -> !r.isPresent() && !r.isJustifiedAbsence())
                .count();
    }
}
