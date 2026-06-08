package com.uda.hrplatform.dto;

import com.uda.hrplatform.model.EmployeeType;
import java.math.BigDecimal;
import java.time.LocalDate;

public record CreateEmployeeRequest(
        String firstName,
        String lastName,
        String email,
        LocalDate hireDate,
        EmployeeType employeeType,
        BigDecimal baseSalary
) {}
