package com.uda.hrplatform.dto;

import java.time.LocalDate;

public record NewVacationRequest(
        LocalDate startDate,
        LocalDate endDate,
        int requestedDays,
        String reason
) {}
