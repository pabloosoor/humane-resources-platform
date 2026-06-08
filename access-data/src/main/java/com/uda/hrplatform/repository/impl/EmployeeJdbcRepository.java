package com.uda.hrplatform.repository.impl;

import com.uda.hrplatform.model.Employee;
import com.uda.hrplatform.model.EmployeeType;
import com.uda.hrplatform.repository.EmployeeRepository;
import com.uda.hrplatform.utils.ConnectionManager;

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
    public List<Employee> findActives() {
        String sql = "SELECT * FROM employees WHERE active = TRUE";
        List<Employee> result = new ArrayList<>();
        try (Connection conn = connectionManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) result.add(mapRow(rs));
        } catch (SQLException e) {
            throw new RuntimeException("Error fetching active employees", e);
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
                return rs.next() ? Optional.of(mapRow(rs)) : Optional.empty();
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error fetching employee by id", e);
        }
    }

    @Override
    public void updateVacationDays(Long id, int days) {
        String sql = "UPDATE employees SET vacation_days = ? WHERE id = ?";
        try (Connection conn = connectionManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, days);
            stmt.setLong(2, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error updating vacation days", e);
        }
    }

    @Override
    public Employee save(Employee employee) {
        String sql = "INSERT INTO employees (first_name, last_name, email, hire_date, employee_type, base_salary, vacation_days, active) VALUES (?,?,?,?,?,?,?,?)";
        try (Connection conn = connectionManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, employee.getFirstName());
            stmt.setString(2, employee.getLastName());
            stmt.setString(3, employee.getEmail());
            stmt.setDate(4, Date.valueOf(employee.getHireDate()));
            stmt.setString(5, employee.getEmployeeType().name());
            stmt.setBigDecimal(6, employee.getBaseSalary());
            stmt.setInt(7, employee.getVacationDays());
            stmt.setBoolean(8, employee.isActive());
            stmt.executeUpdate();
            try (ResultSet keys = stmt.getGeneratedKeys()) {
                if (keys.next()) employee.setId(keys.getLong(1));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error saving employee", e);
        }
        return employee;
    }

    @Override
    public void update(Employee employee) {
        String sql = "UPDATE employees SET first_name=?, last_name=?, email=?, base_salary=?, vacation_days=? WHERE id=?";
        try (Connection conn = connectionManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, employee.getFirstName());
            stmt.setString(2, employee.getLastName());
            stmt.setString(3, employee.getEmail());
            stmt.setBigDecimal(4, employee.getBaseSalary());
            stmt.setInt(5, employee.getVacationDays());
            stmt.setLong(6, employee.getId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error updating employee", e);
        }
    }

    @Override
    public void deactivate(Long id) {
        String sql = "UPDATE employees SET active = FALSE WHERE id = ?";
        try (Connection conn = connectionManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error deactivating employee", e);
        }
    }

    private Employee mapRow(ResultSet rs) throws SQLException {
        Employee e = new Employee();
        e.setId(rs.getLong("id"));
        e.setFirstName(rs.getString("first_name"));
        e.setLastName(rs.getString("last_name"));
        e.setEmail(rs.getString("email"));
        e.setHireDate(rs.getDate("hire_date").toLocalDate());
        e.setEmployeeType(EmployeeType.valueOf(rs.getString("employee_type")));
        e.setBaseSalary(rs.getBigDecimal("base_salary"));
        e.setVacationDays(rs.getInt("vacation_days"));
        e.setActive(rs.getBoolean("active"));
        return e;
    }
}
