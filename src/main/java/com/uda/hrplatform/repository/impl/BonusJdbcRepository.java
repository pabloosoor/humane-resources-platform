package com.uda.hrplatform.repository.impl;

import com.uda.hrplatform.model.BonusRecord;
import com.uda.hrplatform.model.BonusType;
import com.uda.hrplatform.repository.BonusRepository;
import com.uda.hrplatform.utils.ConnectionManager;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BonusJdbcRepository implements BonusRepository {

    private final ConnectionManager connectionManager;

    public BonusJdbcRepository(ConnectionManager connectionManager) {
        this.connectionManager = connectionManager;
    }

    @Override
    public BonusRecord save(BonusRecord record) {
        String sql = "INSERT INTO bonus_records (employee_id, bonus_type, amount, period) VALUES (?,?,?,?)";
        try (Connection conn = connectionManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setLong(1, record.getEmployeeId());
            stmt.setString(2, record.getBonusType().name());
            stmt.setBigDecimal(3, record.getAmount());
            stmt.setString(4, record.getPeriod());
            stmt.executeUpdate();
            try (ResultSet keys = stmt.getGeneratedKeys()) {
                if (keys.next()) record.setId(keys.getLong(1));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error saving bonus record", e);
        }
        return record;
    }

    @Override
    public boolean existsByEmployeeAndTypeAndPeriod(Long employeeId, BonusType type, String period) {
        String sql = "SELECT COUNT(*) FROM bonus_records WHERE employee_id = ? AND bonus_type = ? AND period = ?";
        try (Connection conn = connectionManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, employeeId);
            stmt.setString(2, type.name());
            stmt.setString(3, period);
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next() && rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error checking bonus existence", e);
        }
    }

    @Override
    public List<BonusRecord> findByEmployeeId(Long employeeId) {
        String sql = "SELECT * FROM bonus_records WHERE employee_id = ?";
        List<BonusRecord> result = new ArrayList<>();
        try (Connection conn = connectionManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, employeeId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) result.add(mapRow(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error fetching bonus records", e);
        }
        return result;
    }

    private BonusRecord mapRow(ResultSet rs) throws SQLException {
        BonusRecord b = new BonusRecord();
        b.setId(rs.getLong("id"));
        b.setEmployeeId(rs.getLong("employee_id"));
        b.setBonusType(BonusType.valueOf(rs.getString("bonus_type")));
        b.setAmount(rs.getBigDecimal("amount"));
        b.setPeriod(rs.getString("period"));
        return b;
    }
}
