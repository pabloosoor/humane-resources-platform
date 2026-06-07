package com.uda.backend.vacation;

import com.uda.accessdata.employee.EmployeeType;
import com.uda.accessdata.vacation.VacationPolicy;
import com.uda.backend.vacation.policy.StandardVacationPolicy;

import java.util.List;

public class VacationPolicyRegistry {

    private final List<VacationPolicy> policies;

    public VacationPolicyRegistry() {
        this.policies = List.of(
                new StandardVacationPolicy()
        );
    }

    public VacationPolicy getFor(EmployeeType tipo) {
        return policies.stream()
                .filter(p -> p.supports(tipo))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Sin política de vacaciones para: " + tipo));
    }
}
