package com.uda.accessdata.presentismo;

import com.uda.accessdata.employee.Employee;

import java.util.List;

public interface PresentismoCalculator {
    PresentismoResult calculate(Employee employee, List<AttendanceRecord> records);
}
