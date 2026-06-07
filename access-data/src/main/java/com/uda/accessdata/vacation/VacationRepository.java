package com.uda.accessdata.vacation;

import java.util.List;

public interface VacationRepository {
    void save(VacationRequest request);
    List<VacationRequest> findByEmployeeId(Long employeeId);
}
