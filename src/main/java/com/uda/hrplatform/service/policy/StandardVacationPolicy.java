package com.uda.hrplatform.service.policy;

import com.uda.hrplatform.model.Employee;
import com.uda.hrplatform.model.EmployeeType;
import com.uda.hrplatform.model.VacationApproval;

import java.time.LocalDate;

public class StandardVacationPolicy implements VacationPolicy {

    @Override
    public boolean supports(EmployeeType type) {
        return true;
    }

    // Validates balance and returns approval with remaining days.
    @Override
    public VacationApproval apply(Employee employee, int requestedDays, LocalDate startDate) {
        if (requestedDays <= 0) {
            return new VacationApproval(false, "Requested days must be greater than zero.", employee.getVacationDays());
        }
        if (requestedDays > employee.getVacationDays()) {
            return new VacationApproval(false,
                    "Insufficient balance. Available: " + employee.getVacationDays(),
                    employee.getVacationDays());
        }
        return new VacationApproval(true, "Vacation approved.", employee.getVacationDays() - requestedDays);
    }
}
