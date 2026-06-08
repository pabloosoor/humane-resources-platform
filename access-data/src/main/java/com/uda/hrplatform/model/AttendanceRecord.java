package com.uda.hrplatform.model;

import java.time.LocalDate;

public class AttendanceRecord {

    private Long id;
    private Long employeeId;
    private LocalDate date;
    private boolean present;
    private boolean justifiedAbsence;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getEmployeeId() { return employeeId; }
    public void setEmployeeId(Long employeeId) { this.employeeId = employeeId; }

    public LocalDate getDate() { return date; }
    public void setDate(LocalDate date) { this.date = date; }

    public boolean isPresent() { return present; }
    public void setPresent(boolean present) { this.present = present; }

    public boolean isJustifiedAbsence() { return justifiedAbsence; }
    public void setJustifiedAbsence(boolean justifiedAbsence) { this.justifiedAbsence = justifiedAbsence; }
}
