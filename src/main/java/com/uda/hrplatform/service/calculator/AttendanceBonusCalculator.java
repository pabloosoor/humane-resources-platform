package com.uda.hrplatform.service.calculator;

import com.uda.hrplatform.model.AttendanceRecord;
import com.uda.hrplatform.model.Employee;
import java.math.BigDecimal;
import java.util.List;

public interface AttendanceBonusCalculator {
    BigDecimal calculate(Employee employee, List<AttendanceRecord> records);
    int countUnjustifiedAbsences(List<AttendanceRecord> records);
}
