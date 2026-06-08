package com.uda.hrplatform.service;

import com.uda.hrplatform.dto.CreateEmployeeRequest;
import com.uda.hrplatform.dto.UpdateEmployeeRequest;
import com.uda.hrplatform.model.Employee;
import com.uda.hrplatform.repository.EmployeeRepository;

import java.util.List;

public class EmployeeService {

    private final EmployeeRepository repository;

    public EmployeeService(EmployeeRepository repository) {
        this.repository = repository;
    }

    // Retorna todos los empleados activos.
    public List<Employee> findActives() {
        return repository.findActives();
    }

    // Retorna un empleado por ID o lanza excepción si no existe.
    public Employee findById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Employee not found: " + id));
    }

    // Crea un empleado con 14 días de vacaciones por defecto.
    public Employee create(CreateEmployeeRequest req) {
        Employee employee = new Employee();
        employee.setFirstName(req.firstName());
        employee.setLastName(req.lastName());
        employee.setEmail(req.email());
        employee.setHireDate(req.hireDate());
        employee.setEmployeeType(req.employeeType());
        employee.setBaseSalary(req.baseSalary());
        employee.setVacationDays(14);
        employee.setActive(true);
        return repository.save(employee);
    }

    // Actualiza solo los campos presentes en el request (null = sin cambio).
    public Employee update(Long id, UpdateEmployeeRequest req) {
        Employee employee = findById(id);

        if (req.firstName() != null) employee.setFirstName(req.firstName());
        if (req.lastName() != null) employee.setLastName(req.lastName());
        if (req.email() != null) employee.setEmail(req.email());
        if (req.baseSalary() != null) employee.setBaseSalary(req.baseSalary());
        if (req.vacationDays() != null) employee.setVacationDays(req.vacationDays());

        repository.update(employee);
        return employee;
    }

    // Baja lógica del empleado (active = false).
    public void deactivate(Long id) {
        findById(id);
        repository.deactivate(id);
    }
}
