package com.uda.accessdata.vacation;

import com.uda.accessdata.employee.Employee;
import com.uda.accessdata.employee.EmployeeType;

import java.time.LocalDate;

public interface VacationPolicy {
    boolean supports(EmployeeType tipo);
    VacationResult apply(Employee employee, int diasSolicitados, LocalDate inicio);
}
