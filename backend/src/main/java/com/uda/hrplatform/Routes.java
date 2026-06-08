package com.uda.hrplatform;

import com.uda.hrplatform.utils.Router;

// Registers every API route, similar to an Express routes/index.js.
// To add a new endpoint: add the corresponding line here.
public class Routes {

    private final AppConfig config;

    public Routes(AppConfig config) {
        this.config = config;
    }

    // Builds and returns the router with all routes registered.
    public Router build() {
        Router router = new Router();

        // Employee routes
        router.GET("/api/employees",      config.getEmployeeController()::findAll)
              .POST("/api/employees",     config.getEmployeeController()::create)
              .GET("/api/employees/{id}", config.getEmployeeController()::findById)
              .PUT("/api/employees/{id}", config.getEmployeeController()::update)
              .DELETE("/api/employees/{id}", config.getEmployeeController()::deactivate);

        // Vacation routes
        router.POST("/api/employees/{id}/vacations", config.getVacationController()::request)
              .GET("/api/employees/{id}/vacations",  config.getVacationController()::findByEmployee);

        // Bonus routes
        router.POST("/api/employees/{id}/bonus", config.getBonusController()::calculate)
              .GET("/api/employees/{id}/bonus",  config.getBonusController()::findByEmployee);

        // Attendance routes
        router.POST("/api/employees/{id}/attendance",       config.getAttendanceController()::register)
              .GET("/api/employees/{id}/attendance",         config.getAttendanceController()::findByPeriod)
              .POST("/api/employees/{id}/attendance/bonus",  config.getAttendanceController()::calculateBonus);

        return router;
    }
}
