package com.uda.backend;

import com.uda.accessdata.employee.EmployeeRepository;
import com.uda.accessdata.bonus.BonusRepository;
import com.uda.accessdata.presentismo.AttendanceRepository;
import com.uda.accessdata.vacation.VacationRepository;
import com.uda.backend.bonus.BonusCalculatorRegistry;
import com.uda.backend.bonus.BonusJdbcRepository;
import com.uda.backend.bonus.BonusService;
import com.uda.backend.config.ConnectionManager;
import com.uda.backend.employee.EmployeeJdbcRepository;
import com.uda.backend.employee.EmployeeService;
import com.uda.backend.menu.MenuRunner;
import com.uda.backend.presentismo.AttendanceJdbcRepository;
import com.uda.backend.presentismo.PresentismoService;
import com.uda.backend.presentismo.calculator.StandardPresentismoCalculator;
import com.uda.backend.vacation.VacationJdbcRepository;
import com.uda.backend.vacation.VacationPolicyRegistry;
import com.uda.backend.vacation.VacationService;

public class Main {

    public static void main(String[] args) {

        ConnectionManager connectionManager = new ConnectionManager();

        EmployeeRepository employeeRepository   = new EmployeeJdbcRepository(connectionManager);
        VacationRepository vacationRepository   = new VacationJdbcRepository(connectionManager);
        BonusRepository bonusRepository         = new BonusJdbcRepository(connectionManager);
        AttendanceRepository attendanceRepository = new AttendanceJdbcRepository(connectionManager);

        VacationPolicyRegistry vacationRegistry  = new VacationPolicyRegistry();
        BonusCalculatorRegistry bonusRegistry    = new BonusCalculatorRegistry();

        EmployeeService employeeService     = new EmployeeService(employeeRepository);
        VacationService vacationService     = new VacationService(employeeRepository, vacationRepository, vacationRegistry);
        BonusService bonusService           = new BonusService(employeeRepository, bonusRepository, bonusRegistry);
        PresentismoService presentismoService = new PresentismoService(
                employeeRepository, attendanceRepository, bonusRepository, new StandardPresentismoCalculator()
        );

        new MenuRunner(employeeService, vacationService, bonusService, presentismoService).start();
    }
}
