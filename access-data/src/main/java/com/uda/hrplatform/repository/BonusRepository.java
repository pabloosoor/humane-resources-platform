package com.uda.hrplatform.repository;

import com.uda.hrplatform.model.BonusRecord;
import com.uda.hrplatform.model.BonusType;
import java.util.List;

public interface BonusRepository {
    BonusRecord save(BonusRecord record);
    boolean existsByEmployeeAndTypeAndPeriod(Long employeeId, BonusType type, String period);
    List<BonusRecord> findByEmployeeId(Long employeeId);
}
