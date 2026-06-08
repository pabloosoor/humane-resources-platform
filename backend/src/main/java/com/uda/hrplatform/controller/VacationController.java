package com.uda.hrplatform.controller;

import com.sun.net.httpserver.HttpExchange;
import com.uda.hrplatform.dto.NewVacationRequest;
import com.uda.hrplatform.service.VacationService;
import com.uda.hrplatform.utils.HttpUtils;

import java.io.IOException;
import java.util.Map;

public class VacationController {

    private final VacationService service;

    public VacationController(VacationService service) {
        this.service = service;
    }

    // POST /api/employees/{id}/vacations — submits a vacation request for the employee
    public void request(HttpExchange exchange, Map<String, String> vars) throws IOException {
        try {
            Long employeeId = Long.parseLong(vars.get("id"));
            NewVacationRequest req = HttpUtils.readBody(exchange, NewVacationRequest.class);
            HttpUtils.sendJson(exchange, 201, service.request(employeeId, req));
        } catch (RuntimeException e) {
            HttpUtils.sendError(exchange, 400, e.getMessage());
        }
    }

    // GET /api/employees/{id}/vacations — returns all vacation requests for the employee
    public void findByEmployee(HttpExchange exchange, Map<String, String> vars) throws IOException {
        try {
            Long employeeId = Long.parseLong(vars.get("id"));
            HttpUtils.sendJson(exchange, 200, service.findByEmployee(employeeId));
        } catch (RuntimeException e) {
            HttpUtils.sendError(exchange, 404, e.getMessage());
        }
    }
}
