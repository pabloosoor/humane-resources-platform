package com.uda.hrplatform.controller;

import com.sun.net.httpserver.HttpExchange;
import com.uda.hrplatform.dto.RegisterAttendanceRequest;
import com.uda.hrplatform.service.AttendanceBonusService;
import com.uda.hrplatform.utils.HttpUtils;

import java.io.IOException;
import java.util.Map;

public class AttendanceController {

    private final AttendanceBonusService service;

    public AttendanceController(AttendanceBonusService service) {
        this.service = service;
    }

    // POST /api/employees/{id}/attendance — registra la asistencia de un día
    public void register(HttpExchange exchange, Map<String, String> vars) throws IOException {
        try {
            Long employeeId = Long.parseLong(vars.get("id"));
            RegisterAttendanceRequest req = HttpUtils.readBody(exchange, RegisterAttendanceRequest.class);
            HttpUtils.sendJson(exchange, 201, service.register(employeeId, req));
        } catch (RuntimeException e) {
            HttpUtils.sendError(exchange, 400, e.getMessage());
        }
    }

    // GET /api/employees/{id}/attendance?period=YYYY-MM — retorna asistencias del período
    public void findByPeriod(HttpExchange exchange, Map<String, String> vars) throws IOException {
        try {
            Long employeeId = Long.parseLong(vars.get("id"));
            String period = HttpUtils.queryParams(exchange).get("period");
            HttpUtils.sendJson(exchange, 200, service.findByEmployeeAndPeriod(employeeId, period));
        } catch (RuntimeException e) {
            HttpUtils.sendError(exchange, 400, e.getMessage());
        }
    }

    // POST /api/employees/{id}/attendance/bonus?period=YYYY-MM — calcula el bono de presentismo
    public void calculateBonus(HttpExchange exchange, Map<String, String> vars) throws IOException {
        try {
            Long employeeId = Long.parseLong(vars.get("id"));
            String period = HttpUtils.queryParams(exchange).get("period");
            HttpUtils.sendJson(exchange, 201, service.calculateBonus(employeeId, period));
        } catch (RuntimeException e) {
            HttpUtils.sendError(exchange, 400, e.getMessage());
        }
    }
}
