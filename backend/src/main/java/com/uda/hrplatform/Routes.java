package com.uda.hrplatform;

import com.uda.hrplatform.utils.Router;

public class Routes {

    private final AppConfig config;

    public Routes(AppConfig config) {
        this.config = config;
    }

    // Construye y retorna el router con todas las rutas registradas.
    public Router build() {
        Router router = new Router();

        // Rutas de empleados
        router.GET("/api/employees",      config.getEmployeeController()::findAll)
              .POST("/api/employees",     config.getEmployeeController()::create)
              .GET("/api/employees/{id}", config.getEmployeeController()::findById)
              .PUT("/api/employees/{id}", config.getEmployeeController()::update)
              .DELETE("/api/employees/{id}", config.getEmployeeController()::deactivate);

        // Rutas de vacaciones
        router.POST("/api/employees/{id}/vacations", config.getVacationController()::request)
              .GET("/api/employees/{id}/vacations",  config.getVacationController()::findByEmployee);

        // Rutas de bonos
        router.POST("/api/employees/{id}/bonus", config.getBonusController()::calculate)
              .GET("/api/employees/{id}/bonus",  config.getBonusController()::findByEmployee);

        // Rutas de asistencia
        router.POST("/api/employees/{id}/attendance",       config.getAttendanceController()::register)
              .GET("/api/employees/{id}/attendance",         config.getAttendanceController()::findByPeriod)
              .POST("/api/employees/{id}/attendance/bonus",  config.getAttendanceController()::calculateBonus);

        return router;
    }
}
