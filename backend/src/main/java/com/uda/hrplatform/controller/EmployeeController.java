package com.uda.hrplatform.controller;

import com.sun.net.httpserver.HttpExchange;
import com.uda.hrplatform.dto.CreateEmployeeRequest;
import com.uda.hrplatform.dto.UpdateEmployeeRequest;
import com.uda.hrplatform.service.EmployeeService;
import com.uda.hrplatform.utils.HttpUtils;

import java.io.IOException;
import java.util.Map;

public class EmployeeController {

    private final EmployeeService service;

    public EmployeeController(EmployeeService service) {
        this.service = service;
    }

    // GET /api/employees — returns all active employees
    public void findAll(HttpExchange exchange, Map<String, String> vars) throws IOException {
        HttpUtils.sendJson(exchange, 200, service.findActives());
    }

    // GET /api/employees/{id} — returns a single employee or 404 if not found
    public void findById(HttpExchange exchange, Map<String, String> vars) throws IOException {
        try {
            Long id = Long.parseLong(vars.get("id"));
            HttpUtils.sendJson(exchange, 200, service.findById(id));
        } catch (RuntimeException e) {
            HttpUtils.sendError(exchange, 404, e.getMessage());
        }
    }

    // POST /api/employees — creates a new employee from the request body
    public void create(HttpExchange exchange, Map<String, String> vars) throws IOException {
        try {
            CreateEmployeeRequest req = HttpUtils.readBody(exchange, CreateEmployeeRequest.class);
            HttpUtils.sendJson(exchange, 201, service.create(req));
        } catch (RuntimeException e) {
            HttpUtils.sendError(exchange, 400, e.getMessage());
        }
    }

    // PUT /api/employees/{id} — updates only the fields present in the body
    public void update(HttpExchange exchange, Map<String, String> vars) throws IOException {
        try {
            Long id = Long.parseLong(vars.get("id"));
            UpdateEmployeeRequest req = HttpUtils.readBody(exchange, UpdateEmployeeRequest.class);
            HttpUtils.sendJson(exchange, 200, service.update(id, req));
        } catch (RuntimeException e) {
            HttpUtils.sendError(exchange, 400, e.getMessage());
        }
    }

    // DELETE /api/employees/{id} — soft deletes the employee (sets active = false)
    public void deactivate(HttpExchange exchange, Map<String, String> vars) throws IOException {
        try {
            Long id = Long.parseLong(vars.get("id"));
            service.deactivate(id);
            HttpUtils.sendJson(exchange, 204, null);
        } catch (RuntimeException e) {
            HttpUtils.sendError(exchange, 404, e.getMessage());
        }
    }
}
