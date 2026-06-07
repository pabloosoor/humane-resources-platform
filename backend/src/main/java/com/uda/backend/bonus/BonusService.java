package com.uda.backend.bonus;

import com.uda.accessdata.bonus.*;
import com.uda.accessdata.employee.Employee;
import com.uda.accessdata.employee.EmployeeRepository;

import java.util.List;

public class BonusService {

    private final EmployeeRepository employeeRepository;
    private final BonusRepository bonusRepository;
    private final BonusCalculatorRegistry calculatorRegistry;

    public BonusService(EmployeeRepository employeeRepository,
                        BonusRepository bonusRepository,
                        BonusCalculatorRegistry calculatorRegistry) {
        this.employeeRepository = employeeRepository;
        this.bonusRepository = bonusRepository;
        this.calculatorRegistry = calculatorRegistry;
    }

    public BonusResult calcular(Long employeeId, BonusType tipo, String periodo) {
        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new RuntimeException("Empleado no encontrado con ID: " + employeeId));

        if (bonusRepository.exists(employeeId, tipo, periodo)) {
            throw new RuntimeException("Ya existe un bono de " + tipo + " para el período " + periodo);
        }

        BonusResult result = calculatorRegistry.getFor(tipo).calculate(employee, periodo);

        BonusRecord record = new BonusRecord();
        record.setEmployeeId(employeeId);
        record.setTipoBono(tipo);
        record.setMonto(result.monto());
        record.setPeriodo(periodo);
        bonusRepository.save(record);

        return result;
    }

    public List<BonusRecord> historialPorEmpleado(Long employeeId) {
        return bonusRepository.findByEmployeeId(employeeId);
    }
}
