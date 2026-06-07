package com.uda.accessdata.bonus;

import java.util.List;

public interface BonusRepository {
    void save(BonusRecord record);
    boolean exists(Long employeeId, BonusType type, String period);
    List<BonusRecord> findByEmployeeId(Long employeeId);
}
