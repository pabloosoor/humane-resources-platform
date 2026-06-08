package com.uda.hrplatform.repository;

import com.uda.hrplatform.model.VacationRequest;
import java.util.List;

public interface VacationRepository {
    VacationRequest save(VacationRequest request);
    List<VacationRequest> findByEmployeeId(Long employeeId);
}
