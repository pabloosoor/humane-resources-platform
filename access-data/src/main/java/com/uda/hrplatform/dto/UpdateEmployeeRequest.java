package com.uda.hrplatform.dto;

import java.math.BigDecimal;

public record UpdateEmployeeRequest(
        String firstName,
        String lastName,
        String email,
        BigDecimal baseSalary,
        Integer vacationDays
) {}
