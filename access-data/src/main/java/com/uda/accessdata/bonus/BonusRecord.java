package com.uda.accessdata.bonus;

import java.math.BigDecimal;

public class BonusRecord {

    private Long id;
    private Long employeeId;
    private BonusType tipoBono;
    private BigDecimal monto;
    private String periodo;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getEmployeeId() { return employeeId; }
    public void setEmployeeId(Long employeeId) { this.employeeId = employeeId; }

    public BonusType getTipoBono() { return tipoBono; }
    public void setTipoBono(BonusType tipoBono) { this.tipoBono = tipoBono; }

    public BigDecimal getMonto() { return monto; }
    public void setMonto(BigDecimal monto) { this.monto = monto; }

    public String getPeriodo() { return periodo; }
    public void setPeriodo(String periodo) { this.periodo = periodo; }
}
