package com.uda.hrplatform.repository;

import com.uda.hrplatform.model.AttendanceRecord;
import java.time.LocalDate;
import java.util.List;

public interface AttendanceRepository {
    AttendanceRecord save(AttendanceRecord record);
    boolean existsByEmployeeAndDate(Long employeeId, LocalDate date);
    List<AttendanceRecord> findByEmployeeAndPeriod(Long employeeId, LocalDate start, LocalDate end);
}
