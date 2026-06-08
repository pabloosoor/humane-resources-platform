package com.uda.hrplatform.service;

import com.uda.hrplatform.dto.CalculateBonusRequest;
import com.uda.hrplatform.model.BonusRecord;
import com.uda.hrplatform.model.Employee;
import com.uda.hrplatform.repository.BonusRepository;
import com.uda.hrplatform.repository.EmployeeRepository;
import com.uda.hrplatform.service.calculator.BonusCalculator;

import java.math.BigDecimal;
import java.util.List;

public class BonusService {

    private final EmployeeRepository employeeRepository;
    private final BonusRepository bonusRepository;
    private final List<BonusCalculator> calculators;

    public BonusService(EmployeeRepository employeeRepository,
                        BonusRepository bonusRepository,
                        List<BonusCalculator> calculators) {
        this.employeeRepository = employeeRepository;
        this.bonusRepository = bonusRepository;
        this.calculators = calculators;
    }

    // Calculates and saves a bonus. Fails fast if already calculated for that period.
    public BonusRecord calculate(Long employeeId, CalculateBonusRequest req) {
        if (bonusRepository.existsByEmployeeAndTypeAndPeriod(employeeId, req.bonusType(), req.period())) {
            throw new RuntimeException("Bonus already calculated for employee " + employeeId
                    + ", type " + req.bonusType() + ", period " + req.period());
        }

        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new RuntimeException("Employee not found: " + employeeId));

        BonusCalculator calculator = calculators.stream()
                .filter(c -> c.supports(req.bonusType()))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("No calculator for: " + req.bonusType()));

        BigDecimal amount = calculator.calculate(employee, req.period());

        BonusRecord record = new BonusRecord();
        record.setEmployeeId(employeeId);
        record.setBonusType(req.bonusType());
        record.setAmount(amount);
        record.setPeriod(req.period());
        return bonusRepository.save(record);
    }

    // Returns all bonus records for a given employee.
    public List<BonusRecord> findByEmployee(Long employeeId) {
        return bonusRepository.findByEmployeeId(employeeId);
    }
}
