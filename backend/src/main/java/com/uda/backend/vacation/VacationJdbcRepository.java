package com.uda.backend.vacation;

import com.uda.accessdata.vacation.VacationRepository;
import com.uda.accessdata.vacation.VacationRequest;
import com.uda.accessdata.vacation.VacationStatus;
import com.uda.backend.config.ConnectionManager;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class VacationJdbcRepository implements VacationRepository {

    private final ConnectionManager connectionManager;

    public VacationJdbcRepository(ConnectionManager connectionManager) {
        this.connectionManager = connectionManager;
    }

    @Override
    public void save(VacationRequest request) {
        String sql = "INSERT INTO vacation_requests (employee_id, fecha_inicio, fecha_fin, dias_solicitados, estado, motivo) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = connectionManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, request.getEmployeeId());
            stmt.setDate(2, Date.valueOf(request.getFechaInicio()));
            stmt.setDate(3, Date.valueOf(request.getFechaFin()));
            stmt.setInt(4, request.getDiasSolicitados());
            stmt.setString(5, request.getEstado().name());
            stmt.setString(6, request.getMotivo());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error al guardar solicitud de vacaciones", e);
        }
    }

    @Override
    public List<VacationRequest> findByEmployeeId(Long employeeId) {
        String sql = "SELECT * FROM vacation_requests WHERE employee_id = ? ORDER BY created_at DESC";
        List<VacationRequest> result = new ArrayList<>();
        try (Connection conn = connectionManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, employeeId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    result.add(mapRow(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al consultar vacaciones", e);
        }
        return result;
    }

    private VacationRequest mapRow(ResultSet rs) throws SQLException {
        VacationRequest r = new VacationRequest();
        r.setId(rs.getLong("id"));
        r.setEmployeeId(rs.getLong("employee_id"));
        r.setFechaInicio(rs.getDate("fecha_inicio").toLocalDate());
        r.setFechaFin(rs.getDate("fecha_fin").toLocalDate());
        r.setDiasSolicitados(rs.getInt("dias_solicitados"));
        r.setEstado(VacationStatus.valueOf(rs.getString("estado")));
        r.setMotivo(rs.getString("motivo"));
        return r;
    }
}
