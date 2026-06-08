package com.uda.hrplatform.model;

import java.math.BigDecimal;

public class BonusRecord {

    private Long id;
    private Long employeeId;
    private BonusType bonusType;
    private BigDecimal amount;
    private String period;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getEmployeeId() { return employeeId; }
    public void setEmployeeId(Long employeeId) { this.employeeId = employeeId; }

    public BonusType getBonusType() { return bonusType; }
    public void setBonusType(BonusType bonusType) { this.bonusType = bonusType; }

    public BigDecimal getAmount() { return amount; }
    public void setAmount(BigDecimal amount) { this.amount = amount; }

    public String getPeriod() { return period; }
    public void setPeriod(String period) { this.period = period; }
}
