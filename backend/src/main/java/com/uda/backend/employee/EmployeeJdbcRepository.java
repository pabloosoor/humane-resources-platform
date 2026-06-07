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
            while (rs.next()) {
                result.add(mapRow(rs));
            }
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
