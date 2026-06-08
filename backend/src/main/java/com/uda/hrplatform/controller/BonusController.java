package com.uda.hrplatform.controller;

import com.sun.net.httpserver.HttpExchange;
import com.uda.hrplatform.dto.CalculateBonusRequest;
import com.uda.hrplatform.service.BonusService;
import com.uda.hrplatform.utils.HttpUtils;

import java.io.IOException;
import java.util.Map;

public class BonusController {

    private final BonusService service;

    public BonusController(BonusService service) {
        this.service = service;
    }

    // POST /api/employees/{id}/bonus — calculates and records a bonus for the employee
    public void calculate(HttpExchange exchange, Map<String, String> vars) throws IOException {
        try {
            Long employeeId = Long.parseLong(vars.get("id"));
            CalculateBonusRequest req = HttpUtils.readBody(exchange, CalculateBonusRequest.class);
            HttpUtils.sendJson(exchange, 201, service.calculate(employeeId, req));
        } catch (RuntimeException e) {
            HttpUtils.sendError(exchange, 400, e.getMessage());
        }
    }

    // GET /api/employees/{id}/bonus — returns all bonus records for the employee
    public void findByEmployee(HttpExchange exchange, Map<String, String> vars) throws IOException {
        try {
            Long employeeId = Long.parseLong(vars.get("id"));
            HttpUtils.sendJson(exchange, 200, service.findByEmployee(employeeId));
        } catch (RuntimeException e) {
            HttpUtils.sendError(exchange, 404, e.getMessage());
        }
    }
}
