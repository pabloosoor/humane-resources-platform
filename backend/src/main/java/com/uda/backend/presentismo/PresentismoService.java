package com.uda.backend.presentismo;

import com.uda.accessdata.bonus.BonusRecord;
import com.uda.accessdata.bonus.BonusRepository;
import com.uda.accessdata.bonus.BonusType;
import com.uda.accessdata.employee.Employee;
import com.uda.accessdata.employee.EmployeeRepository;
import com.uda.accessdata.presentismo.AttendanceRecord;
import com.uda.accessdata.presentismo.AttendanceRepository;
import com.uda.accessdata.presentismo.PresentismoCalculator;
import com.uda.accessdata.presentismo.PresentismoResult;

import java.time.LocalDate;
import java.util.List;

public class PresentismoService {

    private final EmployeeRepository employeeRepository;
    private final AttendanceRepository attendanceRepository;
    private final BonusRepository bonusRepository;
    private final PresentismoCalculator calculator;

    public PresentismoService(EmployeeRepository employeeRepository,
                              AttendanceRepository attendanceRepository,
                              BonusRepository bonusRepository,
                              PresentismoCalculator calculator) {
        this.employeeRepository = employeeRepository;
        this.attendanceRepository = attendanceRepository;
        this.bonusRepository = bonusRepository;
        this.calculator = calculator;
    }

    public PresentismoResult calcular(Long employeeId, int year, int month) {
        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new RuntimeException("Empleado no encontrado con ID: " + employeeId));

        String periodo = String.format("%d-%02d", year, month);

        if (bonusRepository.exists(employeeId, BonusType.PRESENTISMO, periodo)) {
            throw new RuntimeException("El presentismo de " + periodo + " ya fue calculado.");
        }

        LocalDate inicio = LocalDate.of(year, month, 1);
        LocalDate fin = inicio.withDayOfMonth(inicio.lengthOfMonth());

        List<AttendanceRecord> records = attendanceRepository
                .findByEmployeeAndPeriod(employeeId, inicio, fin);

        PresentismoResult result = calculator.calculate(employee, records);

        if (result.califica()) {
            BonusRecord bonus = new BonusRecord();
            bonus.setEmployeeId(employeeId);
            bonus.setTipoBono(BonusType.PRESENTISMO);
            bonus.setMonto(result.monto());
            bonus.setPeriodo(periodo);
            bonusRepository.save(bonus);
        }

        return result;
    }

    public void registrarAsistencia(Long employeeId, LocalDate fecha, boolean presente, boolean justificada) {
        if (!employeeRepository.findById(employeeId).isPresent()) {
            throw new RuntimeException("Empleado no encontrado con ID: " + employeeId);
        }
        if (attendanceRepository.existsByEmployeeAndDate(employeeId, fecha)) {
            throw new RuntimeException("Ya existe un registro de asistencia para esa fecha.");
        }
        AttendanceRecord record = new AttendanceRecord();
        record.setEmployeeId(employeeId);
        record.setFecha(fecha);
        record.setPresente(presente);
        record.setAusenciaJustificada(justificada);
        attendanceRepository.save(record);
    }
}
