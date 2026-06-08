package com.uda.hrplatform.service;

import com.uda.hrplatform.dto.RegisterAttendanceRequest;
import com.uda.hrplatform.model.*;
import com.uda.hrplatform.repository.AttendanceRepository;
import com.uda.hrplatform.repository.BonusRepository;
import com.uda.hrplatform.repository.EmployeeRepository;
import com.uda.hrplatform.service.calculator.AttendanceBonusCalculator;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;

public class AttendanceBonusService {

    private final AttendanceRepository attendanceRepository;
    private final EmployeeRepository employeeRepository;
    private final BonusRepository bonusRepository;
    private final AttendanceBonusCalculator calculator;

    public AttendanceBonusService(AttendanceRepository attendanceRepository,
                                  EmployeeRepository employeeRepository,
                                  BonusRepository bonusRepository,
                                  AttendanceBonusCalculator calculator) {
        this.attendanceRepository = attendanceRepository;
        this.employeeRepository = employeeRepository;
        this.bonusRepository = bonusRepository;
        this.calculator = calculator;
    }

    // Registra la asistencia de un día. Falla rápido si ya existe registro para esa fecha.
    public AttendanceRecord register(Long employeeId, RegisterAttendanceRequest req) {
        if (attendanceRepository.existsByEmployeeAndDate(employeeId, req.date())) {
            throw new RuntimeException("Attendance already registered for " + req.date());
        }

        AttendanceRecord record = new AttendanceRecord();
        record.setEmployeeId(employeeId);
        record.setDate(req.date());
        record.setPresent(req.present());
        record.setJustifiedAbsence(req.justifiedAbsence());
        return attendanceRepository.save(record);
    }

    // Calcula el bono de presentismo del período (formato: YYYY-MM) y lo guarda como bonus record.
    public BonusRecord calculateBonus(Long employeeId, String period) {
        if (bonusRepository.existsByEmployeeAndTypeAndPeriod(employeeId, BonusType.ATTENDANCE_BONUS, period)) {
            throw new RuntimeException("Attendance bonus already calculated for period " + period);
        }

        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new RuntimeException("Employee not found: " + employeeId));

        YearMonth ym = YearMonth.parse(period);
        LocalDate start = ym.atDay(1);
        LocalDate end = ym.atEndOfMonth();

        List<AttendanceRecord> records = attendanceRepository.findByEmployeeAndPeriod(employeeId, start, end);
        BigDecimal amount = calculator.calculate(employee, records);

        BonusRecord bonus = new BonusRecord();
        bonus.setEmployeeId(employeeId);
        bonus.setBonusType(BonusType.ATTENDANCE_BONUS);
        bonus.setAmount(amount);
        bonus.setPeriod(period);
        return bonusRepository.save(bonus);
    }

    // Retorna los registros de asistencia de un empleado en el período indicado.
    public List<AttendanceRecord> findByEmployeeAndPeriod(Long employeeId, String period) {
        YearMonth ym = YearMonth.parse(period);
        return attendanceRepository.findByEmployeeAndPeriod(employeeId, ym.atDay(1), ym.atEndOfMonth());
    }
}
