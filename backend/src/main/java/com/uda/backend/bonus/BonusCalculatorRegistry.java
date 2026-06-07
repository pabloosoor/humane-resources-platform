package com.uda.backend.bonus;

import com.uda.accessdata.bonus.BonusCalculator;
import com.uda.accessdata.bonus.BonusType;
import com.uda.backend.bonus.calculator.AntiguedadBonusCalculator;

import java.util.List;

public class BonusCalculatorRegistry {

    private final List<BonusCalculator> calculators;

    public BonusCalculatorRegistry() {
        this.calculators = List.of(
                new AntiguedadBonusCalculator()
        );
    }

    public BonusCalculator getFor(BonusType tipo) {
        return calculators.stream()
                .filter(c -> c.supports(tipo))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Sin calculadora para el tipo: " + tipo));
    }
}
