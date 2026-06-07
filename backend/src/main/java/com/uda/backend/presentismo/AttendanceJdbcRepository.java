package com.uda.backend.presentismo;

import com.uda.accessdata.presentismo.AttendanceRecord;
import com.uda.accessdata.presentismo.AttendanceRepository;
import com.uda.backend.config.ConnectionManager;

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
    public void save(AttendanceRecord record) {
        String sql = "INSERT INTO attendance_records (employee_id, fecha, presente, ausencia_justificada) VALUES (?, ?, ?, ?)";
        try (Connection conn = connectionManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, record.getEmployeeId());
            stmt.setDate(2, Date.valueOf(record.getFecha()));
            stmt.setBoolean(3, record.isPresente());
            stmt.setBoolean(4, record.isAusenciaJustificada());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error al guardar asistencia", e);
        }
    }

    @Override
    public List<AttendanceRecord> findByEmployeeAndPeriod(Long employeeId, LocalDate inicio, LocalDate fin) {
        String sql = "SELECT * FROM attendance_records WHERE employee_id = ? AND fecha BETWEEN ? AND ?";
        List<AttendanceRecord> result = new ArrayList<>();
        try (Connection conn = connectionManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, employeeId);
            stmt.setDate(2, Date.valueOf(inicio));
            stmt.setDate(3, Date.valueOf(fin));
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    result.add(mapRow(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al consultar asistencias", e);
        }
        return result;
    }

    @Override
    public boolean existsByEmployeeAndDate(Long employeeId, LocalDate fecha) {
        String sql = "SELECT COUNT(*) FROM attendance_records WHERE employee_id = ? AND fecha = ?";
        try (Connection conn = connectionManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, employeeId);
            stmt.setDate(2, Date.valueOf(fecha));
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next() && rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al verificar asistencia", e);
        }
    }

    private AttendanceRecord mapRow(ResultSet rs) throws SQLException {
        AttendanceRecord a = new AttendanceRecord();
        a.setId(rs.getLong("id"));
        a.setEmployeeId(rs.getLong("employee_id"));
        a.setFecha(rs.getDate("fecha").toLocalDate());
        a.setPresente(rs.getBoolean("presente"));
        a.setAusenciaJustificada(rs.getBoolean("ausencia_justificada"));
        return a;
    }
}
