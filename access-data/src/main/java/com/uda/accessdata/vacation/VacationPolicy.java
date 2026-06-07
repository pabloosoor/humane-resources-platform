package com.uda.accessdata.vacation;

import com.uda.accessdata.employee.Employee;
import com.uda.accessdata.employee.EmployeeType;

import java.time.LocalDate;

public interface VacationPolicy {
    boolean supports(EmployeeType type);
    VacationResult apply(Employee employee, int requestedDays, LocalDate startDate);
}
