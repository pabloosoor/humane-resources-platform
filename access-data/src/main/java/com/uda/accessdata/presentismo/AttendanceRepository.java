package com.uda.accessdata.presentismo;

import java.time.LocalDate;
import java.util.List;

public interface AttendanceRepository {
    void save(AttendanceRecord record);
    List<AttendanceRecord> findByEmployeeAndPeriod(Long employeeId, LocalDate inicio, LocalDate fin);
    boolean existsByEmployeeAndDate(Long employeeId, LocalDate fecha);
}
