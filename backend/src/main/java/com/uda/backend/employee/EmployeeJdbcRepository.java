package com.uda.backend.employee;

import com.uda.accessdata.employee.Employee;
import com.uda.accessdata.employee.EmployeeRepository;
import com.uda.accessdata.employee.EmployeeType;
import com.uda.backend.config.ConnectionManager;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class EmployeeJdbcRepository implements EmployeeRepository {

    private final ConnectionManager connectionManager;

    public EmployeeJdbcRepository(ConnectionManager connectionManager) {
        this.connectionManager = connectionManager;
    }

    @Override
    public List<Employee> findActivos() {
        String sql = "SELECT * FROM employees WHERE activo = 1";
        List<Employee> result = new ArrayList<>();
        try (Connection conn = connectionManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) result.add(mapRow(rs));
        } catch (SQLException e) {
            throw new RuntimeException("Error al consultar empleados", e);
        }
        return result;
    }

    @Override
    public Optional<Employee> findById(Long id) {
        String sql = "SELECT * FROM employees WHERE id = ?";
        try (Connection conn = connectionManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) return Optional.of(mapRow(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al buscar empleado", e);
        }
        return Optional.empty();
    }

    @Override
    public void save(Employee employee) {
        String sql = "INSERT INTO employees (nombre, apellido, email, fecha_ingreso, tipo_empleado, salario_base, dias_vacaciones, activo) VALUES (?, ?, ?, ?, ?, ?, ?, 1)";
        try (Connection conn = connectionManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, employee.getNombre());
            stmt.setString(2, employee.getApellido());
            stmt.setString(3, employee.getEmail());
            stmt.setDate(4, Date.valueOf(employee.getFechaIngreso()));
            stmt.setString(5, employee.getTipoEmpleado().name());
            stmt.setBigDecimal(6, employee.getSalarioBase());
            stmt.setInt(7, employee.getDiasVacaciones());
            stmt.executeUpdate();
            try (ResultSet keys = stmt.getGeneratedKeys()) {
                if (keys.next()) employee.setId(keys.getLong(1));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al crear empleado", e);
        }
    }

    @Override
    public void update(Employee employee) {
        String sql = "UPDATE employees SET nombre = ?, apellido = ?, email = ?, tipo_empleado = ?, salario_base = ? WHERE id = ?";
        try (Connection conn = connectionManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, employee.getNombre());
            stmt.setString(2, employee.getApellido());
            stmt.setString(3, employee.getEmail());
            stmt.setString(4, employee.getTipoEmpleado().name());
            stmt.setBigDecimal(5, employee.getSalarioBase());
            stmt.setLong(6, employee.getId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error al actualizar empleado", e);
        }
    }

    @Override
    public void updateDiasVacaciones(Long id, int dias) {
        String sql = "UPDATE employees SET dias_vacaciones = ? WHERE id = ?";
        try (Connection conn = connectionManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, dias);
            stmt.setLong(2, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error al actualizar días de vacaciones", e);
        }
    }

    @Override
    public void deactivate(Long id) {
        String sql = "UPDATE employees SET activo = 0 WHERE id = ?";
        try (Connection conn = connectionManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error al desactivar empleado", e);
        }
    }

    private Employee mapRow(ResultSet rs) throws SQLException {
        Employee e = new Employee();
        e.setId(rs.getLong("id"));
        e.setNombre(rs.getString("nombre"));
        e.setApellido(rs.getString("apellido"));
        e.setEmail(rs.getString("email"));
        e.setFechaIngreso(rs.getDate("fecha_ingreso").toLocalDate());
        e.setTipoEmpleado(EmployeeType.valueOf(rs.getString("tipo_empleado")));
        e.setSalarioBase(rs.getBigDecimal("salario_base"));
        e.setDiasVacaciones(rs.getInt("dias_vacaciones"));
        e.setActivo(rs.getBoolean("activo"));
        return e;
    }
}
