package com.uda.hrplatform.service;

import com.uda.hrplatform.dto.NewVacationRequest;
import com.uda.hrplatform.model.*;
import com.uda.hrplatform.repository.EmployeeRepository;
import com.uda.hrplatform.repository.VacationRepository;
import com.uda.hrplatform.service.policy.VacationPolicy;

import java.util.List;

public class VacationService {

    private final EmployeeRepository employeeRepository;
    private final VacationRepository vacationRepository;
    private final List<VacationPolicy> policies;

    public VacationService(EmployeeRepository employeeRepository,
                           VacationRepository vacationRepository,
                           List<VacationPolicy> policies) {
        this.employeeRepository = employeeRepository;
        this.vacationRepository = vacationRepository;
        this.policies = policies;
    }

    // Aplica la política correspondiente, guarda la solicitud y descuenta días si fue aprobada.
    public VacationRequest request(Long employeeId, NewVacationRequest req) {
        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new RuntimeException("Employee not found: " + employeeId));

        VacationPolicy policy = policies.stream()
                .filter(p -> p.supports(employee.getEmployeeType()))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("No vacation policy for: " + employee.getEmployeeType()));

        VacationApproval approval = policy.apply(employee, req.requestedDays(), req.startDate());

        VacationRequest vacationRequest = buildRequest(employeeId, req, approval);

        if (approval.approved()) {
            employeeRepository.updateVacationDays(employeeId, approval.remainingDays());
        }

        return vacationRepository.save(vacationRequest);
    }

    // Retorna todas las solicitudes de vacaciones de un empleado.
    public List<VacationRequest> findByEmployee(Long employeeId) {
        return vacationRepository.findByEmployeeId(employeeId);
    }

    private VacationRequest buildRequest(Long employeeId, NewVacationRequest req, VacationApproval approval) {
        VacationRequest vr = new VacationRequest();
        vr.setEmployeeId(employeeId);
        vr.setStartDate(req.startDate());
        vr.setEndDate(req.endDate());
        vr.setRequestedDays(req.requestedDays());
        vr.setReason(req.reason());
        vr.setStatus(approval.approved() ? VacationStatus.APPROVED : VacationStatus.REJECTED);
        return vr;
    }
}
