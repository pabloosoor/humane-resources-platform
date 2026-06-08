package com.uda.hrplatform.dto;

import java.time.LocalDate;

public record RegisterAttendanceRequest(
        LocalDate date,
        boolean present,
        boolean justifiedAbsence
) {}
