package com.uda.accessdata.employee;

import java.math.BigDecimal;
import java.time.LocalDate;

public class Employee {

    private Long id;
    private String nombre;
    private String apellido;
    private String email;
    private LocalDate fechaIngreso;
    private EmployeeType tipoEmpleado;
    private BigDecimal salarioBase;
    private int diasVacaciones;
    private boolean activo;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getApellido() { return apellido; }
    public void setApellido(String apellido) { this.apellido = apellido; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public LocalDate getFechaIngreso() { return fechaIngreso; }
    public void setFechaIngreso(LocalDate fechaIngreso) { this.fechaIngreso = fechaIngreso; }

    public EmployeeType getTipoEmpleado() { return tipoEmpleado; }
    public void setTipoEmpleado(EmployeeType tipoEmpleado) { this.tipoEmpleado = tipoEmpleado; }

    public BigDecimal getSalarioBase() { return salarioBase; }
    public void setSalarioBase(BigDecimal salarioBase) { this.salarioBase = salarioBase; }

    public int getDiasVacaciones() { return diasVacaciones; }
    public void setDiasVacaciones(int diasVacaciones) { this.diasVacaciones = diasVacaciones; }

    public boolean isActivo() { return activo; }
    public void setActivo(boolean activo) { this.activo = activo; }
}
