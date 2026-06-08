package com.uda.hrplatform.repository.impl;

import com.uda.hrplatform.model.VacationRequest;
import com.uda.hrplatform.model.VacationStatus;
import com.uda.hrplatform.repository.VacationRepository;
import com.uda.hrplatform.utils.ConnectionManager;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class VacationJdbcRepository implements VacationRepository {

    private final ConnectionManager connectionManager;

    public VacationJdbcRepository(ConnectionManager connectionManager) {
        this.connectionManager = connectionManager;
    }

    @Override
    public VacationRequest save(VacationRequest request) {
        String sql = "INSERT INTO vacation_requests (employee_id, start_date, end_date, requested_days, status, reason) VALUES (?,?,?,?,?,?)";
        try (Connection conn = connectionManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setLong(1, request.getEmployeeId());
            stmt.setDate(2, Date.valueOf(request.getStartDate()));
            stmt.setDate(3, Date.valueOf(request.getEndDate()));
            stmt.setInt(4, request.getRequestedDays());
            stmt.setString(5, request.getStatus().name());
            stmt.setString(6, request.getReason());
            stmt.executeUpdate();
            try (ResultSet keys = stmt.getGeneratedKeys()) {
                if (keys.next()) request.setId(keys.getLong(1));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error saving vacation request", e);
        }
        return request;
    }

    @Override
    public List<VacationRequest> findByEmployeeId(Long employeeId) {
        String sql = "SELECT * FROM vacation_requests WHERE employee_id = ?";
        List<VacationRequest> result = new ArrayList<>();
        try (Connection conn = connectionManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, employeeId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) result.add(mapRow(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error fetching vacation requests", e);
        }
        return result;
    }

    private VacationRequest mapRow(ResultSet rs) throws SQLException {
        VacationRequest r = new VacationRequest();
        r.setId(rs.getLong("id"));
        r.setEmployeeId(rs.getLong("employee_id"));
        r.setStartDate(rs.getDate("start_date").toLocalDate());
        r.setEndDate(rs.getDate("end_date").toLocalDate());
        r.setRequestedDays(rs.getInt("requested_days"));
        r.setStatus(VacationStatus.valueOf(rs.getString("status")));
        r.setReason(rs.getString("reason"));
        return r;
    }
}
