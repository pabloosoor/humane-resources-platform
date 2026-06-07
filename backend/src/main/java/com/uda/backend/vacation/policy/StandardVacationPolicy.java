package com.uda.backend.vacation.policy;

import com.uda.accessdata.employee.Employee;
import com.uda.accessdata.employee.EmployeeType;
import com.uda.accessdata.vacation.VacationPolicy;
import com.uda.accessdata.vacation.VacationResult;

import java.time.LocalDate;

public class StandardVacationPolicy implements VacationPolicy {

    @Override
    public boolean supports(EmployeeType tipo) {
        return true;
    }

    @Override
    public VacationResult apply(Employee employee, int diasSolicitados, LocalDate inicio) {
        if (diasSolicitados <= 0) {
            return new VacationResult(false, "Los días solicitados deben ser mayor a 0.", employee.getDiasVacaciones());
        }
        if (employee.getDiasVacaciones() < diasSolicitados) {
            return new VacationResult(false,
                    "Saldo insuficiente. Disponibles: " + employee.getDiasVacaciones() + " día(s).",
                    employee.getDiasVacaciones());
        }
        return new VacationResult(true, "Solicitud aprobada.", employee.getDiasVacaciones() - diasSolicitados);
    }
}
