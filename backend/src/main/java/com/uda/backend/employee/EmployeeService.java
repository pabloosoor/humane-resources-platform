package com.uda.backend.employee;

import com.uda.accessdata.employee.Employee;
import com.uda.accessdata.employee.EmployeeRepository;
import com.uda.accessdata.employee.EmployeeType;

import java.math.BigDecimal;
import java.time.LocalDate;
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

    public Employee crear(String nombre, String apellido, String email,
                          LocalDate fechaIngreso, EmployeeType tipo, BigDecimal salario) {
        Employee e = new Employee();
        e.setNombre(nombre);
        e.setApellido(apellido);
        e.setEmail(email);
        e.setFechaIngreso(fechaIngreso);
        e.setTipoEmpleado(tipo);
        e.setSalarioBase(salario);
        e.setDiasVacaciones(14);
        e.setActivo(true);
        repository.save(e);
        return e;
    }

    public void actualizar(Long id, String nombre, String apellido, String email,
                           EmployeeType tipo, BigDecimal salario) {
        Employee e = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Empleado no encontrado con ID: " + id));
        e.setNombre(nombre);
        e.setApellido(apellido);
        e.setEmail(email);
        e.setTipoEmpleado(tipo);
        e.setSalarioBase(salario);
        repository.update(e);
    }

    public void desactivar(Long id) {
        repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Empleado no encontrado con ID: " + id));
        repository.deactivate(id);
    }
}
