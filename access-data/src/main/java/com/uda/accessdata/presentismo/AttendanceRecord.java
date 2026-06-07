package com.uda.accessdata.presentismo;

import java.time.LocalDate;

public class AttendanceRecord {

    private Long id;
    private Long employeeId;
    private LocalDate fecha;
    private boolean presente;
    private boolean ausenciaJustificada;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getEmployeeId() { return employeeId; }
    public void setEmployeeId(Long employeeId) { this.employeeId = employeeId; }

    public LocalDate getFecha() { return fecha; }
    public void setFecha(LocalDate fecha) { this.fecha = fecha; }

    public boolean isPresente() { return presente; }
    public void setPresente(boolean presente) { this.presente = presente; }

    public boolean isAusenciaJustificada() { return ausenciaJustificada; }
    public void setAusenciaJustificada(boolean ausenciaJustificada) { this.ausenciaJustificada = ausenciaJustificada; }
}
