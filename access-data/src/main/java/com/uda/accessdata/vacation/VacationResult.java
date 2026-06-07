package com.uda.accessdata.vacation;

public record VacationResult(
        boolean approved,
        String message,
        int remainingDays
) {}
