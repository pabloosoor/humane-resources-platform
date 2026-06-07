package com.uda.backend.bonus;

import com.uda.accessdata.bonus.BonusRecord;
import com.uda.accessdata.bonus.BonusRepository;
import com.uda.accessdata.bonus.BonusType;
import com.uda.backend.config.ConnectionManager;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BonusJdbcRepository implements BonusRepository {

    private final ConnectionManager connectionManager;

    public BonusJdbcRepository(ConnectionManager connectionManager) {
        this.connectionManager = connectionManager;
    }

    @Override
    public void save(BonusRecord record) {
        String sql = "INSERT INTO bonus_records (employee_id, tipo_bono, monto, periodo) VALUES (?, ?, ?, ?)";
        try (Connection conn = connectionManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, record.getEmployeeId());
            stmt.setString(2, record.getTipoBono().name());
            stmt.setBigDecimal(3, record.getMonto());
            stmt.setString(4, record.getPeriodo());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error al guardar bono", e);
        }
    }

    @Override
    public boolean exists(Long employeeId, BonusType tipo, String periodo) {
        String sql = "SELECT COUNT(*) FROM bonus_records WHERE employee_id = ? AND tipo_bono = ? AND periodo = ?";
        try (Connection conn = connectionManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, employeeId);
            stmt.setString(2, tipo.name());
            stmt.setString(3, periodo);
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next() && rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al verificar bono", e);
        }
    }

    @Override
    public List<BonusRecord> findByEmployeeId(Long employeeId) {
        String sql = "SELECT * FROM bonus_records WHERE employee_id = ? ORDER BY created_at DESC";
        List<BonusRecord> result = new ArrayList<>();
        try (Connection conn = connectionManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, employeeId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    result.add(mapRow(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al consultar bonos", e);
        }
        return result;
    }

    private BonusRecord mapRow(ResultSet rs) throws SQLException {
        BonusRecord b = new BonusRecord();
        b.setId(rs.getLong("id"));
        b.setEmployeeId(rs.getLong("employee_id"));
        b.setTipoBono(BonusType.valueOf(rs.getString("tipo_bono")));
        b.setMonto(rs.getBigDecimal("monto"));
        b.setPeriodo(rs.getString("periodo"));
        return b;
    }
}
