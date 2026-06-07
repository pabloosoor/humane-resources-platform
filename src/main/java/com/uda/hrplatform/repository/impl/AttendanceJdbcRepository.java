package com.uda.hrplatform.repository.impl;

import com.uda.hrplatform.model.AttendanceRecord;
import com.uda.hrplatform.repository.AttendanceRepository;
import com.uda.hrplatform.utils.ConnectionManager;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class AttendanceJdbcRepository implements AttendanceRepository {

    private final ConnectionManager connectionManager;

    public AttendanceJdbcRepository(ConnectionManager connectionManager) {
        this.connectionManager = connectionManager;
    }

    @Override
    public AttendanceRecord save(AttendanceRecord record) {
        String sql = "INSERT INTO attendance_records (employee_id, record_date, present, justified_absence) VALUES (?,?,?,?)";
        try (Connection conn = connectionManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setLong(1, record.getEmployeeId());
            stmt.setDate(2, Date.valueOf(record.getDate()));
            stmt.setBoolean(3, record.isPresent());
            stmt.setBoolean(4, record.isJustifiedAbsence());
            stmt.executeUpdate();
            try (ResultSet keys = stmt.getGeneratedKeys()) {
                if (keys.next()) record.setId(keys.getLong(1));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error saving attendance record", e);
        }
        return record;
    }

    @Override
    public boolean existsByEmployeeAndDate(Long employeeId, LocalDate date) {
        String sql = "SELECT COUNT(*) FROM attendance_records WHERE employee_id = ? AND record_date = ?";
        try (Connection conn = connectionManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, employeeId);
            stmt.setDate(2, Date.valueOf(date));
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next() && rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error checking attendance existence", e);
        }
    }

    @Override
    public List<AttendanceRecord> findByEmployeeAndPeriod(Long employeeId, LocalDate start, LocalDate end) {
        String sql = "SELECT * FROM attendance_records WHERE employee_id = ? AND record_date BETWEEN ? AND ?";
        List<AttendanceRecord> result = new ArrayList<>();
        try (Connection conn = connectionManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, employeeId);
            stmt.setDate(2, Date.valueOf(start));
            stmt.setDate(3, Date.valueOf(end));
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) result.add(mapRow(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error fetching attendance records", e);
        }
        return result;
    }

    private AttendanceRecord mapRow(ResultSet rs) throws SQLException {
        AttendanceRecord a = new AttendanceRecord();
        a.setId(rs.getLong("id"));
        a.setEmployeeId(rs.getLong("employee_id"));
        a.setDate(rs.getDate("record_date").toLocalDate());
        a.setPresent(rs.getBoolean("present"));
        a.setJustifiedAbsence(rs.getBoolean("justified_absence"));
        return a;
    }
}
