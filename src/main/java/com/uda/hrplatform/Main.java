package com.uda.hrplatform;

import com.sun.net.httpserver.HttpServer;
import com.uda.hrplatform.controller.*;
import com.uda.hrplatform.repository.impl.*;
import com.uda.hrplatform.service.*;
import com.uda.hrplatform.service.calculator.SeniorityBonusCalculator;
import com.uda.hrplatform.service.calculator.StandardAttendanceBonusCalculator;
import com.uda.hrplatform.service.policy.StandardVacationPolicy;
import com.uda.hrplatform.utils.ConnectionManager;
import com.uda.hrplatform.utils.Router;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.List;

public class Main {

    public static void main(String[] args) throws IOException {
        ConnectionManager connectionManager = new ConnectionManager();

        var employeeRepo      = new EmployeeJdbcRepository(connectionManager);
        var vacationRepo      = new VacationJdbcRepository(connectionManager);
        var bonusRepo         = new BonusJdbcRepository(connectionManager);
        var attendanceRepo    = new AttendanceJdbcRepository(connectionManager);

        var employeeService   = new EmployeeService(employeeRepo);
        var vacationService   = new VacationService(employeeRepo, vacationRepo, List.of(new StandardVacationPolicy()));
        var bonusService      = new BonusService(employeeRepo, bonusRepo, List.of(new SeniorityBonusCalculator()));
        var attendanceService = new AttendanceBonusService(attendanceRepo, employeeRepo, bonusRepo, new StandardAttendanceBonusCalculator());

        var employeeCtrl    = new EmployeeController(employeeService);
        var vacationCtrl    = new VacationController(vacationService);
        var bonusCtrl       = new BonusController(bonusService);
        var attendanceCtrl  = new AttendanceController(attendanceService);

        Router router = new Router();

        router.GET("/api/employees",                        employeeCtrl::findAll)
              .POST("/api/employees",                       employeeCtrl::create)
              .GET("/api/employees/{id}",                   employeeCtrl::findById)
              .PUT("/api/employees/{id}",                   employeeCtrl::update)
              .DELETE("/api/employees/{id}",                employeeCtrl::deactivate)
              .POST("/api/employees/{id}/vacations",        vacationCtrl::request)
              .GET("/api/employees/{id}/vacations",         vacationCtrl::findByEmployee)
              .POST("/api/employees/{id}/bonus",            bonusCtrl::calculate)
              .GET("/api/employees/{id}/bonus",             bonusCtrl::findByEmployee)
              .POST("/api/employees/{id}/attendance",       attendanceCtrl::register)
              .GET("/api/employees/{id}/attendance",        attendanceCtrl::findByPeriod)
              .POST("/api/employees/{id}/attendance/bonus", attendanceCtrl::calculateBonus);

        HttpServer server = HttpServer.create(new InetSocketAddress(8080), 0);
        server.createContext("/", router);
        server.start();

        System.out.println("HR Platform running on http://localhost:8080");
    }
}
