package com.uda.backend.vacation;

import com.uda.accessdata.employee.Employee;
import com.uda.accessdata.employee.EmployeeRepository;
import com.uda.accessdata.vacation.*;

import java.time.LocalDate;
import java.util.List;

public class VacationService {

    private final EmployeeRepository employeeRepository;
    private final VacationRepository vacationRepository;
    private final VacationPolicyRegistry policyRegistry;

    public VacationService(EmployeeRepository employeeRepository,
                           VacationRepository vacationRepository,
                           VacationPolicyRegistry policyRegistry) {
        this.employeeRepository = employeeRepository;
        this.vacationRepository = vacationRepository;
        this.policyRegistry = policyRegistry;
    }

    public VacationResult solicitarVacaciones(Long employeeId, int dias, LocalDate inicio) {
        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new RuntimeException("Empleado no encontrado con ID: " + employeeId));

        VacationPolicy policy = policyRegistry.getFor(employee.getTipoEmpleado());
        VacationResult result = policy.apply(employee, dias, inicio);

        VacationRequest request = new VacationRequest();
        request.setEmployeeId(employeeId);
        request.setFechaInicio(inicio);
        request.setFechaFin(inicio.plusDays(dias - 1));
        request.setDiasSolicitados(dias);
        request.setEstado(result.aprobada() ? VacationStatus.APROBADA : VacationStatus.RECHAZADA);
        vacationRepository.save(request);

        if (result.aprobada()) {
            employeeRepository.updateDiasVacaciones(employeeId, result.diasRestantes());
        }

        return result;
    }

    public List<VacationRequest> historialPorEmpleado(Long employeeId) {
        return vacationRepository.findByEmployeeId(employeeId);
    }
}
