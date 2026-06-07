package com.uda.hrplatform.repository;

import com.uda.hrplatform.model.Employee;
import java.util.List;
import java.util.Optional;

public interface EmployeeRepository {
    List<Employee> findActives();
    Optional<Employee> findById(Long id);
    void updateVacationDays(Long id, int days);
    Employee save(Employee employee);
    void update(Employee employee);
    void deactivate(Long id);
}
