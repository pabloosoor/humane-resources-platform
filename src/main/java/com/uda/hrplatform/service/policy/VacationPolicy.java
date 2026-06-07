package com.uda.hrplatform.service.policy;

import com.uda.hrplatform.model.Employee;
import com.uda.hrplatform.model.EmployeeType;
import com.uda.hrplatform.model.VacationApproval;

import java.time.LocalDate;

public interface VacationPolicy {
    boolean supports(EmployeeType type);
    VacationApproval apply(Employee employee, int requestedDays, LocalDate startDate);
}
