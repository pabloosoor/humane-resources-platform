package com.uda.hrplatform;

import com.uda.hrplatform.controller.*;
import com.uda.hrplatform.repository.impl.*;
import com.uda.hrplatform.service.*;
import com.uda.hrplatform.service.calculator.SeniorityBonusCalculator;
import com.uda.hrplatform.service.calculator.StandardAttendanceBonusCalculator;
import com.uda.hrplatform.service.policy.StandardVacationPolicy;
import com.uda.hrplatform.utils.ConnectionManager;

import java.util.List;

// Conecta todas las dependencias: repositorios -> servicios -> controladores.
// Para agregar una funcionalidad: crear su repo, service y controller acá.
public class AppConfig {

    private final EmployeeController employeeController;
    private final VacationController vacationController;
    private final BonusController bonusController;
    private final AttendanceController attendanceController;

    public AppConfig() {
        ConnectionManager connectionManager = new ConnectionManager();

        // Repositorios — uno por tabla
        var employeeRepo   = new EmployeeJdbcRepository(connectionManager);
        var vacationRepo   = new VacationJdbcRepository(connectionManager);
        var bonusRepo      = new BonusJdbcRepository(connectionManager);
        var attendanceRepo = new AttendanceJdbcRepository(connectionManager);

        // Servicios — inyectan repos y estrategias OCP
        var employeeService   = new EmployeeService(employeeRepo);
        var vacationService   = new VacationService(employeeRepo, vacationRepo,
                List.of(new StandardVacationPolicy()));
        var bonusService      = new BonusService(employeeRepo, bonusRepo,
                List.of(new SeniorityBonusCalculator()));
        var attendanceService = new AttendanceBonusService(attendanceRepo, employeeRepo, bonusRepo,
                new StandardAttendanceBonusCalculator());

        // Controladores — inyectan servicios
        this.employeeController   = new EmployeeController(employeeService);
        this.vacationController   = new VacationController(vacationService);
        this.bonusController      = new BonusController(bonusService);
        this.attendanceController = new AttendanceController(attendanceService);
    }

    public EmployeeController getEmployeeController()     { return employeeController; }
    public VacationController getVacationController()     { return vacationController; }
    public BonusController getBonusController()           { return bonusController; }
    public AttendanceController getAttendanceController() { return attendanceController; }
}
