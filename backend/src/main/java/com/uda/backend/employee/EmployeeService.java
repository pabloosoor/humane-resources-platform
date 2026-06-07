package com.uda.backend.employee;

import com.uda.accessdata.employee.Employee;
import com.uda.accessdata.employee.EmployeeRepository;

import java.util.List;
import java.util.Optional;

public class EmployeeService {

    private final EmployeeRepository repository;

    public EmployeeService(EmployeeRepository repository) {
        this.repository = repository;
    }

    public List<Employee> listarActivos() {
        return repository.findActivos();
    }

    public Optional<Employee> buscarPorId(Long id) {
        return repository.findById(id);
    }
}
