package com.uda.accessdata.employee;

import java.util.List;
import java.util.Optional;

public interface EmployeeRepository {
    List<Employee> findActivos();
    Optional<Employee> findById(Long id);
    void updateDiasVacaciones(Long id, int dias);
    void save(Employee employee);
    void update(Employee employee);
    void deactivate(Long id);
}
