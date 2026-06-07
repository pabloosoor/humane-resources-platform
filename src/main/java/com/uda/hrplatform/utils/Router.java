package com.uda.hrplatform.utils;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.util.*;
import java.util.regex.*;

public class Router implements HttpHandler {

    @FunctionalInterface
    public interface RouteHandler {
        void handle(HttpExchange exchange, Map<String, String> pathVars) throws IOException;
    }

    private record Route(String method, Pattern pattern, List<String> paramNames, RouteHandler handler) {}

    private final List<Route> routes = new ArrayList<>();

    public Router GET(String path, RouteHandler handler) {
        return register("GET", path, handler);
    }

    public Router POST(String path, RouteHandler handler) {
        return register("POST", path, handler);
    }

    public Router PUT(String path, RouteHandler handler) {
        return register("PUT", path, handler);
    }

    public Router DELETE(String path, RouteHandler handler) {
        return register("DELETE", path, handler);
    }

    private Router register(String method, String path, RouteHandler handler) {
        List<String> paramNames = new ArrayList<>();
        Matcher m = Pattern.compile("\\{([^}]+)\\}").matcher(path);
        StringBuffer sb = new StringBuffer("^");
        while (m.find()) {
            paramNames.add(m.group(1));
            m.appendReplacement(sb, "([^/]+)");
        }
        m.appendTail(sb);
        sb.append("$");
        routes.add(new Route(method, Pattern.compile(sb.toString()), paramNames, handler));
        return this;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String method = exchange.getRequestMethod();
        String path = exchange.getRequestURI().getPath();

        for (Route route : routes) {
            if (!route.method().equals(method)) continue;
            Matcher m = route.pattern().matcher(path);
            if (m.matches()) {
                Map<String, String> pathVars = new HashMap<>();
                for (int i = 0; i < route.paramNames().size(); i++) {
                    pathVars.put(route.paramNames().get(i), m.group(i + 1));
                }
                route.handler().handle(exchange, pathVars);
                return;
            }
        }
        HttpUtils.sendError(exchange, 404, "Route not found: " + method + " " + path);
    }
}
